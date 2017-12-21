/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author Joan
 */
@Entity
@Table(name = "carril")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carril.findAll", query = "SELECT c FROM Carril c"),
    @NamedQuery(name = "Carril.findByIdcarril", query = "SELECT c FROM Carril c WHERE c.idcarril = :idcarril"),
    @NamedQuery(name = "Carril.findBySentido", query = "SELECT c FROM Carril c WHERE c.sentido = :sentido"),
    @NamedQuery(name = "Carril.findByEstado", query = "SELECT c FROM Carril c WHERE c.estado = :estado")})
public class Carril implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idcarril")
    private Integer idcarril;
    @Basic(optional = false)
    @Column(name = "sentido")
    private String sentido;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idcarril")
    private List<Factura> facturaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carrilIdcarril")
    private List<Informe> informeList;
    @JoinColumn(name = "nombre", referencedColumnName = "nombre")
    @ManyToOne(optional = false)
    private Estacion nombre;

    public Carril() {
    }

    public Carril(Integer idcarril) {
        this.idcarril = idcarril;
    }

    public Carril(Integer idcarril, String sentido, boolean estado) {
        this.idcarril = idcarril;
        this.sentido = sentido;
        this.estado = estado;
    }

    public Integer getIdcarril() {
        return idcarril;
    }

    public void setIdcarril(Integer idcarril) {
        this.idcarril = idcarril;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Factura> getFacturaList() {
        return facturaList;
    }

    public void setFacturaList(List<Factura> facturaList) {
        this.facturaList = facturaList;
    }

    @XmlTransient
    public List<Informe> getInformeList() {
        return informeList;
    }

    public void setInformeList(List<Informe> informeList) {
        this.informeList = informeList;
    }

    public Estacion getNombre() {
        return nombre;
    }

    public void setNombre(Estacion nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcarril != null ? idcarril.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carril)) {
            return false;
        }
        Carril other = (Carril) object;
        if ((this.idcarril == null && other.idcarril != null) || (this.idcarril != null && !this.idcarril.equals(other.idcarril))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Carril[ idcarril=" + idcarril + " ]";
    }
    
}
