package logica;

import modelo.Empleado;
import persistencia.EmpleadoJpaController;
import persistencia.exceptions.NonexistentEntityException;

public class EmpleadoLogica {

    private EmpleadoJpaController EmpleadoDao = new EmpleadoJpaController();

    public Empleado buscarEmpleado(Empleado empleado) {
        return EmpleadoDao.findEmpleado(empleado.getCedula());
    }

    public void registarEmpleado(Empleado empleado) throws Exception {
        EmpleadoDao.create(empleado);
    }

    public void actualizarEmpleado(Empleado empleado) throws NonexistentEntityException, Exception {
        EmpleadoDao.edit(empleado);
    }

}
