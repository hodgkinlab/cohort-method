package edu.wehi.celcalc.cohort.gui.parameterer.type;

import java.util.HashMap;
import java.util.Map;

public class InputParametersSet
{
	
	Map<String, InputParameters> inputParameters = new HashMap<>();
	
	public boolean addInputParameters(String name, InputParameters inputParameter)
	{
		if (inputParameters.containsKey(name))
		{
			return false;
		}
		else
		{
			inputParameters.put(name, inputParameter);
			return true;
		}
	}
	
	public Map<String, InputParameters> getInputParameters()
	{
		return inputParameters;
	}

}
