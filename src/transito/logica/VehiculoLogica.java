/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transito.logica;

import transito.modelo.Vehiculo;
import transito.persistencia.VehiculoJpaController;
import persistencia.exceptions.NonexistentEntityException;
/**
 *
 * @author Usuario
 */
public class VehiculoLogica {
    private VehiculoJpaController VehiculoDao = new VehiculoJpaController();

    public Vehiculo buscarVehiculo(Vehiculo vehiculo) {
        return VehiculoDao.findVehiculo(vehiculo.getPlaca());
    }

    public void registarVehiculo(Vehiculo vehiculo) throws Exception {
        VehiculoDao.create(vehiculo);
    }

    public void actualizarVehiculo(Vehiculo vehiculo) throws NonexistentEntityException, Exception {
        VehiculoDao.edit(vehiculo);
    }
    
}
