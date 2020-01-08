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
import ipc.Main;
import ipc.main.ayuda.FXMLAyudaController;
import ipc.main.configuracion.FXMLConfiguracionController;
import ipc.main.contextPane.Calendar;
import ipc.main.viewTutoria.FXMLVerTutoriaController;
import ipc.main.weekView.Week;
import java.io.IOException;
import static java.lang.Integer.MAX_VALUE;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
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
    private TableView<Tutoria> tableView;
    private Node n;
    private SwitchButton btn;
    private ObservableList<Alumno> alumnos;
    private ObservableList<Asignatura> asignaturas;
    private ObservableList<Tutoria> tutorias;
    private static ObservableList<LocalDate> vacaciones;
    private ObservableList<Asignatura> filtroAsignaturasList;
    private Button selected;
    @FXML
    private Menu configuracion;
    @FXML
    private Menu ayuda;
    
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
        selected = mesesBtn;
        vacaciones = FXCollections.observableArrayList();
        vacaciones.add(LocalDate.of(2019, 12, 24));
        vacaciones.add(LocalDate.of(2019, 12, 25));
        vacaciones.add(LocalDate.of(2019, 12, 31));
        
        
        btn = new SwitchButton();
        btn.setVisible(true);
        tableView = new TableView</*Alumno*/>();
        AnchorPane.setTopAnchor(tableView, 10d);
        AnchorPane.setBottomAnchor(tableView, 10d);
        AnchorPane.setRightAnchor(tableView, 10d);
        AnchorPane.setLeftAnchor(tableView, 10d);
        
        btn.getButton().setOnMouseClicked((event) -> {
            if(btn.switchOnProperty().get()){
                System.out.println("ON");
                n = contextPane.getChildren().get(0);
                contextPane.getChildren().remove(n);
                contextPane.getChildren().add(tableView);
            }else{
                System.out.println("OFF");
                contextPane.getChildren().remove(tableView);
                contextPane.getChildren().add(n);
                System.out.println("ONnnnnnnnn");
                if(diasBtn.isDisabled()){
                System.out.println("days");
                    week.drawTutorias();
                    semanasBtn.fire();
                    diasBtn.fire();
                }else if(semanasBtn.isDisabled()){
                System.out.println("weeks");
                    week.drawTutorias();
                    diasBtn.fire();
                    semanasBtn.fire();
                }else if(mesesBtn.isDisabled()){
                System.out.println("months");
                    calendarStuff();
                    diasBtn.fire();
                    mesesBtn.fire();
                }
            }
        });
        
        menu0.getChildren().add(0,btn);
        
        
        alumnos = AccesoBD.getInstance().getTutorias().getAlumnosTutorizados();
        tutorias = AccesoBD.getInstance().getTutorias().getTutoriasConcertadas();
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
        tableView.getItems().addAll(tutorias);//----------------------------------------------------------------------------------------------------------------------------------------------
        tableView.setRowFactory( tv -> {
            TableRow<Tutoria> row = new TableRow<>();
            if(!row.isEmpty() && row.getItem() != null){
                if(row.getItem().getFecha().isBefore(c.getDate())){
                    row.setDisable(true);
                }
            }
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    try {
                        Tutoria tutoria = row.getItem();
                        viewTutoria(tutoria);
                    } catch (IOException ex) {
                        Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            return row;
        });
        
        TableColumn<Tutoria, Tutoria.EstadoTutoria> estadoCol = new TableColumn<>("Estado");
        TableColumn<Tutoria, String> asignaturaCol = new TableColumn<>("Asignatura");
        TableColumn<Tutoria, String> fechaCol = new TableColumn<>("Fecha");
        TableColumn<Tutoria, String> horaCol = new TableColumn<>("Hora Inicio");
        TableColumn<Tutoria, Duration> duracionCol = new TableColumn<>("Duracion");
        
        estadoCol.setCellValueFactory(cellData -> cellData.getValue().estadoProperty());
        asignaturaCol.setCellValueFactory(cellData -> cellData.getValue().getAsignatura().codigoProperty());
        fechaCol.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        horaCol.setCellValueFactory(cellData -> cellData.getValue().inicioProperty());
        duracionCol.setCellValueFactory(cellData -> cellData.getValue().duracionProperty());
        
        duracionCol.setCellFactory(c->new TutoriaDuracionCelda());
        estadoCol.setCellFactory(c->new TutoriaEstadoCelda());
        
        tableView.getColumns().addAll(estadoCol,asignaturaCol, fechaCol,horaCol,duracionCol);
        
        reloadAlumnosYAsignaturas();
        updateTutorias();
        
        scrollSubjects.maxHeightProperty().bind(filtroAsignaturas.heightProperty().add(20));
        scrollSubjects.setFitToHeight(true);
        
        scrollSubjects.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollSubjects.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollSubjects.disableProperty().bind(selectAllSubjects.selectedProperty());
        
        mesesBtn.setId("monthBtn");
        semanasBtn.setId("weekBtn");
        diasBtn.setId("dayBtn");
        
        
        calendarMenuVBox.visibleProperty().bind(btn.switchOnProperty().not());
        tableView.visibleProperty().bind(btn.switchOnProperty());
        
        calendarStuff();
        
//        filtroAsignaturas.setOnMouseClicked((event) -> {
//            reloadAlumnosYAsignaturas();
//            System.out.println("lista: "+ filtroAsignaturasList);
//        });
        
        
        selectAllSubjects.setOnMouseClicked((event) -> {
           if(!btn.switchOnProperty().getValue()){
                System.out.println("ONnnnnnnnn");
                if(diasBtn.isDisabled()){
                    System.out.println("days");
                    week.drawTutorias();
                    semanasBtn.fire();
                    diasBtn.fire();
                }else if(semanasBtn.isDisabled()){
                    System.out.println("weeks");
                    week.drawTutorias();
                    diasBtn.fire();
                    semanasBtn.fire();
                }else if(mesesBtn.isDisabled()){
                    System.out.println("months");
                    calendarStuff();
                    diasBtn.fire();
                    mesesBtn.fire();
                }
            }else{
               tableView.getItems().clear();
            for(Tutoria t:tutorias){
                boolean add = false;
                for(Asignatura as:filtroAsignaturasList){
                    if(t.getAsignatura().getCodigo().equals(as.getCodigo())){
                        add = true;
                        break;
                    }
                }
                if(add || Main.getMainController().getAllSubjectsFilterButton().isSelected()){
                    tableView.getItems().add(t);
                }
            }
           }
        });
        
        
//PARA VER TODAS LAS SUBCLASES DE LOS NODOS
//        for (Node node : calendarNode.lookupAll("*")) {
//            System.out.println("\t" + node);
//        }
    }
    
    public CheckBox getAllSubjectsFilterButton(){
        return selectAllSubjects;
    }
    
    private void calendarStuff(){
        
        c = new Calendar(true);
        calendarNode = c.getCalendar();
        calendarNode.visibleProperty().bind(btn.switchOnProperty().not());
        
        contextPane.getChildren().add(calendarNode);
        AnchorPane.setTopAnchor(calendarNode, 0d);
        AnchorPane.setLeftAnchor(calendarNode, 0d);
        AnchorPane.setBottomAnchor(calendarNode, 0d);
        AnchorPane.setRightAnchor(calendarNode, 0d);
    }
    
    public static ObservableList<LocalDate> getVacaciones(){
        return vacaciones;
    }
    
    @FXML
    private void swapCalendarView(ActionEvent e) {
        if(e.getSource().equals(mesesBtn)) {
            selected = mesesBtn;
            semanasBtn.setDisable(false);
            diasBtn.setDisable(false);
            mesesBtn.setDisable(true);
            
            contextPane.getChildren().remove(sp);
            contextPane.getChildren().add(calendarNode);
        }else if(e.getSource().equals(semanasBtn)) {
            selected = semanasBtn;
            weeksButton();
        }else if(e.getSource().equals(diasBtn)) {
            selected = diasBtn;
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
        stage.getIcons().add(new Image("ipc/resources/add-icon.png"));
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
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Añadir alumno");
        stage.getIcons().add(new Image("ipc/resources/add-icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLAlumnoController controller = miLoader.getController();
        stage.setResizable(false);
        stage.showAndWait();
    }
    
    @FXML
    private void addAsignatura(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/añadir/FXMLAsignatura.fxml"));
        Parent root = miLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Añadir asignatura");
        stage.getIcons().add(new Image("ipc/resources/add-icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLAsignaturaController controller = miLoader.getController();
        stage.setResizable(false);
        stage.showAndWait();
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
        }else {
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
        filtroAsignaturasList = FXCollections.observableArrayList();
        for(Asignatura a:asignaturas){
            CheckBox c = new CheckBox(a.getCodigo());
            c.setTooltip(new Tooltip(a.getDescripcion()));
            c.setOnMouseClicked((event) -> {
                if(c.isSelected()){
                    filtroAsignaturasList.add(a);
                }else {
                    filtroAsignaturasList.remove(a);
                }
                System.out.println("asignatura: "+filtroAsignaturasList);
                
                if(!btn.switchOnProperty().getValue()){
                    System.out.println("ONnnnnnnnn");
                    if(diasBtn.isDisabled()){
                        week.drawTutorias();
                        semanasBtn.fire();
                        diasBtn.fire();
                    }else if(semanasBtn.isDisabled()){
                        week.drawTutorias();
                        diasBtn.fire();
                        semanasBtn.fire();
                    }else if(mesesBtn.isDisabled()){
                        calendarStuff();
                        diasBtn.fire();
                        mesesBtn.fire();
                    }
                }
            tableView.getItems().clear();
            for(Tutoria t:tutorias){
                boolean add = false;
                for(Asignatura as:filtroAsignaturasList){
                    if(t.getAsignatura().getCodigo().equals(as.getCodigo())){
                        add = true;
                        break;
                    }
                }
                if(add || Main.getMainController().getAllSubjectsFilterButton().isSelected()){
                    tableView.getItems().add(t);
                }
            }
            });
            filtroAsignaturas.getChildren().add(c);
        }
    }
    
    public ObservableList<Asignatura> getAsignaturaFilter(){
        return filtroAsignaturasList;
    }
    
    @FXML
    private void configuracion(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/ipc/main/configuracion/FXMLConfiguracion.fxml"));
        Parent root = miLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Configuración");
        stage.getIcons().add(new Image("ipc/resources/config-icon.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLConfiguracionController controller = miLoader.getController();
        stage.setResizable(false);
        stage.showAndWait();
    }
    
    @FXML
    private void ayuda(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader miLoader = new FXMLLoader(getClass().getResource("/ipc/main/ayuda/FXMLAyuda.fxml"));
        Parent root = miLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Ayuda");
        stage.getIcons().add(new Image("ipc/resources/icon-help.png"));
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLAyudaController controller = miLoader.getController();
        stage.setResizable(false);
        stage.showAndWait();
    }
    
    public void updateTutorias() {
        
    }
    
    public Button getSelectedButton(){
        return selected;
    }
    
    public void viewTutoria(Tutoria tutoria) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ipc/main/viewTutoria/FXMLVerTutoria.fxml"));
        FXMLVerTutoriaController controller = new FXMLVerTutoriaController();
        loader.setController(controller);
        controller.setTutoria(tutoria);
        
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Ver Tutoría");
        
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
    }
    
    public Calendar getCalendar(){
        return c;
    }
    public Week getWeek(){
        return week;
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
class TutoriaDuracionCelda extends TableCell<Tutoria,Duration> {
    
    @Override
    protected void updateItem(Duration item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {
            long h = item.toHours();
            long m = item.toMinutes()%60;
            setText(h+" h "+m+" m");
        }
    }
}

class TutoriaEstadoCelda extends TableCell<Tutoria,Tutoria.EstadoTutoria> {
    
    @Override
    protected void updateItem(Tutoria.EstadoTutoria item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            Color c = new Color(1,1,1,1);
            if(item.value().equals(Tutoria.EstadoTutoria.PEDIDA.value())){
                c = new Color(0.4706, 0.9686, 0.3686,1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.ANULADA.value())){
                c = new Color(1, 0, 0, 1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.REALIZADA.value())){
                c = new Color(0.2784, 0.8549, 1,1);
            }else if(item.value().equals(Tutoria.EstadoTutoria.NO_ASISTIDA.value())){
                c = new Color(1, 0.7686, 0,1);
            }
            
            Circle state = new Circle(10,c);
            
            Text t = new Text(item.value());
            HBox h = new HBox(state,t);
            h.setSpacing(5);
            setGraphic(h);
        }
    }
}