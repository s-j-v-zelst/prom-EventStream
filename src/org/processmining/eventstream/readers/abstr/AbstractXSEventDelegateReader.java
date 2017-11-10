package org.processmining.eventstream.readers.abstr;

import java.util.List;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.stream.core.interfaces.XSVisualization;

@Deprecated // use the delegates internally, i.e., we have more control of the delegates
public abstract class AbstractXSEventDelegateReader<R, V, P extends XSEventReaderParameters>
		extends AbstractXSEventReader<R, V, P> {

	private List<AbstractXSEventReader<?, ?, ? super P>> delegates;

	public AbstractXSEventDelegateReader(final String name, final XSVisualization<V> visualization,
			final P parameters) {
		super(name, visualization, parameters);
	}

	public AbstractXSEventDelegateReader(final String name, final XSVisualization<V> visualization, final P parameters,
			final List<AbstractXSEventReader<?, ?, ? super P>> delegates) {
		super(name, visualization, parameters);
		this.delegates = delegates;
	}

	public List<AbstractXSEventReader<?, ?, ? super P>> getDelegates() {
		return delegates;
	}

	@Override
	public void handleNextPacket(XSEvent event) {
		if (canReadPacket(event)) {
			for (AbstractXSEventReader<?, ?, ? super P> delegate : delegates) {
				delegate.handleNextPacket(event);
			}
		}
	}

	public void processNewXSEvent(String caseId, XEventClass activity) {
		//NOP

	}

	public void setDelegates(List<AbstractXSEventReader<?, ?, ? super P>> delegates) {
		this.delegates = delegates;
	}

	@Override
	public long measureUsedMemory() {
		long res = 0;
		for (AbstractXSEventReader<?, ?, ? super P> delegate : delegates) {
			long readerRes = delegate.measureUsedMemory();
			if (readerRes == -1) {
				res = -1;
				break;
			}
			res += readerRes;
		}
		return res;
	}

}
