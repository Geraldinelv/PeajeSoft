/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Carril;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Estacion;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author Joan
 */
public class EstacionJpaController implements Serializable {

    public EstacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estacion estacion) throws PreexistingEntityException, Exception {
        if (estacion.getCarrilList() == null) {
            estacion.setCarrilList(new ArrayList<Carril>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Carril> attachedCarrilList = new ArrayList<Carril>();
            for (Carril carrilListCarrilToAttach : estacion.getCarrilList()) {
                carrilListCarrilToAttach = em.getReference(carrilListCarrilToAttach.getClass(), carrilListCarrilToAttach.getIdcarril());
                attachedCarrilList.add(carrilListCarrilToAttach);
            }
            estacion.setCarrilList(attachedCarrilList);
            em.persist(estacion);
            for (Carril carrilListCarril : estacion.getCarrilList()) {
                Estacion oldNombreOfCarrilListCarril = carrilListCarril.getNombre();
                carrilListCarril.setNombre(estacion);
                carrilListCarril = em.merge(carrilListCarril);
                if (oldNombreOfCarrilListCarril != null) {
                    oldNombreOfCarrilListCarril.getCarrilList().remove(carrilListCarril);
                    oldNombreOfCarrilListCarril = em.merge(oldNombreOfCarrilListCarril);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstacion(estacion.getNombre()) != null) {
                throw new PreexistingEntityException("Estacion " + estacion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estacion estacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estacion persistentEstacion = em.find(Estacion.class, estacion.getNombre());
            List<Carril> carrilListOld = persistentEstacion.getCarrilList();
            List<Carril> carrilListNew = estacion.getCarrilList();
            List<String> illegalOrphanMessages = null;
            for (Carril carrilListOldCarril : carrilListOld) {
                if (!carrilListNew.contains(carrilListOldCarril)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carril " + carrilListOldCarril + " since its nombre field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Carril> attachedCarrilListNew = new ArrayList<Carril>();
            for (Carril carrilListNewCarrilToAttach : carrilListNew) {
                carrilListNewCarrilToAttach = em.getReference(carrilListNewCarrilToAttach.getClass(), carrilListNewCarrilToAttach.getIdcarril());
                attachedCarrilListNew.add(carrilListNewCarrilToAttach);
            }
            carrilListNew = attachedCarrilListNew;
            estacion.setCarrilList(carrilListNew);
            estacion = em.merge(estacion);
            for (Carril carrilListNewCarril : carrilListNew) {
                if (!carrilListOld.contains(carrilListNewCarril)) {
                    Estacion oldNombreOfCarrilListNewCarril = carrilListNewCarril.getNombre();
                    carrilListNewCarril.setNombre(estacion);
                    carrilListNewCarril = em.merge(carrilListNewCarril);
                    if (oldNombreOfCarrilListNewCarril != null && !oldNombreOfCarrilListNewCarril.equals(estacion)) {
                        oldNombreOfCarrilListNewCarril.getCarrilList().remove(carrilListNewCarril);
                        oldNombreOfCarrilListNewCarril = em.merge(oldNombreOfCarrilListNewCarril);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = estacion.getNombre();
                if (findEstacion(id) == null) {
                    throw new NonexistentEntityException("The estacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estacion estacion;
            try {
                estacion = em.getReference(Estacion.class, id);
                estacion.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Carril> carrilListOrphanCheck = estacion.getCarrilList();
            for (Carril carrilListOrphanCheckCarril : carrilListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estacion (" + estacion + ") cannot be destroyed since the Carril " + carrilListOrphanCheckCarril + " in its carrilList field has a non-nullable nombre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estacion> findEstacionEntities() {
        return findEstacionEntities(true, -1, -1);
    }

    public List<Estacion> findEstacionEntities(int maxResults, int firstResult) {
        return findEstacionEntities(false, maxResults, firstResult);
    }

    private List<Estacion> findEstacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estacion.class));
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

    public Estacion findEstacion(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estacion> rt = cq.from(Estacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
