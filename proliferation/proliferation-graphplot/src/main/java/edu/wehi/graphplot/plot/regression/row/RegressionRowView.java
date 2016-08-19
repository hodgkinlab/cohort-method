package edu.wehi.graphplot.plot.regression.row;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.AbstractValueModel;

import edu.wehi.GUI;
import edu.wehi.swing.GBHelper;

public class RegressionRowView  implements PropertyChangeListener
{

	
	JLabel lblName = null;
	JButton btnColor = null;
	JFormattedTextField txtMid = null;
	JTextField txtDiv1 = null;
	JTextField txtDiv2 = null;
	JLabel lblDivisonDestiny = null;
	JLabel lblA = null;
	JLabel lblB = null;
	JLabel lblSSE = null;
	JCheckBox isShowingRegression = null;
	JCheckBox isShowingData = null;
	JCheckBox isShowingSpline = null;
	JCheckBox isRaw = null;
	JCheckBox btnHideShow = new JCheckBox();
	JButton btnReg = new JButton();
	
	final RegressionRowModel model;
	final PresentationModel<RegressionRowModel> presentationModel;
	final BeanAdapter<RegressionRowModel> adapter;
	
	static final DecimalFormat df = new DecimalFormat(".###");
	
	
	public RegressionRowView(RegressionRowModel model)
	{
		this.model = model;
		presentationModel = new PresentationModel<RegressionRowModel>(model);
		model.addPropertyChangeListener(this);
		adapter = new BeanAdapter<RegressionRowModel>(model, true);
		
		btnHideShow.setSelected(true);
		btnHideShow.addActionListener(e->deselectButtons());
	}
	
	public void deselectButtons()
	{
		isShowingRegression.setSelected(btnHideShow.isSelected());
		isShowingData.setSelected(btnHideShow.isSelected());
		isShowingSpline.setSelected(btnHideShow.isSelected());
		isRaw.setSelected(btnHideShow.isSelected());		
	}
	
	public void addBtnRegActionListiner(ActionListener al)
	{
		btnReg.addActionListener(al);
	}
	
	public JLabel getLblDivisionDestiny()
	{
		if (lblDivisonDestiny == null)
		{
			lblDivisonDestiny = new JLabel(df.format(model.getA()*model.getMiddleAsFloat()+ model.getB()));
		}
		return lblDivisonDestiny;
	}
	
	public void addToComponent(Container container, GBHelper gb)
	{
		container.add(getLabelName(), gb.nextRow().align(GBHelper.EAST));
		container.add(getColorButton(), gb.nextCol());
//		container.add(getMidSlider(), gb.nextCol());
//		container.add(getTxtMid(), gb.nextCol());
//		container.add(getDiv1(), gb.nextCol());
//		container.add(getDiv2(), gb.nextCol());
//		container.add(getLblDivisionDestiny(), gb.nextCol());
//		container.add(getSse(), gb.nextCol());
//		container.add(getA(), gb.nextCol());
//		container.add(getB(), gb.nextCol());
//		container.add(getIsShowingRegression(), gb.nextCol());
		container.add(getIsShowingData(), gb.nextCol());
//		container.add(getIsShowingSpline(), gb.nextCol());
//		container.add(getRaw(), gb.nextCol());
//		container.add(btnHideShow, gb.nextCol());
//		container.add(btnReg, gb.nextCol());
	}
	
	JButton getColorButton()
	{
		if (btnColor == null)
		{
			btnColor = new JButton();
			setBtnColor(model.getColor());
			
			btnColor.setUI(new MyButtonUI());
			
			btnColor.addActionListener(e -> colorAction(e));
			
		}
		return btnColor;
	}
	
