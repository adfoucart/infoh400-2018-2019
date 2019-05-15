/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.his.database;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ulb.lisa.his.database.exceptions.NonexistentEntityException;
import ulb.lisa.his.model.Instance;
import ulb.lisa.his.model.Series;

/**
 *
 * @author Adrien Foucart
 */
public class InstanceJpaController implements Serializable {

    public InstanceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Instance instance) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Series series = instance.getSeries();
            if (series != null) {
                series = em.getReference(series.getClass(), series.getIdSeries());
                instance.setSeries(series);
            }
            em.persist(instance);
            if (series != null) {
                series.getInstanceCollection().add(instance);
                series = em.merge(series);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Instance instance) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Instance persistentInstance = em.find(Instance.class, instance.getIdInstance());
            Series seriesOld = persistentInstance.getSeries();
            Series seriesNew = instance.getSeries();
            if (seriesNew != null) {
                seriesNew = em.getReference(seriesNew.getClass(), seriesNew.getIdSeries());
                instance.setSeries(seriesNew);
            }
            instance = em.merge(instance);
            if (seriesOld != null && !seriesOld.equals(seriesNew)) {
                seriesOld.getInstanceCollection().remove(instance);
                seriesOld = em.merge(seriesOld);
            }
            if (seriesNew != null && !seriesNew.equals(seriesOld)) {
                seriesNew.getInstanceCollection().add(instance);
                seriesNew = em.merge(seriesNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = instance.getIdInstance();
                if (findInstance(id) == null) {
                    throw new NonexistentEntityException("The instance with id " + id + " no longer exists.");
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
            Instance instance;
            try {
                instance = em.getReference(Instance.class, id);
                instance.getIdInstance();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The instance with id " + id + " no longer exists.", enfe);
            }
            Series series = instance.getSeries();
            if (series != null) {
                series.getInstanceCollection().remove(instance);
                series = em.merge(series);
            }
            em.remove(instance);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Instance> findInstanceEntities() {
        return findInstanceEntities(true, -1, -1);
    }

    public List<Instance> findInstanceEntities(int maxResults, int firstResult) {
        return findInstanceEntities(false, maxResults, firstResult);
    }

    private List<Instance> findInstanceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Instance.class));
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

    public Instance findInstance(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Instance.class, id);
        } finally {
            em.close();
        }
    }

    public int getInstanceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Instance> rt = cq.from(Instance.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Instance findInstanceByUid(String instanceUid) {
        EntityManager em = getEntityManager();
        Instance instance = null;
        try {
            TypedQuery<Instance> q = em.createNamedQuery("Instance.findByUid", Instance.class);
            instance = q.setParameter("uid", instanceUid).getSingleResult();
        } catch(NoResultException ex){
            return null;
        } 
        finally {
            em.close();
        }
        return instance;
    }

    public List<Instance> findInstancesBySeries(Series s) {
        EntityManager em = getEntityManager();
        List<Instance> instances = null;
        
        TypedQuery<Instance> q = em.createNamedQuery("Instance.findBySeries", Instance.class);
        instances = q.setParameter("series", s).getResultList();
        
        return instances;
    }
    
}
