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
public class FXMLAsignaturaController implements Initializable {

    @FXML
    private TextField asignatura;
    @FXML
    private TextField abreviatura;
    @FXML
    private Button addAsignatura;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addAsignatura.disableProperty().bind(asignatura.textProperty().isEmpty().or(abreviatura.textProperty().isEmpty()));
    }    

    @FXML
    private void addAsignatura(ActionEvent event) {
        if ((!asignatura.getText().isEmpty())
                && (asignatura.getText().trim().length() != 0)
                && (!abreviatura.getText().isEmpty())
                && (abreviatura.getText().trim().length() != 0)) {
            asignatura.clear();
            abreviatura.clear();
            asignatura.requestFocus();
            ((Stage)asignatura.getScene().getWindow()).close();
        } 
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage)asignatura.getScene().getWindow()).close();
    }
    
}
