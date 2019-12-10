/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipc.main.viewTutoria;

import accesoBD.AccesoBD;
import ipc.Main;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLVerTutoriaController implements Initializable {
    
    @FXML
    private ListView<Alumno> alumnos;
    @FXML
    private Label asignatura;
    @FXML
    private Label fecha;
    @FXML
    private TextArea textArea;
    @FXML
    private ComboBox<Tutoria.EstadoTutoria> estadoComboBox;
    
    private Tutoria tutoria;
    private boolean fromInside = false;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        System.out.println("tutoria: "+tutoria.getAsignatura().getCodigo()+","+tutoria.getEstado()+","+tutoria.getFecha());
alumnos.setItems(tutoria.getAlumnos());
alumnos.setCellFactory(c->new Celda());
asignatura.setText(tutoria.getAsignatura().getDescripcion()+" (" + tutoria.getAsignatura().getCodigo() + ")");
fecha.setText(tutoria.getFecha()+ " a las " + tutoria.getInicio() + " con una duración de " + tutoria.getDuracion().toHours()+" h "+tutoria.getDuracion().toMinutes()%60+" m");
textArea.setText(tutoria.getAnotaciones());

ObservableList<Tutoria.EstadoTutoria> list = FXCollections.observableArrayList(Tutoria.EstadoTutoria.ANULADA,Tutoria.EstadoTutoria.NO_ASISTIDA,Tutoria.EstadoTutoria.PEDIDA,Tutoria.EstadoTutoria.REALIZADA);
estadoComboBox.setItems(list);
estadoComboBox.setCellFactory(c->new ComboCell());
estadoComboBox.setButtonCell(new ListCell<Tutoria.EstadoTutoria>(){
    @Override
    protected void updateItem(Tutoria.EstadoTutoria item, boolean empty){
        super.updateItem(item,empty);
        if (item == null || empty) {
            setText(null);
        } else {
            Color col = new Color(1,1,1,1);
            if(item.value().equals(Tutoria.EstadoTutoria.PEDIDA.value())){
                col = new Color(0.4706, 0.9686, 0.3686,1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.ANULADA.value())){
                col = new Color(1, 0, 0, 1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.REALIZADA.value())){
                col = new Color(0.2784, 0.8549, 1,1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.NO_ASISTIDA.value())){
                col = new Color(1, 0.7686, 0,1);
            }
            Circle c = new Circle(10,col);
            HBox h = new HBox(c,new Text(item.value()));
            h.setSpacing(5);
            setGraphic(h);
        }
    }
});
estadoComboBox.getSelectionModel().select(tutoria.getEstado());

estadoComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    if(newValue != oldValue && !fromInside){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cambio del estado");
        alert.setHeaderText("¿Desea cambiar el estado de la tutoría de " + oldValue + " a " + newValue + " ?");
        
        ButtonType si = new ButtonType("Si");
        ButtonType no = new ButtonType("No");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(si,no);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result == null || result.get() == no){
            fromInside = true;
            estadoComboBox.getSelectionModel().select(oldValue);
        }else if(result.get() == si){
            estadoComboBox.getSelectionModel().select(newValue);
            int i=0;
            for(Tutoria t :AccesoBD.getInstance().getTutorias().getTutoriasConcertadas()){
                if(t.equals(tutoria)){
                    break;
                }
                i++;
            }
            AccesoBD.getInstance().getTutorias().getTutoriasConcertadas().get(i).setEstado(newValue);
            AccesoBD.getInstance().salvar();
            
            Main.getMainController().getCalendar().getDatePicker().getDayCellFactory().call(Main.getMainController().getCalendar().getDatePicker());
            
            //Main.getMainController().getTutoriaController().getCalendar().getDatePicker().getDayCellFactory().call(Main.getMainController().getTutoriaController().getCalendar().getDatePicker());
            Button b = Main.getMainController().getSelectedButton();
            Main.getMainController().mesesBtn.fire();
            Main.getMainController().semanasBtn.fire();
            Main.getMainController().diasBtn.fire();
            Main.getMainController().getWeek().drawTutorias();
            if(Main.getMainController().getTutoriaController() != null){
                Main.getMainController().getTutoriaController().getWeek().drawTutorias();
                Main.getMainController().getTutoriaController().getCalendar().getDatePicker().setValue(tutoria.getFecha().plusDays(1));
            }
            Main.getMainController().getCalendar().getDatePicker().setValue(tutoria.getFecha().plusDays(1));
            b.fire();
        }
    }
    fromInside = false;
});
    }
    
    @FXML
    private void accept(ActionEvent event) {
        ((Stage)estadoComboBox.getScene().getWindow()).close();
    }
    
    public void setTutoria(Tutoria tutoria){
        this.tutoria = tutoria;
    }
    
}
class Celda extends ListCell<Alumno> {
    
    @Override
    protected void updateItem(Alumno item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            setText(item.getApellidos() + ", " + item.getNombre());
            setStyle("-fx-background-color:lightgrey;");
        }
    }
}

class ComboCell extends ListCell<Tutoria.EstadoTutoria> {
    
    @Override
    protected void updateItem(Tutoria.EstadoTutoria item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            Color col = new Color(1,1,1,1);
            if(item.value().equals(Tutoria.EstadoTutoria.PEDIDA.value())){
                col = new Color(0.4706, 0.9686, 0.3686,1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.ANULADA.value())){
                col = new Color(1, 0, 0, 1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.REALIZADA.value())){
                col = new Color(0.2784, 0.8549, 1,1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.NO_ASISTIDA.value())){
                col = new Color(1, 0.7686, 0,1);
            }
            Circle c = new Circle(10,col);
            HBox h = new HBox(c,new Label(item.value()));
            setStyle("-fx-background-color:lightgrey;");
            h.setSpacing(5);
            setGraphic(h);
        }
    }
}
