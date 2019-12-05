/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myLibrary;

import modelo.Alumno;

/**
 *
 * @author JoseluXtv
 */
public class MiAlumno extends Alumno {
    public MiAlumno(String nom, String apell, String mail) {
        super(nom,apell,mail);
    }
    
    public boolean equals(Object o) {
        return (o instanceof MiAlumno) && ((MiAlumno)o).getEmail().equals(this.getEmail());
    }
}
