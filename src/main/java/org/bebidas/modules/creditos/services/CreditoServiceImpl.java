package org.bebidas.modules.creditos.services;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.creditos.repositories.CreditoDAOImpl;

public class CreditoServiceImpl extends GenericServiceImpl<Credito, Long> {

    public CreditoServiceImpl() {
        super(new CreditoDAOImpl());
    }

}