/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package añadir;

import ipc.main.configuracion.FXMLConfiguracionController;
import accesoBD.AccesoBD;
import ipc.Main;
import ipc.main.contextPane.Calendar;
import ipc.main.weekView.Week;
import java.io.IOException;
import static java.lang.Integer.MAX_VALUE;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Asignatura;
import modelo.Tutoria;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLTutoriaController implements Initializable {
    
    @FXML
    private AnchorPane calendarAnchor;
    @FXML
    private AnchorPane dayAnchor;
    @FXML
    private Label dayLabel;
    @FXML
    private TextArea descripcionField;
    @FXML
    private Spinner<Integer> hInicio;
    @FXML
    private Spinner<Integer> duracion;
    @FXML
    private Label timeLabel;
    @FXML
    private Spinner<Integer> mInicio;
    
    
    private ScrollPane sp;
    private Week week;
    private Calendar c;
    private LocalDate date;
    private GridPane days;
    private int lastHInicioValue,lastMInicioValue,lastDuracionValue;
    private ObservableList<Alumno> alumnos;
    private static ObservableList<Alumno> alumnosElegidos;
    
    private LocalDateTime inicio,fin;
    @FXML
    private ComboBox<Alumno> alumnosComboBox;
    @FXML
    private ListView<Alumno> alumnosList;
    @FXML
    private ComboBox<Asignatura> asignaturasComboBox;
    @FXML
    private Button aceptar;
    private Tutoria tutoria = null;
    private Asignatura asignatura = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        c = new Calendar(false);
        
        calendarAnchor.getChildren().add(c.getCalendar());
        AnchorPane.setTopAnchor(c.getCalendar(), 0d);
        AnchorPane.setLeftAnchor(c.getCalendar(), 0d);
        AnchorPane.setBottomAnchor(c.getCalendar(), 0d);
        AnchorPane.setRightAnchor(c.getCalendar(), 0d);
        if(date == null){
            date = c.getDate();
        }
        alumnosElegidos = FXCollections.observableArrayList();
        
        alumnos = AccesoBD.getInstance().getTutorias().getAlumnosTutorizados();
        alumnosComboBox.setCellFactory(c-> new Celda());
        alumnosComboBox.setButtonCell(new ListCell<Alumno>(){
            @Override
            protected void updateItem(Alumno item, boolean btl){
                super.updateItem(item, btl);
            }
        });
        alumnosComboBox.setItems(alumnos);
        alumnosComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(!alumnosElegidos.contains(newValue)){
                alumnosElegidos.add(newValue);
                alumnosList.refresh();
            }
        });
        
        alumnosList.setItems(alumnosElegidos);
        alumnosList.setCellFactory(lv -> new ListCelda());
        
        alumnosList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event) {
                Alumno a = alumnosList.getSelectionModel().getSelectedItem();
                alumnosElegidos.remove(a);
            }
        });
        
        ObservableList<Asignatura> asignaturas = AccesoBD.getInstance().getTutorias().getAsignaturas();
        asignaturasComboBox.setCellFactory(c-> new Celda());
        asignaturasComboBox.setButtonCell(new ListCell<Asignatura>(){
            @Override
            protected void updateItem(Asignatura item, boolean btl){
                super.updateItem(item, btl);
                if(item != null) {
                    setText(item.getCodigo());
                    asignatura = item;
                }
            }
        });
        asignaturasComboBox.setItems(asignaturas);
        
        daysButton();
    }
    
    public static ObservableList<Alumno> getSelectedAlumnos(){
        return alumnosElegidos;
    }
    
    private void daysButton() {
        int diaColumna = 1;
        week = new Week(diaColumna,false);
        week.init();
        week.setDay(date);
        
        days = new GridPane();
        days.setGridLinesVisible(true);
        for(int i=0;i<=diaColumna;i++){
            ColumnConstraints column = new ColumnConstraints();
            column.setFillWidth(true);
            if(i==0){
                column.setPercentWidth(20);
            }else{
                column.setPercentWidth(80);
            }
            days.getColumnConstraints().add(column);
        }
        
        for(int i=0;i<=diaColumna;i++){
            Button l = null;
            if(i>0){
                LocalDate ld = week.getDays()[i-1];
                l = new Button(ld.getDayOfWeek() + " " +ld.getDayOfMonth()+"/"+ld.getMonthValue());
            }
            
            if(l != null){
                l.setMaxSize(MAX_VALUE, MAX_VALUE);
                days.add(l,i,0);
            }
        }
        Button l;
        l = new Button("Dia: ");
        
        l.setMaxSize(MAX_VALUE, MAX_VALUE);
        days.add(l,0,0);
        
        
//        vBoxDay.getChildren().add(0,days);
//        dayAnchorWeek.getChildren().add(week);


VBox vb = new VBox();


AnchorPane a = new AnchorPane(days);
AnchorPane.setRightAnchor(days, 20d);
AnchorPane.setLeftAnchor(days, 0d);
a.setMaxWidth(MAX_VALUE);
vb.getChildren().add(a);

AnchorPane b = new AnchorPane(week);
AnchorPane.setRightAnchor(week, 0d);
AnchorPane.setLeftAnchor(week, 0d);
b.setMaxWidth(MAX_VALUE);


sp = new ScrollPane(b);
sp.setId("scrollWeek");
sp.setVvalue(sp.getVmax()/2);
sp.setHbarPolicy(ScrollBarPolicy.NEVER);

vb.getChildren().add(sp);

b.setStyle("-fx-background-color: grey;");
b.prefWidthProperty().bind(sp.widthProperty().subtract(20));

AnchorPane.setTopAnchor(vb, 0d);
AnchorPane.setLeftAnchor(vb, 0d);
AnchorPane.setBottomAnchor(vb, 0d);
AnchorPane.setRightAnchor(vb, 0d);
dayAnchor.getChildren().add(vb);
c.getDatePicker().setValue(date);
c.getCalendar().setOnMouseClicked((event) -> {
    setDate(date);
});
    }
    
    @FXML
    private void editTextArea(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/añadir/FXMLDescripcionField.fxml"));
        Parent root = miLoader.load();
        //        Parent root = FXMLLoader.load(getClass().getResource("/vista/FXMLpersona.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Editar descripción");
        stage.getIcons().add(new Image("ipc/resources/description-icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLDescripcionFieldController controller = miLoader.getController();
        miLoader.setController(controller);
        controller.setText(descripcionField.getText());
        stage.setResizable(false);
        stage.showAndWait();
        if (controller.isTextEdited()) {
            descripcionField.setText(controller.getText());
        }
    }
    
    public void setDate(LocalDate localDate){
        dayLabel.setText(localDate.getDayOfMonth()+" / "+ localDate.getMonthValue() + " / "+localDate.getYear());
        date = localDate;
        daysButton();
    }
    
    public void setDates(LocalDateTime inicio,LocalDateTime fin){
        this.date = LocalDate.of(inicio.getYear(), inicio.getMonthValue(), inicio.getDayOfMonth());
        this.inicio = inicio;
        this.fin = fin;
        System.out.println("buenass");
        dayLabel.setText(date.getDayOfMonth()+" / "+ date.getMonthValue() + " / "+date.getYear());
        timeLabel.setText("Duración: " + inicio.getHour() + ":" + inicio.getMinute() + " - " + fin.getHour() + ":" + fin.getMinute());
        
        SpinnerValueFactory<Integer> hInicioFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 19, inicio.getHour());
        hInicio.setValueFactory(hInicioFactory);
        lastHInicioValue = hInicio.getValue();
        
        SpinnerValueFactory<Integer> mInicioFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, inicio.getMinute(),10);
        mInicio.setValueFactory(mInicioFactory);
        lastMInicioValue = mInicio.getValue();
        
        SpinnerValueFactory<Integer>duracionFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, FXMLConfiguracionController.getMaxDurTut()*10, getDuracionMins(inicio,fin),10);
        duracion.setValueFactory(duracionFactory);
        lastDuracionValue = duracion.getValue();
        
        hInicio.valueProperty().addListener((observable) -> {
            updateTime();
            lastHInicioValue = hInicio.getValue();
        });
        mInicio.valueProperty().addListener((observable) -> {
            updateTime();
            lastMInicioValue = mInicio.getValue();
        });
        duracion.valueProperty().addListener((observable) -> {
            updateTime();
            lastDuracionValue = duracion.getValue();
        });
    }
    
    private void updateTime(){
        week.initButton();
        LocalDateTime ldt = LocalDateTime.of(2000, 1, 1, hInicio.getValue(), mInicio.getValue());
        int index0 = Week.getIndex(ldt, false);
        int indexF = Week.getIndex(ldt.plusMinutes(duracion.getValue()), true);
        if(indexF < 12 * 6){
            week.drawButtonAndStuff(index0,indexF);
            timeLabel.setText("Duración: " + hInicio.getValue() + ":" + mInicio.getValue() + " - " + Week.getTime(indexF,true).getHour() + ":" + Week.getTime(indexF,true).getMinute());
            
            inicio = LocalDateTime.of(inicio.toLocalDate(), LocalTime.of(hInicio.getValue(), mInicio.getValue()));
            fin = LocalDateTime.of(inicio.toLocalDate(), LocalTime.of(Week.getTime(indexF,true).getHour(), Week.getTime(indexF,true).getMinute()));
                    
        }else {
            hInicio.getValueFactory().setValue(lastHInicioValue);
            mInicio.getValueFactory().setValue(lastMInicioValue);
            duracion.getValueFactory().setValue(lastDuracionValue);
        }
    }
    
    public void updateLabelsFromOutside(int i,int j){
        LocalDateTime ld = Week.getTime(i,false);
        LocalDateTime ld2 = Week.getTime(j,true);
        int duration = getDuracionMins(ld, ld2);
        
        if(hInicio.getValueFactory()==null){
            SpinnerValueFactory<Integer> hInicioFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(8, 19, 14);
            hInicio.setValueFactory(hInicioFactory);
            lastHInicioValue = hInicio.getValue();
        }
        if(mInicio.getValueFactory()==null){
            SpinnerValueFactory<Integer> mInicioFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0,10);
            mInicio.setValueFactory(mInicioFactory);
            lastMInicioValue = mInicio.getValue();
        }
        if(duracion.getValueFactory()==null){
            SpinnerValueFactory<Integer> duracionFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, FXMLConfiguracionController.getMaxDurTut()*10, 20,10);
            duracion.setValueFactory(duracionFactory);
            lastDuracionValue = duracion.getValue();
        }
        hInicio.getValueFactory().setValue(ld.getHour());
        mInicio.getValueFactory().setValue(ld.getMinute());
        duracion.getValueFactory().setValue(duration);
        
        inicio = ld;
        fin = ld2;
        
        timeLabel.setText("Duración: " + hInicio.getValue() + ":" + mInicio.getValue() + " - " + ld2.getHour() + ":" + ld2.getMinute());
    }
    
    public void setDates(LocalDateTime inicio,LocalDateTime fin,int eventSource, int source){
        if(inicio == null || fin == null){
            inicio = c.getDate().atTime(14, 0);
            fin = c.getDate().atTime(14, 20);
            eventSource = Week.getIndex(inicio, false);
            source = Week.getIndex(fin, true);
            System.out.println("dates: "+inicio+"  "+fin);
        }
        setDates(inicio, fin);
        if(eventSource != -1 && source != -1){
            week.initButton();
            week.drawButtonAndStuff(eventSource,source);
        }
        
        
    }
    public void dayChanged(){
        timeLabel.setText("Duración: ");
        
        date = c.getDate();
        c.getDatePicker().setValue(date);
    }
    
    public int getDuracionMins(LocalDateTime inicio,LocalDateTime fin){
        return (fin.getHour()-inicio.getHour()) * 60 + (fin.getMinute() - inicio.getMinute());
    }
    
    private void salvar(){
        if ((!alumnosElegidos.isEmpty())
                && (asignatura != null)
                && (date != null)
                && (inicio != null && fin != null)){
            
            if(descripcionField.getText().trim().isEmpty()){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Alerta");
                alert.setHeaderText("No ha especificado ninguna anotación\n¿Quiere añadirlos ahora?");
                ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("ipc/resources/warning-icon.png"));
                ButtonType si = new ButtonType("Si");
                ButtonType no = new ButtonType("No");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(si,no);
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result == null || result.get() == si){
                    return;
                }
            }else;
            
            tutoria = new Tutoria();
            tutoria.getAlumnos().addAll(alumnosElegidos);
            tutoria.setAsignatura(asignatura);
            tutoria.setDuracion(Duration.between(inicio.toLocalTime(), fin.toLocalTime()));
            tutoria.setFecha(date);
            tutoria.setInicio(inicio.toLocalTime());
            tutoria.setEstado(Tutoria.EstadoTutoria.PEDIDA);
            if(!descripcionField.getText().trim().isEmpty()){
                tutoria.setAnotaciones(descripcionField.getText().trim());
            }
            
            System.out.println("creando tutoria el "+ date+" at "+inicio.toLocalTime()+" durando: "+Duration.between(inicio.toLocalTime(), fin.toLocalTime()).toMinutes());
            
            AccesoBD.getInstance().getTutorias().getTutoriasConcertadas().add(tutoria);
            AccesoBD.getInstance().salvar();
            ((Stage)aceptar.getScene().getWindow()).close();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Tutoría creada");
            alert.setHeaderText("Se ha creado su tutoría con éxito");
            alert.setContentText("Dia: "+date.format(DateTimeFormatter.ISO_LOCAL_DATE)
                    +"\nHora inicio: "+inicio.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME)
                    +"\nHora fin: "+fin.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME)
                    +"\nDuración: "+Duration.between(inicio.toLocalTime(), fin.toLocalTime()).toMinutes()+" m"
                    +"\nAsignatura: "+asignatura.getDescripcion()+" ("+asignatura.getCodigo()+")");

            alert.showAndWait();
        }else{
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image("ipc/resources/error-icon.png"));
            alert.setHeaderText("Rellene todos los campos obligatorios: ");
            alert.setContentText("Alumnos\nAsignatura\nFecha\nHora de inicio\nDuración");
            Main.getMainController().updateTutorias();
            alert.showAndWait();
        }
    }

    @FXML
    private void aceptar(ActionEvent event) {
        salvar();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        ((Stage)aceptar.getScene().getWindow()).close();
    }
}

class Celda<E> extends ListCell<E> {
    
    @Override
    protected void updateItem(E item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            if(item instanceof Alumno){
                setText(((Alumno)item).getApellidos() + ", " + ((Alumno)item).getNombre());
                setStyle("-fx-background-color:lightgrey;");
            }else if(item instanceof Asignatura){
                setText(((Asignatura)item).getCodigo());
                setTooltip(new Tooltip(((Asignatura)item).getDescripcion()));
                setStyle("-fx-background-color:lightgrey;");
            }
        }
    }
}
class ListCelda extends ListCell<Alumno> {
    @Override
    protected void updateItem(Alumno item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(null);
            setText(null);
            Image img = new Image("/ipc/resources/cancel-icon.png");
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(28);
            imgView.setFitWidth(28);
            HBox h = new HBox(imgView,new Label(item.getApellidos() + ", " + item.getNombre()+" "));
            setCursor(Cursor.HAND);
            setGraphic(h);
        }
    }
}