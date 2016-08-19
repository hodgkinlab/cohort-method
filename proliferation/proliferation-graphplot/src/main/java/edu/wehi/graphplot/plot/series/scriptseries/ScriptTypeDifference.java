package edu.wehi.graphplot.plot.series.scriptseries;

import edu.wehi.graphplot.plot.series.scriptables.Scriptable;

public interface ScriptTypeDifference<INPUT,OUTPUT,IN,OUT> extends Scriptable<INPUT,OUTPUT>
{

	@Override
	public default OUTPUT script(INPUT input)
	{
		return convertOutput(scriptxy(convertInput(input)));
	}
	
	OUT scriptxy(IN in);
	IN convertInput(INPUT input);
	OUTPUT convertOutput(OUT out);

}
