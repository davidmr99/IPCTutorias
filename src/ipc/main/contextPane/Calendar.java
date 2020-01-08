/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipc.main.contextPane;

import accesoBD.AccesoBD;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import ipc.Main;
import ipc.main.FXMLMainController;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import javafx.util.Duration;
import modelo.Asignatura;
import modelo.Tutoria;

/**
 *
 * @author FMR
 */
public class Calendar{
    
    private Node nodo;
    private LocalDate[] dates;
    private DatePicker calendar;
    
    public Calendar(boolean fromMainWindow) {
        String[] s = {"CSD","ETC","IIP","EDA","TAL"};
        String[] color = {"orange","blue","lime","grey","magenta"};
        calendar = new DatePicker();
        calendar.setShowWeekNumbers(fromMainWindow);
        calendar.setValue(LocalDate.now());
        final Callback<DatePicker,DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell(){
                    
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if(fromMainWindow){
                            setMinSize(100,100);
                        }else{
                            setMinSize(50,50);
                            setPrefSize(USE_COMPUTED_SIZE, 6852313);
                        }
                        
                        setupCustomTooltipBehavior(0, Integer.MAX_VALUE, 0);
                        
                        setOnMouseClicked((event) -> {
                            calendar.setValue(item);
                            if(fromMainWindow){
                                Main.getMainController().diasBtn.fire();
                            }else{
                                Main.getMainController().getTutoriaController().dayChanged();
                            }
                        });
                        if(!fromMainWindow){
                            if(item.isBefore(LocalDate.now())){
                                setDisable(true);
                            }
                        }
                        DayOfWeek day = DayOfWeek.from(item);
                        if(day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
                            setDisable(true);
                           setStyle("-fx-background-color: gainsboro;");
                        }
                        if(item.isBefore(LocalDate.now())) {
                            setDisable(true);
                        }
                        if(FXMLMainController.getVacaciones().contains(item)){
                            getStyleClass().add("vacaciones");
                            setTooltip(new Tooltip("Vacaciones"));
                        }
                        boolean[] states = new boolean[Tutoria.EstadoTutoria.values().length];
                        for(Tutoria tut:AccesoBD.getInstance().getTutorias().getTutoriasConcertadas()){
                            if(tut.getFecha().equals(item)){
                                for(int i= 0; i<Tutoria.EstadoTutoria.values().length;i++){
                                    if(tut.getEstado().equals(Tutoria.EstadoTutoria.values()[i])){
                                        
//                        System.out.println("filtross calendar");
                    boolean add = false;
                                        System.out.println("-------------------------------UPDATING CALENDAR-------------------------------");
                    for(Asignatura a:Main.getMainController().getAsignaturaFilter()){
//                        System.out.println("tuto: "+tut.getAsignatura().getCodigo()+ " filtro? "+a.getCodigo());
                        if(a.getCodigo().equals(tut.getAsignatura().getCodigo())){
                            System.out.println("No deberia añadir porque no esta en el filtro: "+a.getCodigo()+" pero todas on? "+Main.getMainController().getAllSubjectsFilterButton().isSelected());
                            add = true;
                            break;
                        }
                    }
                    if(add || Main.getMainController().getAllSubjectsFilterButton().isSelected()){
                                        states[i] = true;
                    }
                                    }
                                }
                                //setStyle("-fx-background-color:brown;");
                            }
                        }
                        HBox h = new HBox();
                        Color[] col = {new Color(0.4706, 0.9686, 0.3686,1),new Color(1, 0, 0, 1),new Color(0.2784, 0.8549, 1,1),new Color(1, 0.7686, 0,1)};
                        
                        int spacing,size;
                        
                        if(fromMainWindow){
                            spacing = 5;
                            size = 10;
                        }else{
                            spacing = 2;
                            size = 5;
                        }
                        
                        h.setSpacing(spacing);
                        for(int i=0;i<states.length;i++){
                            if(states[i]){
                                h.getChildren().add(new Circle(size,col[i]));
                            }
                        }
                        setGraphic(h);
                    }
                };
            }
        };
        
        calendar.setDayCellFactory(dayCellFactory);
        
        
        DatePickerSkin dateSkin = new DatePickerSkin(calendar);
        nodo = dateSkin.getPopupContent();
        
        afterDrawStuff();
    }
    
    public LocalDate getDate(){
        return calendar.getValue();
    }
    
    //MWTODO SACADO DE https://coderanch.com/t/622070/java/control-Tooltip-visible-time-duration
    
    private void setupCustomTooltipBehavior(int openDelayInMillis, int visibleDurationInMillis, int closeDelayInMillis) {
        try {
            
            Class TTBehaviourClass = null;
            Class<?>[] declaredClasses = Tooltip.class.getDeclaredClasses();
            for (Class c:declaredClasses) {
                if (c.getCanonicalName().equals("javafx.scene.control.Tooltip.TooltipBehavior")) {
                    TTBehaviourClass = c;
                    break;
                }
            }
            if (TTBehaviourClass == null) {
                // abort
                return;
            }
            Constructor constructor = TTBehaviourClass.getDeclaredConstructor(
                    Duration.class, Duration.class, Duration.class, boolean.class);
            if (constructor == null) {
                // abort
                return;
            }
            constructor.setAccessible(true);
            Object newTTBehaviour = constructor.newInstance(
                    new Duration(openDelayInMillis), new Duration(visibleDurationInMillis),
                    new Duration(closeDelayInMillis), false);
            if (newTTBehaviour == null) {
                // abort
                return;
            }
            Field ttbehaviourField = Tooltip.class.getDeclaredField("BEHAVIOR");
            if (ttbehaviourField == null) {
                // abort
                return;
            }
            ttbehaviourField.setAccessible(true);
            
            // Cache the default behavior if needed.
            Object defaultTTBehavior = ttbehaviourField.get(Tooltip.class);
            ttbehaviourField.set(Tooltip.class, newTTBehaviour);
            
        } catch (Exception e) {
            System.out.println("Aborted setup due to error:" + e.getMessage());
        }
    }
    
    public void afterDrawStuff(){
        if(nodo!=null){
            for (Node node : nodo.lookupAll(".week-number-cell")) {
                DateCell tempDateCell = (DateCell) node;
                tempDateCell.setOnMouseClicked((event) -> {
                    System.out.println("You clicked week: " + tempDateCell.getText());
                    calendar.setValue(getDayFromWeek(Integer.parseInt(tempDateCell.getText()),calendar.getValue().getYear()));
                    Main.getMainController().semanasBtn.fire();
                });
                tempDateCell.setTooltip(new Tooltip("Ver semana " + tempDateCell.getText()));
            }
        }
    }
    
    public Node getCalendar(){
        return nodo;
    }
    
    public DatePicker getDatePicker(){
        return calendar;
    }
    
    public LocalDate getDayFromWeek(int week, int year){
        
        System.out.println("week: "+week + "  year: "+year);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.clear();
        cal.set(java.util.Calendar.WEEK_OF_YEAR, week);
        cal.set(java.util.Calendar.YEAR, year);
        
        int firstDayOfWeek = ((week - 1)*7+1) + (1 - (LocalDate.of(year,1, 1)).getDayOfWeek().getValue());
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
        firstDayOfWeek = LocalDate.ofYearDay(year, firstDayOfWeek).getDayOfMonth();
        int month = cal.get(java.util.Calendar.MONTH) + 1;
        System.out.println("year; "+year+" month: "+month+" day: "+firstDayOfWeek);
        
        //int month = LocalDate.ofYearDay(year, firstDayOfWeek).getMonthValue();
        return LocalDate.of(year,month , firstDayOfWeek);
    }
}
