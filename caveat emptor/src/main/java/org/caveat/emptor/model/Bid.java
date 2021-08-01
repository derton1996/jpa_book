package org.caveat.emptor.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "BID")
@org.hibernate.annotations.Immutable
public class Bid implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private Item item;

    @Column(name = "price", length = 63)
    private MonetaryAmount amount;

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
