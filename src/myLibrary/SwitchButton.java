/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myLibrary;

/**
 *
 * @author FMR
 */
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

public class SwitchButton extends Label
{
    private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);
    private Button switchBtn;
    
    public SwitchButton()
    {
        switchBtn = new Button();
        switchBtn.setPrefWidth(40);
        switchBtn.setId("btn");
        switchBtn.setCursor(javafx.scene.Cursor.HAND);
        switchBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent t)
            {
                switchedOn.set(!switchedOn.get());
            }
        });

        setGraphic(switchBtn);
        
        getStylesheets().add("styles/Main.css");
        
        switchedOn.addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                Boolean t, Boolean t1)
            {
                if (t1)
                {
                    setText(" Listado  ");
                    setId("on");
                    setContentDisplay(ContentDisplay.RIGHT);
                }
                else
                {
                    setText("Calendario");
                    setId("off");
                    setContentDisplay(ContentDisplay.LEFT);
                }
            }
        });

        switchedOn.set(false);
    }

    public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
    
    public Button getButton(){
        return switchBtn;
    }
}