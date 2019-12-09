/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipc.main;

import accesoBD.AccesoBD;
import añadir.FXMLAlumnoController;
import añadir.FXMLAsignaturaController;
import añadir.FXMLTutoriaController;
import ipc.main.contextPane.Calendar;
import ipc.main.weekView.Week;
import java.io.IOException;
import static java.lang.Integer.MAX_VALUE;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Alumno;
import modelo.Asignatura;
import modelo.Tutoria;
import myLibrary.SwitchButton;

/**
 *
 * @author FMR
 */
public class FXMLMainController implements Initializable {
    
    @FXML
    private VBox filtroAsignaturas;
    @FXML
    private VBox calendarMenuVBox;
    @FXML
    private VBox menu0;
    @FXML
    private CheckBox selectAllSubjects;
    @FXML
    private ScrollPane scrollSubjects;
    @FXML
    public Button mesesBtn;
    @FXML
    public Button semanasBtn;
    @FXML
    public Button diasBtn;
    
    public String styleSheet = "styles/Main.css";
    @FXML
    private AnchorPane contextPane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private ComboBox<Alumno> comboBox;
    @FXML
    private MenuItem addTutoria;
    @FXML
    private MenuItem addAlumno;
    @FXML
    private MenuItem addAsignatura;
    
    private Node calendarNode;
    private Week week;
    private ScrollPane sp;
    private Calendar c;
    private FXMLTutoriaController tutoriaController;
    private LocalDateTime inicio,fin;
    private int eventSource, source;
    private ListView listView;
    private Node n;
    private SwitchButton btn;
    private ObservableList<Alumno> alumnos;
    private ObservableList<Asignatura> asignaturas;
    
    public FXMLMainController(){
        
    }
    
    
    public ScrollPane getScrollPane(){
        return scrollSubjects;
    }
    
    public VBox getvBox(){
        return filtroAsignaturas;
    }
    
    public MenuItem getAddTutoriaBtn(){
        return addTutoria;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btn = new SwitchButton();
        btn.setVisible(true);
        listView = new ListView<Tutoria>();
        AnchorPane.setTopAnchor(listView, 10d);
        AnchorPane.setBottomAnchor(listView, 10d);
        AnchorPane.setRightAnchor(listView, 10d);
        AnchorPane.setLeftAnchor(listView, 10d);
        
        btn.getButton().setOnMouseClicked((event) -> {
            if(btn.switchOnProperty().get()){
                System.out.println("ON");
                n = contextPane.getChildren().get(0);
                contextPane.getChildren().remove(n);
                contextPane.getChildren().add(listView);
            }else{
                System.out.println("OFF");
                contextPane.getChildren().remove(listView);
                contextPane.getChildren().add(n);
            }
        });
        
        menu0.getChildren().add(0,btn);
        
        
        alumnos = AccesoBD.getInstance().getTutorias().getAlumnosTutorizados();
        comboBox.setCellFactory(c-> new Celda());
        comboBox.setButtonCell(new ListCell<Alumno>(){
            @Override
            protected void updateItem(Alumno item, boolean btl){
                super.updateItem(item, btl);
                if(item != null) {
                    setText(item.getApellidos()+", "+item.getNombre());
                }
            }
        });
        comboBox.setItems(alumnos);
        reloadAlumnosYAsignaturas();
        
        scrollSubjects.maxHeightProperty().bind(filtroAsignaturas.heightProperty().add(20));
        scrollSubjects.setFitToHeight(true);
        
        scrollSubjects.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollSubjects.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollSubjects.disableProperty().bind(selectAllSubjects.selectedProperty());
        
        //configBtn.getParent().getStylesheets().add(styleSheet);
        
        mesesBtn.setId("monthBtn");
        semanasBtn.setId("weekBtn");
        diasBtn.setId("dayBtn");
        
        
        calendarMenuVBox.visibleProperty().bind(btn.switchOnProperty().not());
//        contextPane.visibleProperty().bind(btn.switchOnProperty().not());
listView.visibleProperty().bind(btn.switchOnProperty());

c = new Calendar(true);
calendarNode = c.getCalendar();
calendarNode.visibleProperty().bind(btn.switchOnProperty().not());

contextPane.getChildren().add(calendarNode);
AnchorPane.setTopAnchor(calendarNode, 0d);
AnchorPane.setLeftAnchor(calendarNode, 0d);
AnchorPane.setBottomAnchor(calendarNode, 0d);
AnchorPane.setRightAnchor(calendarNode, 0d);

//PARA VER TODAS LAS SUBCLASES DE LOS NODOS
//        for (Node node : calendarNode.lookupAll("*")) {
//            System.out.println("\t" + node);
//        }
    }
    
