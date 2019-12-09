/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package añadir;

import accesoBD.AccesoBD;
import ipc.Main;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelo.Asignatura;

/**
 * FXML Controller class
 *
 * @author JoseluXtv
 */
public class FXMLAsignaturaController implements Initializable {

    @FXML
    private TextField asignaturaField;
    @FXML
    private TextField abreviatura;
    @FXML
    private Button addAsignatura;

    private Asignatura asignatura = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addAsignatura.disableProperty().bind(asignaturaField.textProperty().isEmpty().or(abreviatura.textProperty().isEmpty()));
    }    

    @FXML
    private void addAsignatura(ActionEvent event) {
        if ((!asignaturaField.getText().isEmpty())
                && (asignaturaField.getText().trim().length() != 0)
                && (!abreviatura.getText().isEmpty())
                && (abreviatura.getText().trim().length() != 0)) {
            
            asignatura = new Asignatura();
            asignatura.setCodigo(abreviatura.getText().trim());
            asignatura.setDescripcion(asignaturaField.getText().trim());
            
            save();
            
            asignaturaField.clear();
            abreviatura.clear();
            asignaturaField.requestFocus();
            ((Stage)asignaturaField.getScene().getWindow()).close();
        } 
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage)asignaturaField.getScene().getWindow()).close();
    }
    public void save(){
        AccesoBD.getInstance().getTutorias().getAsignaturas().add(asignatura);
        AccesoBD.getInstance().salvar();
        Main.getMainController().reloadAlumnosYAsignaturas();
    }
}
