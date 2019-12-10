/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc.main.configuracion;

import accesoBD.AccesoBD;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import modelo.Alumno;
import modelo.Asignatura;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLConfiguracionController implements Initializable {

    private static int maxDuracionTutoria = 80;//SIEMPRE DE 10 EN 10
    private static final int maxDuracion = 12*60;
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
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        horas.setPromptText("Horas");
        horas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            h = newValue * 60;
            horasSet = true;
            enviar.setDisable(!(horasSet && minutosSet && h + m <= maxDuracion && h + m > 0)); 
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
            enviar.setDisable(!(horasSet && minutosSet && h + m <= maxDuracion && h + m > 0)); 
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
//        alumnos.setItems(AccesoBD.getInstance().getTutorias().getAlumnosTutorizados());
        tablaAlumnos.setItems(AccesoBD.getInstance().getTutorias().getAlumnosTutorizados());
        alumnos.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        //tablaAlumnos.getColumns().add(alumnos);//ERROR DE DUPLICACION
//        alumnos.setCellFactory(lv -> new ListCelda());
//        asignaturas.setItems(AccesoBD.getInstance().getTutorias().getAsignaturas());
//        asignaturas.setCellFactory(lv -> new ListCelda());
    }
    
    /*
    *@return max min per tut / 10
    */
    public static int getMaxDurTut(){
        return maxDuracionTutoria / 10;
    }
    
}

class Celda extends ListCell<Integer> {
    
    @Override
    protected void updateItem(Integer tiempo, boolean empty) {
        super.updateItem(tiempo, empty);
        if (tiempo == null || empty) {
            setText(null);
        } else {
            setText(tiempo.toString());
            setStyle("-fx-background-color:lightgrey;");
        }
    }
}

class ListCelda<T> extends ListCell<T> {
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            if (item instanceof Alumno) {
                setText(((Alumno)item).getApellidos() + ", " + ((Alumno)item).getNombre());
            } else { setText(((Asignatura)item).getDescripcion()); }
        }
    }
}