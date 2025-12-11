package org.bebidas.service.impl;

import org.bebidas.dao.CreditoDAO;
import org.bebidas.dao.impl.CreditoDAOImpl;
import org.bebidas.model.Credito;
import org.bebidas.service.CreditoService;
import org.bebidas.service.impl.GenericServiceImpl;

public class CreditoServiceImpl extends GenericServiceImpl<Credito, Long> implements CreditoService {

    public CreditoServiceImpl() {
        super(new CreditoDAOImpl());
    }
}