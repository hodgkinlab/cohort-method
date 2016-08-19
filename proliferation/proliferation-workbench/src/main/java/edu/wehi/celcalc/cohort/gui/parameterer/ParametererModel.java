package edu.wehi.celcalc.cohort.gui.parameterer;

import edu.wehi.celcalc.cohort.gui.parameterer.type.InputParametersSet;

public class ParametererModel
{

	final InputParametersSet parameters;

	public ParametererModel(InputParametersSet parameters)
	{
		super();
		this.parameters = parameters;
	}

	public InputParametersSet getParameters()
	{
		return parameters;
	}
	
}
