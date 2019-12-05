/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package añadir;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author JoseluXtv
 */
public class FXMLAlumnoController implements Initializable {

    @FXML
    private TextField nombreAlumno;
    @FXML
    private TextField apellidoAlumno;
    @FXML
    private TextField mailAlumno;
    @FXML
    private Button addAlumno;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addAlumno.disableProperty().bind(nombreAlumno.textProperty().isEmpty().or(apellidoAlumno.textProperty().isEmpty()).or((mailAlumno.textProperty().isEmpty())));
    }    

    @FXML
    private void addAlumno(ActionEvent event) {
        if ((!nombreAlumno.getText().isEmpty())
                && (nombreAlumno.getText().trim().length() != 0)
                && (!apellidoAlumno.getText().isEmpty())
                && (apellidoAlumno.getText().trim().length() != 0)) {
            
            nombreAlumno.clear();
            apellidoAlumno.clear();
            nombreAlumno.requestFocus();
            ((Stage)nombreAlumno.getScene().getWindow()).close();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage)nombreAlumno.getScene().getWindow()).close();
    }
    
}
