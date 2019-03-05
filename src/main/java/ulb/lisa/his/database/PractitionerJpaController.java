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
import ulb.lisa.his.model.Person;
import ulb.lisa.his.model.Imagingstudy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import ulb.lisa.his.database.exceptions.NonexistentEntityException;
import ulb.lisa.his.model.Patient;
import ulb.lisa.his.model.Practitioner;
import ulb.lisa.his.model.Series;

/**
 *
 * @author Adrien Foucart
 */
public class PractitionerJpaController implements Serializable {

    public PractitionerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Practitioner practitioner) {
        if (practitioner.getImagingstudyCollection() == null) {
            practitioner.setImagingstudyCollection(new ArrayList<Imagingstudy>());
        }
        if (practitioner.getImagingstudyCollection1() == null) {
            practitioner.setImagingstudyCollection1(new ArrayList<Imagingstudy>());
        }
        if (practitioner.getPatientCollection() == null) {
            practitioner.setPatientCollection(new ArrayList<Patient>());
        }
        if (practitioner.getSeriesCollection() == null) {
            practitioner.setSeriesCollection(new ArrayList<Series>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person person = practitioner.getPerson();
            if (person != null) {
                person = em.getReference(person.getClass(), person.getIdPerson());
                practitioner.setPerson(person);
            }
            Collection<Imagingstudy> attachedImagingstudyCollection = new ArrayList<Imagingstudy>();
            for (Imagingstudy imagingstudyCollectionImagingstudyToAttach : practitioner.getImagingstudyCollection()) {
                imagingstudyCollectionImagingstudyToAttach = em.getReference(imagingstudyCollectionImagingstudyToAttach.getClass(), imagingstudyCollectionImagingstudyToAttach.getIdImagingStudy());
                attachedImagingstudyCollection.add(imagingstudyCollectionImagingstudyToAttach);
            }
            practitioner.setImagingstudyCollection(attachedImagingstudyCollection);
            Collection<Imagingstudy> attachedImagingstudyCollection1 = new ArrayList<Imagingstudy>();
            for (Imagingstudy imagingstudyCollection1ImagingstudyToAttach : practitioner.getImagingstudyCollection1()) {
                imagingstudyCollection1ImagingstudyToAttach = em.getReference(imagingstudyCollection1ImagingstudyToAttach.getClass(), imagingstudyCollection1ImagingstudyToAttach.getIdImagingStudy());
                attachedImagingstudyCollection1.add(imagingstudyCollection1ImagingstudyToAttach);
            }
            practitioner.setImagingstudyCollection1(attachedImagingstudyCollection1);
            Collection<Patient> attachedPatientCollection = new ArrayList<Patient>();
            for (Patient patientCollectionPatientToAttach : practitioner.getPatientCollection()) {
                patientCollectionPatientToAttach = em.getReference(patientCollectionPatientToAttach.getClass(), patientCollectionPatientToAttach.getIdPatient());
                attachedPatientCollection.add(patientCollectionPatientToAttach);
            }
            practitioner.setPatientCollection(attachedPatientCollection);
            Collection<Series> attachedSeriesCollection = new ArrayList<Series>();
            for (Series seriesCollectionSeriesToAttach : practitioner.getSeriesCollection()) {
                seriesCollectionSeriesToAttach = em.getReference(seriesCollectionSeriesToAttach.getClass(), seriesCollectionSeriesToAttach.getIdSeries());
                attachedSeriesCollection.add(seriesCollectionSeriesToAttach);
            }
            practitioner.setSeriesCollection(attachedSeriesCollection);
            em.persist(practitioner);
            if (person != null) {
                person.getPractitionerCollection().add(practitioner);
                person = em.merge(person);
            }
            for (Imagingstudy imagingstudyCollectionImagingstudy : practitioner.getImagingstudyCollection()) {
                Practitioner oldInterpreterOfImagingstudyCollectionImagingstudy = imagingstudyCollectionImagingstudy.getInterpreter();
                imagingstudyCollectionImagingstudy.setInterpreter(practitioner);
                imagingstudyCollectionImagingstudy = em.merge(imagingstudyCollectionImagingstudy);
                if (oldInterpreterOfImagingstudyCollectionImagingstudy != null) {
                    oldInterpreterOfImagingstudyCollectionImagingstudy.getImagingstudyCollection().remove(imagingstudyCollectionImagingstudy);
                    oldInterpreterOfImagingstudyCollectionImagingstudy = em.merge(oldInterpreterOfImagingstudyCollectionImagingstudy);
                }
            }
            for (Imagingstudy imagingstudyCollection1Imagingstudy : practitioner.getImagingstudyCollection1()) {
                Practitioner oldReferrerOfImagingstudyCollection1Imagingstudy = imagingstudyCollection1Imagingstudy.getReferrer();
                imagingstudyCollection1Imagingstudy.setReferrer(practitioner);
                imagingstudyCollection1Imagingstudy = em.merge(imagingstudyCollection1Imagingstudy);
                if (oldReferrerOfImagingstudyCollection1Imagingstudy != null) {
                    oldReferrerOfImagingstudyCollection1Imagingstudy.getImagingstudyCollection1().remove(imagingstudyCollection1Imagingstudy);
                    oldReferrerOfImagingstudyCollection1Imagingstudy = em.merge(oldReferrerOfImagingstudyCollection1Imagingstudy);
                }
            }
            for (Patient patientCollectionPatient : practitioner.getPatientCollection()) {
                Practitioner oldGeneralPractitionerOfPatientCollectionPatient = patientCollectionPatient.getGeneralPractitioner();
                patientCollectionPatient.setGeneralPractitioner(practitioner);
                patientCollectionPatient = em.merge(patientCollectionPatient);
                if (oldGeneralPractitionerOfPatientCollectionPatient != null) {
                    oldGeneralPractitionerOfPatientCollectionPatient.getPatientCollection().remove(patientCollectionPatient);
                    oldGeneralPractitionerOfPatientCollectionPatient = em.merge(oldGeneralPractitionerOfPatientCollectionPatient);
                }
            }
            for (Series seriesCollectionSeries : practitioner.getSeriesCollection()) {
                Practitioner oldPerformerOfSeriesCollectionSeries = seriesCollectionSeries.getPerformer();
                seriesCollectionSeries.setPerformer(practitioner);
                seriesCollectionSeries = em.merge(seriesCollectionSeries);
                if (oldPerformerOfSeriesCollectionSeries != null) {
                    oldPerformerOfSeriesCollectionSeries.getSeriesCollection().remove(seriesCollectionSeries);
                    oldPerformerOfSeriesCollectionSeries = em.merge(oldPerformerOfSeriesCollectionSeries);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Practitioner practitioner) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Practitioner persistentPractitioner = em.find(Practitioner.class, practitioner.getIdPractitioner());
            Person personOld = persistentPractitioner.getPerson();
            Person personNew = practitioner.getPerson();
            Collection<Imagingstudy> imagingstudyCollectionOld = persistentPractitioner.getImagingstudyCollection();
            Collection<Imagingstudy> imagingstudyCollectionNew = practitioner.getImagingstudyCollection();
            Collection<Imagingstudy> imagingstudyCollection1Old = persistentPractitioner.getImagingstudyCollection1();
            Collection<Imagingstudy> imagingstudyCollection1New = practitioner.getImagingstudyCollection1();
            Collection<Patient> patientCollectionOld = persistentPractitioner.getPatientCollection();
            Collection<Patient> patientCollectionNew = practitioner.getPatientCollection();
            Collection<Series> seriesCollectionOld = persistentPractitioner.getSeriesCollection();
            Collection<Series> seriesCollectionNew = practitioner.getSeriesCollection();
            if (personNew != null) {
                personNew = em.getReference(personNew.getClass(), personNew.getIdPerson());
                practitioner.setPerson(personNew);
            }
            Collection<Imagingstudy> attachedImagingstudyCollectionNew = new ArrayList<Imagingstudy>();
            for (Imagingstudy imagingstudyCollectionNewImagingstudyToAttach : imagingstudyCollectionNew) {
                imagingstudyCollectionNewImagingstudyToAttach = em.getReference(imagingstudyCollectionNewImagingstudyToAttach.getClass(), imagingstudyCollectionNewImagingstudyToAttach.getIdImagingStudy());
                attachedImagingstudyCollectionNew.add(imagingstudyCollectionNewImagingstudyToAttach);
            }
            imagingstudyCollectionNew = attachedImagingstudyCollectionNew;
            practitioner.setImagingstudyCollection(imagingstudyCollectionNew);
            Collection<Imagingstudy> attachedImagingstudyCollection1New = new ArrayList<Imagingstudy>();
            for (Imagingstudy imagingstudyCollection1NewImagingstudyToAttach : imagingstudyCollection1New) {
                imagingstudyCollection1NewImagingstudyToAttach = em.getReference(imagingstudyCollection1NewImagingstudyToAttach.getClass(), imagingstudyCollection1NewImagingstudyToAttach.getIdImagingStudy());
                attachedImagingstudyCollection1New.add(imagingstudyCollection1NewImagingstudyToAttach);
            }
            imagingstudyCollection1New = attachedImagingstudyCollection1New;
            practitioner.setImagingstudyCollection1(imagingstudyCollection1New);
            Collection<Patient> attachedPatientCollectionNew = new ArrayList<Patient>();
            for (Patient patientCollectionNewPatientToAttach : patientCollectionNew) {
                patientCollectionNewPatientToAttach = em.getReference(patientCollectionNewPatientToAttach.getClass(), patientCollectionNewPatientToAttach.getIdPatient());
                attachedPatientCollectionNew.add(patientCollectionNewPatientToAttach);
            }
            patientCollectionNew = attachedPatientCollectionNew;
            practitioner.setPatientCollection(patientCollectionNew);
            Collection<Series> attachedSeriesCollectionNew = new ArrayList<Series>();
            for (Series seriesCollectionNewSeriesToAttach : seriesCollectionNew) {
                seriesCollectionNewSeriesToAttach = em.getReference(seriesCollectionNewSeriesToAttach.getClass(), seriesCollectionNewSeriesToAttach.getIdSeries());
                attachedSeriesCollectionNew.add(seriesCollectionNewSeriesToAttach);
            }
            seriesCollectionNew = attachedSeriesCollectionNew;
            practitioner.setSeriesCollection(seriesCollectionNew);
            practitioner = em.merge(practitioner);
            if (personOld != null && !personOld.equals(personNew)) {
                personOld.getPractitionerCollection().remove(practitioner);
                personOld = em.merge(personOld);
            }
            if (personNew != null && !personNew.equals(personOld)) {
                personNew.getPractitionerCollection().add(practitioner);
                personNew = em.merge(personNew);
            }
            for (Imagingstudy imagingstudyCollectionOldImagingstudy : imagingstudyCollectionOld) {
                if (!imagingstudyCollectionNew.contains(imagingstudyCollectionOldImagingstudy)) {
                    imagingstudyCollectionOldImagingstudy.setInterpreter(null);
                    imagingstudyCollectionOldImagingstudy = em.merge(imagingstudyCollectionOldImagingstudy);
                }
            }
            for (Imagingstudy imagingstudyCollectionNewImagingstudy : imagingstudyCollectionNew) {
                if (!imagingstudyCollectionOld.contains(imagingstudyCollectionNewImagingstudy)) {
                    Practitioner oldInterpreterOfImagingstudyCollectionNewImagingstudy = imagingstudyCollectionNewImagingstudy.getInterpreter();
                    imagingstudyCollectionNewImagingstudy.setInterpreter(practitioner);
                    imagingstudyCollectionNewImagingstudy = em.merge(imagingstudyCollectionNewImagingstudy);
                    if (oldInterpreterOfImagingstudyCollectionNewImagingstudy != null && !oldInterpreterOfImagingstudyCollectionNewImagingstudy.equals(practitioner)) {
                        oldInterpreterOfImagingstudyCollectionNewImagingstudy.getImagingstudyCollection().remove(imagingstudyCollectionNewImagingstudy);
                        oldInterpreterOfImagingstudyCollectionNewImagingstudy = em.merge(oldInterpreterOfImagingstudyCollectionNewImagingstudy);
                    }
                }
            }
            for (Imagingstudy imagingstudyCollection1OldImagingstudy : imagingstudyCollection1Old) {
                if (!imagingstudyCollection1New.contains(imagingstudyCollection1OldImagingstudy)) {
                    imagingstudyCollection1OldImagingstudy.setReferrer(null);
                    imagingstudyCollection1OldImagingstudy = em.merge(imagingstudyCollection1OldImagingstudy);
                }
            }
            for (Imagingstudy imagingstudyCollection1NewImagingstudy : imagingstudyCollection1New) {
                if (!imagingstudyCollection1Old.contains(imagingstudyCollection1NewImagingstudy)) {
                    Practitioner oldReferrerOfImagingstudyCollection1NewImagingstudy = imagingstudyCollection1NewImagingstudy.getReferrer();
                    imagingstudyCollection1NewImagingstudy.setReferrer(practitioner);
                    imagingstudyCollection1NewImagingstudy = em.merge(imagingstudyCollection1NewImagingstudy);
                    if (oldReferrerOfImagingstudyCollection1NewImagingstudy != null && !oldReferrerOfImagingstudyCollection1NewImagingstudy.equals(practitioner)) {
                        oldReferrerOfImagingstudyCollection1NewImagingstudy.getImagingstudyCollection1().remove(imagingstudyCollection1NewImagingstudy);
                        oldReferrerOfImagingstudyCollection1NewImagingstudy = em.merge(oldReferrerOfImagingstudyCollection1NewImagingstudy);
                    }
                }
            }
            for (Patient patientCollectionOldPatient : patientCollectionOld) {
                if (!patientCollectionNew.contains(patientCollectionOldPatient)) {
                    patientCollectionOldPatient.setGeneralPractitioner(null);
                    patientCollectionOldPatient = em.merge(patientCollectionOldPatient);
                }
            }
            for (Patient patientCollectionNewPatient : patientCollectionNew) {
                if (!patientCollectionOld.contains(patientCollectionNewPatient)) {
                    Practitioner oldGeneralPractitionerOfPatientCollectionNewPatient = patientCollectionNewPatient.getGeneralPractitioner();
                    patientCollectionNewPatient.setGeneralPractitioner(practitioner);
                    patientCollectionNewPatient = em.merge(patientCollectionNewPatient);
                    if (oldGeneralPractitionerOfPatientCollectionNewPatient != null && !oldGeneralPractitionerOfPatientCollectionNewPatient.equals(practitioner)) {
                        oldGeneralPractitionerOfPatientCollectionNewPatient.getPatientCollection().remove(patientCollectionNewPatient);
                        oldGeneralPractitionerOfPatientCollectionNewPatient = em.merge(oldGeneralPractitionerOfPatientCollectionNewPatient);
                    }
                }
            }
            for (Series seriesCollectionOldSeries : seriesCollectionOld) {
                if (!seriesCollectionNew.contains(seriesCollectionOldSeries)) {
                    seriesCollectionOldSeries.setPerformer(null);
                    seriesCollectionOldSeries = em.merge(seriesCollectionOldSeries);
                }
            }
            for (Series seriesCollectionNewSeries : seriesCollectionNew) {
                if (!seriesCollectionOld.contains(seriesCollectionNewSeries)) {
                    Practitioner oldPerformerOfSeriesCollectionNewSeries = seriesCollectionNewSeries.getPerformer();
                    seriesCollectionNewSeries.setPerformer(practitioner);
                    seriesCollectionNewSeries = em.merge(seriesCollectionNewSeries);
                    if (oldPerformerOfSeriesCollectionNewSeries != null && !oldPerformerOfSeriesCollectionNewSeries.equals(practitioner)) {
                        oldPerformerOfSeriesCollectionNewSeries.getSeriesCollection().remove(seriesCollectionNewSeries);
                        oldPerformerOfSeriesCollectionNewSeries = em.merge(oldPerformerOfSeriesCollectionNewSeries);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = practitioner.getIdPractitioner();
                if (findPractitioner(id) == null) {
                    throw new NonexistentEntityException("The practitioner with id " + id + " no longer exists.");
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
            Practitioner practitioner;
            try {
                practitioner = em.getReference(Practitioner.class, id);
                practitioner.getIdPractitioner();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The practitioner with id " + id + " no longer exists.", enfe);
            }
            Person person = practitioner.getPerson();
            if (person != null) {
                person.getPractitionerCollection().remove(practitioner);
                person = em.merge(person);
            }
            Collection<Imagingstudy> imagingstudyCollection = practitioner.getImagingstudyCollection();
            for (Imagingstudy imagingstudyCollectionImagingstudy : imagingstudyCollection) {
                imagingstudyCollectionImagingstudy.setInterpreter(null);
                imagingstudyCollectionImagingstudy = em.merge(imagingstudyCollectionImagingstudy);
            }
            Collection<Imagingstudy> imagingstudyCollection1 = practitioner.getImagingstudyCollection1();
            for (Imagingstudy imagingstudyCollection1Imagingstudy : imagingstudyCollection1) {
                imagingstudyCollection1Imagingstudy.setReferrer(null);
                imagingstudyCollection1Imagingstudy = em.merge(imagingstudyCollection1Imagingstudy);
            }
            Collection<Patient> patientCollection = practitioner.getPatientCollection();
            for (Patient patientCollectionPatient : patientCollection) {
                patientCollectionPatient.setGeneralPractitioner(null);
                patientCollectionPatient = em.merge(patientCollectionPatient);
            }
            Collection<Series> seriesCollection = practitioner.getSeriesCollection();
            for (Series seriesCollectionSeries : seriesCollection) {
                seriesCollectionSeries.setPerformer(null);
                seriesCollectionSeries = em.merge(seriesCollectionSeries);
            }
            em.remove(practitioner);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Practitioner> findPractitionerEntities() {
        return findPractitionerEntities(true, -1, -1);
    }

    public List<Practitioner> findPractitionerEntities(int maxResults, int firstResult) {
        return findPractitionerEntities(false, maxResults, firstResult);
    }

    private List<Practitioner> findPractitionerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Practitioner.class));
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

    public Practitioner findPractitioner(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Practitioner.class, id);
        } finally {
            em.close();
        }
    }

    public int getPractitionerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Practitioner> rt = cq.from(Practitioner.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
