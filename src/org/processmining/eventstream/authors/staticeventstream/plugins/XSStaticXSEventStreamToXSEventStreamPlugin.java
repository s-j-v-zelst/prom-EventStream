package org.processmining.eventstream.authors.staticeventstream.plugins;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.eventstream.connections.XSEventXSAuthorXSStreamConnectionImpl;
import org.processmining.eventstream.core.factories.XSEventStreamFactory;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.eventstream.core.interfaces.XSStaticXSEventStream;
import org.processmining.eventstream.models.XSEventAuthor;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.stream.core.enums.CommunicationType;

@Plugin(name = "Generate Event Stream (Static Event Stream)", parameterLabels = {
		"Static Event Stream" }, returnLabels = { "Stream Generator",
				"Event Stream" }, returnTypes = { XSEventAuthor.class, XSEventStream.class })
public class XSStaticXSEventStreamToXSEventStreamPlugin {

	@PluginVariant(requiredParameterLabels = { 0 })
	public static Object[] apply(PluginContext context, XSStaticXSEventStream staticStream) {
		XSEventAuthor author = new XSStaticXSEventStreamToXSEventStreamAuthor("Static Event Stream Author",
				staticStream);
		XSEventStream stream = XSEventStreamFactory.createXSEventStream(CommunicationType.SYNC);
		stream.start();
		author.connect(stream);
		context.getConnectionManager().addConnection(new XSEventXSAuthorXSStreamConnectionImpl(stream, author));
		return new Object[] { author, stream };
	}

	@UITopiaVariant(affiliation = "Eindhoven University of Technology", author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl")
	@PluginVariant(requiredParameterLabels = { 0 })
	public static Object[] apply(UIPluginContext context, XSStaticXSEventStream staticStream) {
		XSEventAuthor author = new XSStaticXSEventStreamToXSEventStreamAuthor("Static Event Stream Author",
				staticStream);
		XSEventStream stream = XSEventStreamFactory.createXSEventStream(CommunicationType.SYNC);
		stream.start();
		author.connect(stream);
		context.getConnectionManager().addConnection(new XSEventXSAuthorXSStreamConnectionImpl(stream, author));
		return new Object[] { author, stream };
	}

}
