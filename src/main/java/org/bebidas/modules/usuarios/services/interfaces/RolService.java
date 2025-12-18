package org.bebidas.modules.usuarios.services.interfaces;

import org.bebidas.modules.service.interfaces.GenericService;
import org.bebidas.modules.usuarios.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService extends GenericService<Rol, Long> {

    Optional<Rol> buscarPorNombre(String nombre);

    List<Rol> buscarPorEstado(boolean activo);

    List<Rol> buscarRolesConPermiso(String permiso);
}