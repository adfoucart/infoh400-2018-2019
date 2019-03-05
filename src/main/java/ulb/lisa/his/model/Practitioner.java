/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.his.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Adrien Foucart
 */
@Entity
@Table(name = "practitioner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Practitioner.findAll", query = "SELECT p FROM Practitioner p"),
    @NamedQuery(name = "Practitioner.findByIdPractitioner", query = "SELECT p FROM Practitioner p WHERE p.idPractitioner = :idPractitioner"),
    @NamedQuery(name = "Practitioner.findByActive", query = "SELECT p FROM Practitioner p WHERE p.active = :active"),
    @NamedQuery(name = "Practitioner.findByQualificationCode", query = "SELECT p FROM Practitioner p WHERE p.qualificationCode = :qualificationCode")})
public class Practitioner implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "practitioner")
    private Collection<Practitionerdicomidentifier> practitionerdicomidentifierCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPractitioner")
    private Integer idPractitioner;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @Basic(optional = false)
    @Column(name = "qualification_code")
    private String qualificationCode;
    @OneToMany(mappedBy = "interpreter")
    private Collection<Imagingstudy> imagingstudyCollection;
    @OneToMany(mappedBy = "referrer")
    private Collection<Imagingstudy> imagingstudyCollection1;
    @JoinColumn(name = "person", referencedColumnName = "idPerson")
    @ManyToOne(optional = false)
    private Person person;
    @OneToMany(mappedBy = "generalPractitioner")
    private Collection<Patient> patientCollection;
    @OneToMany(mappedBy = "performer")
    private Collection<Series> seriesCollection;

    public Practitioner() {
    }

    public Practitioner(Integer idPractitioner) {
        this.idPractitioner = idPractitioner;
    }

    public Practitioner(Integer idPractitioner, boolean active, String qualificationCode) {
        this.idPractitioner = idPractitioner;
        this.active = active;
        this.qualificationCode = qualificationCode;
    }

    public Integer getIdPractitioner() {
        return idPractitioner;
    }

    public void setIdPractitioner(Integer idPractitioner) {
        this.idPractitioner = idPractitioner;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getQualificationCode() {
        return qualificationCode;
    }

    public void setQualificationCode(String qualificationCode) {
        this.qualificationCode = qualificationCode;
    }

    @XmlTransient
    public Collection<Imagingstudy> getImagingstudyCollection() {
        return imagingstudyCollection;
    }

    public void setImagingstudyCollection(Collection<Imagingstudy> imagingstudyCollection) {
        this.imagingstudyCollection = imagingstudyCollection;
    }

    @XmlTransient
    public Collection<Imagingstudy> getImagingstudyCollection1() {
        return imagingstudyCollection1;
    }

    public void setImagingstudyCollection1(Collection<Imagingstudy> imagingstudyCollection1) {
        this.imagingstudyCollection1 = imagingstudyCollection1;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @XmlTransient
    public Collection<Patient> getPatientCollection() {
        return patientCollection;
    }

    public void setPatientCollection(Collection<Patient> patientCollection) {
        this.patientCollection = patientCollection;
    }

    @XmlTransient
    public Collection<Series> getSeriesCollection() {
        return seriesCollection;
    }

    public void setSeriesCollection(Collection<Series> seriesCollection) {
        this.seriesCollection = seriesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPractitioner != null ? idPractitioner.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Practitioner)) {
            return false;
        }
        Practitioner other = (Practitioner) object;
        if ((this.idPractitioner == null && other.idPractitioner != null) || (this.idPractitioner != null && !this.idPractitioner.equals(other.idPractitioner))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if( idPractitioner == -1 ) return "None";
        return "[" + idPractitioner + "] " + person.getNameFamily().toUpperCase() + " " + person.getNameGiven() + ", " + qualificationCode;
        //return "ulb.lisa.his.model.Practitioner[ idPractitioner=" + idPractitioner + " ]";
    }

    public String getFormattedText() {
        String str = "----- PRACTITIONER INFORMATION -----\n\n";
        str += person.getFormattedText();
        str += "QUALIFICATION: \t" + qualificationCode + "\n";
        
        if( active == false ) str += "\n !!! Practitioner is currently inactive !!!";
        
        return str;
    }

    @XmlTransient
    public Collection<Practitionerdicomidentifier> getPractitionerdicomidentifierCollection() {
        return practitionerdicomidentifierCollection;
    }

    public void setPractitionerdicomidentifierCollection(Collection<Practitionerdicomidentifier> practitionerdicomidentifierCollection) {
        this.practitionerdicomidentifierCollection = practitionerdicomidentifierCollection;
    }
    
}
