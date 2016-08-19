package edu.wehi.celcalc.cohort.scriptables;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.io.FileUtils;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.gui.scripter.ScripterModel;
import edu.wehi.graphplot.plot.GPDataNode;
import edu.wehi.graphplot.plot.series.ScriptBase;
import edu.wehi.graphplot.plot.series.scriptseries.ScriptTypeDifference;

public class MultiPurposePythonScriptBase extends ScriptBase<List<Measurement>, List<GPDataNode<SeriesAndTable>>>
implements ScriptTypeDifference<
List<Measurement>, 
List<GPDataNode<SeriesAndTable>>,
List<Measurement>,
List<SeriesAndTable>>,
MapAndSeriesScript

{

	private static final long serialVersionUID = 1L;
	
	String code;
	
	String fileName="";
	
	public MultiPurposePythonScriptBase(String code, String fileName)
	{
		this.code = code;
		this.fileName = fileName;
	}
	
	public MultiPurposePythonScriptBase(File file)
	{
		try
		{
			this.code = FileUtils.readFileToString(file);
			fileName = file.getName();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new RuntimeException("File contains bad code!");
		}
	}

	@Override
	public List<SeriesAndTable> scriptxy(List<Measurement> measurements)
	{
		ScripterModel interp =  new ScripterModel(measurements);
		try
		{
			List<SeriesAndTable> result = interp.executeIterCodeTableAndDic(code);
			return result;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Measurement> convertInput(List<Measurement> input)
	{
		return input;
	}

	@Override
	public List<GPDataNode<SeriesAndTable>> convertOutput(List<SeriesAndTable> out)
		{
		List<GPDataNode<SeriesAndTable>> output = new ArrayList<>();
		for (SeriesAndTable o: out)
		{
			output.add(new GPDataNode<SeriesAndTable>(o));
		}
		return output;
	}

	@Override
	public String getName()
	{
		return fileName;
	}
	
	@Override
	public String getDoc()
	{
		String[] splitted = code.split("\"\"\"");
		if (splitted.length > 1)
		{
			System.out.println(splitted[1]);
			return splitted[1];
		}
		else
		{
			return "\\text{NO Doc}";
		}
	}
	
	@Override
	public void showDocComp()
	{
		
		JPanel pnlMain = new JPanel(new BorderLayout());
		
		String latex = getDoc();
		
		try
		{
			TeXFormula formula = new TeXFormula(latex);
			TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY)
					.setSize(16)
					.setWidth(TeXConstants.UNIT_PIXEL, 256f, TeXConstants.ALIGN_CENTER)
					.setIsMaxWidth(true).setInterLineSpacing(TeXConstants.UNIT_PIXEL, 20f)
					.build();
			final JLabel label = new JLabel(icon);
			label.setMaximumSize(new Dimension(100,300));
			label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		pnlMain.add(label, BorderLayout.NORTH);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			
			pnlMain.add(new JLabel("Error: couldn't pass latex text")
			{
				private static final long serialVersionUID = 1L;
				{
					setForeground(Color.red);
				}
				
			}, BorderLayout.NORTH);
		}
		
		

		
		
		JEditorPane editorPane = new JEditorPane();
		JScrollPane sp = new JScrollPane(editorPane);
		editorPane.setContentType("text/python");
		editorPane.setEditable(false);
		pnlMain.add(sp, BorderLayout.CENTER);
		editorPane.setText(code);
		
		JOptionPane.showMessageDialog(null, pnlMain, "My custom dialog", JOptionPane.PLAIN_MESSAGE);
	}
	
	public static void main(String[] args)
	{
		String[] string = "\"\"\"Hello here is some doc \"\"\" What is doing ".split("\"\"\"");
		
		int i =0;
		for (String s : string)
		{
			System.out.println(i+"-----"+s);
			i++;
		}
		
	}

}
