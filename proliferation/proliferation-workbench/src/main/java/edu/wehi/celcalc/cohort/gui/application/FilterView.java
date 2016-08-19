package edu.wehi.celcalc.cohort.gui.application;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.wehi.GUI;
import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.swing.GBHelper;
import edu.wehi.swing.checkboxtable.CheckBoxTable;

public  class FilterView extends JPanel
{

	private static final long serialVersionUID = 1L;

	final JLabel 			lblTitle			= new JLabel("Filter Measurements");
	final JLabel 			lblCellType 		= new JLabel("Cell Types:");
	final JLabel 			lblTimeFrom 		= new JLabel("Time min:");
	final JLabel 			lblTimeTo 			= new JLabel("to:");
	final JLabel 			lblTreatments 		= new JLabel("Treatments:");
	final JLabel 			lblDivFrom 			= new JLabel("Div from:");
	final JLabel 			lblCellCountFrom 	= new JLabel("Cell Count min:");
	final JLabel 			lblCellCountTo 		= new JLabel("Max:");
	final JLabel 			lblDivTo 			= new JLabel("Max:");
	final JLabel			lblMeasurementsNo	= new JLabel("Measurements:");
	final JLabel			lblMesNo			= new JLabel("0/0");

	final List<JCheckBox> 	checkboxs 			= makeCheckBoxs();
	final JTextField 		txtTimeFrom 		= new JTextField();
	final JTextField 		txtTimeTo 			= new JTextField();
	final JTextField 		txtTreatments 		= new JTextField();
	final JTextField 		txtDivionFrom 		= new JTextField();
	final JTextField 		txtDivionTo 		= new JTextField();
	final JTextField 		txtCellFrom 		= new JTextField();
	final JTextField 		txtCellTo			= new JTextField();
	final JButton 			btnHelpTime 		= new JButton("?");

	final JCheckBox			btnIsNoTreatmentFiltered = new JCheckBox("all");
	CheckBoxTable 			tableTreatments 			 = new CheckBoxTable(new String[]{},"treatment", "on");
	
	private static final int BORDER = 12; // Window border in pixels.

	public FilterView()
	{
		
		super(new GridBagLayout());
		setBorder(BorderFactory.createEmptyBorder(BORDER, BORDER, BORDER,BORDER));
		
		GBHelper pos = new GBHelper();
		
						add(lblTitle,pos.width().align(GBHelper.CENTER));
		add(lblCellType,pos.nextRow());			add(getCheckboxPanel(),pos.nextCol().width(4).expandW()); 
		add(lblTreatments, pos.nextRow()); pos.anchor=GridBagConstraints.EAST;	;add(txtTreatments, pos.nextCol().width(2)); add(btnIsNoTreatmentFiltered, pos.nextCol().nextCol().width(1));
		add(lblTimeFrom, pos.nextRow()); 	add(txtTimeFrom, pos.nextCol().expandW()); add(lblTimeTo, pos.nextCol()); add(txtTimeTo, pos.nextCol().expandW());	
		add(lblDivFrom, pos.nextRow()); 	add(txtDivionFrom, pos.nextCol().expandW()); add(lblDivTo, pos.nextCol()); add(txtDivionTo, pos.nextCol().expandW());
		add(lblCellCountFrom, pos.nextRow()); 	add(txtCellFrom, pos.nextCol().expandW()); add(lblCellCountTo, pos.nextCol()); add(txtCellTo, pos.nextCol().expandW());
		add(lblMeasurementsNo, pos.nextRow());	add(lblMesNo, pos.nextCol());
		
		txtTreatments.addMouseListener(new TreatmentSelections());
		
		addInputChangedActionListener(e ->syncFilter());
		btnIsNoTreatmentFiltered.addActionListener(e -> btnIsNoTreatmentsFilteredAction());
		txtTreatments.setEnabled(false);
	}

	
	private void btnIsNoTreatmentsFilteredAction()
	{
		tableTreatments.setEnabled(!btnIsNoTreatmentFiltered.isSelected());
		txtTreatments.setEnabled(!btnIsNoTreatmentFiltered.isSelected());
	}


	private synchronized void  syncFilter()
	{
		if (filter == null)
		{
			return;
		}
		filter.setNewValues(this.getSelectionOptions());
	}


	JPanel pnlBoxs = null;
	public JPanel getCheckboxPanel()
	{
		if (pnlBoxs == null)
		{
			pnlBoxs = new JPanel();
			checkboxs.forEach(box -> pnlBoxs.add(box));
		}
		
		return pnlBoxs;
	}

