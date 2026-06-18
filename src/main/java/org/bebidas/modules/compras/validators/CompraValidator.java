package org.bebidas.modules.compras.validators;

import org.bebidas.modules.compras.models.Compra;
import org.bebidas.modules.mail.crud_seleccion.ServiceProvider;

public class CompraValidator {

    public static String validarCrear(String[] params) {
        if (params.length < 2) {
            return "Se requieren: proveedorId, descripcion";
        }
        try {
            Long.parseLong(params[0]);
        } catch (NumberFormatException e) {
            return "Error: El proveedorId debe ser un número válido.";
        }
        return null;
    }

    public static String validarActualizar(String[] params, ServiceProvider services) {
        if (params.length < 1) {
            return "Se requiere: id";
        }
        try {
            Long id = Long.parseLong(params[0]);
            Compra compra = services.getCompraService().findById(id).orElse(null);
            if (compra == null) {
                return "Compra no encontrada con ID: " + id;
            }
            if (params.length > 1) {
                Long.parseLong(params[1]); // Validar formato de proveedorId
            }
        } catch (NumberFormatException e) {
            return "Error: Los IDs deben ser números válidos.";
        }
        return null;
    }
}
