/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import modelo.Categoria;
import persistencia.CategoriaJpaController;
import persistencia.exceptions.NonexistentEntityException;
/**
 *
 * @author Usuario
 */
public class CategoriaLogica {
    private CategoriaJpaController CategoriaDao = new CategoriaJpaController();

    public Categoria buscarCategoria(Categoria categoria) {
        return CategoriaDao.findCategoria(categoria.getCategoria());
    }

    public void registarCategoria(Categoria categoria) throws Exception {
        CategoriaDao.create(categoria);
    }

    public void actualizarCategoria(Categoria categoria) throws NonexistentEntityException, Exception {
        CategoriaDao.edit(categoria);
    }
    
}
