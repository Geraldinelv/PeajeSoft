
package logica;

import java.util.List;
import modelo.Factura;
import modelo.Informe;
import persistencia.FacturaJpaController;
import persistencia.InformeJpaController;
import persistencia.exceptions.NonexistentEntityException;

public class FacturaLogica {
    private FacturaJpaController FacturaDAO = new FacturaJpaController();
    
   public Factura buscarFactura(Factura factura){
    return FacturaDAO.findFactura(factura.getIdfactura());
   }
   
   public List<Factura> consultarTodos() throws Exception{
        return FacturaDAO.findFacturaEntities();
    }
   public void registarFactura(Factura factura) throws Exception{
       FacturaDAO.create(factura);
   }
   
   public void actualizarFactura(Factura factura) throws NonexistentEntityException, Exception{
       FacturaDAO.edit(factura);
   }
}
