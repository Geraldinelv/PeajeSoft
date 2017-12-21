/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import modelo.Estacion;
import persistencia.EstacionJpaController;
import persistencia.exceptions.NonexistentEntityException;
/**
 *
 * @author Usuario
 */
public class EstacionLogica {
    private EstacionJpaController EstacionDao = new EstacionJpaController();

    public Estacion buscarEstacion(Estacion estacion) {
        return EstacionDao.findEstacion(estacion.getNombre());
    }

    public void registarEstacion(Estacion estacion) throws Exception {
        EstacionDao.create(estacion);
    }

    public void actualizarEstacion(Estacion estacion) throws NonexistentEntityException, Exception {
        EstacionDao.edit(estacion);
    }
    
}
