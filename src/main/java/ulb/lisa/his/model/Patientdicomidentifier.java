/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ulb.lisa.his.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Adrien Foucart
 */
@Entity
@Table(name = "patientdicomidentifier")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Patientdicomidentifier.findAll", query = "SELECT p FROM Patientdicomidentifier p"),
    @NamedQuery(name = "Patientdicomidentifier.findByIdPatientDicomIdentifier", query = "SELECT p FROM Patientdicomidentifier p WHERE p.idPatientDicomIdentifier = :idPatientDicomIdentifier"),
    @NamedQuery(name = "Patientdicomidentifier.findByDicomIdentifier", query = "SELECT p FROM Patientdicomidentifier p WHERE p.dicomIdentifier = :dicomIdentifier")})
public class Patientdicomidentifier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPatientDicomIdentifier")
    private Integer idPatientDicomIdentifier;
    @Basic(optional = false)
    @Column(name = "dicomIdentifier")
    private String dicomIdentifier;
    @JoinColumn(name = "patient", referencedColumnName = "idPatient")
    @ManyToOne(optional = false)
    private Patient patient;

    public Patientdicomidentifier() {
    }

    public Patientdicomidentifier(Integer idPatientDicomIdentifier) {
        this.idPatientDicomIdentifier = idPatientDicomIdentifier;
    }

    public Patientdicomidentifier(Integer idPatientDicomIdentifier, String dicomIdentifier) {
        this.idPatientDicomIdentifier = idPatientDicomIdentifier;
        this.dicomIdentifier = dicomIdentifier;
    }

    public Integer getIdPatientDicomIdentifier() {
        return idPatientDicomIdentifier;
    }

    public void setIdPatientDicomIdentifier(Integer idPatientDicomIdentifier) {
        this.idPatientDicomIdentifier = idPatientDicomIdentifier;
    }

    public String getDicomIdentifier() {
        return dicomIdentifier;
    }

    public void setDicomIdentifier(String dicomIdentifier) {
        this.dicomIdentifier = dicomIdentifier;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPatientDicomIdentifier != null ? idPatientDicomIdentifier.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Patientdicomidentifier)) {
            return false;
        }
        Patientdicomidentifier other = (Patientdicomidentifier) object;
        if ((this.idPatientDicomIdentifier == null && other.idPatientDicomIdentifier != null) || (this.idPatientDicomIdentifier != null && !this.idPatientDicomIdentifier.equals(other.idPatientDicomIdentifier))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ulb.lisa.his.model.Patientdicomidentifier[ idPatientDicomIdentifier=" + idPatientDicomIdentifier + " ]";
    }
    
}
