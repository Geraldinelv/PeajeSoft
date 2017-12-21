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
import modelo.Informe;
import persistencia.exceptions.NonexistentEntityException;

/**
 *
 * @author Joan
 */
public class InformeJpaController implements Serializable {

    public InformeJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Peaje1.0PU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Informe informe) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carril carrilIdcarril = informe.getCarrilIdcarril();
            if (carrilIdcarril != null) {
                carrilIdcarril = em.getReference(carrilIdcarril.getClass(), carrilIdcarril.getIdcarril());
                informe.setCarrilIdcarril(carrilIdcarril);
            }
            em.persist(informe);
            if (carrilIdcarril != null) {
                carrilIdcarril.getInformeList().add(informe);
                carrilIdcarril = em.merge(carrilIdcarril);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Informe informe) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Informe persistentInforme = em.find(Informe.class, informe.getIdinforme());
            Carril carrilIdcarrilOld = persistentInforme.getCarrilIdcarril();
            Carril carrilIdcarrilNew = informe.getCarrilIdcarril();
            if (carrilIdcarrilNew != null) {
                carrilIdcarrilNew = em.getReference(carrilIdcarrilNew.getClass(), carrilIdcarrilNew.getIdcarril());
                informe.setCarrilIdcarril(carrilIdcarrilNew);
            }
            informe = em.merge(informe);
            if (carrilIdcarrilOld != null && !carrilIdcarrilOld.equals(carrilIdcarrilNew)) {
                carrilIdcarrilOld.getInformeList().remove(informe);
                carrilIdcarrilOld = em.merge(carrilIdcarrilOld);
            }
            if (carrilIdcarrilNew != null && !carrilIdcarrilNew.equals(carrilIdcarrilOld)) {
                carrilIdcarrilNew.getInformeList().add(informe);
                carrilIdcarrilNew = em.merge(carrilIdcarrilNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = informe.getIdinforme();
                if (findInforme(id) == null) {
                    throw new NonexistentEntityException("The informe with id " + id + " no longer exists.");
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
            Informe informe;
            try {
                informe = em.getReference(Informe.class, id);
                informe.getIdinforme();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The informe with id " + id + " no longer exists.", enfe);
            }
            Carril carrilIdcarril = informe.getCarrilIdcarril();
            if (carrilIdcarril != null) {
                carrilIdcarril.getInformeList().remove(informe);
                carrilIdcarril = em.merge(carrilIdcarril);
            }
            em.remove(informe);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Informe> findInformeEntities() {
        return findInformeEntities(true, -1, -1);
    }

    public List<Informe> findInformeEntities(int maxResults, int firstResult) {
        return findInformeEntities(false, maxResults, firstResult);
    }

    private List<Informe> findInformeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Informe.class));
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

    public Informe findInforme(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Informe.class, id);
        } finally {
            em.close();
        }
    }

    public int getInformeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Informe> rt = cq.from(Informe.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
