package org.bebidas.modules.service.impl;

import org.bebidas.modules.dao.interfaces.GenericDAO;
import org.bebidas.modules.service.interfaces.GenericService;

import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {
    
    protected final GenericDAO<T, ID> dao;
    
    protected GenericServiceImpl(GenericDAO<T, ID> dao) {
        this.dao = dao;
    }
    
    @Override
    public List<T> findAll() {
        return dao.findAll();
    }
    
    @Override
    public Optional<T> findById(ID id) {
        return dao.findById(id);
    }
    
    @Override
    public T save(T entity) {
        return dao.save(entity);
    }
    
    @Override
    public void delete(ID id) {
        dao.delete(id);
    }
    
    @Override
    public boolean existsById(ID id) {
        return dao.existsById(id);
    }
}
