/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc.main.ayuda;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author JoseluXtv
 */
public class FXMLImagenController implements Initializable {
    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView vista;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void setImageView(Image imagen) {
        vista.setImage(imagen);
        vista.fitWidthProperty().bind(((AnchorPane)vista.getParent()).widthProperty());
    }
}
