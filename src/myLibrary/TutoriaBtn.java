/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myLibrary;

import javafx.scene.control.Button;
import modelo.Tutoria;

/**
 *
 * @author FMR
 */
public class TutoriaBtn extends Button{
    private Tutoria tutoria;
    
    public TutoriaBtn(Tutoria tut){
        tutoria = tut;
    }
    
    public void setTutoria(Tutoria t){
        tutoria = t;
    }
    
    public Tutoria getTutoria(){
        return tutoria;
    }
}
