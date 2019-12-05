/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipc.main.contextPane;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import ipc.Main;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Random;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 *
 * @author FMR
 */
public class Calendar{
    
    private Node nodo;
    
    public Calendar() {
        String[] s = {"CSD","ETC","IIP","EDA","TAL"};
        String[] color = {"orange","blue","lime","grey","magenta"};
        DatePicker calendar = new DatePicker();
        calendar.setShowWeekNumbers(true);
        calendar.setValue(LocalDate.now());
        final Callback<DatePicker,DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell(){
                    
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setMinSize(100,100);
                        setPrefSize(USE_COMPUTED_SIZE, 6852313);
                        setupCustomTooltipBehavior(0, Integer.MAX_VALUE, 0);
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
                        afterDrawStuff();
                    }
                };
            }
        };
        
        calendar.setDayCellFactory(dayCellFactory);
        
        
        DatePickerSkin dateSkin = new DatePickerSkin(calendar);
        nodo = dateSkin.getPopupContent();
        
        afterDrawStuff();
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
                    Main.getMainController().semanasBtn.fire();
                });
                tempDateCell.setTooltip(new Tooltip("Ver semana " + tempDateCell.getText()));
            }
        }
    }
    
    public Node getCalendar(){
        return nodo;
    }
}
