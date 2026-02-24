package org.bebidas.modules.compras.repositories.interfaces;

import java.util.Date;
import java.util.List;

import org.bebidas.core.util.GenericDAO;
import org.bebidas.modules.compras.Compra;

public interface CompraDAO extends GenericDAO<Compra, Long> {
    List<Compra> buscarPorProveedor(Long proveedorId);
      List<Compra> buscarPorRangoFechas(Date fechaInicio, Date fechaFin);
    List<Compra> buscarPorEstado(String estado);
}