package org.bebidas.modules.service.impl;


import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.dao.impl.CreditoDAOImpl;
import org.bebidas.modules.service.CreditoService;
import org.bebidas.modules.service.impl.GenericServiceImpl;

public class CreditoServiceImpl extends GenericServiceImpl<Credito, Long> implements CreditoService {

    public CreditoServiceImpl() {
        super(new CreditoDAOImpl());
    }
}