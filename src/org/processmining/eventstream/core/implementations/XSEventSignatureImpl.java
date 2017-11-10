package org.processmining.eventstream.core.implementations;

import org.processmining.eventstream.core.interfaces.XSEventSignature;
import org.processmining.stream.core.abstracts.AbstractXSSignature;
import org.processmining.stream.core.annotations.XSStreamSignature;
import org.processmining.stream.core.interfaces.XSDataPacket;

@XSStreamSignature(value = XSEventSignature.class)
public final class XSEventSignatureImpl extends AbstractXSSignature implements XSEventSignature {

	private static final long serialVersionUID = -1631961820760983701L;

	@Override
	public boolean evaluate(XSDataPacket<?, ?> dataPacket) {
		if (dataPacket.keySet().contains(TRACE) && dataPacket.size() > 1) {
			return super.evaluate(dataPacket);
		}
		return false;
	}
}
