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
import ulb.lisa.his.model.Practitioner;
import ulb.lisa.his.model.Imagingstudy;
import ulb.lisa.his.model.Series;

/**
 *
 * @author Adrien Foucart
 */
public class SeriesJpaController implements Serializable {

    public SeriesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Series series) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Practitioner performer = series.getPerformer();
            if (performer != null) {
                performer = em.getReference(performer.getClass(), performer.getIdPractitioner());
                series.setPerformer(performer);
            }
            Imagingstudy study = series.getStudy();
            if (study != null) {
                study = em.getReference(study.getClass(), study.getIdImagingStudy());
                series.setStudy(study);
            }
            em.persist(series);
            if (performer != null) {
                performer.getSeriesCollection().add(series);
                performer = em.merge(performer);
            }
            if (study != null) {
                study.getSeriesCollection().add(series);
                study = em.merge(study);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Series series) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Series persistentSeries = em.find(Series.class, series.getIdSeries());
            Practitioner performerOld = persistentSeries.getPerformer();
            Practitioner performerNew = series.getPerformer();
            Imagingstudy studyOld = persistentSeries.getStudy();
            Imagingstudy studyNew = series.getStudy();
            if (performerNew != null) {
                performerNew = em.getReference(performerNew.getClass(), performerNew.getIdPractitioner());
                series.setPerformer(performerNew);
            }
            if (studyNew != null) {
                studyNew = em.getReference(studyNew.getClass(), studyNew.getIdImagingStudy());
                series.setStudy(studyNew);
            }
            series = em.merge(series);
            if (performerOld != null && !performerOld.equals(performerNew)) {
                performerOld.getSeriesCollection().remove(series);
                performerOld = em.merge(performerOld);
            }
            if (performerNew != null && !performerNew.equals(performerOld)) {
                performerNew.getSeriesCollection().add(series);
                performerNew = em.merge(performerNew);
            }
            if (studyOld != null && !studyOld.equals(studyNew)) {
                studyOld.getSeriesCollection().remove(series);
                studyOld = em.merge(studyOld);
            }
            if (studyNew != null && !studyNew.equals(studyOld)) {
                studyNew.getSeriesCollection().add(series);
                studyNew = em.merge(studyNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = series.getIdSeries();
                if (findSeries(id) == null) {
                    throw new NonexistentEntityException("The series with id " + id + " no longer exists.");
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
            Series series;
            try {
                series = em.getReference(Series.class, id);
                series.getIdSeries();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The series with id " + id + " no longer exists.", enfe);
            }
            Practitioner performer = series.getPerformer();
            if (performer != null) {
                performer.getSeriesCollection().remove(series);
                performer = em.merge(performer);
            }
            Imagingstudy study = series.getStudy();
            if (study != null) {
                study.getSeriesCollection().remove(series);
                study = em.merge(study);
            }
            em.remove(series);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Series> findSeriesEntities() {
        return findSeriesEntities(true, -1, -1);
    }

    public List<Series> findSeriesEntities(int maxResults, int firstResult) {
        return findSeriesEntities(false, maxResults, firstResult);
    }

    private List<Series> findSeriesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Series.class));
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

    public Series findSeries(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Series.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeriesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Series> rt = cq.from(Series.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Series findSeriesByUid(String seriesUid) {
        EntityManager em = getEntityManager();
        Series series = null;
        try {
            TypedQuery<Series> q = em.createNamedQuery("Series.findByUid", Series.class);
            series = q.setParameter("uid", seriesUid).getSingleResult();
        } catch(NoResultException ex){
            return null;
        } 
        finally {
            em.close();
        }
        return series;
    }
    
}
