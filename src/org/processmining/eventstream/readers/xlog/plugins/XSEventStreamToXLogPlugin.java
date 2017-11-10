//package org.processmining.eventstream.readers.xlog.plugins;
//
//import org.processmining.contexts.uitopia.UIPluginContext;
//import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
//import org.processmining.eventstream.core.interfaces.XSEventStream;
//import org.processmining.eventstream.readers.xlog.implementations.XSEventToXLogReader;
//import org.processmining.framework.plugin.annotations.Plugin;
//import org.processmining.framework.plugin.annotations.PluginVariant;
//import org.processmining.stream.core.factories.XSSubscriberFactory;
//import org.processmining.stream.core.interfaces.XSSubscriber;
//
//@Plugin(name = "XSStream Subscriber (XSEvent -> Log/XLog)", parameterLabels = {
//		"Event Stream (XSEvent)" }, returnLabels = { "Subscriber (XSSubscriber)" }, returnTypes = {
//				XSSubscriber.class })
//public class XSEventStreamToXLogPlugin {
//
//	@UITopiaVariant(author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl", affiliation = "Eindhoven University of Technology")
//	@PluginVariant(requiredParameterLabels = { 0 })
//	public XSSubscriber xsEventStreamToXLog(final UIPluginContext context, final XSEventStream stream) {
//
//		XSSubscriber subscriber = XSSubscriberFactory.createXSSubscriber();
//		XSEventToXLogReader reader = new XSEventToXLogReader(stream.getCommunicationType());
//		subscriber.addReader(reader, stream);
//		return subscriber;
//	}
//
//}
