package edu.wehi.celcalc.cohort.bell;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.wehi.GUI;
import edu.wehi.swing.GBHelper;

public class BellView extends JPanel
{

	private static final long serialVersionUID = 1L;
	
	boolean isUpdating = true;
	
	JPanel pnlPlot = new JPanel();
	JPanel pnlBottom = new JPanel();
	
	JLabel lblSigma = new JLabel("σ=");
	JTextField txtSigma = new JTextField();
	
	JLabel lblMu = new JLabel("μ=");
	JTextField txtMu = new JTextField();
	
	JLabel lblScale = new JLabel("Scale=");
	JTextField txtScale = new JTextField();
	
	JButton btnAuto = new JButton("Fit");
	
	final String treatment;
	final int div;
	final double time;
	
	public BellView(String treatment, int div, double time)
	{	
		Font font = new Font("Serif", Font.PLAIN, 10);
		
		this.treatment = treatment;
		this.div = div;
		this.time = time;
		
		lblMu.setFont(font);
		lblScale.setFont(font);
		lblSigma.setFont(font);
		
		txtMu.setFont(font);
		txtScale.setFont(font);
		txtSigma.setFont(font);
		
		lblMu.setBackground(Color.WHITE);
		lblMu.setOpaque(true);
		lblScale.setBackground(Color.WHITE);
		lblScale.setOpaque(true);
		lblSigma.setBackground(Color.WHITE);
		lblSigma.setOpaque(true);
		
		setLayout(new BorderLayout());
		add(pnlPlot, BorderLayout.CENTER);
		add(pnlBottom, BorderLayout.SOUTH);
		
		pnlBottom.setLayout(new GridBagLayout());
		GBHelper gb = new GBHelper();
		
		pnlBottom.add(lblMu, gb);
		pnlBottom.add(txtMu, gb.nextCol().expandW());
		
		pnlBottom.add(lblSigma, gb.nextCol());
		pnlBottom.add(txtSigma, gb.nextCol().expandW());
		
		pnlBottom.add(lblScale, gb.nextCol());
		pnlBottom.add(txtScale, gb.nextCol().expandW());
		
		pnlBottom.add(btnAuto, gb.nextCol());
		
		pnlPlot.setLayout(new BorderLayout());
		
		DocumentListener dl = new DocumentListener()
		{
			
			@Override public void removeUpdate(DocumentEvent e) {notifyBellables(); }
			@Override public void insertUpdate(DocumentEvent e) {notifyBellables(); }
			@Override public void changedUpdate(DocumentEvent e) {notifyBellables(); }
		};
		
		txtMu.getDocument().addDocumentListener(dl);
		txtSigma.getDocument().addDocumentListener(dl);
		txtScale.getDocument().addDocumentListener(dl);
	}
	
	List<Bellable> bellables = new ArrayList<>();
	public void addBellable(Bellable bellable)
	{
		bellables.add(bellable);
	}
	
	public void notifyBellables()
	{
		if (isUpdating) bellables.forEach(b -> b.bellChange(treatment, div, time, getMu(), getSigma(), getScale()));
	}
	
	public void plot(Component comp)
	{
		pnlPlot.removeAll();
		pnlPlot.add(comp, BorderLayout.CENTER);
		pnlPlot.revalidate();
		pnlPlot.repaint();
	}
	
	public void setSigma(String sigma)
	{
		isUpdating = false;
		txtSigma.setText(sigma);
		isUpdating = true;
		refresh();
	}
	
	public void setMu(String mu)
	{
		isUpdating = false;
		txtMu.setText(mu);
		isUpdating = true;
		refresh();
	}
	
	public void setScale(String scale)
	{
		isUpdating = false;
		txtScale.setText(scale);
		isUpdating = true;
		refresh();
	}
	
	private void refresh()
	{
		revalidate();
		repaint();
	}

	public String getSigma()
	{
		return txtSigma.getText();
	}
	
	public String getMu()
	{
		return txtMu.getText();
	}
	
	public String getScale()
	{
		return txtScale.getText();
	}
	
	public void addBtnFitActionListener(ActionListener al)
	{
		btnAuto.addActionListener(al);
	}
	
	public static void main(String[] args)
	{
		BellView view = new BellView("treatment", 2, 0.3);
		view.addBellable( new Bellable()
		{

			@Override
			public void bellChange(String treatment, int div, double time,
					String mu, String sigma, String scaleFactor)
			{
				System.out.println("hello");
			}
		});
		
		view.addBtnFitActionListener(e -> view.setMu("mu"));
		view.addBtnFitActionListener(e -> view.setSigma("sigma"));
		
		GUI.gui(view);
	}

}