	private class MyButtonUI extends BasicButtonUI
	{
	    public void paint ( Graphics g, JComponent c )
	    {
	        JButton myButton = ( JButton ) c;
	        ButtonModel buttonModel = myButton.getModel ();

	        if ( buttonModel.isPressed () || buttonModel.isSelected () )
	        {
	            g.setColor ( Color.GRAY );
	        }
	        else
	        {
	            g.setColor ( btnColor.getBackground());
	        }
	        g.fillRect ( 0, 0, c.getWidth (), c.getHeight () );

	        super.paint ( g, c );
	    }
	}
	
	public void setBtnColor(Color color)
	{
		btnColor.setBackground(color);
		btnColor.setForeground(color);
	}
	
	private void colorAction(ActionEvent e)
	{
		Color color = JColorChooser.showDialog(null, "Choose a Color", Color.GRAY);
		if (color == null) return;
		model.setColor(color);
		if (colorBtnListiner != null)  colorBtnListiner.actionPerformed(e);
	}
	
	public static void addHeadings(Container container, GBHelper gb)
	{
		//container.add(new JLabel("Name"), gb.nextRow().align(GBHelper.EAST));
		//container.add(new JLabel("Color"), gb.nextCol());
//		container.add(new JLabel("----"), gb.nextCol().align(GBHelper.CENTER));
//		container.add(new JLabel("mid"), gb.nextCol());
//		container.add(new JLabel("div1"), gb.nextCol());
//		container.add(new JLabel("div2"), gb.nextCol());
//		container.add(new JLabel("div dest."), gb.nextCol());
//		container.add(new JLabel("SSE"), gb.nextCol());
//		container.add(new JLabel("a"), gb.nextCol());
//		container.add(new JLabel("b"), gb.nextCol());
//		container.add(new JLabel("reg"), gb.nextCol());
		//container.add(new JLabel("data"), gb.nextCol());
//		container.add(new JLabel("spline"), gb.nextCol());
//		container.add(new JLabel("raw"), gb.nextCol());
//		container.add(new JLabel("hide/show"), gb.nextCol());
//		container.add(new JLabel("opt"), gb.nextCol());
	}
	
	public JLabel getA()
	{
		if (lblA == null)
		{
			lblA = new JLabel(df.format(model.getA()));
			lblA.setPreferredSize(new Dimension(75,20));
		}
		return lblA;
	}
	
	public JLabel getB()
	{
		if (lblB == null)
		{
			lblB = new JLabel(df.format(model.getB()));
			lblB.setPreferredSize(new Dimension(75,20));
		}
		return lblB;
	}
	
