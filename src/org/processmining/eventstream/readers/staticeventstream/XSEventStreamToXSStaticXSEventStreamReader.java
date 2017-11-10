package org.processmining.eventstream.readers.staticeventstream;

import java.util.List;

import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSStaticXSEventStream;
import org.processmining.eventstream.readers.staticeventstream.parameters.XSEventStreamToXSStaticEventStreamParameters;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.providedobjects.ProvidedObjectID;
import org.processmining.stream.reader.staticstream.AbstractStaticStreamXSReader;

public class XSEventStreamToXSStaticXSEventStreamReader
		extends AbstractStaticStreamXSReader<XSEvent, XSStaticXSEventStream> {

	private final ProvidedObjectID providedObjectID;

	public XSEventStreamToXSStaticXSEventStreamReader(String name, XSEventStreamToXSStaticEventStreamParameters params,
			XSStaticXSEventStream container) {
		this(name, params, container, null, null);
	}

	public XSEventStreamToXSStaticXSEventStreamReader(String name, XSEventStreamToXSStaticEventStreamParameters params,
			XSStaticXSEventStream container, PluginContext context, ProvidedObjectID providedObjectID) {
		super(name, null, params.getTotalNumberOfEvents(), container, context);
		this.providedObjectID = providedObjectID;
	}

	public Class<XSEvent> getTopic() {
		return XSEvent.class;
	}

	protected void writeContainerToContext(List<XSEvent> container, PluginContext context) {
		XSStaticXSEventStream stream = computeCurrentResult();
		context.getProvidedObjectManager().createProvidedObject("Static Stream", stream, context);
		if (providedObjectID != null) {
			context.getProvidedObjectManager().getProvidedObjectLifeCylceListeners()
					.fireProvidedObjectObjectChanged(providedObjectID);
		}
	}

	protected XSStaticXSEventStream computeCurrentResult() {
		return (XSStaticXSEventStream) getContainer();
	}

}
