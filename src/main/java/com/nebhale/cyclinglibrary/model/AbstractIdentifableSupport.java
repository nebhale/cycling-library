
package com.nebhale.cyclinglibrary.model;

abstract class AbstractIdentifableSupport implements Identifiable {

    private final long id;

    protected AbstractIdentifableSupport(long id) {
        this.id = id;
    }

    @Override
    public final long getId() {
        return this.id;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Identifiable other = (Identifiable) obj;
        if (id != other.getId()) {
            return false;
        }
        return true;
    }

}
