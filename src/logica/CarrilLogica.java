/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import modelo.Carril;
import persistencia.CarrilJpaController;
import persistencia.exceptions.NonexistentEntityException;
/**
 *
 * @author Usuario
 */
public class CarrilLogica {
    private CarrilJpaController CarrilDao = new CarrilJpaController();

    public Carril buscarCarril(Carril carril) {
        return CarrilDao.findCarril(carril.getIdcarril());
    }

    public void registarCarril(Carril carril) throws Exception {
        CarrilDao.create(carril);
    }

    public void actualizarCarril(Carril carril) throws NonexistentEntityException, Exception {
        CarrilDao.edit(carril);
    }
    
}
