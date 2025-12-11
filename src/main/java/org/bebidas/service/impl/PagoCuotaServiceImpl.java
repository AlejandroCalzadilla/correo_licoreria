package org.bebidas.service.impl;

import org.bebidas.dao.PagoCuotaDAO;
import org.bebidas.dao.impl.PagoCuotaDAOImpl;
import org.bebidas.model.PagoCuota;
import org.bebidas.service.PagoCuotaService;
import org.bebidas.service.impl.GenericServiceImpl;

public class PagoCuotaServiceImpl extends GenericServiceImpl<PagoCuota, Long> implements PagoCuotaService {

    public PagoCuotaServiceImpl() {
        super(new PagoCuotaDAOImpl());
    }
}