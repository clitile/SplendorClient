package interaction;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public abstract class StartInterface extends Application{
	public static void start() throws Exception {
	    
		Stage primaryStage = new Stage();
		
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(15, 10, 50, 50));
		
		Button button1 = new Button("Log in");
		Button button2 = new Button("Register");
		
		hBox.getChildren().addAll(button1, button2);
		button1.setPrefWidth(100);
		button2.setPrefWidth(100);
		
		primaryStage.setTitle("Login or Register");
		primaryStage.setScene(new Scene(hBox, 300, 50));
		
		primaryStage.show();
		
		
		button1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				primaryStage.close();
				// Login Stage
				Login.LoginInterface();
				
			}
		});
		
		button2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				primaryStage.close();
				// Register Stage
				Register.RegisterInterface();
			}
		});
	}

}
