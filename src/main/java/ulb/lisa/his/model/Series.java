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
@Table(name = "series")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Series.findAll", query = "SELECT s FROM Series s"),
    @NamedQuery(name = "Series.findByIdSeries", query = "SELECT s FROM Series s WHERE s.idSeries = :idSeries"),
    @NamedQuery(name = "Series.findByUid", query = "SELECT s FROM Series s WHERE s.uid = :uid"),
    @NamedQuery(name = "Series.findByModality", query = "SELECT s FROM Series s WHERE s.modality = :modality")})
public class Series implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "series")
    private Collection<Instance> instanceCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idSeries")
    private Integer idSeries;
    @Basic(optional = false)
    @Column(name = "uid")
    private String uid;
    @Column(name = "modality")
    private String modality;
    @Lob
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "performer", referencedColumnName = "idPractitioner")
    @ManyToOne
    private Practitioner performer;
    @JoinColumn(name = "study", referencedColumnName = "idImagingStudy")
    @ManyToOne(optional = false)
    private Imagingstudy study;

    public Series() {
    }

    public Series(Integer idSeries) {
        this.idSeries = idSeries;
    }

    public Series(Integer idSeries, String uid) {
        this.idSeries = idSeries;
        this.uid = uid;
    }

    public Integer getIdSeries() {
        return idSeries;
    }

    public void setIdSeries(Integer idSeries) {
        this.idSeries = idSeries;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Practitioner getPerformer() {
        return performer;
    }

    public void setPerformer(Practitioner performer) {
        this.performer = performer;
    }

    public Imagingstudy getStudy() {
        return study;
    }

    public void setStudy(Imagingstudy study) {
        this.study = study;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSeries != null ? idSeries.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Series)) {
            return false;
        }
        Series other = (Series) object;
        if ((this.idSeries == null && other.idSeries != null) || (this.idSeries != null && !this.idSeries.equals(other.idSeries))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ulb.lisa.his.model.Series[ idSeries=" + idSeries + " ]";
    }

    @XmlTransient
    public Collection<Instance> getInstanceCollection() {
        return instanceCollection;
    }

    public void setInstanceCollection(Collection<Instance> instanceCollection) {
        this.instanceCollection = instanceCollection;
    }
    
}
