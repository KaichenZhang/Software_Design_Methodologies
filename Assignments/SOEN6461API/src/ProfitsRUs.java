

import java.awt.Dimension;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ProfitsRUs extends Application {
	private static String TITLE = "ProfitsRUs - Market Analysis Tool";
	//private static String STOCK_DATA_FILE = "data/sample.csv";
	private static Dimension GRAPH_DIMENSIONS = new Dimension(650, 550);
	private Stage stage;
	private BorderPane rootLayout;


	@FXML
	private CheckBox moving20;

	@FXML
	private CheckBox moving50;

	@FXML
	private CheckBox moving100;

	@FXML
	private CheckBox moving200;

	@FXML
	private ChoiceBox<String> stock;

	@FXML
	private TextField fromday;

	@FXML
	private TextField frommonth;

	@FXML
	private TextField fromyear;

	@FXML
	private TextField today;

	@FXML
	private TextField tomonth;

	@FXML
	private TextField toyear;

	@FXML
	private Button enter;

	@FXML
	protected void handleButtonAction(ActionEvent event) {
		enter.setText("Enter");

		try {
			DateData fromDate = DateData.CreateDateData(Integer.parseInt(fromday.getText()), Integer.parseInt(frommonth.getText()), Integer.parseInt(fromyear.getText()));
			DateData toDate = DateData.CreateDateData(Integer.parseInt(today.getText()), Integer.parseInt(tomonth.getText()), Integer.parseInt(toyear.getText()));
			String stockname = stock.getValue();
			updateStockDiagram(stockname, fromDate, toDate);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			enter.setText("Invalid input!");
		}
	}

	@FXML
	protected void initialize() {
		stock.setItems(FXCollections.observableArrayList(
				"AXP", "AAPL", "BA", "CAT", "CSCO", "CVX", "DD", "XOM",
				"GE", "GS", "HD", "IBM", "INTC", "JNJ", "KO", "JPM",
				"MCD", "MMM", "MRK", "MSFT", "NKE", "PFE", "PG", "TRV",
				"UNH", "UTX", "VZ", "V", "WMT", "DIS"));
		stock.setValue(stock.getItems().get(0));
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setTitle(TITLE);
		initRootLayout();
		showStockview();
		createStockDiagram();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ProfitsRUs.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the Stock overview inside the root layout.
	 */
	public void showStockview() {
		try {
			// Load Stock overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ProfitsRUs.class.getResource("Stockview.fxml"));
			AnchorPane stockView = (AnchorPane) loader.load();

			// Set Stock overview into the center of root layout.
			rootLayout.setLeft(stockView);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows the Stock Diagram
	 */
	public void createStockDiagram() {
		try {
			// Load Stock Diagram
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ProfitsRUs.class.getResource("StockDiagram.fxml"));
			FlowPane root = (FlowPane) loader.load();

			NumberAxis xAxis = new NumberAxis(0, 0, 1);
			NumberAxis yAxis = new NumberAxis(0, 0, 1);
			chart = new LineChart<Number, Number>(xAxis, yAxis);
			chart.setMinSize(GRAPH_DIMENSIONS.width, GRAPH_DIMENSIONS.height);
			root.getChildren().add(chart);

			rootLayout.setCenter(root);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	private static LineChart<Number, Number> chart;

	private void updateStockDiagram(String stock, DateData fromDate, DateData toDate) {
		chart.getData().clear();

		if (fromDate.GetYear() == toDate.GetYear() && fromDate.GetMonth() == toDate.GetMonth()) {
			createDayLineChart(stock, fromDate.GetYear(), fromDate.GetMonth());
		} else if (fromDate.GetYear() == toDate.GetYear()) {
			createMonthLineChart(stock, fromDate.GetYear());
		} else {
			createYearLineChart(stock, fromDate.GetYear(), toDate.GetYear());
		}
	}

	private LineChart<Number, Number> createYearLineChart(String stock, int minimumYear, int maximumYear) {
		int minimumStock = -1;
		int maximumStock = -1;
		GregorianCalendar startDate = new GregorianCalendar(minimumYear-1, 1, 1); //get extra data for moving averages
		GregorianCalendar endDate = new GregorianCalendar(maximumYear, 12, 31);

		chart.setTitle("Stock " + stock + " between " + minimumYear + " and " + maximumYear);
		chart.setMinSize(GRAPH_DIMENSIONS.width, GRAPH_DIMENSIONS.height);

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("'Open' values");
		List<StockData> stockDatas = StockCSVReader.ComputeStockDataList(YahooFinanceAPI.FetchData(stock, startDate, endDate));
		for (StockData stockData : stockDatas) {
			int stockDataYear = stockData.GetDate().GetYear();
			if (stockDataYear >= minimumYear && stockDataYear <= maximumYear) {
				if (minimumStock == -1 || maximumStock == -1) {
					minimumStock = maximumStock = stockData.GetClose().intValue();
				}
				minimumStock = (int) Math.min(minimumStock, stockData.GetClose());
				maximumStock = (int) Math.max(maximumStock, stockData.GetClose());
				series.getData().add(new XYChart.Data<Number, Number>(stockDataYear + stockData.GetDate().GetMonth()/12.0 + stockData.GetDate().GetDay()/365.0, stockData.GetClose()));
			}
		}
		chart.getData().add(series);
		chart.setCreateSymbols(false);

		//add moving averages that are selected
		int numMAselected = 0;
		if(moving20.isSelected()) {
			chart.getData().add(createYearMovingAverage(stockDatas, minimumYear, maximumYear, 20));
			numMAselected++;
		}
		if(moving50.isSelected()) {
			chart.getData().add(createYearMovingAverage(stockDatas, minimumYear, maximumYear, 50));
			numMAselected++;
		}
		if(moving100.isSelected()) {
			chart.getData().add(createYearMovingAverage(stockDatas, minimumYear, maximumYear, 100));
			numMAselected++;
		}
		if(moving200.isSelected()) {
			chart.getData().add(createYearMovingAverage(stockDatas, minimumYear, maximumYear, 200));
			numMAselected++;
		}

		if(numMAselected == 2)
			drawArrows();

		((NumberAxis)chart.getXAxis()).setLabel("Year");
		((NumberAxis)chart.getXAxis()).setLowerBound(minimumYear);
		//by setting the upper bound to maxYear, data stop displaying at 12-31-(maxYear-1), that is, maxYear is excluded
		((NumberAxis)chart.getXAxis()).setUpperBound(maximumYear+1);
		((NumberAxis)chart.getXAxis()).setTickUnit(1);
		((NumberAxis)chart.getYAxis()).setLowerBound(minimumStock);
		((NumberAxis)chart.getYAxis()).setUpperBound(maximumStock);
		((NumberAxis)chart.getYAxis()).setTickUnit(2);

		// DEBUG
		System.out.println("Plotting years chart, years " + minimumYear + " to " + maximumYear + ": " + series.getData().size() + " results");

		return chart;
	}

	private LineChart<Number, Number> createMonthLineChart(String stock, int year) {
		int minimumStock = -1;
		int maximumStock = -1;
		GregorianCalendar startDate = new GregorianCalendar(year-1, 1, 1); //get extra data for moving averages
		GregorianCalendar endDate = new GregorianCalendar(year, 12, 31);

		chart.setTitle("Stock " + stock + " in " + year);
		chart.setMinSize(GRAPH_DIMENSIONS.width, GRAPH_DIMENSIONS.height);

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("'Close' values");
		List<StockData> stockDatas = StockCSVReader.ComputeStockDataList(YahooFinanceAPI.FetchData(stock, startDate, endDate));
		//iterating through fetched stock data comes in this order
		//(month,day) --> (1,31)-(1,1) of the next year, (12,31)-(12,1), (11,31)..., (2,1) of the correct year
		//since most of the data comes in reverse order, it could be possible to reverse it with a stack
		//except for the month of January
		for (StockData stockData : stockDatas) {
			if (stockData.GetDate().GetYear() == year) {
				if (minimumStock == -1 || maximumStock == -1) {
					minimumStock = maximumStock = stockData.GetClose().intValue();
				}
				minimumStock = (int) Math.min(minimumStock, stockData.GetClose());
				maximumStock = (int) Math.max(maximumStock, stockData.GetClose());
				series.getData().add(new XYChart.Data<Number, Number>(stockData.GetDate().GetMonth() + stockData.GetDate().GetDay()/30.5, stockData.GetClose()));
			}
		}
		chart.getData().add(series);
		chart.setCreateSymbols(false);

		//add moving averages for selected months
		int numMAselected = 0; //arrows displayed only when 2 MAs selected
		if(moving20.isSelected()) {
			chart.getData().add(createMonthMovingAverage(stockDatas, year, 20));
			numMAselected++;
		}
		if(moving50.isSelected()) {
			chart.getData().add(createMonthMovingAverage(stockDatas, year, 50));
			numMAselected++;
		}
		if(moving100.isSelected()) {
			chart.getData().add(createMonthMovingAverage(stockDatas, year, 100));
			numMAselected++;
		}
		if(moving200.isSelected()) {
			chart.getData().add(createMonthMovingAverage(stockDatas, year, 200));
			numMAselected++;
		}

		if(numMAselected == 2)
			drawArrows();

		((NumberAxis)chart.getXAxis()).setLabel("Month");
		((NumberAxis)chart.getXAxis()).setLowerBound(1);
		//13 so that month of December displays
		((NumberAxis)chart.getXAxis()).setUpperBound(13);
		((NumberAxis)chart.getXAxis()).setTickUnit(1);
		((NumberAxis)chart.getYAxis()).setLowerBound(minimumStock);
		((NumberAxis)chart.getYAxis()).setUpperBound(maximumStock);
		((NumberAxis)chart.getYAxis()).setTickUnit(2);

		// DEBUG
		System.out.println("Plotting months chart, year " + year + ": " + series.getData().size() + " results");

		return chart;
	}

	private LineChart<Number, Number> createDayLineChart(String stock, int year, int month) {
		int minimumStock = -1;
		int maximumStock = -1;
		GregorianCalendar startDate = new GregorianCalendar(year, month, 1);
		GregorianCalendar endDate = new GregorianCalendar(year, month, 31);

		chart.setTitle("Stock " + stock + " in " + month + " of " + year);
		chart.setMinSize(GRAPH_DIMENSIONS.width, GRAPH_DIMENSIONS.height);

		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		series.setName("'Open' values");
		List<StockData> stockDatas = StockCSVReader.ComputeStockDataList(YahooFinanceAPI.FetchData(stock, startDate, endDate));
		for (StockData stockData : stockDatas) {
			if (stockData.GetDate().GetYear() == year && stockData.GetDate().GetMonth() == month) {
				if (minimumStock == -1 || maximumStock == -1) {
					minimumStock = maximumStock = stockData.GetClose().intValue();
				}
				minimumStock = (int) Math.min(minimumStock, stockData.GetClose());
				maximumStock = (int) Math.max(maximumStock, stockData.GetClose());
				series.getData().add(new XYChart.Data<Number, Number>(stockData.GetDate().GetDay(), stockData.GetClose()));
			}
		}
		chart.getData().add(series);
		chart.setCreateSymbols(false);

		//add moving averages that are selected
		int numMAselected = 0;
		if(moving20.isSelected()) {
			chart.getData().add(createDayMovingAverage(stockDatas, year, month, 20));
			numMAselected++;
		}
		if(moving50.isSelected()) {
			chart.getData().add(createDayMovingAverage(stockDatas, year, month, 50));
			numMAselected++;
		}
		if(moving100.isSelected()) {
			chart.getData().add(createDayMovingAverage(stockDatas, year, month, 100));
			numMAselected++;
		}
		if(moving200.isSelected()) {
			chart.getData().add(createDayMovingAverage(stockDatas, year, month, 200));
			numMAselected++;
		}

		if(numMAselected == 2)
			drawArrows();

		((NumberAxis)chart.getXAxis()).setLabel("Day");
		((NumberAxis)chart.getXAxis()).setLowerBound(1);
		((NumberAxis)chart.getXAxis()).setUpperBound(32);
		((NumberAxis)chart.getXAxis()).setTickUnit(1);
		((NumberAxis)chart.getYAxis()).setLowerBound(minimumStock);
		((NumberAxis)chart.getYAxis()).setUpperBound(maximumStock);
		((NumberAxis)chart.getYAxis()).setTickUnit(2);

		// DEBUG
		System.out.println("Plotting days chart, year " + year + ", month " + month + ": " + series.getData().size() + " results");

		return chart;
	}

	private XYChart.Series<Number, Number> createYearMovingAverage(List<StockData> data, int minimumYear, int maximumYear, int period) {
		//create new chart series
		XYChart.Series<Number, Number> movingSeries = new XYChart.Series<Number, Number>();
		movingSeries.setName(period + "-day moving average");

		//reverse order
		Stack<StockData> stack = new Stack<StockData>();
		for (StockData stockData : data) {
			stack.push(stockData);
		}

		Queue<Double> queue = new LinkedList<Double>();
		while(!stack.isEmpty()) {
			StockData stockData = stack.pop();
			int stockDataYear = stockData.GetDate().GetYear();
			//want to maintain stack of size == period
			queue.add(stockData.GetClose());
			if(queue.size() == period + 1) {
				queue.remove();
			}
			if (stockDataYear >= minimumYear && stockDataYear <= maximumYear && queue.size() == period) {
				//get the average of every value in the queue
				double movingAverage = 0;
				for(Double value : queue) {
					movingAverage += value;
				}
				movingAverage /= (period*1.0);
				//add it to the chart
				movingSeries.getData().add(new XYChart.Data<Number, Number>(stockDataYear + stockData.GetDate().GetMonth()/12.0 + stockData.GetDate().GetDay()/365.0, movingAverage));
			}
		}

		return movingSeries;
	}

	private XYChart.Series<Number, Number> createMonthMovingAverage(List<StockData> data, int year, int period) {
		//create new chart series
		XYChart.Series<Number, Number> movingSeries = new XYChart.Series<Number, Number>();
		movingSeries.setName(period + "-day moving average");

		Queue<Double> queue = new LinkedList<Double>();
		//because fetched data comes in reverse, average chart would be shifted to the left
		//also, even is we want MA for year of 2015, we need data from year of 2014 since need <=200 data-points before we can graph
		Stack<StockData> stack = new Stack<StockData>();
		for (StockData stockData : data) {
			stack.push(stockData);
		}
		while (!stack.isEmpty()) {
			StockData stockData = stack.pop(); //get the proper order
			//want to maintain stack of size == period
			queue.add(stockData.GetClose());
			if(queue.size() == period + 1) {
				queue.remove();
			}
			if (stockData.GetDate().GetYear() == year && queue.size() == period) {
				//get the average of every value in the queue
				double movingAverage = 0;
				for(Double value : queue) {
					movingAverage += value;
				}
				movingAverage /= (period*1.0);
				//add it to the chart
				movingSeries.getData().add(new XYChart.Data<Number, Number>(stockData.GetDate().GetMonth() + stockData.GetDate().GetDay()/30.5, movingAverage));
			}
		}

		return movingSeries;
	}

	private XYChart.Series<Number, Number> createDayMovingAverage(List<StockData> data, int year, int month, int period) {
		//create new chart series
		XYChart.Series<Number, Number> movingSeries = new XYChart.Series<Number, Number>();
		movingSeries.setName(period + "-day moving average");

		//reverse order
		Stack<StockData> stack = new Stack<StockData>();
		for (StockData stockData : data) {
			stack.push(stockData);
		}

		Queue<Double> queue = new LinkedList<Double>();
		while(!stack.isEmpty()) {
			StockData stockData = stack.pop();
			//want to maintain stack of size == period
			queue.add(stockData.GetClose());
			if(queue.size() == period + 1) {
				queue.remove();
			}
			if (stockData.GetDate().GetYear() == year && stockData.GetDate().GetMonth() == month && queue.size() == period) {
				//get the average of every value in the queue
				double movingAverage = 0;
				for(Double value : queue) {
					movingAverage += value;
				}
				movingAverage /= (period*1.0);
				//add it to the chart
				movingSeries.getData().add(new XYChart.Data<Number, Number>(stockData.GetDate().GetDay(), movingAverage));
			}
		}

		return movingSeries;
	}

	private void drawArrows() {
		ObservableList<Series<Number, Number>> allSeries = chart.getData();
		ObservableList<XYChart.Data<Number, Number>> shortData = allSeries.get(1).getData(); //shorter MA period
		ObservableList<XYChart.Data<Number, Number>> longData = allSeries.get(2).getData(); //longer MA period

		boolean shortOnTop;
		if((shortData.get(0).getYValue().doubleValue() >= longData.get(0).getYValue().doubleValue()))
			shortOnTop = true;
		else
			shortOnTop = false;


		for(int i = 1; i < shortData.size(); i++) {
			//longMA passes shortMA
			if(shortData.get(i).getYValue().doubleValue() < longData.get(i).getYValue().doubleValue() && shortOnTop) {
				shortOnTop = false;
				//TODO DRAW RED SELL ARROW DOWN
				System.out.printf("Sell at x=%.2f\n", shortData.get(i).getXValue().doubleValue());
			}
			//shortMA passes longMA
			else if(shortData.get(i).getYValue().doubleValue() >= longData.get(i).getYValue().doubleValue() && !shortOnTop) {
				shortOnTop = true;
				//TODO DRAW GREEN BUY ARROW UP
				System.out.printf("Buy at x=%.2f\n", + shortData.get(i).getXValue().doubleValue());
			}
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
