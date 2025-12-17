package org.bebidas.modules.creditos.services;


import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.dao.impl.CreditoDAOImpl;
import org.bebidas.modules.service.CreditoService;

public class CreditoServiceImpl extends GenericServiceImpl<Credito, Long> implements CreditoService {

    public CreditoServiceImpl() {
        super(new CreditoDAOImpl());
    }

    
}