	public static List<JCheckBox> makeCheckBoxs()
	{
		List<JCheckBox> checks = new ArrayList<>();
		for (CellType type : CellType.values())
		{
			JCheckBox box = new JCheckBox(type.name());
			box.setSelected(true);
			checks.add(box);
		}
		return checks;
	}
	
	
	public synchronized  void setSelectionOptions(Filter filter)
	{
		isNotifyingChanges = false;
		resetViewNoClearTreatments();
		
		if (filter == null)
		{
			isNotifyingChanges = true;
			return;
		}
		
		if (filter.getTypes()!= null)
		{
			checkboxs.forEach(b -> {b.setSelected(false);});
			checkboxs.forEach(b ->
			{
				for (CellType t : filter.getTypes())
				{
					if (b.getText().equals(t.name()))
					{
						b.setSelected(true);
					}
				}
			});
		}
		else
		{
			checkboxs.forEach(b -> {b.setSelected(true);});
		}
		
		if (filter.getTypes() != null)
		{
			for (CellType type :filter.getTypes())
			{
				for (JCheckBox box : checkboxs)
				{
					if (box.getText().equals(type.name()))
					{
						box.setSelected(true);
					}
				}
			}
		}

		setJTextFieldTextDectorated(txtTimeFrom, 	filter.getTimeFrom());
		setJTextFieldTextDectorated(txtTimeTo, 		filter.getTimeTo());
		setJTextFieldTextDectorated(txtDivionFrom, 	filter.getDivFrom());
		setJTextFieldTextDectorated(txtDivionTo, 	filter.getDivTo());
		setJTextFieldTextDectorated(txtCellFrom, 	filter.getCountFrom());
		setJTextFieldTextDectorated(txtCellTo, 		filter.getCountTo());
		
		List<String> treatments = filter.getTreatments();
		if (treatments != null)
		{
			StringBuilder sb = new StringBuilder();
			treatments.forEach(t -> sb.append(t + ", "));
			setJTextFieldTextDectorated(txtTreatments,  sb.toString());
			btnIsNoTreatmentFiltered.setSelected(false);
			tableTreatments.setEnabled(true);
		}
		else
		{
			btnIsNoTreatmentFiltered.setSelected(true);
			tableTreatments.setEnabled(false);
		}
		
		tableTreatments.setSelectedNoFireChange(false);
		tableTreatments.setSelectedNoFireChange(filter.getTreatments(), true);
		
		isNotifyingChanges = true;
	}
	
	public void resetViewNoClearTreatments()
	{
		checkboxs.forEach(b -> b.setSelected(true));
		txtTimeFrom.setText("");
		txtTimeTo.setText("");
		txtTreatments.setText("");
		txtDivionFrom.setText("");
		txtDivionTo.setText("");
		txtCellFrom.setText("");
		txtCellTo.setText("");
		txtTimeFrom.setText("");
		btnIsNoTreatmentFiltered.setSelected(true);
		tableTreatments.setSelected(false);
	}


	Filter filter = null;
	public synchronized  void sync(Filter filter)
	{
		if (filter == null)
		{
			setEnabledControls(false);
		}
		else
		{
			setEnabledControls(true);
		}
		this.filter = filter;
		setSelectionOptions(filter);
		
		
		if (filter == null)
		{
			return;
		}
		
		if (filter.getTreatments() == null)
		{
			txtTreatments.setText("");
			txtTreatments.setEnabled(false);
			btnIsNoTreatmentFiltered.setSelected(true);
		}
		else
		{
			btnIsNoTreatmentFiltered.setSelected(false);
		}
		
	}
	
	public void setEnabledControls(boolean b)
	{
		checkboxs.forEach(box -> box.setEnabled(b));
		txtTimeFrom.setEnabled(b);
		txtTimeTo.setEnabled(b);
		//txtTreatments.setEnabled(b);
		txtDivionFrom.setEnabled(b);
		txtDivionTo.setEnabled(b);
		txtCellFrom.setEnabled(b);
		txtCellTo.setEnabled(b);
		txtTimeFrom.setEnabled(b);
		tableTreatments.setEnabled(b);
		btnIsNoTreatmentFiltered.setEnabled(b);
		setEnabled(b);
	}


	public static void setJTextFieldTextDectorated(JTextField txtField, Object txt)
	{
		if (txt == null)
		{
			txtField.setText("");
		}
		else
		{
			try
			{
				txtField.setText(txt.toString());
			}
			catch (Exception e)
			{
				txtField.setText(txt+"");
			}
			
		}
	}
	
