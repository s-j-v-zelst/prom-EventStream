package org.processmining.eventstream.readers.abstr;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.processmining.basicutils.parameters.impl.PluginParametersImpl;
import org.processmining.eventstream.connections.XSEventXSAuthorXSStreamConnectionImpl;
import org.processmining.eventstream.core.interfaces.XSEventSignature;

public class XSEventReaderParameters extends PluginParametersImpl {

	private String activityIdentifier = XConceptExtension.KEY_NAME;
	private String caseIdentifier = XSEventSignature.TRACE;
	private XSEventXSAuthorXSStreamConnectionImpl connection = null;
	private int refreshRate = -1;

	public XSEventReaderParameters() {
		super();
	}

	public XSEventReaderParameters(final XSEventReaderParameters params) {
		this.caseIdentifier = new String(params.getCaseIdentifier());
		this.activityIdentifier = new String(params.getActivityIdentifier());

	}

	public XSEventReaderParameters(final String caseIdentifier, final String activityIdentifier) {
		this.caseIdentifier = new String(caseIdentifier);
		this.activityIdentifier = new String(activityIdentifier);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof XSEventReaderParameters) {
			XSEventReaderParameters c = (XSEventReaderParameters) o;
			if (!c.getCaseIdentifier().equals(getCaseIdentifier()))
				return false;
			if (!c.getActivityIdentifier().equals(getActivityIdentifier()))
				return false;
			if (!(c.getRefreshRate() == getRefreshRate()))
				return false;
			return true;
		}
		return false;
	}

	public String getActivityIdentifier() {
		return activityIdentifier;
	}

	public String getCaseIdentifier() {
		return caseIdentifier;
	}

	/**
	 * @return the connection
	 */
	public XSEventXSAuthorXSStreamConnectionImpl getConnection() {
		return connection;
	}

	public int getRefreshRate() {
		return refreshRate;
	}

	public void setActivityIdentifier(final String activityIdentifier) {
		this.activityIdentifier = new String(activityIdentifier);
	}

	public void setCaseIdentifier(final String caseIdentifier) {
		this.caseIdentifier = new String(caseIdentifier);
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(XSEventXSAuthorXSStreamConnectionImpl connection) {
		this.connection = connection;
	}

	public void setRefreshRate(int refreshRate) {
		this.refreshRate = refreshRate;
	}

}
