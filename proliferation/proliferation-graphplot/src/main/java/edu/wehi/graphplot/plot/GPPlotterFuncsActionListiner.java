package edu.wehi.graphplot.plot;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.data.xy.XYDataset;

public abstract class GPPlotterFuncsActionListiner
{
	public GPPlotterFuncsActionListiner(ChartPanel chartPanel)
	{
		chartPanel.addChartMouseListener(new ChartMouseListener()
		{
			@Override
			public void chartMouseMoved(ChartMouseEvent event){}
			
			@Override
			public void chartMouseClicked(ChartMouseEvent event)
			{
				int x = event.getTrigger().getX();
				int y = event.getTrigger().getY();
				XYItemEntity entity = null;
				try {entity = (XYItemEntity) chartPanel.getEntityForPoint(x,y);} catch(Exception e){}
				if(entity != null)
				{
					int item = entity.getItem();
					int series = entity.getSeriesIndex();
					XYDataset data = entity.getDataset();
					pointClicked(data.getXValue(series, item), data.getYValue(series, item), data.getSeriesKey(series).toString());
				}
			}
		});
	}
	
	public abstract void pointClicked(double x, double y, String series);
	
}
