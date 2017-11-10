package org.processmining.eventstream.authors.json.implementations;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.extension.std.XTimeExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.processmining.eventstream.core.factories.XSEventFactory;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.stream.author.json.abstracts.AbstractJsonUrlHttpXSAuthor;
import org.processmining.stream.util.JSONUtils;
import org.processmining.stream.visualization.XSDynamicBarChart;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MeetupJSONToXSEventAuthor extends AbstractJsonUrlHttpXSAuthor<XSEvent, String> {

	private XFactory xFactory = new XFactoryNaiveImpl();
	//Map of id (type String) -> corresponding JSON Object
	private Map<String, JsonObject> meetupEventStore = new HashMap<String, JsonObject>();

	public MeetupJSONToXSEventAuthor(final URL url, final Charset charset) {
		super("XSEvent Author (Meetup/JSON)", new XSDynamicBarChart("JSON Author Visualizer", 1024), url, charset);
	}

	@Override
	public void workPackage() {
		try {
			if (this.jsonStream.hasNext()) {
				JsonObject message = this.jsonStream.next().getAsJsonObject();
				Set<Map.Entry<String, JsonElement>> meetupEventEntrySet = message.entrySet();
				String id = (String) JSONUtils.getValue(meetupEventEntrySet, "id", String.class);
				if (this.meetupEventStore.containsKey(id)) {
					for (XSEvent event : this.createConsecutiveMeetupXSEvents(meetupEventEntrySet, id)) {
						this.write(event);
						getVisualization().updateVisualization(event.get(XConceptExtension.KEY_NAME).toString());
					}
				} else {
					XSEvent event = this.createNewMeetupXSEvent(meetupEventEntrySet, id);
					this.write(event);
					this.meetupEventStore.put(id, message);
					getVisualization().updateVisualization(event.get(XConceptExtension.KEY_NAME).toString());
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Trying to reconnect...");
			this.reConnect();

		}
	}

	private XSEvent createNewMeetupXSEvent(Set<Map.Entry<String, JsonElement>> meetupEventEntrySet,
			String correlationKey) {
		XAttribute traceIdAttr = this.xFactory.createAttributeLiteral("trace", correlationKey, null);
		XSEvent event = XSEventFactory.createXSEvent(traceIdAttr);
		XAttribute eventName = xFactory.createAttributeLiteral(XConceptExtension.KEY_NAME, "create new meetup event",
				XConceptExtension.instance());
		event.put(XConceptExtension.KEY_NAME, eventName);

		Date eventTimeStamp = new Date(
				Long.parseLong((String) JSONUtils.getValue(meetupEventEntrySet, "mtime", String.class)));
		XAttribute eventTimeStampAttribute = xFactory.createAttributeTimestamp(XTimeExtension.KEY_TIMESTAMP,
				eventTimeStamp, XTimeExtension.instance());
		event.put(XTimeExtension.KEY_TIMESTAMP, eventTimeStampAttribute);

		for (Map.Entry<String, JsonElement> meetupEventDataEntry : meetupEventEntrySet) {
			XAttribute newAttribute = xFactory.createAttributeLiteral(meetupEventDataEntry.getKey(),
					meetupEventDataEntry.getValue().toString(), null);
			event.put(meetupEventDataEntry.getKey(), newAttribute);
		}
		return event;
	}

	//TODO: refactor functions into smaller chuncks
	private Set<XSEvent> createConsecutiveMeetupXSEvents(Set<Map.Entry<String, JsonElement>> meetupEventEntrySet,
			String correlationKey) {
		Set<XSEvent> events = new HashSet<XSEvent>();
		XAttribute traceIdAttr = this.xFactory.createAttributeLiteral("trace", correlationKey, null);
		XAttributeMap attributeMap = this.xFactory.createAttributeMap();

		for (Map.Entry<String, JsonElement> meetupEventDataEntry : meetupEventEntrySet) {
			XAttribute newAttribute = xFactory.createAttributeLiteral(meetupEventDataEntry.getKey(),
					meetupEventDataEntry.getValue().toString(), null);
			attributeMap.put(meetupEventDataEntry.getKey(), newAttribute);
		}

		for (String updatedFields : this.findChangesInMeetupEvent(meetupEventEntrySet,
				this.meetupEventStore.get(correlationKey).entrySet())) {
			// Filter out "mtime change"
			if (!updatedFields.equals("mtime")) {
				XSEvent event = XSEventFactory.createXSEvent(traceIdAttr, attributeMap);
				XAttribute eventName = xFactory.createAttributeLiteral(XConceptExtension.KEY_NAME,
						"change " + updatedFields, XConceptExtension.instance());
				event.put(XConceptExtension.KEY_NAME, eventName);
				events.add(event);
			}
		}

		return events;
	}

	private Set<String> findChangesInMeetupEvent(Set<Map.Entry<String, JsonElement>> newEvent,
			Set<Map.Entry<String, JsonElement>> oldEvent) {
		Set<String> result = new HashSet<String>();
		for (Map.Entry<String, JsonElement> newEntry : newEvent) {
			if (!(oldEvent.contains(newEntry))) {
				result.add(newEntry.getKey());
			}
		}
		return result;
	}

	public Class<XSEvent> getTopic() {
		return XSEvent.class;
	}
}
