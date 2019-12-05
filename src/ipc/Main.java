/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc;

import ipc.main.FXMLMainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author FMR
 */
public class Main extends Application {
    private static FXMLLoader loader;
    @Override
    public void start(Stage stage) throws Exception {
        loader = new FXMLLoader(getClass().getResource("main/FXMLMain.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        //Al abrirse la ventana, se ajusta al tamaño minimo necesario, ponemos ese tamaño como el mínimo de la ventana
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static FXMLMainController getMainController(){
        return loader.getController();
    }
    
}
