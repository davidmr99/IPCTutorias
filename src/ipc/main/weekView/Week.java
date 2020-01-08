/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipc.main.weekView;

import accesoBD.AccesoBD;
import ipc.main.configuracion.FXMLConfiguracionController;
import ipc.Main;
import java.io.IOException;
import static java.lang.Integer.MAX_VALUE;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import modelo.Asignatura;
import modelo.Tutoria;
import myLibrary.TutoriaBtn;

/**
 *
 * @author FMR
 */
public class Week extends GridPane{
    
    private int days,hours = 12 ,division= 10;
    private int selectedDay = -1, startingHour = -1;
    private int lastRowIndex;
    private Button tutoriaBtn;
    private Tooltip t = new Tooltip("Click para añadir tutoría");
    private Pane endOne;
    private LocalDate[] daysArray;
    private int year, week;
    private boolean fromMainWindow;
    private ObservableList<Tutoria> tutorias;
    
    public Week(int days,boolean fromMainWindow){
        this.days = days + 1;
        this.fromMainWindow = fromMainWindow;
        
        getStylesheets().add("/styles/Main.css");
        
        for(int i=0;i< hours * (60/division);i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.TOP);
            getRowConstraints().add(row);
        }
        for(int i=0;i< this.days;i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setFillWidth(true);
            column.setHgrow(Priority.ALWAYS);
            if(fromMainWindow){
                column.setPercentWidth(((Double)(100/(double)this.days)));
            }else{
                if(i==0){
                    column.setPercentWidth(20d);
                }else{
                    column.setPercentWidth(80d);
                }
            }
            getColumnConstraints().add(column);
        }
        //setGridLinesVisible(true);
    }
    public void setWeekDays(LocalDate day){
        daysArray = new LocalDate[7];
        year = day.getYear();
        week = (day.getDayOfYear()/7)+1;
        int firstDayOfWeek = ((week - 0)*7+1) + (1 - (LocalDate.of(year,1, 1)).getDayOfWeek().getValue());
        if(firstDayOfWeek<=0){
            year--;
            int n;
            if(Year.isLeap(year)){
                n = 366;
            }else {
                n = 365;
            }
            firstDayOfWeek = n + firstDayOfWeek;
        }
        for(int i=0;i<7;i++){
            daysArray[i] = LocalDate.ofYearDay(year, firstDayOfWeek).plusDays(i);
        }
        System.out.println("Week "+week +": "+daysArray[0]+" - "+ daysArray[6]);
        drawTutorias();
    }
    
    public void setDay(LocalDate day){
        year = day.getYear();
        week = (day.getDayOfYear()/7)+1;
        daysArray = new LocalDate[1];
        daysArray[0] = day;
        drawTutorias();
    }
    
    public void init(){
        int h = 8;
        int m = 0;
        for(int i=0;i< days;i++) {
            for(int j=0;j< hours * (60/division);j++) {
                if(i!=0){
                    addPane(i,j);
                }else {
                    if(j % 3 == 0){
                        if(j>0){
                            if(j % 6 == 0){
                                h++;
                            }
                            if(m==30){
                                m=0;
                            }else {
                                m = 30;
                            }
                        }
                        Label l = new Label(h+":"+m);
                        l.setMaxWidth(MAX_VALUE);
                        l.setAlignment(Pos.CENTER);
                        add(l ,i, j);
                        
                    }
                }
            }
        }
    }
    
    public void initButton(){
        if(getChildren().contains(tutoriaBtn)){
            getChildren().remove(tutoriaBtn);
        }
        tutoriaBtn = new Button();
        tutoriaBtn.getStyleClass().add("tutoriaBtn");
    }
    
    private void initDrag(MouseEvent evt){
        Pane src = (Pane) evt.getSource();
        System.out.println("columns.rows :"+getColumnIndex(src)+" , "+getRowIndex(src));
        initButton();
    }
    
    public void drawButtonAndStuff(int evtSrcRow,int srcRow){
        Pane evtSrc = null;
        Pane src = null;
        System.out.println("indexes: " + evtSrcRow+"    "+srcRow);
        for(Node n:getChildren()){
            if(n instanceof Pane){    
                if(getColumnIndex(n) == 1){
                    if(getRowIndex(n) == evtSrcRow){
                        evtSrc = (Pane)n;
                    }
                    if(getRowIndex(n) == srcRow){
                        src = (Pane)n;
                    }
                }
            }
        }
        lastRowIndex = srcRow;
        endOne = src;
        drawButtonAndStuff(evtSrc,src);
    }
    
    private void drawButtonAndStuff(Pane eventSource,Pane source){
        add(tutoriaBtn, getColumnIndex(eventSource), getRowIndex(eventSource), 1, lastRowIndex - getRowIndex(eventSource) + 1);
        tutoriaBtn.setPrefSize(tutoriaBtn.getParent().getBoundsInLocal().getWidth(), tutoriaBtn.getParent().getBoundsInLocal().getHeight());
        tutoriaBtn.setMinSize(((Control)getChildren().get(0)).getWidth(), ((Control)getChildren().get(0)).getHeight());
        LocalDateTime inicio = getTime(getColumnIndex(eventSource),getRowIndex(eventSource));
        LocalDateTime fin = getTime(getColumnIndex(source),lastRowIndex+1);
        Text text = new Text(inicio.getHour() + ":" + inicio.getMinute() + " - " + fin.getHour() + ":" + fin.getMinute());
        tutoriaBtn.setGraphic(text);
        tutoriaBtn.prefWidthProperty().bind(((Pane)tutoriaBtn.getParent()).widthProperty());
        tutoriaBtn.prefHeightProperty().bind(((Pane)tutoriaBtn.getParent()).heightProperty());
        tutoriaBtn.setOnMouseClicked((event) -> {
            
            if(fromMainWindow){
                Main.getMainController().launchTutFromWeekPane(inicio,fin,getRowIndex(eventSource),lastRowIndex);
                getChildren().remove(tutoriaBtn);
                Main.getMainController().getAddTutoriaBtn().fire();
            }
        });
        
        t.hide();
        Bounds bounds = endOne.getBoundsInLocal();
        Bounds screenBounds = endOne.localToScreen(bounds);
        if(fromMainWindow){
            t.show(endOne.getScene().getWindow(),screenBounds.getMaxX(),screenBounds.getMaxY());
        }
        
        MyTask mt = new MyTask();
        
        Thread th = new Thread(mt);
        th.setDaemon(true);
        th.start();
        for(Node node:getChildren()){
            if(getColumnIndex(node) != 6 && getColumnIndex(node) != 7){
                node.setStyle("");
            }
        }
    }
    
    private void addPane(int i, int j) {
        Pane pane = new Pane();
        pane.getStylesheets().add("styles/Main.css");
        pane.setId("weekCell");
        pane.setMaxWidth(MAX_VALUE);

        if(j % 3 == 0){
            pane.getStyleClass().add("minMark");
        }
        if(i % 2 == 0){
            pane.getStyleClass().add("par");
        }else {
            pane.getStyleClass().add("impar");
        }
        if(i == 6 || i == 7){
            pane.setDisable(true);
            pane.setStyle("-fx-background-color: gainsboro;");
        }else{

        pane.setOnDragDetected(evt -> {

            initDrag(evt);
            for(Node node: getChildren()){
                if(getColumnIndex(node) == i && getRowIndex(node) == j){
                    System.out.println(i+" ...,,.. "+j);
                    node.setStyle("-fx-background-color: peru;");
                    node.startFullDrag();
                    lastRowIndex = j;
                }else{
                    if(getColumnIndex(node) != 6 && getColumnIndex(node) != 7){
                        node.setStyle("");
                    }
                }
            }
        });

        pane.setOnMouseDragOver(evt -> {
            Pane source = (Pane) evt.getSource();
            Pane eventSource = (Pane) evt.getGestureSource();

            if( j > getRowIndex(eventSource)){
                for(Node node: getChildren()){
                    if(getColumnIndex(node) == i && getRowIndex(node) == j){
                        System.out.println("Over: "+i+", "+j);
                    }
                    if(!(node instanceof Button)){
                        if(getColumnIndex(node) == getColumnIndex(eventSource) && getRowIndex(node) >= getRowIndex(eventSource) && getRowIndex(node) <= j){
                            if(j - getRowIndex(eventSource) < FXMLConfiguracionController.getMaxDurTut()){
                                System.out.println("SI! yo: "+i+", "+j+"    eSource: "+getColumnIndex(eventSource)+", "+getRowIndex(eventSource));
                                if(node != eventSource){
                                    node.setStyle("-fx-background-color: cyan;");
                                }
                                lastRowIndex = j;
                            }else if(getRowIndex(node)- getRowIndex(eventSource) >= FXMLConfiguracionController.getMaxDurTut()) {
                                node.setStyle("-fx-background-color: red;");
                            }
                        }else{
                            if(getColumnIndex(node) != 6 && getColumnIndex(node) != 7){
                                node.setStyle("");
                            }
                        }
                    }
                }
            }

    //FUNCIONA
    //            for(Node node: getChildren()){
    //                if(node == source){
    //                    if(i == getColumnIndex(eventSource) && j > getRowIndex(eventSource)){
    //                        System.out.println("SI! yo: "+i+", "+j+"    eSource: "+getColumnIndex(eventSource)+", "+getRowIndex(eventSource));
    //                        source.setStyle("-fx-background-color: cyan;");
    //                        list.add(((Pane) evt.getSource()));
    //                    }
    //                }else{
    //                    node.setStyle("");
    //                }
    //            }
        });

        pane.setOnMouseClicked((event) -> {
            initDrag(event);
            Object source = event.getSource();
            Tooltip.uninstall(endOne, t);
            endOne = (Pane)source;
            lastRowIndex = getRowIndex(endOne);
            drawButtonAndStuff(endOne,endOne);
            if(!fromMainWindow){
                Main.getMainController().getTutoriaController().updateLabelsFromOutside(lastRowIndex,lastRowIndex);
            }
        });


        pane.setOnMouseDragReleased(evt -> {
            Pane source = (Pane) evt.getSource();
            Pane eventSource = (Pane) evt.getGestureSource();
            Tooltip.uninstall(endOne, t);
            endOne = eventSource;
            if (getRowIndex(source) > lastRowIndex) {
                for(Node node: getChildren()){
                    if(!(node instanceof Button)){
                        if(getColumnIndex(node) == getColumnIndex(eventSource) && getRowIndex(node) >= getRowIndex(eventSource) && getRowIndex(node) <= j){
                            if(getColumnIndex(node) == getColumnIndex(eventSource) && getRowIndex(node) == lastRowIndex){
                                node.setStyle("-fx-background-color: gray;");
                                endOne = (Pane)node;
                            }
                        }else {
                            if(getColumnIndex(node) != 6 && getColumnIndex(node) != 7){
                                node.setStyle("");
                            }
                        }
                    }
                }
            }else if (getRowIndex(source) <= getRowIndex(eventSource)){
                for(Node node: getChildren()){
                    if(!(node instanceof Button)){
                        if(getColumnIndex(node) != 6 && getColumnIndex(node) != 7){
                            node.setStyle("");
                        }
                    }
                }
                eventSource.setStyle("-fx-background-color: gray;");
                endOne = (Pane)eventSource;
            }else {
                for(Node node: getChildren()){
                    if(!(node instanceof Button)){
                        if(getRowIndex(node)==getRowIndex(source) && getColumnIndex(node) == getColumnIndex(eventSource)){
                            source.setStyle("-fx-background-color: gray;");
                            endOne = (Pane)source;
                        }}
                }
            }
            drawButtonAndStuff(eventSource,endOne);
            if(!fromMainWindow){
                Main.getMainController().getTutoriaController().updateLabelsFromOutside(getRowIndex(eventSource), getRowIndex(endOne));
            }
        });
        }
        add(pane, i, j);
    }
    class MyTask extends Task<Void>{
        
        @Override
        protected Void call() throws Exception {
            
            Thread.sleep(3500);
            if(isCancelled())return null;
            //updateMessage(LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
            Platform.runLater(()->{
                t.hide();
            });
            
            return null;
        }
    }
    
    public LocalDateTime getTime(int i, int j){
        LocalDate chosen = daysArray[i-1];
        int h = 8 + j / 6;
        int m = (j % 6) * 10;
        LocalDateTime date = LocalDateTime.of(chosen.getYear(), chosen.getMonth(), chosen.getDayOfMonth(), h, m);
        return date;
    }
    
    public static LocalDateTime getTime(int j,boolean isFinal){
        LocalDate chosen = LocalDate.of(2000, 1, 1);
        int n=0;
        if(isFinal){
            n=1;
        }
        int h = 8 + (j+n) / 6;
        int m = ((j+n) % 6) * 10;
        LocalDateTime date = LocalDateTime.of(chosen.getYear(), chosen.getMonth(), chosen.getDayOfMonth(), h, m);
        return date;
    }
    
    public static int getIndex(LocalDateTime ldt,boolean isFinal){
        int n=0;
        if(isFinal){
            n=-1;
        }
        return (ldt.getHour()-8) * 6 + (ldt.getMinute()/10) + n;
    }
    
    public static int getIndex(LocalTime ldt,boolean isFinal){
        int n=0;
        if(isFinal){
            n=-1;
        }
        return (ldt.getHour()-8) * 6 + (ldt.getMinute()/10) + n;
    }
    
    public LocalDate[] getDays(){
        return daysArray;
    }
    
    //PONER SIEMPRE DESPUES DEL SETWEEKDAYS O SETDAY
    public void drawTutorias(){
        tutorias = FXCollections.observableArrayList();
        
        for(LocalDate ld : daysArray){
            for(Tutoria tut : AccesoBD.getInstance().getTutorias().getTutoriasConcertadas()){
                if(tut.getFecha().getYear() == ld.getYear() && tut.getFecha().getMonthValue() == ld.getMonthValue() && tut.getFecha().getDayOfMonth() == ld.getDayOfMonth()){
                        tutorias.add(tut);
                }
            }
        }
        
        TutoriaBtn[] tuts = new TutoriaBtn[tutorias.size()];
        Tutoria.EstadoTutoria[] states = Tutoria.EstadoTutoria.values();
        
        Color[] col = {new Color(0.4706, 0.9686, 0.3686,1),new Color(1, 0, 0, 1),new Color(0.2784, 0.8549, 1,1),new Color(1, 0.7686, 0,1)};
        if(daysArray.length == 1){
            for(int i=0;i<tuts.length;i++){
                
//                System.out.println("filtross");
                    boolean add = false;
                    for(Asignatura a:Main.getMainController().getAsignaturaFilter()){
//                        System.out.println("tuto: "+tutorias.get(i).getAsignatura().getCodigo()+ " filtro? "+a.getCodigo());
                        if(a.getCodigo().equals(tutorias.get(i).getAsignatura().getCodigo())){
                            add = true;
                            break;
                        }
                    }
                    if(add || Main.getMainController().getAllSubjectsFilterButton().isSelected()){
                
                tuts[i] = new TutoriaBtn(tutorias.get(i));
                tuts[i].getStyleClass().add("tutoriaBtn");

                LocalTime time0 = tutorias.get(i).getInicio();
                LocalTime timef = time0.plusMinutes(tutorias.get(i).getDuracion().toMinutes());

                int hInicio = Week.getIndex(time0, false);
                int hFin = Week.getIndex(timef, false);

                add(tuts[i],1,hInicio,1,hFin-hInicio);

                tuts[i].setPrefSize(tuts[i].getParent().getBoundsInLocal().getWidth(), tuts[i].getParent().getBoundsInLocal().getHeight());
                tuts[i].setMinSize(((Control)getChildren().get(i)).getWidth(), ((Control)getChildren().get(i)).getHeight());


                Text text = new Text(time0.getHour() + ":" + time0.getMinute() + " - " + timef.getHour() + ":" + timef.getMinute());
                //tuts[i].setGraphic(text);
                tuts[i].prefWidthProperty().bind(((Pane)tuts[i].getParent()).widthProperty());
                tuts[i].prefHeightProperty().bind(((Pane)tuts[i].getParent()).heightProperty());
                
                Color color = new Color(1,1,1,1);
                
                for(int j=0;j<states.length;j++){
                    if(states[j].equals(tutorias.get(i).getEstado())){
                        color = col[j];
                    }
                }
                
                tuts[i].setAlignment(Pos.BOTTOM_LEFT);
                
                HBox hb = new HBox(new Circle(10,color),new Text(tutorias.get(i).getAsignatura().getCodigo()),text);
                hb.setSpacing(10);
                tuts[i].setGraphic(hb);
                
                tuts[i].setOnMouseClicked((MouseEvent t1) -> {
                    try {
                        Main.getMainController().viewTutoria(((TutoriaBtn) t1.getSource()).getTutoria());
                    }catch (IOException ex) {
                        Logger.getLogger(Week.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                    }else{
                        System.out.println("aplicando filtro a "+tutorias.get(i).getAsignatura().getCodigo());
                    }
            }
            
        }else{
            int k = 1;
            for(LocalDate ld : daysArray){
                for(int i = 0; i<tuts.length;i++){
//                    System.out.println("filtross");
                    boolean add = false;
                    for(Asignatura a:Main.getMainController().getAsignaturaFilter()){
//                        System.out.println("tuto: "+tutorias.get(i).getAsignatura().getCodigo()+ " filtro? "+a.getCodigo());
                        if(a.getCodigo().equals(tutorias.get(i).getAsignatura().getCodigo())){
                            add = true;
                            break;
                        }
                    }
                    if(add || Main.getMainController().getAllSubjectsFilterButton().isSelected()){
                    if(tutorias.get(i).getFecha().getYear() == ld.getYear() && tutorias.get(i).getFecha().getMonthValue()== ld.getMonthValue() && tutorias.get(i).getFecha().getDayOfMonth()== ld.getDayOfMonth()){
                        tuts[i] = new TutoriaBtn(tutorias.get(i));
                        tuts[i].getStyleClass().add("tutoriaBtn");

                        LocalTime time0 = tutorias.get(i).getInicio();
                        LocalTime timef = time0.plusMinutes(tutorias.get(i).getDuracion().toMinutes());

                        int hInicio = Week.getIndex(time0, false);
                        int hFin = Week.getIndex(timef, false);

                        add(tuts[i],k,hInicio,1,hFin-hInicio);
                        
                        tuts[i].setPrefSize(tuts[i].getParent().getBoundsInLocal().getWidth(), tuts[i].getParent().getBoundsInLocal().getHeight());
                        tuts[i].setMinSize(((Control)getChildren().get(i)).getWidth(), ((Control)getChildren().get(i)).getHeight());


                        Text text = new Text(time0.getHour() + ":" + time0.getMinute() + " - " + timef.getHour() + ":" + timef.getMinute());
                        //tuts[i].setGraphic(text);
                        tuts[i].prefWidthProperty().bind(((Pane)tuts[i].getParent()).widthProperty());
                        tuts[i].prefHeightProperty().bind(((Pane)tuts[i].getParent()).heightProperty());

                        Color color = new Color(1,1,1,1);

                        for(int j=0;j<states.length;j++){
                            if(states[j].equals(tutorias.get(i).getEstado())){
                                color = col[j];
                            }
                        }

                        tuts[i].setAlignment(Pos.BOTTOM_LEFT);

                        HBox hb = new HBox(new Circle(10,color),new Text(tutorias.get(i).getAsignatura().getCodigo()),text);
                        
                        hb.setSpacing(10);
                        tuts[i].setGraphic(hb);
                        
                        tuts[i].setOnMouseClicked((MouseEvent t1) -> {
                            try {
                                Main.getMainController().viewTutoria(((TutoriaBtn) t1.getSource()).getTutoria());
                            }catch (IOException ex) {
                                Logger.getLogger(Week.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                    }else{
                        System.out.println("aplicando filtro a "+tutorias.get(i).getAsignatura().getCodigo());
                    }
                }
                k++;
            }
        }
    }
}
