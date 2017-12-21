/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Carril;
import modelo.Categoria;
import modelo.Empleado;
import modelo.Factura;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Joan
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Peaje1.0PU");
    }
    private EntityManagerFactory emf = null;


    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carril idcarril = factura.getIdcarril();
            if (idcarril != null) {
                idcarril = em.getReference(idcarril.getClass(), idcarril.getIdcarril());
                factura.setIdcarril(idcarril);
            }
            Categoria categoria = factura.getCategoria();
            if (categoria != null) {
                categoria = em.getReference(categoria.getClass(), categoria.getCategoria());
                factura.setCategoria(categoria);
            }
            Empleado cedula = factura.getCedula();
            if (cedula != null) {
                cedula = em.getReference(cedula.getClass(), cedula.getCedula());
                factura.setCedula(cedula);
            }
            em.persist(factura);
            if (idcarril != null) {
                idcarril.getFacturaList().add(factura);
                idcarril = em.merge(idcarril);
            }
            if (categoria != null) {
                categoria.getFacturaList().add(factura);
                categoria = em.merge(categoria);
            }
            if (cedula != null) {
                cedula.getFacturaList().add(factura);
                cedula = em.merge(cedula);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Factura factura) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura persistentFactura = em.find(Factura.class, factura.getIdfactura());
            Carril idcarrilOld = persistentFactura.getIdcarril();
            Carril idcarrilNew = factura.getIdcarril();
            Categoria categoriaOld = persistentFactura.getCategoria();
            Categoria categoriaNew = factura.getCategoria();
            Empleado cedulaOld = persistentFactura.getCedula();
            Empleado cedulaNew = factura.getCedula();
            if (idcarrilNew != null) {
                idcarrilNew = em.getReference(idcarrilNew.getClass(), idcarrilNew.getIdcarril());
                factura.setIdcarril(idcarrilNew);
            }
            if (categoriaNew != null) {
                categoriaNew = em.getReference(categoriaNew.getClass(), categoriaNew.getCategoria());
                factura.setCategoria(categoriaNew);
            }
            if (cedulaNew != null) {
                cedulaNew = em.getReference(cedulaNew.getClass(), cedulaNew.getCedula());
                factura.setCedula(cedulaNew);
            }
            factura = em.merge(factura);
            if (idcarrilOld != null && !idcarrilOld.equals(idcarrilNew)) {
                idcarrilOld.getFacturaList().remove(factura);
                idcarrilOld = em.merge(idcarrilOld);
            }
            if (idcarrilNew != null && !idcarrilNew.equals(idcarrilOld)) {
                idcarrilNew.getFacturaList().add(factura);
                idcarrilNew = em.merge(idcarrilNew);
            }
            if (categoriaOld != null && !categoriaOld.equals(categoriaNew)) {
                categoriaOld.getFacturaList().remove(factura);
                categoriaOld = em.merge(categoriaOld);
            }
            if (categoriaNew != null && !categoriaNew.equals(categoriaOld)) {
                categoriaNew.getFacturaList().add(factura);
                categoriaNew = em.merge(categoriaNew);
            }
            if (cedulaOld != null && !cedulaOld.equals(cedulaNew)) {
                cedulaOld.getFacturaList().remove(factura);
                cedulaOld = em.merge(cedulaOld);
            }
            if (cedulaNew != null && !cedulaNew.equals(cedulaOld)) {
                cedulaNew.getFacturaList().add(factura);
                cedulaNew = em.merge(cedulaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = factura.getIdfactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getIdfactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            Carril idcarril = factura.getIdcarril();
            if (idcarril != null) {
                idcarril.getFacturaList().remove(factura);
                idcarril = em.merge(idcarril);
            }
            Categoria categoria = factura.getCategoria();
            if (categoria != null) {
                categoria.getFacturaList().remove(factura);
                categoria = em.merge(categoria);
            }
            Empleado cedula = factura.getCedula();
            if (cedula != null) {
                cedula.getFacturaList().remove(factura);
                cedula = em.merge(cedula);
            }
            em.remove(factura);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
