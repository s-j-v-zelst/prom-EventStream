package org.processmining.eventstream.readers.trie;
//package org.processmining.eventstream.readers.graph;
//
//import org.processmining.contexts.uitopia.UIPluginContext;
//import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
//import org.processmining.eventstream.core.interfaces.XSEvent;
//import org.processmining.eventstream.core.interfaces.XSEventStream;
//import org.processmining.framework.plugin.PluginContext;
//import org.processmining.framework.plugin.annotations.Plugin;
//import org.processmining.framework.plugin.annotations.PluginVariant;
//import org.processmining.stream.core.interfaces.XSReader;
//import org.processmining.stream.model.datastructure.DSParameterFactory;
//import org.processmining.stream.model.datastructure.DataStructure.Type;
//
//@Plugin(name = "Discover Case Trie", parameterLabels = { "Event Stream", "Parameters" }, returnLabels = {
//		"Live Case Trie" }, returnTypes = { XSReader.class })
//public class DiscoverCaseTriePlugin {
//
//	@UITopiaVariant(author = "S.J. van Zelst", email = "s.j.v.zelst@tue.nl", affiliation = "Eindhoven University of Technology")
//	@PluginVariant(variantLabel = "Discover Case Trie", requiredParameterLabels = { 0 })
//	public static XSReader<XSEvent, StreamDirectedRootedGraphImpl<Object, ContainingObjectEqualVertexImpl<Object>>> apply(
//			final UIPluginContext context, final XSEventStream stream) {
//		GraphOnStreamAlgorithmParameters params = new GraphOnStreamAlgorithmParameters();
//		params.setObjectIdentifier("xsevent:data:org:resource");
//		params.setCaseDataStructure(Type.INFINITE_HASH_POINTER);
//		params.setCaseDataStructureParameters(DSParameterFactory.createDefaultParameters(Type.INFINITE_HASH_POINTER));
//		return apply(context, stream, params);
//	}
//
//	@PluginVariant(variantLabel = "Discover Handover-of-Work Network", requiredParameterLabels = { 0, 1 })
//	public static XSReader<XSEvent, StreamDirectedRootedGraphImpl<Object, ContainingObjectEqualVertexImpl<Object>>> apply(
//			final PluginContext context, final XSEventStream stream,
//			final GraphOnStreamAlgorithmParameters parameters) {
//		ContainingObjectEqualVertexImpl<Object> root = new ContainingObjectEqualVertexImpl<Object>("root");
//		StreamDirectedRootedGraphImpl<Object, ContainingObjectEqualVertexImpl<Object>> graph = new StreamDirectedRootedGraphImpl<Object, ContainingObjectEqualVertexImpl<Object>>(
//				root);
//		StreamCaseTrieAlgorithmImpl<GraphOnStreamAlgorithmParameters> algo = new StreamCaseTrieAlgorithmImpl<>(
//				"how", parameters, graph);
//		algo.startXSRunnable();
//		stream.connect(algo);
//		return algo;
//	}
//}
