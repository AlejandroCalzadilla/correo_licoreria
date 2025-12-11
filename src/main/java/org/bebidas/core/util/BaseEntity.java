package org.bebidas.core.util;

import java.io.Serializable;

public abstract class BaseEntity implements Serializable {
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseEntity other = (BaseEntity) obj;
        return id != null && id.equals(other.id);
    }
}
