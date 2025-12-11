package org.bebidas.dao.interfaces;

import org.bebidas.model.Compra;

import java.util.Date;
import java.util.List;

public interface CompraDAO extends GenericDAO<Compra, Long> {
    List<Compra> buscarPorProveedor(Long proveedorId);
      List<Compra> buscarPorRangoFechas(Date fechaInicio, Date fechaFin);
    List<Compra> buscarPorEstado(String estado);
}