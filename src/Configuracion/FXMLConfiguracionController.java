/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Configuracion;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLConfiguracionController implements Initializable {

    private static int maxDuracionTutoria = 80;//SIEMPRE DE 10 EN 10
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    /*
    *@return max min per tut / 10
    */
    public static int getMaxDurTut(){
        return maxDuracionTutoria / 10;
    }
    
}
