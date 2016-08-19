package edu.wehi.graphplot.plot.series;

public abstract class ScriptAdapter <INPUT,OUTPUT,IN,OUT> extends ScriptBase<INPUT,OUTPUT> 
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public OUTPUT script(INPUT input)
	{
		return handleOutput(scriptxy(handleInput(input)));
	}
	
	public abstract OUT scriptxy(IN in);
	public abstract OUTPUT handleOutput(OUT out);
	public abstract IN handleInput(INPUT in);
	
}
