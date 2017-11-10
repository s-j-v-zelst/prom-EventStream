package org.processmining.eventstream.core.implementations;

import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.processmining.eventstream.core.factories.XSEventSignatureFactory;
import org.processmining.eventstream.core.interfaces.XSEvent;
import org.processmining.eventstream.core.interfaces.XSEventSignature;
import org.processmining.eventstream.core.interfaces.XSEventStream;
import org.processmining.framework.util.Pair;
import org.processmining.stream.core.abstracts.AbstractXSStream;
import org.processmining.stream.core.abstracts.AbstractXSVisualization;
import org.processmining.stream.core.annotations.XSStreamTopic;
import org.processmining.stream.core.enums.CommunicationType;
import org.processmining.stream.core.interfaces.XSVisualization;

@XSStreamTopic(value = XSEvent.class)
public class XSEventStreamImpl extends AbstractXSStream<XSEvent> implements XSEventStream {

	public XSEventStreamImpl(final CommunicationType communicationType) {
		this(XSEventSignatureFactory.getStandardXSEventSignature(), communicationType);
	}

	// this constructor is for reflection based construction of the event streams (which is slow)
	public XSEventStreamImpl(final XSEventSignature signature, final CommunicationType communciationType) {
		super("XSEvent Stream", signature, communciationType);
	}

	public Class<XSEvent> getTopic() {
		return XSEvent.class;
	}

	public XSVisualization<?> getVisualization() {
		return new AbstractXSVisualization<Object>("Dummy Visualization") {

			public JComponent asComponent() {
				return new JPanel();
			}

			protected void workPackage() {
				this.interrupt();
			}

			public void update(Pair<Date, String> message) {
			}

			public void update(String object) {

			}

			public void updateVisualization(Pair<Date, Object> newArtifact) {
			}

			public void updateVisualization(Object newArtifact) {
			}
		};
	}
}
