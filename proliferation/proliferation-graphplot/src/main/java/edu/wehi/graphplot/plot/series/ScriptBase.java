package edu.wehi.graphplot.plot.series;


import java.awt.Dimension;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;


import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.series.scriptables.Scriptable;

public abstract class ScriptBase<INPUT,OUTPUT> implements Scriptable<INPUT,OUTPUT>
{
	private static final long serialVersionUID = 1L;
	
	protected INPUT input;
	

	public ScriptBase()
	{
		this(null);
	}
	
	long id = UUID.randomUUID().getMostSignificantBits();
	
	public ScriptBase(INPUT input)
	{
		setInput(input);
	}
	
	@SuppressWarnings({ "unchecked" })
	public void setInput(INPUT input)
	{
		this.input = input;
		
		// TODO clean this up one day (might not be possible in java)
		
		if (input == null)
		{
			// skip
		}
		else if (input instanceof List)
		{
			for (Object obj : (List<GPDataNode<?>>) input)
			{
				if (obj instanceof GPDataNode<?>)
				{
					((GPDataNode<?>) obj).setParentScript(this);
				}
			}
		}
		else if (input instanceof GPDataNode)
		{
			((GPDataNode<?>) input).setParentScript(this);
		}
		else
		{
			throw new RuntimeException("Input is of the wrong type");
		}
	}
	
	public OUTPUT getOutput()
	{
		return script(input);
	}
	
	public INPUT getInput()
	{
		return input;
	}
	
	public String getName()
	{
		return this.getClass().getName() +"/" + id;
	}
	
	@Override
	public void showDocComp()
	{
		String latex = getDoc();
		TeXFormula formula = new TeXFormula(latex);
		TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY)
				.setSize(16)
				.setWidth(TeXConstants.UNIT_PIXEL, 256f, TeXConstants.ALIGN_CENTER)
				.setIsMaxWidth(true).setInterLineSpacing(TeXConstants.UNIT_PIXEL, 20f)
				.build();
		
		final JLabel label = new JLabel(icon);
		label.setMaximumSize(new Dimension(100,300));
		label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		
		JOptionPane.showMessageDialog(null, label, "My custom dialog", JOptionPane.PLAIN_MESSAGE);
	}
	
	public String getDoc()
	{
		return "\\text{TODO}";
	}

}