    @FXML
    private void swapCalendarView(ActionEvent e) {
        if(e.getSource().equals(mesesBtn)) {
            semanasBtn.setDisable(false);
            diasBtn.setDisable(false);
            mesesBtn.setDisable(true);
            
            contextPane.getChildren().remove(sp);
            contextPane.getChildren().add(calendarNode);
        }else if(e.getSource().equals(semanasBtn)) {
            weeksButton();
        }else if(e.getSource().equals(diasBtn)) {
            daysButton();
        }
    }
    
    @FXML
    private void addTutoria(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/añadir/FXMLTutoria.fxml"));
        Parent root = loader.load();
        tutoriaController = loader.getController();
        
        //obtenemos la referencia del controlador par poder invocar el metodo publico initText()
        //tutoriaController = loader.getController();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Añadir Tutoría");
        //stage.setResizable(false);
        DayOfWeek day = DayOfWeek.from(c.getDate());
        if(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
            tutoriaController.setDate(c.getDate().plusDays(8 - day.getValue()));
        }else{
            tutoriaController.setDate(c.getDate());
        }
        
        //if(launchTutFromWeekPane){
        tutoriaController.setDates(inicio, fin,eventSource,source);
        //}
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setMinWidth(920);
        stage.setMinHeight(625);
        stage.showAndWait();
    }
    
    @FXML
    private void addAlumno(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/añadir/FXMLAlumno.fxml"));
        Parent root = miLoader.load();
        //        Parent root = FXMLLoader.load(getClass().getResource("/vista/FXMLpersona.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Añadir alumno");
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLAlumnoController controller = miLoader.getController();
        //        miLoader.setController(controller);
        //        miLoader.setRoot(root);//        controller.initStage(stage);
        stage.setResizable(false);
        stage.showAndWait();
        //        if (controller.hayPersona()) {
        //            datos.add(controller.getPersona());
        //        }
    }
    
    @FXML
    private void addAsignatura(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/añadir/FXMLAsignatura.fxml"));
        Parent root = miLoader.load();
        //        Parent root = FXMLLoader.load(getClass().getResource("/vista/FXMLpersona.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Añadir asignatura");
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLAsignaturaController controller = miLoader.getController();
        //        miLoader.setController(controller);
        //        miLoader.setRoot(root);//        controller.initStage(stage);
        stage.setResizable(false);
        stage.showAndWait();
        //        if (controller.hayPersona()) {
        //            datos.add(controller.getPersona());
        //        }
    }
    
