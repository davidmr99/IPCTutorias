/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc.main.configuracion;

import accesoBD.AccesoBD;
import ipc.main.FXMLMainController;
import ipc.main.contextPane.Calendar;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Asignatura;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLConfiguracionController implements Initializable {

    private static int maxDuracionTutoria = 80;//SIEMPRE DE 10 EN 10
    private static final int MAX_DURACION = 12*60;
    @FXML
    private ComboBox<Integer> horas;
    @FXML
    private ComboBox<Integer> minutos;
    @FXML
    private Button enviar;
    private boolean minutosSet = false;
    private boolean horasSet = false;
    private int h = 12, m = 50;
    @FXML
    private TableColumn<Alumno, String> alumnos;
    @FXML
    private TableColumn<Asignatura, String> asignaturas;
    @FXML
    private TableView<Alumno> tablaAlumnos;
    @FXML
    private TableView<Asignatura> tablaAsignaturas;
    @FXML
    private TableColumn<Alumno,String> correos;
    @FXML
    private TableColumn<Asignatura, String> codigo;
    @FXML
    private Label timeExceded;
    
    @FXML
    private ListView<LocalDate> vacacionesList;
    @FXML
    private AnchorPane calendarNode;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vacacionesList.setItems(FXMLMainController.getVacaciones());
        
        // TODO
        horas.setPromptText("Horas");
        horas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            h = newValue * 60;
            horasSet = true;
            enviar.setDisable(!(horasSet && minutosSet && h + m <= MAX_DURACION && h + m > 0)); 
            timeExceded.setVisible(!(horasSet && minutosSet && h + m <= MAX_DURACION && h + m > 0));
        });
        for (int i = 0; i <= 12; i++) {
            horas.getItems().add(i);
            horas.setCellFactory(c-> new Celda());
            horas.setButtonCell(new ListCell<Integer>(){
                @Override
                protected void updateItem(Integer tiempo, boolean btl){
                    super.updateItem(tiempo, btl);
                    if(tiempo != null) {
                        setText(tiempo.toString());
                    }
                }
            });
        }
        
        minutos.setPromptText("Minutos");
        minutos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            m = newValue;
            minutosSet = true;
            enviar.setDisable(!(horasSet && minutosSet && h + m <= MAX_DURACION && h + m > 0));
            timeExceded.setVisible(!(horasSet && minutosSet && h + m <= MAX_DURACION && h + m > 0));
        });
        for (int i = 0; i <= 50; i = i + 10) {
            minutos.getItems().add(i);
            minutos.setCellFactory(c-> new Celda());
            minutos.setButtonCell(new ListCell<Integer>(){
                @Override
                protected void updateItem(Integer tiempo, boolean btl){
                    super.updateItem(tiempo, btl);
                    if(tiempo != null) {
                        setText(tiempo.toString());
                    }
                }
            });
        }
        System.out.println("h: "+maxDuracionTutoria/60+"  m: "+maxDuracionTutoria%60);
        minutos.getSelectionModel().select((maxDuracionTutoria % 60)/10);
        horas.getSelectionModel().select(maxDuracionTutoria / 60);
        
        
        tablaAlumnos.setItems(AccesoBD.getInstance().getTutorias().getAlumnosTutorizados());
        alumnos.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        correos.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        
        tablaAsignaturas.setItems(AccesoBD.getInstance().getTutorias().getAsignaturas());
        asignaturas.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        codigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        
        Calendar c = new Calendar(false);
        Node calendar = c.getCalendar();
        
        
        calendarNode.getChildren().add(calendar);
        AnchorPane.setTopAnchor(calendar, 0d);
        AnchorPane.setLeftAnchor(calendar, 0d);
        AnchorPane.setBottomAnchor(calendar, 0d);
        AnchorPane.setRightAnchor(calendar, 0d);
        
    }
    
    /*
    *@return max min per tut / 10
    */
    public static int getMaxDurTut(){
        return maxDuracionTutoria / 10;
    }

    @FXML
    private void save(ActionEvent event) {
        maxDuracionTutoria = horas.getValue()*60+minutos.getValue();
        ((Stage)horas.getScene().getWindow()).close();
    }
}

class Celda<T> extends ListCell<T> {
    
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            if(item instanceof Integer){
                setText(item.toString());
                setStyle("-fx-background-color:lightgrey;");
            }else if(item instanceof LocalDate){
                setText(((LocalDate) item).format(DateTimeFormatter.ISO_LOCAL_DATE));
                setStyle("-fx-background-color:lightgrey;");
            }
        }
    }
}