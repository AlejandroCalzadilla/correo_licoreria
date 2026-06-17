package org.bebidas.modules.creditos;

import org.bebidas.core.util.GenericServiceImpl;
import org.bebidas.modules.creditos.repositories.PagoCuotaDAOImpl;
import org.bebidas.modules.pagos.PagoCuotaService;
import org.bebidas.modules.pagos.repostiories.PagoCuotaDAO;

public class PagoCuotaServiceImpl extends GenericServiceImpl<PagoCuota, Long> implements PagoCuotaService {

    public PagoCuotaServiceImpl() {
        super(new PagoCuotaDAOImpl());
    }
}