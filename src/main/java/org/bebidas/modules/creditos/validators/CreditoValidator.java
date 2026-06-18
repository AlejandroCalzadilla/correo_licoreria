package org.bebidas.modules.creditos.validators;

import org.bebidas.modules.creditos.Credito;
import org.bebidas.modules.mail.crud_seleccion.ServiceProvider;

public class CreditoValidator {

    public static String validarActualizar(String[] params, ServiceProvider services) {
        if (params.length < 1) {
            return "Se requiere: id";
        }
        try {
            Long id = Long.parseLong(params[0]);
            Credito credito = services.getCreditoService().findById(id).orElse(null);
            if (credito == null) {
                return "Crédito no encontrado con ID: " + id;
            }
            if (params.length > 1) {
                Long.parseLong(params[1]); // Validar formato de ventaId
            }
        } catch (NumberFormatException e) {
            return "Error: Los IDs deben ser números válidos.";
        }
        return null;
    }
}
