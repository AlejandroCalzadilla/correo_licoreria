package org.bebidas.modules.service.interfaces;

import java.util.List;
import java.util.Optional;

import org.bebidas.modules.usuarios.Rol;

public interface RolService extends GenericService<Rol, Long> {
    Optional<Rol> buscarPorNombre(String nombre);
    List<Rol> buscarPorEstado(boolean activo);
    List<Rol> buscarRolesConPermiso(String permiso);
}