
package logica;

import modelo.Informe;
import persistencia.InformeJpaController;
import persistencia.exceptions.NonexistentEntityException;


public class InformeLogica {
    
    private InformeJpaController InformeDAO = new InformeJpaController();
    
   public  Informe buscarEmpleado(Informe informe){
    return InformeDAO.findInforme(informe.getIdinforme());
   }
   
   public void registarInforme(Informe informe) throws Exception{
       InformeDAO.create(informe);
   }
   
   public void actualizarInforme(Informe informe) throws NonexistentEntityException, Exception{
       InformeDAO.edit(informe);
   }
   
}
