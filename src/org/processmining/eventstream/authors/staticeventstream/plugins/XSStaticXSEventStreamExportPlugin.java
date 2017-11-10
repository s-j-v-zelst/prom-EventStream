package org.processmining.eventstream.authors.staticeventstream.plugins;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UIExportPlugin;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.eventstream.core.interfaces.XSStaticXSEventStream;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Export Static Event Stream", parameterLabels = { "Log", "File" }, returnLabels = {}, returnTypes = {})
@UIExportPlugin(description = "Static Event Stream", extension = "evst")
public class XSStaticXSEventStreamExportPlugin {

	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl")
	@PluginVariant(requiredParameterLabels = { 0, 1 }, variantLabel = "Export Log to XES File")
	public static void export(UIPluginContext context, XSStaticXSEventStream stream, File file) throws IOException {
		export(stream, file);
	}

	public static void export(XSStaticXSEventStream stream, File file) throws IOException {
		FileWriter fw = new FileWriter(file);
		final String newLine = System.getProperty("line.separator");
		for (int i = 0; i < stream.size(); i++) {
			if (i < stream.size() - 1)
				fw.write(stream.get(i).toString() + newLine );
			else
				fw.write(stream.get(i).toString());
		}
		fw.flush();
		fw.close();
	}

}
