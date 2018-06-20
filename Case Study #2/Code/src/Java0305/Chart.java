package Java0305;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.ChartFactory;
import java.awt.Color;
import java.util.ArrayList;

public class Chart {
	
	public void drawChart(ArrayList<String[]> data){
		
		drawPriceChart(data);
		drawVolumeChart(data);

	}

	
	public void drawPriceChart(ArrayList<String[]> data){
		
				
		CategoryDataset mDataset = GetDataSet(data);
		JFreeChart mChart = ChartFactory.createLineChart("", "Date", "Price", mDataset, PlotOrientation.VERTICAL,
				true, true, false);
		
		CategoryPlot mPlot = (CategoryPlot)mChart.getPlot();
		mPlot.setBackgroundPaint(Color.LIGHT_GRAY);
		mPlot.setRangeGridlinePaint(Color.WHITE);
		mPlot.setOutlinePaint(Color.BLACK);
		
		ChartFrame mChartFrame = new ChartFrame("Stock Data", mChart);
		mChartFrame.pack();
		mChartFrame.setVisible(true);
		
		
	}
	
	public void drawVolumeChart(ArrayList<String[]> data){
		
		
		CategoryDataset mDataset = GetVolumeSet(data);
		JFreeChart mChart = ChartFactory.createLineChart("", "Date", "Volume", mDataset, PlotOrientation.VERTICAL,
				true, true, false);
		
		CategoryPlot mPlot = (CategoryPlot)mChart.getPlot();
		mPlot.setBackgroundPaint(Color.LIGHT_GRAY);
		mPlot.setRangeGridlinePaint(Color.WHITE);
		mPlot.setOutlinePaint(Color.BLACK);
		
		ChartFrame mChartFrame = new ChartFrame("Stock Data", mChart);
		mChartFrame.pack();
		mChartFrame.setVisible(true);
		
		
	}
	
	
	
	public static CategoryDataset GetDataSet(ArrayList<String[]> data) {
		
		DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
		for(String[] s : data){
			mDataset.addValue(Double.parseDouble(s[1]), "Open", s[0]);
			mDataset.addValue(Double.parseDouble(s[2]), "High", s[0]);
			mDataset.addValue(Double.parseDouble(s[3]), "Low", s[0]);
			mDataset.addValue(Double.parseDouble(s[4]), "Close", s[0]);
			mDataset.addValue(Double.parseDouble(s[6]), "Adj Close", s[0]);
		}
		
		return mDataset;
	}
	
	public static CategoryDataset GetVolumeSet(ArrayList<String[]> data) {
		
		DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
		for(String[] s:data){
			
			mDataset.addValue(Double.parseDouble(s[5]), "Volume", s[0]);
			
		}
		
		return mDataset;
	}

}
