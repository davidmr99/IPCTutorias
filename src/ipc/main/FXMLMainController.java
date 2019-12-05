/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc.main;

import añadir.FXMLAlumnoController;
import añadir.FXMLAsignaturaController;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Random;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
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
    private Button mesesBtn;
    @FXML
    private Button semanasBtn;
    @FXML
    private Button diasBtn;

    private String styleSheet = "styles/Main.css";
    @FXML
    private AnchorPane contextPane;
    @FXML
    private Button configBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button viewtutsBtn;
    @FXML
    private Text debug;
    @FXML
    private Text debug1;
//    @FXML
//    private AnchorPane scrollAnchorPane;
//    @FXML
//    private BorderPane filterPane;
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
        
        DatePicker calendar = new DatePicker();
        calendar.setValue(LocalDate.now());
        
        String[] s = {"CSD","ETC","IIP","EDA","TAL"};
        String[] color = {"orange","blue","lime","grey","magenta"};
        
        final Callback<DatePicker,DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
                        setMaxSize(getMaxWidth(), getMaxHeight());
                        if(item.isAfter(LocalDate.now().minusWeeks(1)) && item.isBefore(LocalDate.now())) {
                            setStyle("-fx-background-color: green;");
                            VBox vBox = new VBox();
                            vBox.setSpacing(5);
                            vBox.setFillWidth(true);
                            
                            Random r = new Random();
                            int n = r.nextInt(s.length);
                                System.out.println("random: " + n);
                            for(int i = 0; i <= n; i++) {
                                Button b = new Button(s[i]);
                                b.setPrefSize(100, USE_PREF_SIZE);
                                //b.setPrefSize(USE_PREF_SIZE,USE_PREF_SIZE);
                                b.setStyle("-fx-background-color: " + color[i] + "; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-text-fill: black; -fx-font-weight: bold");
                                vBox.getChildren().add(b);
                            }
                            
                            //setGraphic(vBox);
                            Tooltip t = new Tooltip("Vas sobrao");
                            t.setGraphic(vBox);
                            setTooltip(t);
                        }else if(item.isAfter(LocalDate.now().minusWeeks(2)) && item.isBefore(LocalDate.now().minusWeeks(1))) {
                            setStyle("-fx-background-color: orange; -fx-color: white; -fx-border-radius: 5px; -fx-background-radius: 10px;");
                            setTooltip(new Tooltip("Corrrre"));
                        }else if(item.isAfter(LocalDate.now().minusWeeks(3).minusDays(3)) && item.isBefore(LocalDate.now().minusWeeks(2))) {
                            setStyle("-fx-background-color: red; -fx-border-radius: 10px; -fx-background-radius: 10px;");
                            setTooltip(new Tooltip("Ya llegas un poco tarde no?"));
                        }
                    }
                };
            }
        };

        calendar.setDayCellFactory(dayCellFactory);

        DatePickerSkin dateSkin = new DatePickerSkin(calendar);
        Node nodo = dateSkin.getPopupContent();
        nodo.setStyle(".popup{-fx-pref-height:500;-fx-background-color:yellow;-fx-border-radius:20px;}");

        calendar.setPrefSize(PopupControl.USE_PREF_SIZE, PopupControl.USE_PREF_SIZE);
        nodo.prefHeight(contextPane.getHeight());

        contextPane.getChildren().add(nodo);
        contextPane.setTopAnchor(nodo, 0d);
        contextPane.setLeftAnchor(nodo, 0d);
        //contextPane.setBottomAnchor(nodo, 0d);
        contextPane.setRightAnchor(nodo, 0d);
        
    }

    @FXML
    private void swapCalendarView(ActionEvent e) {
        if(e.getSource().equals(mesesBtn)) {
            semanasBtn.setDisable(false);
            diasBtn.setDisable(false);
            mesesBtn.setDisable(true);
        }else if(e.getSource().equals(semanasBtn)) {
            mesesBtn.setDisable(false);
            diasBtn.setDisable(false);
            semanasBtn.setDisable(true);
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
