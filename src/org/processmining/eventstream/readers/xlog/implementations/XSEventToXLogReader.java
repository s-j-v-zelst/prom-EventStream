//package org.processmining.eventstream.readers.xlog.implementations;
//
//import java.awt.Component;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Queue;
//
//import org.deckfour.xes.extension.XExtensionManager;
//import org.deckfour.xes.factory.XFactory;
//import org.deckfour.xes.factory.XFactoryNaiveImpl;
//import org.deckfour.xes.model.XAttributeLiteral;
//import org.deckfour.xes.model.XEvent;
//import org.deckfour.xes.model.XLog;
//import org.deckfour.xes.model.XTrace;
//import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
//import org.processmining.eventstream.core.interfaces.XSEvent;
//import org.processmining.eventstream.core.interfaces.XSEventSignature;
//import org.processmining.eventstream.readers.xlog.views.XSEventStreamToXLogView;
//import org.processmining.stream.core.abstracts.AbstractXSReader;
//import org.processmining.stream.core.enums.CommunicationType;
//
//public class XSEventToXLogReader extends AbstractXSReader<XSEvent, XLog> {
//
//	protected Queue<XSEvent> queue = new LinkedList<XSEvent>();
//	protected boolean busy = true;
//	private XSEventStreamToXLogView visualizer = new XSEventStreamToXLogView();
//	private boolean flush = false;
//
//	public XSEventToXLogReader(final CommunicationType communicationType) {
//		super("XSEvent -> XLog Reader (Finite)", communicationType);
//	}
//
//	public XSEventToXLogReader(String name, final CommunicationType communicationType) {
//		super(name, communicationType);
//	}
//
//	/**
//	 * 
//	 * @see getCurrentResult
//	 */
//	@Deprecated
//	public static XLog extractXLogFromQueue(Queue<XSEvent> queue, boolean flushQueue) {
//		XFactory factory = new XFactoryNaiveImpl();
//		XExtensionManager xesExtensionManager = XExtensionManager.instance();
//		XLog log = factory.createLog();
//		log.getExtensions().add(xesExtensionManager.getByName("Lifecycle"));
//		log.getExtensions().add(xesExtensionManager.getByName("Organizational"));
//		log.getExtensions().add(xesExtensionManager.getByName("Time"));
//		log.getExtensions().add(xesExtensionManager.getByName("Concept"));
//		HashMap<String, LinkedList<XSEvent>> map = new HashMap<String, LinkedList<XSEvent>>();
//
//		for (XSEvent event : queue) {
//			String caseId = ((XAttributeLiteral) event.get(XSEventSignature.TRACE)).getValue();
//			if (map.containsKey(caseId)) {
//				map.get(caseId).add(event);
//			} else {
//				LinkedList<XSEvent> list = new LinkedList<XSEvent>();
//				list.add(event);
//				map.put(caseId, list);
//			}
//		}
//
//		for (String currentCase : map.keySet()) {
//			XTrace t = factory.createTrace();
//			t.getAttributes().put("concept:name", new XAttributeLiteralImpl("concept:name", currentCase));
//
//			for (XSEvent xsEvent : map.get(currentCase)) {
//				XEvent e = factory.createEvent();
//				for (String xsAttribute : xsEvent.keySet()) {
//					e.getAttributes().put(xsAttribute, xsEvent.get(xsAttribute));
//				}
//				t.add(e);
//			}
//
//			log.add(t);
//		}
//
//		if (flushQueue) {
//			queue.clear();
//		}
//
//		return log;
//	}
//
//	/**
//	 * @return the flush
//	 */
//	public boolean isFlush() {
//		return flush;
//	}
//
//	/**
//	 * @param flush
//	 *            the flush to set
//	 */
//	public void setFlush(boolean flush) {
//		this.flush = flush;
//	}
//
//	@Override
//	public void stop() {
//		super.stop();
//		//		this.socket.disconnect(this);
//		//		this.socket.stop();
//		//		this.socket = null;
//		this.busy = false;
//	}
//
//	@Override
//	public void workPackage() {
//		synchronized (getLock()) {
//			try {
//				getLock().wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public Class<XSEvent> getTopic() {
//		return XSEvent.class;
//	}
//
//	@Override
//	public Component visualize() {
//		return this.visualizer;
//	}
//
//	public XLog getCurrentResult() {
//		XFactory factory = new XFactoryNaiveImpl();
//		XExtensionManager xesExtensionManager = XExtensionManager.instance();
//		XLog log = factory.createLog();
//		log.getExtensions().add(xesExtensionManager.getByName("Lifecycle"));
//		log.getExtensions().add(xesExtensionManager.getByName("Organizational"));
//		log.getExtensions().add(xesExtensionManager.getByName("Time"));
//		log.getExtensions().add(xesExtensionManager.getByName("Concept"));
//		HashMap<String, LinkedList<XSEvent>> map = new HashMap<String, LinkedList<XSEvent>>();
//
//		for (XSEvent event : queue) {
//			String caseId = ((XAttributeLiteral) event.get(XSEventSignature.TRACE)).getValue();
//			if (map.containsKey(caseId)) {
//				map.get(caseId).add(event);
//			} else {
//				LinkedList<XSEvent> list = new LinkedList<XSEvent>();
//				list.add(event);
//				map.put(caseId, list);
//			}
//		}
//
//		for (String currentCase : map.keySet()) {
//			XTrace t = factory.createTrace();
//			t.getAttributes().put("concept:name", new XAttributeLiteralImpl("concept:name", currentCase));
//
//			for (XSEvent xsEvent : map.get(currentCase)) {
//				XEvent e = factory.createEvent();
//				for (String xsAttribute : xsEvent.keySet()) {
//					e.getAttributes().put(xsAttribute, xsEvent.get(xsAttribute));
//				}
//				t.add(e);
//			}
//
//			log.add(t);
//		}
//
//		if (flush) {
//			queue.clear();
//		}
//
//		return log;
//	}
//
//	protected void processDelivery(XSEvent packet) {
//		this.queue.add(packet);
//		this.visualizer.newMessageReceived(new Date());
//	}
//
//	protected XSEvent getNextPacket() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	protected void storeNewPacket(XSEvent packet) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	protected void handleNextPacket(XSEvent packet) {
//		// TODO Auto-generated method stub
//		
//	}
//
//}
