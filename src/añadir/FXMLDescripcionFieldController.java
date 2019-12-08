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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLDescripcionFieldController implements Initializable {

    @FXML
    private TextArea textArea;
    
    private boolean textEdited = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void aceptar(ActionEvent event) {
        textEdited = true;
        cancelar(event);
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage)textArea.getScene().getWindow()).close();
    }
    
    public void setText(String text){
        textArea.setText(text);
    }
    
    public String getText(){
        return textArea.getText();
    }
    
    public boolean isTextEdited(){
        return textEdited;
    }
}
