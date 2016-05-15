package Oberfläche;

import javafx.application.Application;
import javafx.stage.Stage;

public class main extends Application{

	public static void main(String[] args) {
		launch(args);
	}
    
	public void start(Stage primaryStage) {

        // init the GUI controller
        party_planer_gui_controller controller = new party_planer_gui_controller(primaryStage);

    }

	   
}

