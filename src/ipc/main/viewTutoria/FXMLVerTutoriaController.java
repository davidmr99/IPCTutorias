/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc.main.viewTutoria;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.shape.Circle;
import modelo.Alumno;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLVerTutoriaController implements Initializable {

    @FXML
    private ListView<Alumno> alumnos;
    @FXML
    private Label estado;
    @FXML
    private Circle estadoCirculo;
    @FXML
    private Label asignatura;
    @FXML
    private Label fecha;
    @FXML
    private TextArea textArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void accept(ActionEvent event) {
        
    }
    
}
