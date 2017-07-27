/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rahulsehrawat
 */
public class Myplayer extends Application {
    private static Stage mystage;
   
    @Override
    public void start(Stage stage) throws Exception {
        mystage = stage;
                
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
//        root.setStyle("-fx-background-color: #000; ");
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    public static Stage getStage()
    {
        return mystage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
