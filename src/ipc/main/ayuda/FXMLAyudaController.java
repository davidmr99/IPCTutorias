/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc.main.ayuda;

import añadir.FXMLAlumnoController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JoseluXtv
 */
public class FXMLAyudaController implements Initializable {

    @FXML
    private ImageView tutoria1;
    @FXML
    private ImageView tutoria2;
    @FXML
    private ImageView alumno1;
    @FXML
    private ImageView alumno2;
    @FXML
    private ImageView asignatura1;
    @FXML
    private ImageView asignatura2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void sacarImagen(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/ipc/main/ayuda/FXMLImagen.fxml"));
        Parent root = (Parent)miLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLImagenController controller = miLoader.getController();
        stage.setResizable(false);
        controller.setImageView(((ImageView)event.getSource()).getImage());
        stage.showAndWait();
    }
    
}
