package org.processmining.eventstream.authors.staticeventstream.plugins;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.processmining.contexts.uitopia.annotations.UIImportPlugin;
import org.processmining.eventstream.core.factories.XSEventFactory;
import org.processmining.eventstream.core.implementations.XSStaticXSEventStreamArrayListImpl;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSStaticXSEventStream;
import org.processmining.framework.abstractplugins.AbstractImportPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;

/**
 * this importer is build for the purpose of reusing event streams, from an
 * "academic", experimental perspective. The importer assumes some text field
 * that contains one event per line. The format expected is of the form
 * "{key=value, key=value, ...,} \n {key=... ". Since the nature of the code is
 * purely for scientific purposes, no fancy check on the input are performed.
 * 
 * @author Sebastiaan J. van Zelst.
 *
 */
@Plugin(name = "Import Static Event Stream", parameterLabels = { "Filename" }, returnLabels = {
		"Static Event Stream" }, returnTypes = { XSStaticXSEventStream.class })
@UIImportPlugin(description = "Importer of Static Event Stream Files", extensions = { "evst" })
public class XSStaticXSEventStreamImportPlugin extends AbstractImportPlugin {

	protected Object importFromStream(PluginContext context, InputStream input, String filename, long fileSizeInBytes)
			throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		XSStaticXSEventStream stream = new XSStaticXSEventStreamArrayListImpl();
		while ((line = reader.readLine()) != null) {
			//remove "{" and "}"
			line = line.substring(1, line.length() - 1);
			String[] data = line.split(",");
			XSEvent event = XSEventFactory.createXSEvent();
			for (String d : data) {
				String[] keyVal = d.split("=");
				String key = keyVal[0].trim();
				String value = keyVal[1].trim();
				event.put(key, new XAttributeLiteralImpl(key, value));
			}
			stream.add(event);
		}
		return stream;
	}

}
