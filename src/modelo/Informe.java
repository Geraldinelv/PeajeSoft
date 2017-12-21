/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Joan
 */
@Entity
@Table(name = "informe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Informe.findAll", query = "SELECT i FROM Informe i"),
    @NamedQuery(name = "Informe.findByIdinforme", query = "SELECT i FROM Informe i WHERE i.idinforme = :idinforme"),
    @NamedQuery(name = "Informe.findByFecha", query = "SELECT i FROM Informe i WHERE i.fecha = :fecha"),
    @NamedQuery(name = "Informe.findByHora", query = "SELECT i FROM Informe i WHERE i.hora = :hora"),
    @NamedQuery(name = "Informe.findByPlaca", query = "SELECT i FROM Informe i WHERE i.placa = :placa")})
public class Informe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idinforme")
    private Integer idinforme;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "hora")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Column(name = "placa")
    private String placa;
    @JoinColumn(name = "carril_idcarril", referencedColumnName = "idcarril")
    @ManyToOne(optional = false)
    private Carril carrilIdcarril;

    public Informe() {
    }

    public Informe(Integer idinforme) {
        this.idinforme = idinforme;
    }

    public Integer getIdinforme() {
        return idinforme;
    }

    public void setIdinforme(Integer idinforme) {
        this.idinforme = idinforme;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Carril getCarrilIdcarril() {
        return carrilIdcarril;
    }

    public void setCarrilIdcarril(Carril carrilIdcarril) {
        this.carrilIdcarril = carrilIdcarril;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idinforme != null ? idinforme.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Informe)) {
            return false;
        }
        Informe other = (Informe) object;
        if ((this.idinforme == null && other.idinforme != null) || (this.idinforme != null && !this.idinforme.equals(other.idinforme))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Informe[ idinforme=" + idinforme + " ]";
    }
    
}
