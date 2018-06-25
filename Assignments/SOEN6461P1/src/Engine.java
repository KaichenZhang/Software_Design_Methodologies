import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/* References:
 * http://www.jfree.org/jfreechart/api/javadoc/org/jfree/data/time/TimeSeries.html
 * http://www.jfree.org/jfreechart/api/javadoc/org/jfree/chart/StandardChartTheme.html
 * https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
 * */



public class Engine extends ApplicationFrame {

    private static final long serialVersionUID = 1L;
    TimeSeriesCollection dataset;
    JFreeChart chart;
    final ChartPanel chartPanel;
    final int chartHei = 500;
    final int chartWid = 1600;
	static String[] head;
	static double[][] data = new double[14000][7]; 
	
	

    public Engine(String applicationTitle,Integer option,Integer range,Integer start,Integer end) throws IOException, NumberFormatException, ParseException {
        super(applicationTitle);
        dataset = createDataset(option,range,start,end);
        chart = createChart();
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(chartWid,chartHei));
        this.add(chartPanel);
    }

    ArrayList<Double> closeData = new ArrayList<Double>();
    
    public TimeSeriesCollection createDataset(int option,int rangeMA,int start,int end){
        dataset = new TimeSeriesCollection();
        try {
        	File file = new File("Sample data.csv");
    		Scanner input = new Scanner(file);
    		head = input.nextLine().split(",");
    		
    		
            TimeSeries seriesX = new TimeSeries("Open Price", Day.class);
            TimeSeries seriesY = new TimeSeries("High Price", Day.class);
            TimeSeries seriesZ = new TimeSeries("Low Price", Day.class);
            TimeSeries seriesO = new TimeSeries("Close Price", Day.class);
 //           TimeSeries seriesP = new TimeSeries("Volume", Day.class);
            TimeSeries seriesQ = new TimeSeries("AdjClose Price", Day.class);
            TimeSeries seriesMV = new TimeSeries("MA", Day.class);
            
            
            //data processing
            for(int i = 0;input.hasNextLine(); i++) {

            	String[] line = input.nextLine().split(",");
            	
            	for(int j = 1; j <= 6; j++) {
    				data[i][j] = Double.parseDouble(line[j]);
    				
    			}
    			
    			String[] date = line[0].split("-");

    			String keyDate = date[0] + date[1] + date[2];
    			//pointer
    			int timePointer = Integer.parseInt(date[0] + date[1] + date[2]);
    			    						

    			int day = Integer.parseInt(date[2]);
    			int month = Integer.parseInt(date[1]);
    			int year = Integer.parseInt(date[0]);
            	
             
                double X = data[i][1];
                double Y = data[i][2];
                double Z = data[i][3];
                double O = data[i][4];
//                double P = data[i][5];
                double Q = data[i][6];
                double AVG = 0;
                closeData.add(O);
                
                //calculating moving average total in time period
                for(int f=0;f<rangeMA;f++){
                	if(i>=rangeMA-1){              	
                		AVG+=closeData.get(i-f);             	
                	}
                	else{
                		AVG+=data[i][4];
 
                	}
                }
                
                //select time period
                if(timePointer>=start && timePointer<=end){	
               
                	seriesX.add(new Day(day,month,year), X);
                
                	seriesY.add(new Day(day,month,year), Y);
                
                	seriesZ.add(new Day(day,month,year), Z);
               
                	seriesO.add(new Day(day,month,year), O);
            
//                	seriesP.add(new Day(day,month,year), P);
                
                	seriesQ.add(new Day(day,month,year), Q);
                
                	seriesMV.add(new Day(day,month,year), AVG/rangeMA);
                
                
                switch (option) {
                
                case 1:
                	
                	break;
                
                case 2:
    	
                	if(Math.floor(AVG/rangeMA)==Math.floor(O)&&O>closeData.get(i-1)){
                		System.out.println("A buy signal showed at ["+keyDate+"]");          
                	}
                
               
                	if(Math.floor(AVG/rangeMA)==Math.floor(O)&&O<closeData.get(i-1)){
                		System.out.println("A sell signal showed at ["+keyDate+"]");
                	}
              
                	break;
                }
              
                
                }
           
            }



			//select display content
            switch (option) {
            
            //show all
            case 1:
                dataset.addSeries(seriesX);
                dataset.addSeries(seriesY);
                dataset.addSeries(seriesZ);
                dataset.addSeries(seriesO);
            	break;
            
           
            //MA
            case 2:
                dataset.addSeries(seriesO);
                dataset.addSeries(seriesMV);
            	break;
            	
            
            }
            
            
            
 	       StandardChartTheme standardChartTheme=new StandardChartTheme("Dotum");  
 	       standardChartTheme.setExtraLargeFont(new Font("Dotum",Font.BOLD,30));  
 	       standardChartTheme.setRegularFont(new Font("Dotum",Font.BOLD,20));  
 	       standardChartTheme.setLargeFont(new Font("Dotum",Font.BOLD,20));  
 	       ChartFactory.setChartTheme(standardChartTheme); 
            
           input.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return dataset;
    }

    public JFreeChart createChart() throws NumberFormatException, IOException {
        chart =ChartFactory.createTimeSeriesChart("Value/Time",  "Time", "Value", dataset, true,true,false);
        chart.setBackgroundPaint(new Color(255,255,100));   
        return chart;
    }


}