/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipc.main.weekView;

import static java.lang.Integer.MAX_VALUE;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

/**
 *
 * @author FMR
 */
public class Week extends GridPane{
    
    private int days,hours,division;
    private int selectedDay = -1, startingHour = -1;
    private int lastRowIndex;
    private Button tutoriaBtn;
    private Tooltip t = new Tooltip("Click para añadir tutoría");
    private Pane endOne;
    
    public Week(int days, int hours, int division){
        this.days = days;
        this.hours = hours;
        this.division = division;
        
        for(int i=0;i< hours * (60/division);i++) {
            RowConstraints row = new RowConstraints(20);
            row.setValignment(VPos.TOP);
            getRowConstraints().add(row);
        }
        for(int i=0;i< days;i++) {
            ColumnConstraints column = new ColumnConstraints(100);
            column.setFillWidth(true);
            column.setPercentWidth(((Double)(100/(double)days)));
            getColumnConstraints().add(column);
        }
        //setGridLinesVisible(true);
    }
    
    public void init(){
        
        for(int i=0;i< days;i++) {
            for(int j=0;j< hours * (60/division);j++) {
                addPane(i,j);
            }
        }
        tutoriaBtn = new Button();
        tutoriaBtn.setPrefSize(MAX_VALUE, MAX_VALUE);
        tutoriaBtn.setMinSize(((Pane)getChildren().get(0)).getWidth(), ((Pane)getChildren().get(0)).getHeight());
        tutoriaBtn.getStyleClass().add("tutoriaBtn");
        add(tutoriaBtn, 4, 30,1,1);
        setFillWidth(tutoriaBtn, true);
        setFillHeight(tutoriaBtn, true);
    }
    
    private void addPane(int i, int j) {
        Pane pane = new Pane(new Text(i+","+j));
        pane.getStylesheets().add("styles/Main.css");
        pane.setId("weekCell");
        
        if(j % 3 == 0){
            pane.getStyleClass().add("minMark");
        }
        if(i % 2 == 0){
            pane.getStyleClass().add("par");
        }else {
            pane.getStyleClass().add("impar");
        }
        
        
        pane.setOnDragDetected(evt -> {
            Pane src = (Pane) evt.getSource();
            System.out.println("DRAGDETECTED");
            System.out.println("columns.rows :"+getColumnIndex(src)+" , "+getRowIndex(src));
            if(getChildren().contains(tutoriaBtn)){
                getChildren().remove(tutoriaBtn);
            }
            tutoriaBtn = new Button();
            tutoriaBtn.getStyleClass().add("tutoriaBtn");
            
            for(Node node: getChildren()){
                if(getColumnIndex(node) == i && getRowIndex(node) == j){
                    System.out.println(i+" ...,,.. "+j);
                    node.setStyle("-fx-background-color: peru;");
                    node.startFullDrag();
                    lastRowIndex = j;
                }else{
                    node.setStyle("");
                }
            }
        });
        
        pane.setOnMouseDragOver(evt -> {
            Pane source = (Pane) evt.getSource();
            Pane eventSource = (Pane) evt.getGestureSource();
            System.out.println("over " + ((Text)((Pane)source).getChildren().get(0)).getText());
            
            if( j > getRowIndex(eventSource)){
                for(Node node: getChildren()){
                    if(!(node instanceof Button)){
                        if(getColumnIndex(node) == getColumnIndex(eventSource) && getRowIndex(node) >= getRowIndex(eventSource) && getRowIndex(node) <= j){
                            if(j - getRowIndex(eventSource) < 8){
                                System.out.println("SI! yo: "+i+", "+j+"    eSource: "+getColumnIndex(eventSource)+", "+getRowIndex(eventSource));
                                if(node != eventSource){
                                    node.setStyle("-fx-background-color: cyan;");
                                }
                                lastRowIndex = j;
                            }else if(getRowIndex(node)- getRowIndex(eventSource) >= 8) {
                                node.setStyle("-fx-background-color: red;");
                            }
                        }else{
                            node.setStyle("");
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
                            node.setStyle("");
                        }}
                }
            }else if (getRowIndex(source) <= getRowIndex(eventSource)){
                for(Node node: getChildren()){
                    if(!(node instanceof Button)){ node.setStyle("");}}
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
            add(tutoriaBtn, getColumnIndex(eventSource), getRowIndex(eventSource), 1, lastRowIndex - getRowIndex(eventSource) + 1);
            tutoriaBtn.setPrefSize(MAX_VALUE, MAX_VALUE);
            tutoriaBtn.setMinSize(((Pane)getChildren().get(0)).getWidth(), ((Pane)getChildren().get(0)).getHeight());
            t.hide();
            Tooltip.install(endOne, t);
            Bounds bounds = endOne.getBoundsInLocal();
            Bounds screenBounds = endOne.localToScreen(bounds);
            
            t.show(endOne.getScene().getWindow(),screenBounds.getMaxX(),screenBounds.getMaxY());
            
            MyTask mt = new MyTask();
           
            Thread th = new Thread(mt);
            th.setDaemon(true);
            th.start();
                    
            for(Node node:getChildren()){
                node.setStyle("");
            }
        });
        
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
}
