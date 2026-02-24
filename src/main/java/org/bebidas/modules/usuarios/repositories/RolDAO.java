package org.bebidas.modules.usuarios.repositories;

import java.util.List;
import java.util.Optional;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.usuarios.Rol;

public interface RolDAO extends GenericDAO<Rol, Long> {
    Optional<Rol> buscarPorNombre(String nombre);
    List<Rol> buscarPorEstado(boolean activo);
    List<Rol> buscarRolesConPermiso(String permiso);
}
