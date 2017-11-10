package org.processmining.eventstream.core.factories;

import org.processmining.eventstream.core.implementations.XSEventSignatureImpl;
import org.processmining.eventstream.core.interfaces.XSEventSignature;

public class XSEventSignatureFactory {

	public static XSEventSignature getStandardXSEventSignature() {
		return new XSEventSignatureImpl();
	}

}
