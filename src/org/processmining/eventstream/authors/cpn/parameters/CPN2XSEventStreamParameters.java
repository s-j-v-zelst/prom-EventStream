package org.processmining.eventstream.authors.cpn.parameters;

import org.processmining.basicutils.parameters.impl.PluginParametersImpl;
import org.processmining.stream.core.enums.CommunicationType;

public class CPN2XSEventStreamParameters extends PluginParametersImpl {

	private CPN2XSEventStreamCaseIdentification caseIdentification = CPN2XSEventStreamCaseIdentification.CPN_VARIABLE;
	private String caseIdentifier = CPN2XSEventStreamCaseIdentification.CPN_VARIABLE.getDefaultValue();
	private CommunicationType communicationType = CommunicationType.ASYNC;
	private int delayMs = 1;
	private boolean includeVariables = true;
	private int maxSteps = -1;
	private int numRepetitions = 1;
	private boolean ignorePage = false;
	private String[] ignorePatterns = new String[0];
	// seeds not fully supported by CPN Tools.
	//	private boolean useSeed = false;
	//	private long seed = -1;

	@Override
	public boolean equals(Object o) {
		boolean result = super.equals(o);
		if (result) {
			CPN2XSEventStreamParameters parameters = (CPN2XSEventStreamParameters) o;
			result &= parameters.getCaseIdentificationType().equals(getCaseIdentificationType())
					&& parameters.getCaseIdentifier().equals(getCaseIdentifier())
					&& parameters.getTransitionDelayMs() == getTransitionDelayMs()
					&& parameters.getMaximumNumberOfStepsPerRepetition() == getMaximumNumberOfStepsPerRepetition()
					&& parameters.getTotalNumberOfRepetitions() == getTotalNumberOfRepetitions()
					&& parameters.isIncludeVariables() == isIncludeVariables()
					&& parameters.getCommunicationType().equals(getCommunicationType())
					&& parameters.isIgnorePage() == isIgnorePage()
					&& parameters.getIgnorePatterns().equals(getIgnorePatterns());
			//			if (parameters.isUseSeed() && isUseSeed()) {
			//				result &= parameters.getSeed() == getSeed();
			//			}
		}
		return result;
	}

	public CPN2XSEventStreamCaseIdentification getCaseIdentificationType() {
		return caseIdentification;
	}

	/**
	 * @return the caseIdentifier
	 */
	public String getCaseIdentifier() {
		return caseIdentifier;
	}

	/**
	 * @return the communicationType
	 */
	public CommunicationType getCommunicationType() {
		return communicationType;
	}

	public String[] getIgnorePatterns() {
		return ignorePatterns;
	}

	/**
	 * @return the maxSteps
	 */
	public int getMaximumNumberOfStepsPerRepetition() {
		return maxSteps;
	}

	/**
	 * @return the numRepetitions
	 */
	public int getTotalNumberOfRepetitions() {
		return numRepetitions;
	}

	/**
	 * @return the delayMs
	 */
	public int getTransitionDelayMs() {
		return delayMs;
	}

	public boolean isIgnorePage() {
		return ignorePage;
	}

	/**
	 * @return the includeVariables
	 */
	public boolean isIncludeVariables() {
		return includeVariables;
	}

	public void setCaseIdentificationType(CPN2XSEventStreamCaseIdentification caseIdentification) {
		this.caseIdentification = caseIdentification;
	}

	/**
	 * @param caseIdentifier
	 *            the caseIdentifier to set
	 */
	public void setCaseIdentifier(String caseIdentifier) {
		this.caseIdentifier = caseIdentifier;
	}

	/**
	 * @param communicationType
	 *            the communicationType to set
	 */
	public void setCommunicationType(CommunicationType communicationType) {
		this.communicationType = communicationType;
	}

	public void setIgnorePage(boolean ignorePage) {
		this.ignorePage = ignorePage;
	}

	public void setIgnorePatterns(String[] ignorePatterns) {
		// avoid the empty string as an ignore pattern
		if (ignorePatterns.length == 1 && ignorePatterns[0].equals("")) {
			this.ignorePatterns = new String[0];
		} else {
			this.ignorePatterns = ignorePatterns;
		}
	}

	/**
	 * @param includeVariables
	 *            the includeVariables to set
	 */
	public void setIncludeVariables(boolean includeVariables) {
		this.includeVariables = includeVariables;
	}

	/**
	 * @param maxSteps
	 *            the maxSteps to set
	 */
	public void setMaximumNumberOfStepsPerRepetition(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	/**
	 * @param numRepetitions
	 *            the numRepetitions to set
	 */
	public void setTotalNumberOfRepetitions(int numRepetitions) {
		this.numRepetitions = numRepetitions;
	}

	/**
	 * @param delayMs
	 *            the delayMs to set
	 */
	public void setTransitionDelayMs(int delayMs) {
		this.delayMs = delayMs;
	}

	//	/**
	//	 * @return the seed
	//	 */
	//	public long getSeed() {
	//		return seed;
	//	}
	//
	//	/**
	//	 * @param seed
	//	 *            the seed to set
	//	 */
	//	public void setSeed(long seed) {
	//		this.seed = seed;
	//	}
	//
	//	/**
	//	 * @return the useSeed
	//	 */
	//	public boolean isUseSeed() {
	//		return useSeed;
	//	}
	//
	//	/**
	//	 * @param useSeed
	//	 *            the useSeed to set
	//	 */
	//	public void setUseSeed(boolean useSeed) {
	//		this.useSeed = useSeed;
	//	}

}
