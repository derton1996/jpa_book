package org.caveat.emptor.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "BID")
@org.hibernate.annotations.Immutable
public class Bid implements Serializable {

    protected Item item;

    public Bid() {
    }

    public Bid(Item item) {
        this.item = item;
        item.getBids().add(this);
    }

    public Item getItem() {
        return item;
    }

    void setItem(Item item) {
        this.item = item;
    }
}
