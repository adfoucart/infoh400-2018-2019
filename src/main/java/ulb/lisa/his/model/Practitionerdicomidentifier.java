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
@Table(name = "practitionerdicomidentifier")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Practitionerdicomidentifier.findAll", query = "SELECT p FROM Practitionerdicomidentifier p"),
    @NamedQuery(name = "Practitionerdicomidentifier.findByIdPractitionerDicomIdentifier", query = "SELECT p FROM Practitionerdicomidentifier p WHERE p.idPractitionerDicomIdentifier = :idPractitionerDicomIdentifier"),
    @NamedQuery(name = "Practitionerdicomidentifier.findByDicomIdentifier", query = "SELECT p FROM Practitionerdicomidentifier p WHERE p.dicomIdentifier = :dicomIdentifier")})
public class Practitionerdicomidentifier implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idPractitionerDicomIdentifier")
    private Integer idPractitionerDicomIdentifier;
    @Basic(optional = false)
    @Column(name = "dicomIdentifier")
    private String dicomIdentifier;
    @JoinColumn(name = "practitioner", referencedColumnName = "idPractitioner")
    @ManyToOne(optional = false)
    private Practitioner practitioner;

    public Practitionerdicomidentifier() {
    }

    public Practitionerdicomidentifier(Integer idPractitionerDicomIdentifier) {
        this.idPractitionerDicomIdentifier = idPractitionerDicomIdentifier;
    }

    public Practitionerdicomidentifier(Integer idPractitionerDicomIdentifier, String dicomIdentifier) {
        this.idPractitionerDicomIdentifier = idPractitionerDicomIdentifier;
        this.dicomIdentifier = dicomIdentifier;
    }

    public Integer getIdPractitionerDicomIdentifier() {
        return idPractitionerDicomIdentifier;
    }

    public void setIdPractitionerDicomIdentifier(Integer idPractitionerDicomIdentifier) {
        this.idPractitionerDicomIdentifier = idPractitionerDicomIdentifier;
    }

    public String getDicomIdentifier() {
        return dicomIdentifier;
    }

    public void setDicomIdentifier(String dicomIdentifier) {
        this.dicomIdentifier = dicomIdentifier;
    }

    public Practitioner getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(Practitioner practitioner) {
        this.practitioner = practitioner;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPractitionerDicomIdentifier != null ? idPractitionerDicomIdentifier.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Practitionerdicomidentifier)) {
            return false;
        }
        Practitionerdicomidentifier other = (Practitionerdicomidentifier) object;
        if ((this.idPractitionerDicomIdentifier == null && other.idPractitionerDicomIdentifier != null) || (this.idPractitionerDicomIdentifier != null && !this.idPractitionerDicomIdentifier.equals(other.idPractitionerDicomIdentifier))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ulb.lisa.his.model.Practitionerdicomidentifier[ idPractitionerDicomIdentifier=" + idPractitionerDicomIdentifier + " ]";
    }
    
}
