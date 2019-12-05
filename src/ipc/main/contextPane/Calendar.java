/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc.main.contextPane;

import com.sun.javafx.scene.control.skin.DatePickerSkin;
import java.time.LocalDate;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 *
 * @author FMR
 */
public class Calendar extends Pane{
    
    public Calendar() {
        
        DatePicker calendar = new DatePicker();
        calendar.setValue(LocalDate.now());
        final Callback<DatePicker,DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item.isAfter(LocalDate.now().minusWeeks(1)) && item.isBefore(LocalDate.now())) {
                            setStyle("-fx-background-color: green;");
                            setTooltip(new Tooltip("Vas sobrao"));
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
        
        getChildren().add(nodo);
        calendar.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        
    }
}
