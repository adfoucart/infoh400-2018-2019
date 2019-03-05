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
import ulb.lisa.his.model.Patient;
import ulb.lisa.his.model.Patientdicomidentifier;

/**
 *
 * @author Adrien Foucart
 */
public class PatientdicomidentifierJpaController implements Serializable {

    public PatientdicomidentifierJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Patientdicomidentifier patientdicomidentifier) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Patient patient = patientdicomidentifier.getPatient();
            if (patient != null) {
                patient = em.getReference(patient.getClass(), patient.getIdPatient());
                patientdicomidentifier.setPatient(patient);
            }
            em.persist(patientdicomidentifier);
            if (patient != null) {
                patient.getPatientdicomidentifierCollection().add(patientdicomidentifier);
                patient = em.merge(patient);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Patientdicomidentifier patientdicomidentifier) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Patientdicomidentifier persistentPatientdicomidentifier = em.find(Patientdicomidentifier.class, patientdicomidentifier.getIdPatientDicomIdentifier());
            Patient patientOld = persistentPatientdicomidentifier.getPatient();
            Patient patientNew = patientdicomidentifier.getPatient();
            if (patientNew != null) {
                patientNew = em.getReference(patientNew.getClass(), patientNew.getIdPatient());
                patientdicomidentifier.setPatient(patientNew);
            }
            patientdicomidentifier = em.merge(patientdicomidentifier);
            if (patientOld != null && !patientOld.equals(patientNew)) {
                patientOld.getPatientdicomidentifierCollection().remove(patientdicomidentifier);
                patientOld = em.merge(patientOld);
            }
            if (patientNew != null && !patientNew.equals(patientOld)) {
                patientNew.getPatientdicomidentifierCollection().add(patientdicomidentifier);
                patientNew = em.merge(patientNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = patientdicomidentifier.getIdPatientDicomIdentifier();
                if (findPatientdicomidentifier(id) == null) {
                    throw new NonexistentEntityException("The patientdicomidentifier with id " + id + " no longer exists.");
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
            Patientdicomidentifier patientdicomidentifier;
            try {
                patientdicomidentifier = em.getReference(Patientdicomidentifier.class, id);
                patientdicomidentifier.getIdPatientDicomIdentifier();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The patientdicomidentifier with id " + id + " no longer exists.", enfe);
            }
            Patient patient = patientdicomidentifier.getPatient();
            if (patient != null) {
                patient.getPatientdicomidentifierCollection().remove(patientdicomidentifier);
                patient = em.merge(patient);
            }
            em.remove(patientdicomidentifier);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Patientdicomidentifier> findPatientdicomidentifierEntities() {
        return findPatientdicomidentifierEntities(true, -1, -1);
    }

    public List<Patientdicomidentifier> findPatientdicomidentifierEntities(int maxResults, int firstResult) {
        return findPatientdicomidentifierEntities(false, maxResults, firstResult);
    }

    private List<Patientdicomidentifier> findPatientdicomidentifierEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Patientdicomidentifier.class));
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

    public Patientdicomidentifier findPatientdicomidentifier(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Patientdicomidentifier.class, id);
        } finally {
            em.close();
        }
    }

    public int getPatientdicomidentifierCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Patientdicomidentifier> rt = cq.from(Patientdicomidentifier.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Patientdicomidentifier findPatientdicomidentifierByDicomIdentifier(String dicomIdentifier){
        EntityManager em = getEntityManager();
        Patientdicomidentifier p = null;
        try {
            TypedQuery<Patientdicomidentifier> q = em.createNamedQuery("Patientdicomidentifier.findByDicomIdentifier", Patientdicomidentifier.class);
            p = q.setParameter("dicomIdentifier", dicomIdentifier).getSingleResult();
        } catch(NoResultException ex){
            return null;
        } 
        finally {
            em.close();
        }
        return p;
    }
    
}