	JLabel getLabelName()
	{
		if (lblName == null)
		{
			lblName = new JLabel(model.getName());
			lblName.setPreferredSize(new Dimension(100,20));
			lblName.setMinimumSize(new Dimension(100,20));
			lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblName;
	}
	
	
	JCheckBox getIsShowingRegression()
	{
		if (isShowingRegression == null)
		{
			isShowingRegression = BasicComponentFactory.createCheckBox(
	                presentationModel.getModel(RegressionRowModel.ISSHOWINGREGRESSIN_PROPERTY), 
	                "");
			isShowingRegression.setSelected(model.isShowingRegression());
		}
		return isShowingRegression;
	}
	
	JCheckBox getIsShowingData()
	{
		if (isShowingData == null)
		{
			isShowingData = BasicComponentFactory.createCheckBox(
	                presentationModel.getModel(RegressionRowModel.ISSHOWINGDATA_PROPERTY), 
	                "");
			isShowingData.setSelected(model.isShowingData());
		}
		return isShowingData;
	}

	JCheckBox getIsShowingSpline()
	{
		if (isShowingSpline == null)
		{
			isShowingSpline = BasicComponentFactory.createCheckBox(
	                presentationModel.getModel(RegressionRowModel.SHOWINGSPLINE_PROPERTY), 
	                "");
			isShowingSpline.setSelected(model.isShowingSpline());
		}
		return isShowingSpline;
	}
	
	public JCheckBox getRaw()
	{
		if (isRaw == null)
		{
			isRaw = BasicComponentFactory.createCheckBox(
	                presentationModel.getModel(RegressionRowModel.SHOWINGRAW_PROPERTY), 
	                "");
			isRaw.setSelected(model.isShowingRaw());
		}
		return isRaw;
	}
	
	JTextField getDiv1()
	{
		if (txtDiv1 == null)
		{
			txtDiv1 = BasicComponentFactory.createFormattedTextField(adapter.getValueModel(RegressionRowModel.DIV1_PROPERTY), df);
			txtDiv1.setPreferredSize(new Dimension(40,20));
		}
		return txtDiv1;
	}
	
	JTextField getDiv2()
	{
		if (txtDiv2 == null)
		{
			txtDiv2 = BasicComponentFactory.createFormattedTextField(adapter.getValueModel(RegressionRowModel.DIV2_PROPERTY), df);
			txtDiv2.setPreferredSize(new Dimension(40,20));
		}
		return txtDiv2;
	}
	
	JLabel getSse()
	{
		if (lblSSE == null)
		{
			lblSSE = BasicComponentFactory.createLabel(adapter.getValueModel(RegressionRowModel.SSE_PROPERTY), df);
			lblSSE.setText(df.format(model.getSse()));
			lblSSE.setPreferredSize(new Dimension(40,20));
		}
		return lblSSE;
	}
	
	
	JFormattedTextField getTxtMid()
	{
		if (txtMid == null)
		{
			txtMid = new JFormattedTextField();
			txtMid.setEditable(false);
			txtMid.setPreferredSize(new Dimension(75,20));
		}
		return txtMid;
	}
	
	JSlider sliderMid = null;
	JSlider getMidSlider()
	{
		if (sliderMid == null)
		{
			AbstractValueModel midValueModel = presentationModel.getModel(RegressionRowModel.MID_PROPERTY);
			BoundedRangeAdapter midValueRangeModel = new BoundedRangeAdapter(midValueModel, 0, 0, RegressionRowModel.N);
			sliderMid = new JSlider(midValueRangeModel);
			getTxtMid().setText(df.format(model.getMiddleAsFloat()));
		}
		return sliderMid;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0)
	{
		System.out.println("PROPERTY CHANGE: \t"+arg0.getPropertyName() + "\t" + arg0.getNewValue());
		System.out.println(model);
		System.out.println("\n\n");
//		lblDivisonDestiny.setText(df.format(model.getA()*model.getMiddleAsFloat()+ model.getB()));
		switch(arg0.getPropertyName())
		{
		case RegressionRowModel.MID_PROPERTY:
		case RegressionRowModel.DIV1_PROPERTY:
		case RegressionRowModel.DIV2_PROPERTY:
			syncObs();
			break;
		case RegressionRowModel.COLOR_PROPERTY:
			setBtnColor((Color) arg0.getNewValue());
			break;
		default:
			break;
		}
	}
	
	public void syncObs()
	{
		txtMid.setText(df.format(model.getMiddleAsFloat()));
		lblA.setText(df.format(model.getA()));
		lblB.setText(df.format(model.getB()));
	}
	
	public static void main(String[] args) throws Exception
	{
		RegressionRowModel model = new RegressionRowModel("name", Color.GREEN, 900, 20, 234, 00.1, 9, 9, false, true, false);
		JPanel pnl = new JPanel(new GridBagLayout());
		RegressionRowView view = new RegressionRowView(model);
		GBHelper gb = new GBHelper();
		addHeadings(pnl, gb);
		view.addToComponent(pnl, gb);
		GUI.gui(pnl);
		
		Thread.sleep(3000);
		model.setMid(100);
	}

	ActionListener colorBtnListiner = null;
	public void addBtnColorActionListiner(ActionListener colorBtnListiner)
	{
		if (colorBtnListiner != null) this.colorBtnListiner = colorBtnListiner;
	}

}
