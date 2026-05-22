package Nonogram;

import Nonogram.controller.AppController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        
        AppController appController = new AppController(stage);
        appController.initialize();
        
    }
    
    public static void main(String[] args) {
        launch(args);  
    }
}