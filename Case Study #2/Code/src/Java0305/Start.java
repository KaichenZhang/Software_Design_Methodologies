package Java0305;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Start extends Application {
	
	ReadCSV readCSV;
	SelectDate selectDate;
	Chart chart;
	Chart averageChart;
	MovingAverageDiaplay movingAverageDiaplay;

	@Override
	public void start(Stage stage) {
		
		
		Button start = new Button("Select File Path");
		start.setOnAction(e -> {
			try {
				select(stage);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		StackPane pane = new StackPane();
		pane.getChildren().addAll(start);
		
		Scene scene = new Scene(pane, 300, 150);
		stage.setScene(scene);
		stage.setTitle("Stock app");
		stage.show();
		
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	public void select(Stage stage) throws IOException {
		
        JFileChooser fileChooser = new JFileChooser();  
        
        if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        	File file = fileChooser.getSelectedFile();
        	readCSV = new ReadCSV();
        	readCSV.readData(file.getAbsolutePath());
        }

        
        Button bA = new Button("Choose the Date to Display");
		Button bB = new Button("Calculate Moving Average  ");
		bA.setOnAction(e -> display(stage));
		bB.setOnAction(e -> selectDate(stage));
		
		VBox vBox = new VBox(100);
		vBox.getChildren().addAll(bA, bB);
		
		Pane pane = new Pane();
		pane.getChildren().addAll(vBox);
		
		Scene scene = new Scene(pane, 400, 300);
		stage.setScene(scene);
		
	}
	
	public void display(Stage stage) {
		
		Label startLabel = new Label("Start date:");
		
		Label endLabel = new Label("End date:  ");
		Label sample = new Label("Sample Input: 2016-09-30");
		Label error = new Label("Error Input: 20160930 or 2016/9/30");
		
		TextField start = new TextField();
		TextField end = new TextField();
		
		Button show = new Button("Show");
		show.setOnAction(e -> dis(start, end));
		
		VBox vBox = new VBox(10);
		vBox.getChildren().addAll(startLabel, start, endLabel, end, sample, error, show);
		
		Pane pane = new Pane();
		pane.getChildren().addAll(vBox);
		
		Scene scene = new Scene(pane, 400, 300);
		stage.setScene(scene);
		stage.setTitle("Select Date");
		
	}
	
	public void dis(TextField start, TextField end) {
		
		
		String startDate = start.getText();
		String endDate = end.getText();
		
		selectDate = new SelectDate();
		selectDate.select(startDate, endDate ,readCSV);
		
		chart = new Chart();
		chart.drawChart(selectDate.newData);
		
	}
	
	public void selectDate(Stage stage) {
		
		Label startLabel = new Label("Start Date:");
		
		Label endLabel = new Label("End Date:");
		Label sample = new Label("Sample Input: 2016-09-30");
		Label error = new Label("Error Input: 20160930 or 2016/9/30");
		
		Label last = new Label("Moving Average Last Days:");
		
		TextField days = new TextField();
		
		TextField start = new TextField();
		TextField end = new TextField();
		
		Button show = new Button("Show");
		show.setOnAction(e -> calculate(start, end, days, stage));
		
		VBox vBox = new VBox(10);
		vBox.getChildren().addAll(startLabel, start, endLabel, end, sample, error, last, days, show);
		
		Pane pane = new Pane();
		pane.getChildren().addAll(vBox);
		
		Scene scene = new Scene(pane, 400, 400);
		stage.setScene(scene);
		stage.setTitle("Moving Average");
	}
	
	public void calculate(TextField start, TextField end, TextField days, Stage stage) {

		selectDate = new SelectDate();
		selectDate.select(start.getText(), end.getText() ,readCSV);
		
		movingAverageDiaplay = new MovingAverageDiaplay();
		movingAverageDiaplay.data = selectDate.newData;
		
		
		averageChart = new Chart();
		averageChart.drawChart(movingAverageDiaplay.showAverage(Integer.parseInt(days.getText())));
		
		Label advice = new Label();
		advice.setText(selectDate.getAdvice());
		
		Pane pane = new Pane();
		pane.getChildren().addAll(advice);
		
		Scene scene = new Scene(pane, 300, 150);
		stage.setScene(scene);
		stage.setTitle("Advice");
	}

}
