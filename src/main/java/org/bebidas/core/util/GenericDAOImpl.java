package org.bebidas.core.util;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

    protected final Class<T> entityClass;

    protected GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // Métodos abstractos que cada DAO específico debe implementar
    // ya que cada tabla tiene columnas diferentes
    
    @Override
    public abstract Optional<T> findById(ID id);

    @Override
    public abstract List<T> findAll();

    @Override
    public abstract T save(T entity);

    @Override
    public abstract void delete(ID id);

    @Override
    public abstract boolean existsById(ID id);
}
