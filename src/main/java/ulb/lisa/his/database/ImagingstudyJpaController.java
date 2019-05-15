/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.his.database;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ulb.lisa.his.model.Practitioner;
import ulb.lisa.his.model.Patient;
import ulb.lisa.his.model.Series;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import ulb.lisa.his.database.exceptions.IllegalOrphanException;
import ulb.lisa.his.database.exceptions.NonexistentEntityException;
import ulb.lisa.his.model.Imagingstudy;
import ulb.lisa.his.model.Patientdicomidentifier;

/**
 *
 * @author Adrien Foucart
 */
public class ImagingstudyJpaController implements Serializable {

    public ImagingstudyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Imagingstudy imagingstudy) {
        if (imagingstudy.getSeriesCollection() == null) {
            imagingstudy.setSeriesCollection(new ArrayList<Series>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Practitioner interpreter = imagingstudy.getInterpreter();
            if (interpreter != null) {
                interpreter = em.getReference(interpreter.getClass(), interpreter.getIdPractitioner());
                imagingstudy.setInterpreter(interpreter);
            }
            Patient patient = imagingstudy.getPatient();
            if (patient != null) {
                patient = em.getReference(patient.getClass(), patient.getIdPatient());
                imagingstudy.setPatient(patient);
            }
            Practitioner referrer = imagingstudy.getReferrer();
            if (referrer != null) {
                referrer = em.getReference(referrer.getClass(), referrer.getIdPractitioner());
                imagingstudy.setReferrer(referrer);
            }
            Collection<Series> attachedSeriesCollection = new ArrayList<Series>();
            for (Series seriesCollectionSeriesToAttach : imagingstudy.getSeriesCollection()) {
                seriesCollectionSeriesToAttach = em.getReference(seriesCollectionSeriesToAttach.getClass(), seriesCollectionSeriesToAttach.getIdSeries());
                attachedSeriesCollection.add(seriesCollectionSeriesToAttach);
            }
            imagingstudy.setSeriesCollection(attachedSeriesCollection);
            em.persist(imagingstudy);
            if (interpreter != null) {
                interpreter.getImagingstudyCollection().add(imagingstudy);
                interpreter = em.merge(interpreter);
            }
            if (patient != null) {
                patient.getImagingstudyCollection().add(imagingstudy);
                patient = em.merge(patient);
            }
            if (referrer != null) {
                referrer.getImagingstudyCollection().add(imagingstudy);
                referrer = em.merge(referrer);
            }
            for (Series seriesCollectionSeries : imagingstudy.getSeriesCollection()) {
                Imagingstudy oldStudyOfSeriesCollectionSeries = seriesCollectionSeries.getStudy();
                seriesCollectionSeries.setStudy(imagingstudy);
                seriesCollectionSeries = em.merge(seriesCollectionSeries);
                if (oldStudyOfSeriesCollectionSeries != null) {
                    oldStudyOfSeriesCollectionSeries.getSeriesCollection().remove(seriesCollectionSeries);
                    oldStudyOfSeriesCollectionSeries = em.merge(oldStudyOfSeriesCollectionSeries);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Imagingstudy imagingstudy) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Imagingstudy persistentImagingstudy = em.find(Imagingstudy.class, imagingstudy.getIdImagingStudy());
            Practitioner interpreterOld = persistentImagingstudy.getInterpreter();
            Practitioner interpreterNew = imagingstudy.getInterpreter();
            Patient patientOld = persistentImagingstudy.getPatient();
            Patient patientNew = imagingstudy.getPatient();
            Practitioner referrerOld = persistentImagingstudy.getReferrer();
            Practitioner referrerNew = imagingstudy.getReferrer();
            Collection<Series> seriesCollectionOld = persistentImagingstudy.getSeriesCollection();
            Collection<Series> seriesCollectionNew = imagingstudy.getSeriesCollection();
            List<String> illegalOrphanMessages = null;
            for (Series seriesCollectionOldSeries : seriesCollectionOld) {
                if (!seriesCollectionNew.contains(seriesCollectionOldSeries)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Series " + seriesCollectionOldSeries + " since its study field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (interpreterNew != null) {
                interpreterNew = em.getReference(interpreterNew.getClass(), interpreterNew.getIdPractitioner());
                imagingstudy.setInterpreter(interpreterNew);
            }
            if (patientNew != null) {
                patientNew = em.getReference(patientNew.getClass(), patientNew.getIdPatient());
                imagingstudy.setPatient(patientNew);
            }
            if (referrerNew != null) {
                referrerNew = em.getReference(referrerNew.getClass(), referrerNew.getIdPractitioner());
                imagingstudy.setReferrer(referrerNew);
            }
            Collection<Series> attachedSeriesCollectionNew = new ArrayList<Series>();
            for (Series seriesCollectionNewSeriesToAttach : seriesCollectionNew) {
                seriesCollectionNewSeriesToAttach = em.getReference(seriesCollectionNewSeriesToAttach.getClass(), seriesCollectionNewSeriesToAttach.getIdSeries());
                attachedSeriesCollectionNew.add(seriesCollectionNewSeriesToAttach);
            }
            seriesCollectionNew = attachedSeriesCollectionNew;
            imagingstudy.setSeriesCollection(seriesCollectionNew);
            imagingstudy = em.merge(imagingstudy);
            if (interpreterOld != null && !interpreterOld.equals(interpreterNew)) {
                interpreterOld.getImagingstudyCollection().remove(imagingstudy);
                interpreterOld = em.merge(interpreterOld);
            }
            if (interpreterNew != null && !interpreterNew.equals(interpreterOld)) {
                interpreterNew.getImagingstudyCollection().add(imagingstudy);
                interpreterNew = em.merge(interpreterNew);
            }
            if (patientOld != null && !patientOld.equals(patientNew)) {
                patientOld.getImagingstudyCollection().remove(imagingstudy);
                patientOld = em.merge(patientOld);
            }
            if (patientNew != null && !patientNew.equals(patientOld)) {
                patientNew.getImagingstudyCollection().add(imagingstudy);
                patientNew = em.merge(patientNew);
            }
            if (referrerOld != null && !referrerOld.equals(referrerNew)) {
                referrerOld.getImagingstudyCollection().remove(imagingstudy);
                referrerOld = em.merge(referrerOld);
            }
            if (referrerNew != null && !referrerNew.equals(referrerOld)) {
                referrerNew.getImagingstudyCollection().add(imagingstudy);
                referrerNew = em.merge(referrerNew);
            }
            for (Series seriesCollectionNewSeries : seriesCollectionNew) {
                if (!seriesCollectionOld.contains(seriesCollectionNewSeries)) {
                    Imagingstudy oldStudyOfSeriesCollectionNewSeries = seriesCollectionNewSeries.getStudy();
                    seriesCollectionNewSeries.setStudy(imagingstudy);
                    seriesCollectionNewSeries = em.merge(seriesCollectionNewSeries);
                    if (oldStudyOfSeriesCollectionNewSeries != null && !oldStudyOfSeriesCollectionNewSeries.equals(imagingstudy)) {
                        oldStudyOfSeriesCollectionNewSeries.getSeriesCollection().remove(seriesCollectionNewSeries);
                        oldStudyOfSeriesCollectionNewSeries = em.merge(oldStudyOfSeriesCollectionNewSeries);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = imagingstudy.getIdImagingStudy();
                if (findImagingstudy(id) == null) {
                    throw new NonexistentEntityException("The imagingstudy with id " + id + " no longer exists.");
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
            Imagingstudy imagingstudy;
            try {
                imagingstudy = em.getReference(Imagingstudy.class, id);
                imagingstudy.getIdImagingStudy();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The imagingstudy with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Series> seriesCollectionOrphanCheck = imagingstudy.getSeriesCollection();
            for (Series seriesCollectionOrphanCheckSeries : seriesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Imagingstudy (" + imagingstudy + ") cannot be destroyed since the Series " + seriesCollectionOrphanCheckSeries + " in its seriesCollection field has a non-nullable study field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Practitioner interpreter = imagingstudy.getInterpreter();
            if (interpreter != null) {
                interpreter.getImagingstudyCollection().remove(imagingstudy);
                interpreter = em.merge(interpreter);
            }
            Patient patient = imagingstudy.getPatient();
            if (patient != null) {
                patient.getImagingstudyCollection().remove(imagingstudy);
                patient = em.merge(patient);
            }
            Practitioner referrer = imagingstudy.getReferrer();
            if (referrer != null) {
                referrer.getImagingstudyCollection().remove(imagingstudy);
                referrer = em.merge(referrer);
            }
            em.remove(imagingstudy);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Imagingstudy> findImagingstudyEntities() {
        return findImagingstudyEntities(true, -1, -1);
    }

    public List<Imagingstudy> findImagingstudyEntities(int maxResults, int firstResult) {
        return findImagingstudyEntities(false, maxResults, firstResult);
    }

    private List<Imagingstudy> findImagingstudyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Imagingstudy.class));
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

    public Imagingstudy findImagingstudy(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Imagingstudy.class, id);
        } finally {
            em.close();
        }
    }

    public int getImagingstudyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Imagingstudy> rt = cq.from(Imagingstudy.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Imagingstudy findImagingstudyByUid(String studyUid) {
        EntityManager em = getEntityManager();
        Imagingstudy is = null;
        try {
            TypedQuery<Imagingstudy> q = em.createNamedQuery("Imagingstudy.findByUid", Imagingstudy.class);
            is = q.setParameter("uid", studyUid).getSingleResult();
        } catch(NoResultException ex){
            return null;
        } 
        finally {
            em.close();
        }
        return is;
    }
    
    public List<Imagingstudy> findImagingstudiesByPatient(Patient pat) {
        EntityManager em = getEntityManager();
        List<Imagingstudy> is = null;
        try {
            TypedQuery<Imagingstudy> q = em.createNamedQuery("Imagingstudy.findByPatient", Imagingstudy.class);
            is = q.setParameter("patient", pat).getResultList();
        } catch(NoResultException ex){
            return null;
        } 
        finally {
            em.close();
        }
        return is;
    }
    
}
