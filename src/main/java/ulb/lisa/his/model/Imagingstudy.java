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
import javax.persistence.Lob;
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
@Table(name = "imagingstudy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Imagingstudy.findAll", query = "SELECT i FROM Imagingstudy i"),
    @NamedQuery(name = "Imagingstudy.findByIdImagingStudy", query = "SELECT i FROM Imagingstudy i WHERE i.idImagingStudy = :idImagingStudy"),
    @NamedQuery(name = "Imagingstudy.findByUid", query = "SELECT i FROM Imagingstudy i WHERE i.uid = :uid"),
    @NamedQuery(name = "Imagingstudy.findByReason", query = "SELECT i FROM Imagingstudy i WHERE i.reason = :reason")})
public class Imagingstudy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idImagingStudy")
    private Integer idImagingStudy;
    @Basic(optional = false)
    @Column(name = "uid")
    private String uid;
    @Column(name = "reason")
    private Integer reason;
    @Lob
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "interpreter", referencedColumnName = "idPractitioner")
    @ManyToOne
    private Practitioner interpreter;
    @JoinColumn(name = "patient", referencedColumnName = "idPatient")
    @ManyToOne(optional = false)
    private Patient patient;
    @JoinColumn(name = "referrer", referencedColumnName = "idPractitioner")
    @ManyToOne
    private Practitioner referrer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "study")
    private Collection<Series> seriesCollection;

    public Imagingstudy() {
    }

    public Imagingstudy(Integer idImagingStudy) {
        this.idImagingStudy = idImagingStudy;
    }

    public Imagingstudy(Integer idImagingStudy, String uid) {
        this.idImagingStudy = idImagingStudy;
        this.uid = uid;
    }

    public Integer getIdImagingStudy() {
        return idImagingStudy;
    }

    public void setIdImagingStudy(Integer idImagingStudy) {
        this.idImagingStudy = idImagingStudy;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getReason() {
        return reason;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Practitioner getInterpreter() {
        return interpreter;
    }

    public void setInterpreter(Practitioner interpreter) {
        this.interpreter = interpreter;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Practitioner getReferrer() {
        return referrer;
    }

    public void setReferrer(Practitioner referrer) {
        this.referrer = referrer;
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
        hash += (idImagingStudy != null ? idImagingStudy.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Imagingstudy)) {
            return false;
        }
        Imagingstudy other = (Imagingstudy) object;
        if ((this.idImagingStudy == null && other.idImagingStudy != null) || (this.idImagingStudy != null && !this.idImagingStudy.equals(other.idImagingStudy))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ulb.lisa.his.model.Imagingstudy[ idImagingStudy=" + idImagingStudy + " ]";
    }
    
}
