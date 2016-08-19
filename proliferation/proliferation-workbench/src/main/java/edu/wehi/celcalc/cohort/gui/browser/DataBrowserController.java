package edu.wehi.celcalc.cohort.gui.browser;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.wehi.GUI;
import edu.wehi.application.RecentFiles;
import edu.wehi.celcalc.cohort.data.CellType;
import edu.wehi.celcalc.cohort.data.Measurement;
import edu.wehi.celcalc.cohort.data.gui.Filter;
import edu.wehi.celcalc.cohort.data.gui.MeasurementStatistician;
import edu.wehi.celcalc.cohort.data.gui.MeasurementStatistics;
import edu.wehi.celcalc.cohort.data.gui.MeasurementTableModel;
import edu.wehi.celcalc.cohort.gui.application.FilterListener;
import edu.wehi.celcalc.cohort.gui.measurements.MeasurementsDialogConfirmActionListeners;
import edu.wehi.celcalc.cohort.gui.measurements.MeasurementsDialogController;
import edu.wehi.celcalc.cohort.plotting.PlotOptions;
import edu.wehi.celcalc.cohort.util.PROPERTIES;
import edu.wehi.graphplot.plot.GPChartType;
import edu.wehi.swing.FileDrop;
import edu.wehi.swing.checkboxtable.CheckBoxTable;
import edu.wehi.celcalc.cohort.data.Dataset;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class DataBrowserController
{
	final DataBrowserModel model;
	final DataBrowserView view;
	final DataBrowsingMenu mnu;
	
	PlotOptions currentPlot = PlotOptions.MEANTREATMENT;
	GPChartType currentScale = GPChartType.LINE;
	
	Filter currentFilter = null;
	
	final static int noRecentFiles = 10;
		
	static final String DEFAULT_SETTINGS_FILE = "settings.xml";
	String workspacePath = null;
	String lastDataFile = null;
	
	public DataBrowserController()
	{
		model = new DataBrowserModel();
		view = new DataBrowserView();
		mnu = new DataBrowsingMenu();
	}
	
	public DataBrowserController(
			DataBrowserModel model, DataBrowserView view, DataBrowsingMenu mnu)
	{
		this.model = model;
		this.view = view;
		this.mnu = mnu;

		model.initFromWorkspace(workspacePath);
		
		mnu.miImportExcel						.addActionListener(e -> importExcelDialog(true));
		mnu.miCreateNewFilter					.addActionListener(e -> newSelectionDialog());
		mnu.miNewMeasurements					.addActionListener(e -> newMeasurementsDialog());
		mnu.miSaveMeasurements					.addActionListener(e -> saveMeasurementsDialog());
		mnu.miOpenMeasurementsCSV				.addActionListener(e -> openMeasurementsCSVDialog());
		mnu.miSaveFilters						.addActionListener(e -> saveFiltersDialog());
		mnu.miOpenFilters						.addActionListener(e -> openFiltersDialog());
		
		mnu.miPlotMeanTreatmentLinear			.addActionListener(e -> syncPlot(PlotOptions.MEANTREATMENT, 			GPChartType.LINE, 		true));
		mnu.miPlotMeanTreatmentLogarythmic		.addActionListener(e -> syncPlot(PlotOptions.MEANTREATMENT,				GPChartType.LOG, 		true));
		mnu.miPlotMeanDivisionTreatmentLinear	.addActionListener(e -> syncPlot(PlotOptions.MEANTREATMENTDIVISION, 	GPChartType.LINE, 		true));
		mnu.miPlotMeanDivisionTreatmentLog		.addActionListener(e -> syncPlot(PlotOptions.MEANTREATMENTDIVISION, 	GPChartType.LOG, 		true));
		mnu.miPlotRawData						.addActionListener(e -> syncPlot(PlotOptions.RAWDATA, 					GPChartType.SCATTER,	true));
		mnu.miPlotCohort						.addActionListener(e -> syncPlot(PlotOptions.COHORT, 					GPChartType.LINE, 		true));
		mnu.mimiPlotCohortFitter				.addActionListener(e -> syncPlot(PlotOptions.COHORTFITTER, 				GPChartType.LINE, 		true));
		
		view.addOptionsChangedListener(e -> viewOptionsChanged());
		view.addSelectionTableModelListener(new MeasurementTableListener());
		view.addMeasurementTableMouseListener(new MeasurementTablePopup());
		
		view.addFilterRowSelectionListener(new FilterRowSelectionListener());		
	}
	
	private void openFiltersDialog()
	{
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.showOpenDialog(view);
		File[] file = fc.getSelectedFiles();
		boolean isSuccessfull = model.openOpenFilters(file);
		if (!isSuccessfull)
		{
			JOptionPane.showMessageDialog(view,
				    "Could not open Filters.",
				    "Failed to Filters.",
				    JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			syncView();
			syncFilterTable();
		}
	}

	private void openMeasurementsCSVDialog()
	{
		JFileChooser fc = new JFileChooser();
		fc.showOpenDialog(view);
		File file = fc.getSelectedFile();
		boolean isSuccessfull = model.openMeasurementsCSVFile(file.getAbsolutePath());
		if (!isSuccessfull)
		{
			JOptionPane.showMessageDialog(view,
				    "Could not open Measurements.",
				    "Failed to Open.",
				    JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			syncTreatments();
			syncView();
		}
	}
	
	public void saveMeasurementsDialog()
	{
		boolean isSaved = saveMeasurements();
		if (!isSaved)
		{
			JOptionPane.showMessageDialog(view,
				    "Could not save Measurements.",
				    "Failed to Save.",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public boolean saveMeasurements()
	{
		return model.saveAllMeasurements(workspacePath);
	}

	public void saveFiltersDialog()
	{
		boolean isSaved = saveFilters();
		if (!isSaved)
		{
			JOptionPane.showMessageDialog(view,
				    "Could not save Filters.",
				    "Failed to Save.",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public boolean saveFilters()
	{
		return model.saveAllFilters(workspacePath);
	}

	void syncPlot(PlotOptions type, GPChartType scale, boolean forceVisible) 
	{
		if (forceVisible != true && !framePlot.isVisible())
		{
			return;
		}
		
		currentPlot = type;
		currentScale = scale;
		syncPlotView(forceVisible);
	}
	
	
	final JPanel pnlPlot = new JPanel(new BorderLayout());
	final JFrame framePlot = new JFrame()
	{
		private static final long serialVersionUID = 1L;

		{
			setSize(500,500);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			add(pnlPlot);
			setAlwaysOnTop(true);
		}
		
	};
	
	public void syncPlotView(boolean forceVisible)
	{
		if (forceVisible != true && !framePlot.isVisible())
		{
			return;
		}

		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run() 
			{	
				pnlPlot.removeAll();
				pnlPlot.add(model.plot(currentPlot, currentScale,getSelectedMeasurements()), BorderLayout.CENTER);
				pnlPlot.revalidate();
				pnlPlot.repaint();
				if (forceVisible)
				{
					framePlot.setVisible(true);
				}
			}
		});
	}

	public void newMeasurementsDialog()
	{
		MeasurementsDialogController mesDialogController = new MeasurementsDialogController();

		final JFrame frame = mesDialogController.getView();
		mesDialogController
				.addMeasurementsDialogConfirmActionListener(new MeasurementsDialogConfirmActionListeners()
				{
					@Override
					public void notifiedOfNewMeasurements(List<Measurement> measurements)
					{
						controlerNotifiedOfNewMeasurements(measurements);
						frame.dispose();
					}
				});
		mesDialogController.getView().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GUI.guinofnoex(mesDialogController.getView());
	}
	
	public void controlerNotifiedOfNewMeasurements(List<Measurement> measurements)
	{
		model.addAllMeasurements(measurements);
		syncFilterTableKeepingSelections();
		syncView();
		syncTreatments();
	}


	public void syncFilterTableKeepingSelections()
	{
		List<String> selectedFilters = view.getSelectedFilters();
		syncFilterTable();
		view.setSelectedFilters(selectedFilters);
	}

	private class MeasurementTablePopup extends MouseAdapter
	{
		@Override
		public void mouseReleased(MouseEvent arg0)
		{
			if (arg0.isPopupTrigger())
			{
				measurementTablePopupAction(arg0);
			}
		}
	}
	
	protected void measurementTablePopupAction(MouseEvent arg0)
	{
		JPopupMenu menu = new JPopupMenu();
		JMenuItem delete = new JMenuItem("Delete Selected Measurements from Every Selection");
		delete.addActionListener(e -> delteSelectedMeasurementsInMeasurementTable());
		menu.add(delete);

		if (view.getMeasurementTable().getSelectedRowCount() != 0)
		{
			menu.show(view.getMeasurementTable(), arg0.getX(), arg0.getY());
		}
	}
	
	public void delteSelectedMeasurementsInMeasurementTable()
	{
		model.deleteMeasurements(view.getMeasurementTable().getSelectedMeasurements());
		syncView();
	}
	
	void viewOptionsChanged()
	{
		if (currentFilter == null)
		{
			return;
		}
		currentFilter.setNewValues(view.filterView.getSelectionOptions());
		syncWithoutQuery();
		syncQueryViewStats();
	}
	

	private void syncWithoutQuery()
	{
		syncImportRecentFiles();
		syncMeasurementTable();
		syncPlotView(false);
		syncStatsView();
	}
	
	public void syncView()
	{
		syncImportRecentFiles();
		syncMeasurementTable();
		syncQueryView();
		syncPlotView(false);
		syncStatsView();
	}
	
	public boolean importExcelDialog(boolean forceShow) {
		if (!forceShow) {
			if (lastDataFile != null && !lastDataFile.isEmpty()) {
				File selectedFile = new File(workspacePath, lastDataFile);
				return importFile(selectedFile);
			}
		}
		File selectedFile = selectExcelFile();
		if (selectedFile != null) {
			workspacePath = selectedFile.getParent();
			lastDataFile = selectedFile.getName();
			return importFile(selectedFile);
		}
		return false;
	}
	
	private File selectExcelFile()
	{
		final JFileChooser fc = new JFileChooser(workspacePath);
		fc.showOpenDialog(view);
		return fc.getSelectedFile();
	}
	
	public void syncQueryView()
	{
		syncQueryViewStats();
		view.filterView.setSelectionOptions(currentFilter);
	}
	
	public void syncQueryViewStats()
	{
		int selectedNo = getSelectedMeasurements().size();
		int total = model.getTotalNumberOfMeasurements();
		view.setFilteredText(selectedNo + "/" + total);
	}
	
	private class MeasurementTableListener implements TableModelListener
	{
		@Override
		public void tableChanged(TableModelEvent tme)
		{
			syncView();
		}
	}
	
	public void syncMeasurementTable()
	{
		List<Measurement> measurements = getSelectedMeasurements();
		MeasurementTableModel measurementmodel = new MeasurementTableModel(measurements);
		measurementmodel.addTableModelListener(e ->
		{
			syncView();
		});
		view.setMeasurementTableModel(measurementmodel);
	}
	
	public List<Measurement> getSelectedMeasurements()
	{
		if (model.getAllFilters().size() == 0)
		{
			return new ArrayList<>(model.getAllMeasurements());
		}
		List<String> selectedNames = view.getSelectedFilters();
		if (selectedNames.size() == 0)
		{
			return new ArrayList<>(model.getAllMeasurements());
		}
		return model.getSelection(selectedNames);
	}
	
	public Dataset getDataset() {
		return model.getDataset();
	}
	
	RecentFiles recentImportFiles = 		new RecentFiles(noRecentFiles, PROPERTIES.RECENTIMPORTFILENAME.fileName);
	
	public void syncImportRecentFiles()
	{
		List<JMenuItem> items = new ArrayList<>();
		recentImportFiles.getRecentFiles().forEach(
		f ->
		{
			JMenuItem itm = new JMenuItem(f.getName());
			itm.addActionListener(e -> importFile(f));
			items.add(itm);
		});
		mnu.setRecentImportFiles(items);
	}
	
	public void syncFilterTable()
	{
		List<String> selectionNames = new ArrayList<>(model.getSelectionNames());
		CheckBoxTable selectionTable = new CheckBoxTable(selectionNames,"filter", "inc.");
		view.setSelectionAndImportsTable(selectionTable);
	}
	
	public boolean importFile(File file)
	{
		boolean result = true;
		try
		{
			model.importExcelFile(file);

			recordRecentImportFile(file);
			//List<String> previousSelections = view.getSelectedFilters();
			syncFilterTable();
			
			//view.setSelectedFilters(previousSelections);
			view.setAllFiltersSelected();
			
			syncTreatments();
			syncView();
		}
		catch (Exception e)
		{
			showFileImportError(e.getMessage());
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	public void syncTreatments()
	{
		view.setTreatmentOptions(model.getAllTreatments());
	}

	public void recordRecentImportFile(File file)
	{
		//recentImportFiles.addFile(file);
		syncImportRecentFiles();
	}
	
	public void syncStatsView()
	{
		MeasurementStatistics stats = MeasurementStatistician.stats(getSelectedMeasurements());
		view.setStatisticsPanel(stats.getView());
	}
	
	void showFileImportError(String errMessage)
	{
		JOptionPane.showMessageDialog(
				view, errMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
	}
	
	
	DataBrowserView getView()
	{
		return view;
	}
	
	public class FilterRowSelectionListener implements FilterListener
	{
		@Override
		public void newSelectedFilter(String filterName)
		{
			if (currentFilter == model.getFilter(filterName))
			{
				return;
			}
			currentFilter = model.getFilter(filterName);
			view.filterView.sync(currentFilter);
			syncView();
		}
	}
	
	public Filter getFilter(String filterName)
	{
		return model.getFilter(filterName);
	}
	
	public void syncWithFilter(String filterName)
	{
		currentFilter = model.getFilter(filterName);
		view.filterView.sync(currentFilter);
		syncView();
	}
	
	String getSelectionNameInput() {return (String) JOptionPane.showInputDialog(view,  "Enter in unqiue name for filter.", 										"Create new Selection", JOptionPane.PLAIN_MESSAGE, null, null, "new_selection");}
	
	void showCreateError() 		{		JOptionPane.showMessageDialog(view, "Could not create filter",		"Creation Error", JOptionPane.ERROR_MESSAGE);}
	
	public void newSelectionDialog()
	{
		String name = getSelectionNameInput();

		if (name == null)
		{
			return;
		}

		for (String existingName : model.getSelectionNames())
		{
			if (existingName.equals(name))
			{
				showCreateError();
				return;
			}
		}

		Filter f = createNewSelection(name);
		view.setSelectedSelections(f.getName());
		view.setSelectedSelectionsRow(f.getName());
	}
	
	public Filter createNewSelection(String name)
	{
		view.filterView.sync(null);
		List<CellType> cellTypes = new ArrayList<>();
		cellTypes.add(CellType.LIVE);
		Filter filter = new Filter(name, cellTypes, null, null, null, null, null, null, null);
		model.createSelection(filter);
		syncFilterTable();
		return filter;
	}

	
	public void initGUI()
	{
		syncImportRecentFiles();
		view.revalidate();
		view.repaint();
		view.filterView.sync(null);
		syncTreatments();
		syncView();
		syncFilterTable();
		
		new FileDrop(view, new FileDrop.Listener()
		{
			public void filesDropped(java.io.File[] files)
			{
				for (File f : files) 
				{
					importFile(f);
				}
			}
		});
		
		view.filterView.sync(null);
	}
	
	public DataBrowsingMenu getMenu()
	{
		return mnu;
	}
	
	public static void main(String[] args)
	{
		DataBrowserModel model = new DataBrowserModel();
		DataBrowserView view = new DataBrowserView();	
		DataBrowsingMenu mnu = new DataBrowsingMenu();
		
		DataBrowserController controller
				= new DataBrowserController(model, view, mnu);
		
		JFrame frame = new JFrame();
		
		JMenuBar bar = new JMenuBar();
		
		bar.add(mnu.getMeasurements());
		bar.add(mnu.getPlotView());
		bar.add(mnu.getRecentExcelImport());
		JMenu miFile = new JMenu("File");
		bar.add(miFile);
		miFile.add(mnu.getImportExcelJMenuItem());

		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.add(controller.getView());
		frame.setJMenuBar(bar);
		controller.initGUI();
		frame.setVisible(true);
	}

	public List<String> getAllTreatments()
	{
		return model.getAllTreatments();
	}

	public void limitedMode()
	{
		mnu.limitedMode();
	}

	public void loadWorkspace() {
		try {
			// read settings, e.g. working path, from a pre-defined file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(DEFAULT_SETTINGS_FILE);

			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("startup");
			Node nNode = nList.item(0);
			System.out.println("\nCurrent Element :" + nNode.getNodeName());
			Element eElement = (Element) nNode;
			workspacePath = eElement.getAttribute("path");
			lastDataFile = eElement.getAttribute("file");

		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println("Loading settings error: " + e.getMessage());
			System.err.println("Cannot load from file, using default");
		}
	}

	public void saveWorkspace() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			Element rootElement = doc.createElement("class");
			doc.appendChild(rootElement);

			Element startup = doc.createElement("startup");
			Attr attrType = doc.createAttribute("path");
			attrType.setValue(workspacePath);
			startup.setAttributeNode(attrType);

			attrType = doc.createAttribute("file");
			attrType.setValue(lastDataFile);
			startup.setAttributeNode(attrType);
			rootElement.appendChild(startup);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(
					new FileOutputStream(DEFAULT_SETTINGS_FILE));
			transformer.transform(source, result);

		} catch (ParserConfigurationException | FileNotFoundException | TransformerException | DOMException e) {
			System.err.println("Saving settings error: " + e.getMessage());
		}
	}
}
