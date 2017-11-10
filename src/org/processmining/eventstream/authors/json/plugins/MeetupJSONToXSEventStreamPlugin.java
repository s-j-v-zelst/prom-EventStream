package org.processmining.eventstream.authors.json.plugins;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.eventstream.authors.json.implementations.MeetupJSONToXSEventAuthor;
import org.processmining.eventstream.core.factories.XSEventStreamFactory;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.stream.core.enums.CommunicationType;
import org.processmining.stream.core.interfaces.XSAuthor;

@Plugin(name = "Meetup.com to Event Stream Converter (JSON)", parameterLabels = {}, returnLabels = { "Converter",
		"Event Stream (XSEvent)" }, returnTypes = { XSAuthor.class,
				XSEventStream.class }, help = "Converts the JSON-based stream of meetup.com to an event stream (XSEvent).")
public class MeetupJSONToXSEventStreamPlugin {

	@PluginVariant(requiredParameterLabels = {})
	@UITopiaVariant(author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl", affiliation = "Eindhoven University of Technology")
	public Object[] jsonToXSeventStreamPlugin(final UIPluginContext context) {
		try {
			XSAuthor<XSEvent> author = new MeetupJSONToXSEventAuthor(new URL("http://stream.meetup.com/2/open_events"),
					Charset.forName("UTF-8"));
			XSEventStream stream = XSEventStreamFactory.createXSEventStream(CommunicationType.ASYNC);
			stream.start();
			author.connect(stream);
			return new Object[] { author, stream };
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
