/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package propietario.logica;

import transito.modelo.Propietario;
import transito.persistencia.PropietarioJpaController;
import persistencia.exceptions.NonexistentEntityException;
/**
 *
 * @author Usuario
 */
public class PropietarioLogica {
    private PropietarioJpaController PropietarioDao = new PropietarioJpaController();

    public Propietario buscarPropietario(Propietario propietario) {
        return PropietarioDao.findPropietario(propietario.getCedula());
    }

    public void registarPropietario(Propietario propietario) throws Exception {
        PropietarioDao.create(propietario);
    }

    public void actualizarPropietario(Propietario propietario) throws NonexistentEntityException, Exception {
        PropietarioDao.edit(propietario);
    }
    
}
