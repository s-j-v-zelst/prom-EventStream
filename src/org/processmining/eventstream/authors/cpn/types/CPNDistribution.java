package org.processmining.eventstream.authors.cpn.types;

/**
 * Enum describing all probabilistic distributions present in the CPN modeling
 * language. A distribution has a name, a specified number of input parameters
 * and a return type.
 * 
 * @author svzelst
 *
 */
public enum CPNDistribution {

	BERNOULLI("Bernoulli", 1, CPNStdColset.INT), BETA("Beta", 2, CPNStdColset.REAL), BINOMIAL("Binomial", 2,
			CPNStdColset.INT), CHISQ("Chi Squared", 1, CPNStdColset.REAL), DISCRETE("Discrete", 2, CPNStdColset.INT), ERLANG(
			"Erlang", 2, CPNStdColset.REAL), EXPONENTIAL("Exponential", 1, CPNStdColset.REAL), GAMMA("Gamma", 2,
			CPNStdColset.REAL), NORMAL("Normal", 2, CPNStdColset.REAL), POISSON("Poisson", 1, CPNStdColset.INT), RAYLEIGH(
			"Rayleigh", 1, CPNStdColset.REAL), STUDENT("Student", 1, CPNStdColset.REAL), UNIFORM("Uniform", 2,
			CPNStdColset.REAL);

	private String[] args = null;
	private String name;
	private int numArgs;
	private CPNStdColset returnType;

	CPNDistribution(String name, int numArgs, CPNStdColset returnType) {
		this.name = name;
		this.numArgs = numArgs;
		this.returnType = returnType;
	}

	public String[] getArgs() {
		return args;
	}

	public String getCPNDecl() {
		String decl = "";
		switch (this) {
			case CHISQ :
				decl += "chisq";
				break;
			default :
				decl += name.toLowerCase();
		}
		decl += "(";
		for (int i = 0; i < args.length; i++) {
			decl += args[i];
			if (i < args.length - 1) {
				decl += ",";
			}
		}
		decl += ")";
		return decl;
	}

	public String getName() {
		return name;
	}

	public CPNStdColset getReturnType() {
		return returnType;
	}

	public boolean setArgs(String... args) throws IllegalArgumentException {
		boolean result = false;
		if (args.length == numArgs) {
			this.args = args;
			result = true;
		} else {
			throw new IllegalArgumentException("Wrong number of arguments provided for a " + name
					+ " distribution. Given: " + args.length + ", needed: " + numArgs);
		}
		return result;
	}

}
