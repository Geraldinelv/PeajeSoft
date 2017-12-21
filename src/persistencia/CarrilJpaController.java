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
import modelo.Estacion;
import modelo.Factura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Carril;
import modelo.Informe;
import persistencia.exceptions.IllegalOrphanException;
import persistencia.exceptions.NonexistentEntityException;
import persistencia.exceptions.PreexistingEntityException;

/**
 *
 * @author Joan
 */
public class CarrilJpaController implements Serializable {

    public CarrilJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public CarrilJpaController() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carril carril) throws PreexistingEntityException, Exception {
        if (carril.getFacturaList() == null) {
            carril.setFacturaList(new ArrayList<Factura>());
        }
        if (carril.getInformeList() == null) {
            carril.setInformeList(new ArrayList<Informe>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estacion nombre = carril.getNombre();
            if (nombre != null) {
                nombre = em.getReference(nombre.getClass(), nombre.getNombre());
                carril.setNombre(nombre);
            }
            List<Factura> attachedFacturaList = new ArrayList<Factura>();
            for (Factura facturaListFacturaToAttach : carril.getFacturaList()) {
                facturaListFacturaToAttach = em.getReference(facturaListFacturaToAttach.getClass(), facturaListFacturaToAttach.getIdfactura());
                attachedFacturaList.add(facturaListFacturaToAttach);
            }
            carril.setFacturaList(attachedFacturaList);
            List<Informe> attachedInformeList = new ArrayList<Informe>();
            for (Informe informeListInformeToAttach : carril.getInformeList()) {
                informeListInformeToAttach = em.getReference(informeListInformeToAttach.getClass(), informeListInformeToAttach.getIdinforme());
                attachedInformeList.add(informeListInformeToAttach);
            }
            carril.setInformeList(attachedInformeList);
            em.persist(carril);
            if (nombre != null) {
                nombre.getCarrilList().add(carril);
                nombre = em.merge(nombre);
            }
            for (Factura facturaListFactura : carril.getFacturaList()) {
                Carril oldIdcarrilOfFacturaListFactura = facturaListFactura.getIdcarril();
                facturaListFactura.setIdcarril(carril);
                facturaListFactura = em.merge(facturaListFactura);
                if (oldIdcarrilOfFacturaListFactura != null) {
                    oldIdcarrilOfFacturaListFactura.getFacturaList().remove(facturaListFactura);
                    oldIdcarrilOfFacturaListFactura = em.merge(oldIdcarrilOfFacturaListFactura);
                }
            }
            for (Informe informeListInforme : carril.getInformeList()) {
                Carril oldCarrilIdcarrilOfInformeListInforme = informeListInforme.getCarrilIdcarril();
                informeListInforme.setCarrilIdcarril(carril);
                informeListInforme = em.merge(informeListInforme);
                if (oldCarrilIdcarrilOfInformeListInforme != null) {
                    oldCarrilIdcarrilOfInformeListInforme.getInformeList().remove(informeListInforme);
                    oldCarrilIdcarrilOfInformeListInforme = em.merge(oldCarrilIdcarrilOfInformeListInforme);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCarril(carril.getIdcarril()) != null) {
                throw new PreexistingEntityException("Carril " + carril + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carril carril) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carril persistentCarril = em.find(Carril.class, carril.getIdcarril());
            Estacion nombreOld = persistentCarril.getNombre();
            Estacion nombreNew = carril.getNombre();
            List<Factura> facturaListOld = persistentCarril.getFacturaList();
            List<Factura> facturaListNew = carril.getFacturaList();
            List<Informe> informeListOld = persistentCarril.getInformeList();
            List<Informe> informeListNew = carril.getInformeList();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaListOldFactura : facturaListOld) {
                if (!facturaListNew.contains(facturaListOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaListOldFactura + " since its idcarril field is not nullable.");
                }
            }
            for (Informe informeListOldInforme : informeListOld) {
                if (!informeListNew.contains(informeListOldInforme)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Informe " + informeListOldInforme + " since its carrilIdcarril field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nombreNew != null) {
                nombreNew = em.getReference(nombreNew.getClass(), nombreNew.getNombre());
                carril.setNombre(nombreNew);
            }
            List<Factura> attachedFacturaListNew = new ArrayList<Factura>();
            for (Factura facturaListNewFacturaToAttach : facturaListNew) {
                facturaListNewFacturaToAttach = em.getReference(facturaListNewFacturaToAttach.getClass(), facturaListNewFacturaToAttach.getIdfactura());
                attachedFacturaListNew.add(facturaListNewFacturaToAttach);
            }
            facturaListNew = attachedFacturaListNew;
            carril.setFacturaList(facturaListNew);
            List<Informe> attachedInformeListNew = new ArrayList<Informe>();
            for (Informe informeListNewInformeToAttach : informeListNew) {
                informeListNewInformeToAttach = em.getReference(informeListNewInformeToAttach.getClass(), informeListNewInformeToAttach.getIdinforme());
                attachedInformeListNew.add(informeListNewInformeToAttach);
            }
            informeListNew = attachedInformeListNew;
            carril.setInformeList(informeListNew);
            carril = em.merge(carril);
            if (nombreOld != null && !nombreOld.equals(nombreNew)) {
                nombreOld.getCarrilList().remove(carril);
                nombreOld = em.merge(nombreOld);
            }
            if (nombreNew != null && !nombreNew.equals(nombreOld)) {
                nombreNew.getCarrilList().add(carril);
                nombreNew = em.merge(nombreNew);
            }
            for (Factura facturaListNewFactura : facturaListNew) {
                if (!facturaListOld.contains(facturaListNewFactura)) {
                    Carril oldIdcarrilOfFacturaListNewFactura = facturaListNewFactura.getIdcarril();
                    facturaListNewFactura.setIdcarril(carril);
                    facturaListNewFactura = em.merge(facturaListNewFactura);
                    if (oldIdcarrilOfFacturaListNewFactura != null && !oldIdcarrilOfFacturaListNewFactura.equals(carril)) {
                        oldIdcarrilOfFacturaListNewFactura.getFacturaList().remove(facturaListNewFactura);
                        oldIdcarrilOfFacturaListNewFactura = em.merge(oldIdcarrilOfFacturaListNewFactura);
                    }
                }
            }
            for (Informe informeListNewInforme : informeListNew) {
                if (!informeListOld.contains(informeListNewInforme)) {
                    Carril oldCarrilIdcarrilOfInformeListNewInforme = informeListNewInforme.getCarrilIdcarril();
                    informeListNewInforme.setCarrilIdcarril(carril);
                    informeListNewInforme = em.merge(informeListNewInforme);
                    if (oldCarrilIdcarrilOfInformeListNewInforme != null && !oldCarrilIdcarrilOfInformeListNewInforme.equals(carril)) {
                        oldCarrilIdcarrilOfInformeListNewInforme.getInformeList().remove(informeListNewInforme);
                        oldCarrilIdcarrilOfInformeListNewInforme = em.merge(oldCarrilIdcarrilOfInformeListNewInforme);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carril.getIdcarril();
                if (findCarril(id) == null) {
                    throw new NonexistentEntityException("The carril with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carril carril;
            try {
                carril = em.getReference(Carril.class, id);
                carril.getIdcarril();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carril with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Factura> facturaListOrphanCheck = carril.getFacturaList();
            for (Factura facturaListOrphanCheckFactura : facturaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Carril (" + carril + ") cannot be destroyed since the Factura " + facturaListOrphanCheckFactura + " in its facturaList field has a non-nullable idcarril field.");
            }
            List<Informe> informeListOrphanCheck = carril.getInformeList();
            for (Informe informeListOrphanCheckInforme : informeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Carril (" + carril + ") cannot be destroyed since the Informe " + informeListOrphanCheckInforme + " in its informeList field has a non-nullable carrilIdcarril field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estacion nombre = carril.getNombre();
            if (nombre != null) {
                nombre.getCarrilList().remove(carril);
                nombre = em.merge(nombre);
            }
            em.remove(carril);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carril> findCarrilEntities() {
        return findCarrilEntities(true, -1, -1);
    }

    public List<Carril> findCarrilEntities(int maxResults, int firstResult) {
        return findCarrilEntities(false, maxResults, firstResult);
    }

    private List<Carril> findCarrilEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carril.class));
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

    public Carril findCarril(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carril.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarrilCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carril> rt = cq.from(Carril.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