	public synchronized  Filter getSelectionOptions()
	{
		
		final List<CellType> types = new ArrayList<>();
		for (JCheckBox box : checkboxs)
		{
			if (box.isSelected())
			{
				CellType type = CellType.valueOf(box.getText());
				types.add(type);
			}
		}
		
		
		List<String> treatments;
		if (btnIsNoTreatmentFiltered.isSelected())
		{
			treatments = null;
		}
		else
		{
			treatments = tableTreatments.getSelectedRowNames();
		}
		
		
		Double timeFrom = null;
		try
		{
			timeFrom =	txtTimeFrom		.getText().equals("")	? null : Double.parseDouble(txtTimeFrom	.getText());
		}
		catch (Exception e){}
		
		Double 	timeTo = null;
		try
		{
			timeTo = txtTimeTo		.getText().equals("") 	? null : Double.parseDouble(txtTimeTo	.getText());
		}
		catch (Exception e){}
		
		Integer divFrom = null;
		try
		{
			divFrom = 	txtDivionFrom	.getText().equals("") 	? null : Integer.parseInt(txtDivionFrom	.getText());
		}
		catch (Exception e){}
		
		Integer divTo = null;
		try
		{
			divTo = txtDivionTo		.getText().equals("") 	? null : Integer.parseInt(txtDivionTo	.getText());
		} catch (Exception e){}
		
		Double 	countFrom = null;
		
		try
		{
			countFrom =txtCellFrom .getText().equals("")	? null : Double.parseDouble(txtCellFrom	.getText());
		}
		catch(Exception e){}
		
		Double countTo = null;
		try
		{
			countTo = 	txtCellTo		.getText().equals("")	? null : Double.parseDouble(txtCellTo	.getText());
		}
		catch(Exception e){}

		return new Filter("",types, treatments, timeFrom, timeTo, divFrom, divTo, countFrom, countTo);
	}

	public boolean isNotifyingChanges = true;
	
	public synchronized  void addInputChangedActionListener(ActionListener al)
	{
		
		ActionListener alDecorated = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!isNotifyingChanges)
				{
					return;
				}
				al.actionPerformed(e);
			}
		};
		
		btnIsNoTreatmentFiltered.addActionListener(alDecorated);
		checkboxs.stream().forEach(b -> b.addActionListener(alDecorated));
		
		DocumentListener listener = new DocumentListener()
		{
			
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				if (!isNotifyingChanges)
				{
					return;
				}
				al.actionPerformed(new ActionEvent(this, -1, null));
			}
			
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				if (!isNotifyingChanges)
				{
					return;
				}
				al.actionPerformed(new ActionEvent(this, -1, null));
			}
			
			@Override
			public void changedUpdate(DocumentEvent e)
			{
				if (!isNotifyingChanges)
				{
					return;
				}
				al.actionPerformed(new ActionEvent(this, -1, null));
			}
		};
		
		txtTimeFrom.getDocument().addDocumentListener(listener);
		txtTimeTo.getDocument().addDocumentListener(listener);
		txtTreatments.getDocument().addDocumentListener(listener);
		txtDivionFrom.getDocument().addDocumentListener(listener);
		txtDivionTo.getDocument().addDocumentListener(listener);
		txtCellFrom.getDocument().addDocumentListener(listener);
		txtCellTo.getDocument().addDocumentListener(listener);
		txtTimeFrom.getDocument().addDocumentListener(listener);
		
		tableTreatments.getModel().addTableModelListener(new TableModelListener()
		{
			
			@Override
			public void tableChanged(TableModelEvent e)
			{
				if (!isNotifyingChanges)
				{
					return;
				}
				al.actionPerformed(new ActionEvent(this, -1, ""));
				syncTextTreatments();
			}
		});
	}
	
	public synchronized  void syncTextTreatments()
	{
		List<String>selections = tableTreatments.getSelectedRowNames();
		String txt = String.join(", ", selections);
		txtTreatments.setText(txt);
	}
	
	public void setFilteredText(String text)
	{
		lblMesNo.setText(text);
	}
	

	public synchronized  void resetView()
	{
		resetViewNoClearTreatments();
		tableTreatments.getCheckBoxTableModel().clearAll();
	}

	public void selectTreatmentsListener(MouseListener ml)
	{
		txtTreatments.addMouseListener(ml);
	}
	
	public void syncTreatments(List<String> treatments)
	{
		tableTreatments.getCheckBoxTableModel().setOptions(treatments);
	}
	
	boolean isTreatmentOpen = false;
	
	public class TreatmentSelections extends MouseAdapter
	{

		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (!tableTreatments.isEnabled() | btnIsNoTreatmentFiltered.isSelected())
			{
				return;
			}
			
			if (isTreatmentOpen == true)
			{
				return;
			}
			
			JPanel pnl = new JPanel(new BorderLayout());
			pnl.add(new JScrollPane(tableTreatments), BorderLayout.CENTER);
			JOptionPane.showMessageDialog(null,pnl,"Pick Treatments",JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	public static void main(String[] args)
	{
		FilterView view = new FilterView();
		List<String> treatments = new ArrayList<>();
		treatments.add("hello");
		treatments.add("goodbye");
		view.syncTreatments(treatments);
		List<CellType> types = new ArrayList<>();
		types.add(CellType.DEAD);
		Filter oldFilter = new Filter(null,types,null,null,null,null,null,null,0.0);
		view.sync(new Filter(null,null,null,null,null,null,null,null,0.0));
		System.out.println(oldFilter);
		GUI.gui(view);
	}

}
