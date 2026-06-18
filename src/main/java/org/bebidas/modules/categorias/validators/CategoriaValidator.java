package org.bebidas.modules.categorias.validators;

import org.bebidas.modules.categorias.Categoria;
import org.bebidas.modules.mail.crud_seleccion.ServiceProvider;

public class CategoriaValidator {

    public static String validarCrear(String[] params) {
        if (params.length < 1) {
            return "Se requieren: nombre,";
        }
        return null;
    }

    public static String validarActualizar(String[] params, ServiceProvider services) {
        if (params.length < 1) {
            return "Se requiere: id";
        }
        try {
            Long id = Long.parseLong(params[0]);
            Categoria categoria = services.getCategoriaService().findById(id).orElse(null);
            if (categoria == null) {
                return "Categoría no encontrada con ID: " + id;
            }
        } catch (NumberFormatException e) {
            return "Error: El ID de la categoría debe ser un número válido.";
        }
        return null;
    }
}
