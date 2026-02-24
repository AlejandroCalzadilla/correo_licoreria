package org.bebidas.modules.creditos.services;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.creditos.repositories.CreditoDAOImpl;
import org.bebidas.modules.creditos.services.interfaces.CreditoService;

public class CreditoServiceImpl extends GenericServiceImpl<Credito, Long> implements CreditoService {

    public CreditoServiceImpl() {
        super(new CreditoDAOImpl());
    }

    @Override
    public List<Credito> buscarPorVenta(Long ventaId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorVenta'");
    }

    @Override
    public List<Credito> buscarPorEstado(String estado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorEstado'");
    }

    @Override
    public List<Credito> buscarPorCliente(Long clienteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorCliente'");
    }

    @Override
    public List<Credito> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorRangoFechas'");
    }

    @Override
    public BigDecimal obtenerSaldoPendientePorCliente(Long clienteId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerSaldoPendientePorCliente'");
    }

    @Override
    public List<Credito> buscarCreditosVencidos() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarCreditosVencidos'");
    }

    @Override
    public Credito generarCredito(Credito credito) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generarCredito'");
    }

    @Override
    public void registrarPago(Long creditoId, BigDecimal monto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarPago'");
    }

    @Override
    public void marcarComoVencido(Long creditoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'marcarComoVencido'");
    }

    

    
}