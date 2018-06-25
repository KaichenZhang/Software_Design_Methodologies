package caseStudy2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Chart {
	
	void START(ArrayList<String> stocks, int MAdays) {
		
		ArrayList<Series<String, Number>> series = new ArrayList<Series<String, Number>>();
		
		while(!stocks.isEmpty()) {
			series.add(dataPrepare(stocks.remove(0), "MA", MAdays).remove(0));	
		}
		
		drawChart(series, MAdays + "MA for Watch-List", "Price");
		
	}

	void show(String stock, int D) {
		
		if(D != 0) {
			drawChart(dataPrepare("BOEN", "MA", D), stock + "  " + D + "days MA", "Price");
		} else {
			drawChart(dataPrepare(stock, "normal", 0), stock, "Price");
			drawChart(dataPrepare(stock, "Volume", 0), stock, "Volume");
		}
	}
	
	
	
	void drawChart(ArrayList<Series<String, Number>> series, String title, String TY) {
		
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		
		xAxis.setLabel("Date");
	    yAxis.setLabel(TY);
	    
	    LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
		lineChart.setTitle(title);
		
		
		Scene scene = new Scene(lineChart, 600, 600);
		
		for(Series<String, Number> i : series) {
			lineChart.getData().add(i);
		}

		stage.setScene(scene);
		stage.setTitle(title);
		stage.show();
		
	}
	
	ArrayList<Series<String, Number>> dataPrepare(String stock, String type, int MAdays) {
		
		ArrayList<Series<String, Number>> series = new ArrayList<Series<String, Number>>();
		
		Stack<double[]> data = new Stack<double[]>();
		
		File file = new File(stock + ".csv");
		
		try {
			
			Scanner input = new Scanner(file);
			input.nextLine();
			
			while(input.hasNextLine()) {
				
				double[] dataLine = new double[7];
				String[] line = input.nextLine().split(",");

				for(int j = 1; j <= 6; j++) {
					dataLine[j] = Double.parseDouble(line[j]);
				}
				
				String[] date = line[0].split("-");

				dataLine[0] = Double.parseDouble(date[0] + date[1] + date[2]);
				data.push(dataLine);
			}
			
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println("Local Data" + stock + " is not exist!");
		}
		
		
		if(type == "MA") {
			
			Series<String, Number> SMA = calculateMA(data, MAdays);
			SMA.setName(stock);
			series.add(SMA);
			
		} else if(type == "Volume") {
			
			
			Series<String, Number> SV = new Series<String, Number>();
			SV.setName("Volume");
			while(!data.isEmpty()) {
				SV.getData().add(new Data<String, Number>(String.valueOf((long)data.peek()[0]), data.pop()[5]));
			}
			series.add(SV);
			
		} else {
			
			Series<String, Number> SO = new Series<String, Number>();
			Series<String, Number> SH = new Series<String, Number>();
			Series<String, Number> SL = new Series<String, Number>();
			Series<String, Number> SC = new Series<String, Number>();
			Series<String, Number> SAC = new Series<String, Number>();
			
			SO.setName("Open");
			SH.setName("High");
			SL.setName("Low");
			SC.setName("Close");
			SAC.setName("Adj Close");
			
			while(!data.isEmpty()) {
				
				SO.getData().add(new Data<String, Number>(String.valueOf((long)data.peek()[0]), data.peek()[1]));
				SH.getData().add(new Data<String, Number>(String.valueOf((long)data.peek()[0]), data.peek()[2]));
				SL.getData().add(new Data<String, Number>(String.valueOf((long)data.peek()[0]), data.peek()[3]));
				SC.getData().add(new Data<String, Number>(String.valueOf((long)data.peek()[0]), data.peek()[4]));
				SAC.getData().add(new Data<String, Number>(String.valueOf((long)data.peek()[0]), data.pop()[6]));
			}
			
			series.add(SO);
			series.add(SH);
			series.add(SL);
			series.add(SC);
			series.add(SAC);
		}
		return series;	
	}
	
	Series<String, Number> calculateMA(Stack<double[]> data, int D) {
		
		Series<String, Number> SMA = new Series<String, Number>();
		ArrayList<Double> DMA = new ArrayList<Double>();
		
		double last = 0;
		
		for(int i = 1; !data.isEmpty(); i++) {
			
			double P = data.peek()[1];
			
			if(i > D) {
				last = last - DMA.remove(0) / D + P / D;
			} else {
				last = (last * (i - 1) + P) / i;
			}
			
			DMA.add(P);
			SMA.getData().add(new Data<String, Number>(String.valueOf((long)data.pop()[0]), last));	
		}
		
		return SMA;	
	}
	
}