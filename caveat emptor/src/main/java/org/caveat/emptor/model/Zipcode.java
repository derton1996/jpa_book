package org.caveat.emptor.model;

import javax.persistence.Embeddable;

@Embeddable
public abstract class Zipcode {

    private final String  value;

    public Zipcode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zipcode zipcode = (Zipcode) o;
        return value.equals(zipcode.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
