/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc.main;

import añadir.FXMLAlumnoController;
import añadir.FXMLAsignaturaController;
import ipc.main.contextPane.Calendar;
import ipc.main.weekView.Week;
import java.io.IOException;
import static java.lang.Integer.MAX_VALUE;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private ComboBox<String> comboBox;
    @FXML
    private MenuItem addTutoria;
    @FXML
    private MenuItem addAlumno;
    @FXML
    private MenuItem addAsignatura;
    
    private Node calendarNode;
    private Week week;
    private ScrollPane sp;

    public FXMLMainController(){
        
    }

    private void handleButtonAction(ActionEvent e) {
    }

    public ScrollPane getScrollPane(){
        return scrollSubjects;
    }
    
    public VBox getvBox(){
        return filtroAsignaturas;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SwitchButton btn = new SwitchButton();
        btn.setVisible(true);
      
        menu0.getChildren().add(0,btn);

        for(int i=0;i<10;i++) {
            comboBox.getItems().addAll(Integer.toString(i)+"nyas coca");
            filtroAsignaturas.getChildren().add(new CheckBox(Integer.toString(i)+"molerioso"));
        }
        
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
        Calendar c = new Calendar();
        calendarNode = c.getCalendar();
        calendarNode.setStyle(".popup{-fx-background-color:yellow;-fx-border-radius:20px;-fx-fit-to-height:true;}");
        
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
            mesesBtn.setDisable(false);
            diasBtn.setDisable(false);
            semanasBtn.setDisable(true);
            
            contextPane.getChildren().remove(calendarNode);
            
            week = new Week(8, 12, 10);
            week.init();
            
            GridPane days = new GridPane();
            days.setGridLinesVisible(true);
            String[] dias = {" ","L","M","X","J","V","S","D"};
            for(int i=0;i<=7;i++){
                ColumnConstraints column = new ColumnConstraints(100);
                column.setFillWidth(true);
                column.setPercentWidth(((Double)(100/(double)8)));
                days.getColumnConstraints().add(column);
            }
            for(int i=0;i<=7;i++){
                Button l = new Button(dias[i]);
                l.setMaxSize(MAX_VALUE, MAX_VALUE);
                days.add(l,i,0);
            }
            
            sp = new ScrollPane(week);
            sp.setId("scrollWeek");
            sp.setVvalue(sp.getVmax()/2);
            
            VBox vb = new VBox();
            vb.getChildren().add(days);
            vb.getChildren().add(sp);
            
            
            
            //AÑADIR BARRA LATERAL CON LAS HORAS Y UN ARRIBA PARA LOS DIAS
            
            
            
            
            AnchorPane.setTopAnchor(vb, 0d);
            AnchorPane.setLeftAnchor(vb, 0d);
            AnchorPane.setBottomAnchor(vb, 0d);
            AnchorPane.setRightAnchor(vb, 0d);
            contextPane.getChildren().add(vb);
        }else if(e.getSource().equals(diasBtn)) {
            mesesBtn.setDisable(false);
            semanasBtn.setDisable(false);
            diasBtn.setDisable(true);
        }
    }

    @FXML
    private void addTutoria(ActionEvent event) {
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
}
