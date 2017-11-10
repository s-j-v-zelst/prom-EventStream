package org.processmining.eventstream.readers.flower;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.OperationNotSupportedException;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetFactory;
import org.processmining.eventstream.readers.abstr.AbstractXSEventReader;
import org.processmining.eventstream.readers.acceptingpetrinet.AcceptingPetriNetXSReaderResultVisualizer;
import org.processmining.eventstream.readers.acceptingpetrinet.XSEventStreamToAcceptingPetriNetReader;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.stream.model.datastructure.DSParameterMissingException;
import org.processmining.stream.model.datastructure.DSWrongParameterException;
import org.processmining.stream.model.datastructure.DataStructure.Type;
import org.processmining.stream.model.datastructure.DataStructureFactory;
import org.processmining.stream.model.datastructure.IterableDataStructure;
import org.processmining.stream.reader.views.XSReaderSequenceOfResultsView;

public class StreamFlowerDiscoveryAlgorithmImpl extends
		AbstractXSEventReader<AcceptingPetriNet, AcceptingPetriNet, StreamFlowerDiscoveryAlgorithmParametersImpl>
		implements XSEventStreamToAcceptingPetriNetReader {

	public static Collection<Type> ALLOWED_DATA_STRUCTURES = EnumSet.of(Type.LOSSY_COUNTING, Type.FREQUENT_ALGORITHM,
			Type.SPACE_SAVING, Type.RESERVOIR);

	private IterableDataStructure<XEventClass> dataStructure;

	private static String NAME = "Stream Based Flower Discovery";

	// maintain the "result" in memory, such that querying the Petri net is faster
	private final Petrinet net = PetrinetFactory.newPetrinet("Petri net (Stream Flower Process Discovery)");
	private final Map<XEventClass, Transition> actTrMap = new ConcurrentHashMap<>();
	private final Place place;
	private final Marking marking;

	public StreamFlowerDiscoveryAlgorithmImpl(final PluginContext context,
			StreamFlowerDiscoveryAlgorithmParametersImpl parameters) {
		super(NAME,
				new XSReaderSequenceOfResultsView<AcceptingPetriNet>("Visualizer of: " + NAME, null,
						parameters.getRefreshRate(), AcceptingPetriNetXSReaderResultVisualizer.instance(context)),
				parameters);
		((XSReaderSequenceOfResultsView<AcceptingPetriNet>) getVisualization()).setReader(this);
		try {
			dataStructure = DataStructureFactory.createIterableDataStructure(parameters.getDataStructureType(),
					parameters.getDataStructureParameters());
		} catch (OperationNotSupportedException | DSParameterMissingException | DSWrongParameterException e) {
			e.printStackTrace();
			this.interrupt();
			dataStructure = null;
		}
		place = net.addPlace("place");
		marking = new Marking();
		marking.add(place);
	}

	@Override
	public void startXSRunnable() {
		super.start();
		getVisualization().start();
	}

	public void processNewXSEvent(String caseId, XEventClass activity) {
		dataStructure.add(activity);
	}

	protected AcceptingPetriNet computeCurrentResult() {
		// add all event classes into the Petri net.
		for (XEventClass e : dataStructure) {
			if (!actTrMap.containsKey(e)) {
				Transition t = net.addTransition(e.getId());
				net.addArc(place, t);
				net.addArc(t, place);
				actTrMap.put(e, t);
			}
		}

		// clean all forgotten events
		for (XEventClass e : actTrMap.keySet()) {
			if (!dataStructure.contains(e)) {
				Transition t = actTrMap.get(e);
				net.removeTransition(t);
				actTrMap.remove(e);
			}
		}
		return AcceptingPetriNetFactory.createAcceptingPetriNet(net, marking, marking);
	}

}
