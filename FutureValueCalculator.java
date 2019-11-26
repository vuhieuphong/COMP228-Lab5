package lab5;
	
import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.shape.Line;

public class FutureValueCalculator extends Application {
	
	//public static variables
	public static XYChart.Data investData;
	public static XYChart.Data futureData;
	public static TextField tfInvestAmount;
	public static TextField tfInterestRate;
	public static TextField tfYear;
	public static TextField tfFutureValue;
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane windowPane=new BorderPane();		
		
		//tool bar buttons
		ToolBar toolBar=new ToolBar();
		windowPane.setTop(toolBar);
		Button btExit=new Button(" Exit ");
		Button btCalculate=new Button(" Calculate ");
		Button btReset=new Button(" Reset ");
		btExit.setPrefWidth(295);
		btCalculate.setPrefWidth(295);
		btReset.setPrefWidth(295);
		btCalculate.setStyle("-fx-background-color:#90EE90;-fx-cursor:hand");
		btReset.setStyle("-fx-background-color:	#F8DE7E;-fx-cursor:hand");
		btExit.setStyle("-fx-background-color: #FF91A4;-fx-cursor:hand");
		btCalculate.setOnAction(new CalculateHandlerClass());
		btReset.setOnAction(new ResetHandlerClass());
		btExit.setOnAction(new ExitHandlerClass());
		toolBar.getItems().addAll(btCalculate,btReset,btExit);
		
		//main calculator pane
		GridPane calcPane=new GridPane();
		windowPane.setLeft(calcPane);
		calcPane.setAlignment(Pos.CENTER);
		calcPane.setPadding(new Insets(11.5,12.5,13.5,14.5));
		calcPane.setHgap(5.5);
		calcPane.setVgap(5.5);
		
		//labels and text fields
		Label lbIntro=new Label(" Enter your: ");
		lbIntro.setStyle("-fx-font-weight: bold;-fx-font-size: 14pt");
		calcPane.add(lbIntro, 0, 0);
		Label lbInvestAmount=new Label(" Investment Amount: ");
		tfInvestAmount=new TextField();
		GridPane.setHalignment(lbInvestAmount, HPos.RIGHT);
		calcPane.add(lbInvestAmount,0,1);
		calcPane.add(tfInvestAmount, 1,1);
		
		Label lbInterestRate=new Label(" Annual Interest (%): ");
		tfInterestRate=new TextField();
		GridPane.setHalignment(lbInterestRate, HPos.RIGHT);
		calcPane.add(lbInterestRate, 0, 2);
		calcPane.add(tfInterestRate, 1, 2);
		
		Label lbYear=new Label(" Number of Years: ");
		tfYear=new TextField();
		GridPane.setHalignment(lbYear, HPos.RIGHT);
		calcPane.add(lbYear, 0, 3);
		calcPane.add(tfYear, 1, 3);
		
		Line secondLine=new Line();
		secondLine.setStartX(0);
		secondLine.setStartX(200);
		calcPane.add(secondLine, 1, 5);
		
		Label lbFutureValue=new Label(" Future Value: ");
		tfFutureValue=new TextField();
		GridPane.setHalignment(lbFutureValue, HPos.RIGHT);
		tfFutureValue.setEditable(false);
		tfFutureValue.setStyle("-fx-background-color:#B0E0E6;-fx-cursor:hand");
		calcPane.add(lbFutureValue, 0, 7);
		calcPane.add(tfFutureValue, 1, 7);
		
		//bar chart
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("");
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Value");
		BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);
		barChart.setLegendVisible(false);
		XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();	
		investData=new XYChart.Data<>("Investment Amount",0);
		futureData=new XYChart.Data<>("Future Value",0);
		dataSeries.getData().addAll(investData,futureData);
		barChart.getData().add(dataSeries);
		
		VBox chartPane = new VBox(barChart);
		chartPane.setAlignment(Pos.CENTER);
		windowPane.setRight(chartPane);
		
		//setting the scene
		Scene scene=new Scene(windowPane,900,450);
		primaryStage.setTitle("Future Value Calculator");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}	
	
	public static void main(String[] args) {
		launch(args);
	}	
}

class CalculateHandlerClass implements EventHandler<ActionEvent>
{
	//calculate button handler
	@Override
	public void handle(ActionEvent e)
	{
		try {
			double investAmount=Double
					.parseDouble(FutureValueCalculator.tfInvestAmount.getText());
			double monthlyInterestRate=Double
					.parseDouble(FutureValueCalculator.tfInterestRate.getText())/1200;
			double year=Double
					.parseDouble(FutureValueCalculator.tfYear.getText());
			double futureValue=investAmount*Math.pow(1.0+monthlyInterestRate, year*12);
			
			if(investAmount<0||monthlyInterestRate<0||year<0)
			{
				throw new Exception();
			}
			
			FutureValueCalculator.tfFutureValue.setText(String.format("%.2f", futureValue));			
			FutureValueCalculator.investData.setYValue(investAmount);
			FutureValueCalculator.futureData.setYValue(futureValue);
			
		} catch (Exception exception) {
			FutureValueCalculator.tfFutureValue.setText("");
			FutureValueCalculator.investData.setYValue(0);
			FutureValueCalculator.futureData.setYValue(0);
			Alert errorAlert=new Alert(AlertType.WARNING,
					" Please Enter Numeric Positive Values. ");
			errorAlert.setTitle("Exception Dialog");
			errorAlert.setHeaderText("Exeception Caught!");
			errorAlert.show();
		}
	}
}

class ResetHandlerClass implements EventHandler<ActionEvent>
{
	//reset button handler
	@Override
	public void handle(ActionEvent e)
	{
		FutureValueCalculator.tfInvestAmount.setText("");
		FutureValueCalculator.tfInterestRate.setText("");
		FutureValueCalculator.tfYear.setText("");
		FutureValueCalculator.tfFutureValue.setText("");
		FutureValueCalculator.investData.setYValue(0);
		FutureValueCalculator.futureData.setYValue(0);
	}
}

class ExitHandlerClass implements EventHandler<ActionEvent>
{
	//exit button handler
	@Override
	public void handle(ActionEvent e)
	{
		Alert exitAlert=new Alert(AlertType.CONFIRMATION," Do you want to Exit? ");
		exitAlert.setTitle("Exit Dialog");
		exitAlert.setHeaderText("Exit?");
		Optional<ButtonType> result = exitAlert.showAndWait();
		if(result.get()==ButtonType.OK)
		{
        System.exit(0);
		}
	}
}
