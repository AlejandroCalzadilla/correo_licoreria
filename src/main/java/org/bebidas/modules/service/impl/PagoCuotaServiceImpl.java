package org.bebidas.modules.service.impl;

import org.bebidas.modules.dao.impl.PagoCuotaDAOImpl;
import org.bebidas.modules.dao.interfaces.PagoCuotaDAO;
import org.bebidas.modules.model.PagoCuota;
import org.bebidas.modules.service.PagoCuotaService;
import org.bebidas.modules.service.impl.GenericServiceImpl;

public class PagoCuotaServiceImpl extends GenericServiceImpl<PagoCuota, Long> implements PagoCuotaService {

    public PagoCuotaServiceImpl() {
        super(new PagoCuotaDAOImpl());
    }
}