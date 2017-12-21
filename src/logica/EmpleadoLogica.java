/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import modelo.Empleado;
import persistencia.EmpleadoJpaController;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Joan
 */
public class EmpleadoLogica {
    
    
      
    private EmpleadoJpaController EmpleadoDao = new EmpleadoJpaController();
    
   public  Empleado buscarEmpleado(Empleado empleado){
    return EmpleadoDao.findEmpleado(empleado.getCedula());
   }
   
   public void registarEmpleado(Empleado empleado) throws Exception{
       EmpleadoDao.create(empleado);
   }
   
   public void actualizarEmpleado(Empleado empleado) throws NonexistentEntityException, Exception{
       EmpleadoDao.edit(empleado);
   }
   
    
}
