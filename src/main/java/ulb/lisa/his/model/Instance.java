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
@Table(name = "instance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Instance.findAll", query = "SELECT i FROM Instance i"),
    @NamedQuery(name = "Instance.findByIdInstance", query = "SELECT i FROM Instance i WHERE i.idInstance = :idInstance"),
    @NamedQuery(name = "Instance.findByUid", query = "SELECT i FROM Instance i WHERE i.uid = :uid")})
public class Instance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idInstance")
    private Integer idInstance;
    @Basic(optional = false)
    @Column(name = "uid")
    private String uid;
    @JoinColumn(name = "series", referencedColumnName = "idSeries")
    @ManyToOne(optional = false)
    private Series series;

    public Instance() {
    }

    public Instance(Integer idInstance) {
        this.idInstance = idInstance;
    }

    public Instance(Integer idInstance, String uid) {
        this.idInstance = idInstance;
        this.uid = uid;
    }

    public Integer getIdInstance() {
        return idInstance;
    }

    public void setIdInstance(Integer idInstance) {
        this.idInstance = idInstance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInstance != null ? idInstance.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Instance)) {
            return false;
        }
        Instance other = (Instance) object;
        if ((this.idInstance == null && other.idInstance != null) || (this.idInstance != null && !this.idInstance.equals(other.idInstance))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ulb.lisa.his.model.Instance[ idInstance=" + idInstance + " ]";
    }
    
}
