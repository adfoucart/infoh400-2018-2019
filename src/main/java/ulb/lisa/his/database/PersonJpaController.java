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
import ulb.lisa.his.model.Address;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import ulb.lisa.his.database.exceptions.IllegalOrphanException;
import ulb.lisa.his.database.exceptions.NonexistentEntityException;
import ulb.lisa.his.model.Practitioner;
import ulb.lisa.his.model.Patient;
import ulb.lisa.his.model.Person;
import ulb.lisa.his.model.User;

/**
 *
 * @author Adrien Foucart
 */
public class PersonJpaController implements Serializable {

    public PersonJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Person person) {
        if (person.getAddressCollection() == null) {
            person.setAddressCollection(new ArrayList<Address>());
        }
        if (person.getPractitionerCollection() == null) {
            person.setPractitionerCollection(new ArrayList<Practitioner>());
        }
        if (person.getPatientCollection() == null) {
            person.setPatientCollection(new ArrayList<Patient>());
        }
        if (person.getUserCollection() == null) {
            person.setUserCollection(new ArrayList<User>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Address> attachedAddressCollection = new ArrayList<Address>();
            for (Address addressCollectionAddressToAttach : person.getAddressCollection()) {
                addressCollectionAddressToAttach = em.getReference(addressCollectionAddressToAttach.getClass(), addressCollectionAddressToAttach.getIdAddress());
                attachedAddressCollection.add(addressCollectionAddressToAttach);
            }
            person.setAddressCollection(attachedAddressCollection);
            Collection<Practitioner> attachedPractitionerCollection = new ArrayList<Practitioner>();
            for (Practitioner practitionerCollectionPractitionerToAttach : person.getPractitionerCollection()) {
                practitionerCollectionPractitionerToAttach = em.getReference(practitionerCollectionPractitionerToAttach.getClass(), practitionerCollectionPractitionerToAttach.getIdPractitioner());
                attachedPractitionerCollection.add(practitionerCollectionPractitionerToAttach);
            }
            person.setPractitionerCollection(attachedPractitionerCollection);
            Collection<Patient> attachedPatientCollection = new ArrayList<Patient>();
            for (Patient patientCollectionPatientToAttach : person.getPatientCollection()) {
                patientCollectionPatientToAttach = em.getReference(patientCollectionPatientToAttach.getClass(), patientCollectionPatientToAttach.getIdPatient());
                attachedPatientCollection.add(patientCollectionPatientToAttach);
            }
            person.setPatientCollection(attachedPatientCollection);
            Collection<User> attachedUserCollection = new ArrayList<User>();
            for (User userCollectionUserToAttach : person.getUserCollection()) {
                userCollectionUserToAttach = em.getReference(userCollectionUserToAttach.getClass(), userCollectionUserToAttach.getIdUser());
                attachedUserCollection.add(userCollectionUserToAttach);
            }
            person.setUserCollection(attachedUserCollection);
            em.persist(person);
            for (Address addressCollectionAddress : person.getAddressCollection()) {
                Person oldPersonOfAddressCollectionAddress = addressCollectionAddress.getPerson();
                addressCollectionAddress.setPerson(person);
                addressCollectionAddress = em.merge(addressCollectionAddress);
                if (oldPersonOfAddressCollectionAddress != null) {
                    oldPersonOfAddressCollectionAddress.getAddressCollection().remove(addressCollectionAddress);
                    oldPersonOfAddressCollectionAddress = em.merge(oldPersonOfAddressCollectionAddress);
                }
            }
            for (Practitioner practitionerCollectionPractitioner : person.getPractitionerCollection()) {
                Person oldPersonOfPractitionerCollectionPractitioner = practitionerCollectionPractitioner.getPerson();
                practitionerCollectionPractitioner.setPerson(person);
                practitionerCollectionPractitioner = em.merge(practitionerCollectionPractitioner);
                if (oldPersonOfPractitionerCollectionPractitioner != null) {
                    oldPersonOfPractitionerCollectionPractitioner.getPractitionerCollection().remove(practitionerCollectionPractitioner);
                    oldPersonOfPractitionerCollectionPractitioner = em.merge(oldPersonOfPractitionerCollectionPractitioner);
                }
            }
            for (Patient patientCollectionPatient : person.getPatientCollection()) {
                Person oldPersonOfPatientCollectionPatient = patientCollectionPatient.getPerson();
                patientCollectionPatient.setPerson(person);
                patientCollectionPatient = em.merge(patientCollectionPatient);
                if (oldPersonOfPatientCollectionPatient != null) {
                    oldPersonOfPatientCollectionPatient.getPatientCollection().remove(patientCollectionPatient);
                    oldPersonOfPatientCollectionPatient = em.merge(oldPersonOfPatientCollectionPatient);
                }
            }
            for (User userCollectionUser : person.getUserCollection()) {
                Person oldPersonOfUserCollectionUser = userCollectionUser.getPerson();
                userCollectionUser.setPerson(person);
                userCollectionUser = em.merge(userCollectionUser);
                if (oldPersonOfUserCollectionUser != null) {
                    oldPersonOfUserCollectionUser.getUserCollection().remove(userCollectionUser);
                    oldPersonOfUserCollectionUser = em.merge(oldPersonOfUserCollectionUser);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Person person) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Person persistentPerson = em.find(Person.class, person.getIdPerson());
            Collection<Address> addressCollectionOld = persistentPerson.getAddressCollection();
            Collection<Address> addressCollectionNew = person.getAddressCollection();
            Collection<Practitioner> practitionerCollectionOld = persistentPerson.getPractitionerCollection();
            Collection<Practitioner> practitionerCollectionNew = person.getPractitionerCollection();
            Collection<Patient> patientCollectionOld = persistentPerson.getPatientCollection();
            Collection<Patient> patientCollectionNew = person.getPatientCollection();
            Collection<User> userCollectionOld = persistentPerson.getUserCollection();
            Collection<User> userCollectionNew = person.getUserCollection();
            List<String> illegalOrphanMessages = null;
            for (Address addressCollectionOldAddress : addressCollectionOld) {
                if (!addressCollectionNew.contains(addressCollectionOldAddress)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Address " + addressCollectionOldAddress + " since its person field is not nullable.");
                }
            }
            for (Practitioner practitionerCollectionOldPractitioner : practitionerCollectionOld) {
                if (!practitionerCollectionNew.contains(practitionerCollectionOldPractitioner)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Practitioner " + practitionerCollectionOldPractitioner + " since its person field is not nullable.");
                }
            }
            for (Patient patientCollectionOldPatient : patientCollectionOld) {
                if (!patientCollectionNew.contains(patientCollectionOldPatient)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Patient " + patientCollectionOldPatient + " since its person field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Address> attachedAddressCollectionNew = new ArrayList<Address>();
            for (Address addressCollectionNewAddressToAttach : addressCollectionNew) {
                addressCollectionNewAddressToAttach = em.getReference(addressCollectionNewAddressToAttach.getClass(), addressCollectionNewAddressToAttach.getIdAddress());
                attachedAddressCollectionNew.add(addressCollectionNewAddressToAttach);
            }
            addressCollectionNew = attachedAddressCollectionNew;
            person.setAddressCollection(addressCollectionNew);
            Collection<Practitioner> attachedPractitionerCollectionNew = new ArrayList<Practitioner>();
            for (Practitioner practitionerCollectionNewPractitionerToAttach : practitionerCollectionNew) {
                practitionerCollectionNewPractitionerToAttach = em.getReference(practitionerCollectionNewPractitionerToAttach.getClass(), practitionerCollectionNewPractitionerToAttach.getIdPractitioner());
                attachedPractitionerCollectionNew.add(practitionerCollectionNewPractitionerToAttach);
            }
            practitionerCollectionNew = attachedPractitionerCollectionNew;
            person.setPractitionerCollection(practitionerCollectionNew);
            Collection<Patient> attachedPatientCollectionNew = new ArrayList<Patient>();
            for (Patient patientCollectionNewPatientToAttach : patientCollectionNew) {
                patientCollectionNewPatientToAttach = em.getReference(patientCollectionNewPatientToAttach.getClass(), patientCollectionNewPatientToAttach.getIdPatient());
                attachedPatientCollectionNew.add(patientCollectionNewPatientToAttach);
            }
            patientCollectionNew = attachedPatientCollectionNew;
            person.setPatientCollection(patientCollectionNew);
            Collection<User> attachedUserCollectionNew = new ArrayList<User>();
            for (User userCollectionNewUserToAttach : userCollectionNew) {
                userCollectionNewUserToAttach = em.getReference(userCollectionNewUserToAttach.getClass(), userCollectionNewUserToAttach.getIdUser());
                attachedUserCollectionNew.add(userCollectionNewUserToAttach);
            }
            userCollectionNew = attachedUserCollectionNew;
            person.setUserCollection(userCollectionNew);
            person = em.merge(person);
            for (Address addressCollectionNewAddress : addressCollectionNew) {
                if (!addressCollectionOld.contains(addressCollectionNewAddress)) {
                    Person oldPersonOfAddressCollectionNewAddress = addressCollectionNewAddress.getPerson();
                    addressCollectionNewAddress.setPerson(person);
                    addressCollectionNewAddress = em.merge(addressCollectionNewAddress);
                    if (oldPersonOfAddressCollectionNewAddress != null && !oldPersonOfAddressCollectionNewAddress.equals(person)) {
                        oldPersonOfAddressCollectionNewAddress.getAddressCollection().remove(addressCollectionNewAddress);
                        oldPersonOfAddressCollectionNewAddress = em.merge(oldPersonOfAddressCollectionNewAddress);
                    }
                }
            }
            for (Practitioner practitionerCollectionNewPractitioner : practitionerCollectionNew) {
                if (!practitionerCollectionOld.contains(practitionerCollectionNewPractitioner)) {
                    Person oldPersonOfPractitionerCollectionNewPractitioner = practitionerCollectionNewPractitioner.getPerson();
                    practitionerCollectionNewPractitioner.setPerson(person);
                    practitionerCollectionNewPractitioner = em.merge(practitionerCollectionNewPractitioner);
                    if (oldPersonOfPractitionerCollectionNewPractitioner != null && !oldPersonOfPractitionerCollectionNewPractitioner.equals(person)) {
                        oldPersonOfPractitionerCollectionNewPractitioner.getPractitionerCollection().remove(practitionerCollectionNewPractitioner);
                        oldPersonOfPractitionerCollectionNewPractitioner = em.merge(oldPersonOfPractitionerCollectionNewPractitioner);
                    }
                }
            }
            for (Patient patientCollectionNewPatient : patientCollectionNew) {
                if (!patientCollectionOld.contains(patientCollectionNewPatient)) {
                    Person oldPersonOfPatientCollectionNewPatient = patientCollectionNewPatient.getPerson();
                    patientCollectionNewPatient.setPerson(person);
                    patientCollectionNewPatient = em.merge(patientCollectionNewPatient);
                    if (oldPersonOfPatientCollectionNewPatient != null && !oldPersonOfPatientCollectionNewPatient.equals(person)) {
                        oldPersonOfPatientCollectionNewPatient.getPatientCollection().remove(patientCollectionNewPatient);
                        oldPersonOfPatientCollectionNewPatient = em.merge(oldPersonOfPatientCollectionNewPatient);
                    }
                }
            }
            for (User userCollectionOldUser : userCollectionOld) {
                if (!userCollectionNew.contains(userCollectionOldUser)) {
                    userCollectionOldUser.setPerson(null);
                    userCollectionOldUser = em.merge(userCollectionOldUser);
                }
            }
            for (User userCollectionNewUser : userCollectionNew) {
                if (!userCollectionOld.contains(userCollectionNewUser)) {
                    Person oldPersonOfUserCollectionNewUser = userCollectionNewUser.getPerson();
                    userCollectionNewUser.setPerson(person);
                    userCollectionNewUser = em.merge(userCollectionNewUser);
                    if (oldPersonOfUserCollectionNewUser != null && !oldPersonOfUserCollectionNewUser.equals(person)) {
                        oldPersonOfUserCollectionNewUser.getUserCollection().remove(userCollectionNewUser);
                        oldPersonOfUserCollectionNewUser = em.merge(oldPersonOfUserCollectionNewUser);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = person.getIdPerson();
                if (findPerson(id) == null) {
                    throw new NonexistentEntityException("The person with id " + id + " no longer exists.");
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
            Person person;
            try {
                person = em.getReference(Person.class, id);
                person.getIdPerson();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The person with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Address> addressCollectionOrphanCheck = person.getAddressCollection();
            for (Address addressCollectionOrphanCheckAddress : addressCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Address " + addressCollectionOrphanCheckAddress + " in its addressCollection field has a non-nullable person field.");
            }
            Collection<Practitioner> practitionerCollectionOrphanCheck = person.getPractitionerCollection();
            for (Practitioner practitionerCollectionOrphanCheckPractitioner : practitionerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Practitioner " + practitionerCollectionOrphanCheckPractitioner + " in its practitionerCollection field has a non-nullable person field.");
            }
            Collection<Patient> patientCollectionOrphanCheck = person.getPatientCollection();
            for (Patient patientCollectionOrphanCheckPatient : patientCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Person (" + person + ") cannot be destroyed since the Patient " + patientCollectionOrphanCheckPatient + " in its patientCollection field has a non-nullable person field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<User> userCollection = person.getUserCollection();
            for (User userCollectionUser : userCollection) {
                userCollectionUser.setPerson(null);
                userCollectionUser = em.merge(userCollectionUser);
            }
            em.remove(person);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Person> findPersonEntities() {
        return findPersonEntities(true, -1, -1);
    }

    public List<Person> findPersonEntities(int maxResults, int firstResult) {
        return findPersonEntities(false, maxResults, firstResult);
    }

    private List<Person> findPersonEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Person.class));
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

    public Person findPerson(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Person.class, id);
        } finally {
            em.close();
        }
    }
    
    public Person findPersonByNationalNumber(String nationalNumber) {
        EntityManager em = getEntityManager();
        try{
            return (Person) em.createNamedQuery("Person.findByNationalNumber").setParameter("nationalNumber", nationalNumber).getSingleResult();
        } catch( NoResultException ex ){
            return null;
        } 
        finally {
            em.close();
        }
    }

    public int getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Person> rt = cq.from(Person.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
