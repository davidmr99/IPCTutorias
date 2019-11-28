/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import myLibrary.SwitchButton;

/**
 *
 * @author FMR
 */
public class FXMLMainController implements Initializable {
    
    @FXML
    private VBox sideBar;
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox filtroAsignaturas;
    @FXML
    private CheckBox selectAllSubjects;
    @FXML
    private ScrollPane scrollSubjects;
    @FXML
    private Button mesesBtn;
    @FXML
    private Button semanasBtn;
    @FXML
    private Button diasBtn;
    
    private String styleSheet = "styles/Main.css";
    
    public FXMLMainController(){
    }
    
    private void handleButtonAction(ActionEvent e) {
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SwitchButton btn = new SwitchButton();
        btn.setVisible(true);
        
        Button b = new Button("jeje");
        sideBar.getChildren().add(0,btn);
        borderPane.setCenter(b);
        
        for(int i=0;i<10;i++) {
            filtroAsignaturas.getChildren().add(new CheckBox(Integer.toString(i)+"dawdawdawdawwdwdawdawdawdaw"));
        }
        scrollSubjects.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollSubjects.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollSubjects.disableProperty().bind(selectAllSubjects.selectedProperty());
        
        borderPane.getStylesheets().add(styleSheet);
        
        
        mesesBtn.getStylesheets().add(styleSheet);
        semanasBtn.getStylesheets().add(styleSheet);
        diasBtn.getStylesheets().add(styleSheet);
        mesesBtn.setId("monthBtn");
        semanasBtn.setId("weekBtn");
        diasBtn.setId("dayBtn");
        
    }    
}