    private void weeksButton() {
        mesesBtn.setDisable(false);
        diasBtn.setDisable(false);
        semanasBtn.setDisable(true);
        
        contextPane.getChildren().remove(calendarNode);
        int diasDeLaSemana = 7;
        week = new Week(diasDeLaSemana,true);
        week.init();
        week.setWeekDays(c.getDate());
        
        GridPane days = new GridPane();
        days.setGridLinesVisible(true);
        String[] dias = {"","L","M","X","J","V","S","D"};
        for(int i=0;i<=diasDeLaSemana;i++){
            ColumnConstraints column = new ColumnConstraints(100);
            column.setFillWidth(true);
            column.setPercentWidth(((Double)(100/(double)(diasDeLaSemana+1))));
            days.getColumnConstraints().add(column);
        }
        int m = 0;
        int m2 = 0;
        for(int i=0;i<=diasDeLaSemana;i++){
            Button l;
            if(i>0){
                LocalDate ld = week.getDays()[i-1];
                l = new Button(dias[i] + " " +ld.getDayOfMonth()+"/"+ld.getMonthValue());
                l.setOnMouseClicked((event) -> {
                    c.getDatePicker().setValue(ld);
                    diasBtn.fire();
                });
                if(m != ld.getMonthValue()){
                    m2 = ld.getMonthValue();
                }
            }else {
                m = week.getDays()[0].getMonthValue();
                l = null;
            }
            if(l != null){
                l.setMaxSize(MAX_VALUE, MAX_VALUE);
                days.add(l,i,0);
            }
            
            if(i == 6 || i == 7){
                l.setDisable(true);
                l.setOpacity(1);
                l.setStyle("-fx-background-color: gainsboro;");
            }
        }
        Button l;
        if(m2 == 0){
            l = new Button("Mes: " + m);
        }else{
            l = new Button("Meses: " + m + " / " + m2);
        }
        l.setMaxSize(MAX_VALUE, MAX_VALUE);
        days.add(l,0,0);
        sp = new ScrollPane(week);
        sp.visibleProperty().bind(btn.switchOnProperty().not());
        sp.setId("scrollWeek");
        sp.setVvalue(sp.getVmax()/2);
        
        VBox vb = new VBox();
        vb.visibleProperty().bind(btn.switchOnProperty().not());
        
        AnchorPane a = new AnchorPane(days);
        vb.getChildren().add(a);
        AnchorPane.setRightAnchor(days, 20d);
        AnchorPane.setLeftAnchor(days, 0d);
        a.setMaxWidth(MAX_VALUE);
        vb.getChildren().add(sp);
        
        AnchorPane.setTopAnchor(vb, 0d);
        AnchorPane.setLeftAnchor(vb, 0d);
        AnchorPane.setBottomAnchor(vb, 0d);
        AnchorPane.setRightAnchor(vb, 0d);
        contextPane.getChildren().add(vb);
    }
    
    private void daysButton() {
        mesesBtn.setDisable(false);
        diasBtn.setDisable(true);
        semanasBtn.setDisable(false);
        
        contextPane.getChildren().remove(calendarNode);
        int diaColumna = 1;
        week = new Week(diaColumna,true);
        week.init();
        
        DayOfWeek day = DayOfWeek.from(c.getDate());
        if(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
            week.setDay(c.getDate().plusDays(8 - day.getValue()));
        }else{
            week.setDay(c.getDate());
        }
        
        GridPane days = new GridPane();
        days.setGridLinesVisible(true);
        for(int i=0;i<=diaColumna;i++){
            ColumnConstraints column = new ColumnConstraints(100);
            column.setFillWidth(true);
            column.setPercentWidth(((Double)(100/(double)(diaColumna+1))));
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
        sp = new ScrollPane(week);
        sp.setId("scrollWeek");
        sp.setVvalue(sp.getVmax()/2);
        
        VBox vb = new VBox();
        vb.visibleProperty().bind(btn.switchOnProperty().not());
        AnchorPane a = new AnchorPane(days);
        vb.getChildren().add(a);
        AnchorPane.setRightAnchor(days, 20d);
        AnchorPane.setLeftAnchor(days, 0d);
        a.setMaxWidth(MAX_VALUE);
        vb.getChildren().add(sp);
        
        AnchorPane.setTopAnchor(vb, 0d);
        AnchorPane.setLeftAnchor(vb, 0d);
        AnchorPane.setBottomAnchor(vb, 0d);
        AnchorPane.setRightAnchor(vb, 0d);
        contextPane.getChildren().add(vb);
    }
    
    public FXMLTutoriaController getTutoriaController() {
        return tutoriaController;
    }
    
    public void launchTutFromWeekPane(LocalDateTime inicio, LocalDateTime fin,int eventSouce, int source) {
        this.inicio = inicio;
        this.fin = fin;
        this.eventSource = eventSouce;
        this.source = source;
    }
    
    public void reloadAlumnosYAsignaturas() {
        filtroAsignaturas.getChildren().remove(0,filtroAsignaturas.getChildren().size());
        
        asignaturas = AccesoBD.getInstance().getTutorias().getAsignaturas();
        for(Asignatura a:asignaturas){
            CheckBox c = new CheckBox(a.getCodigo());
            c.setTooltip(new Tooltip(a.getDescripcion()));
            filtroAsignaturas.getChildren().add(c);
        }
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