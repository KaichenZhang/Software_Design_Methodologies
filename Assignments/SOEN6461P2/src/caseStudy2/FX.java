package caseStudy2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FX extends Application {

	@Override
	public void start(Stage stage) {

		Scene scene = new Scene(showPrimary(), 800, 600);

		stage.setScene(scene);
		stage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
	
	static Pane showPrimary() {
		
		Stock stock = new Stock();
		Chart chart = new Chart();
		stock.startProgram();
		
		Label YSs = new Label("Year-Start:");
		Label YEs = new Label("Year-End:");
		Label MSs = new Label("Month-Start:");
		Label MEs = new Label("Month-End:");
		Label DSs = new Label("Date-Start:");
		Label DEs = new Label("Date-End:");
		Label MAs = new Label("Moving Average Last Days:");
		
		Label CS = new Label("Current Stock : ");

		TextField YS = new TextField();
		TextField YE = new TextField();
		TextField MS = new TextField();
		TextField ME = new TextField();
		TextField DS = new TextField();
		TextField DE = new TextField();
		TextField MA = new TextField();
		
		TextArea SA = new TextArea(stock.changeShowArea());
		SA.setEditable(false);
		
		ComboBox<String> cbo = new ComboBox<String>();
		cbo.getItems().addAll(FXCollections.observableArrayList(stock.getStockName()));
		cbo.setOnAction(e -> CS.setText("Current Stock : " + cbo.getValue()));
		
		Button OK = new Button("Show!");
		Button ADD = new Button("Add stock to Watch-List");
		Button CLE = new Button("Clear Watch-List       ");
		
		OK.setOnAction(e -> {
			
			stock.download(cbo.getValue(), Integer.parseInt(YS.getText()), Integer.parseInt(MS.getText()),
					Integer.parseInt(DS.getText()), Integer.parseInt(YE.getText()), 
					Integer.parseInt(ME.getText()), Integer.parseInt(DE.getText()));
			
			if(MA.getText() == "0") {
				chart.show(cbo.getValue(), 0);
			} else {
				chart.show(cbo.getValue(), Integer.parseInt(MA.getText()));
			}
			
		});
		
		ADD.setOnAction(e -> SA.setText(stock.addToWatch(cbo.getValue())));
		CLE.setOnAction(e -> SA.setText(stock.clearAll()));
		
		
		
		Pane top = new Pane();
		top.getChildren().add(cbo);
		
		VBox vBox = new VBox(10);
		vBox.getChildren().addAll(YSs, YS, MSs, MS, DSs, DS, YEs, YE, MEs, ME, DEs, DE, MAs, MA, OK);
		
		VBox vBoxWatch = new VBox(30);
		vBoxWatch.getChildren().addAll(CS, ADD, CLE, SA);
		
		BorderPane pane = new BorderPane();
		pane.setTop(top);
		pane.setLeft(vBox);
		pane.setRight(vBoxWatch);
		
		return pane;
	}

}
