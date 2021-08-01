package org.caveat.emptor.model;

import com.sun.istack.internal.NotNull;
import org.caveat.emptor.service.MonetaryAmountConverter;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Size(
            min = 2,
            max = 255,
            message = "Name is required, maximum 255 characters."
    )
    private String name;

    @Future
    private Date auctionEnd;

    /**
     * @Convert не обязяательна, так как в {@link MonetaryAmountConverter} уже выставлен autoApply
     */
    @Convert(converter = MonetaryAmountConverter.class, disableConversion = false)
    @org.hibernate.annotations.Type(type = "monetary_amount_usd")
    @org.hibernate.annotations.Columns(columns = {
            @Column( name="buy_now_price", nullable = false, length = 2),
            @Column( name="buy_now_price_currency", nullable = false, length = 3)
    })
    private MonetaryAmount buyNowPrice;

    @org.hibernate.annotations.Type(type = "monetary_amount_eur")
    @org.hibernate.annotations.Columns(columns = {
            @Column( name="initial_price", nullable = false, length = 2),
            @Column( name="initial_price_currency", nullable = false, length = 3)
    })
    private MonetaryAmount initialPrice;

    private Set<Bid> bids = new HashSet<>();

    public Set<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        if (bid == null) {
            throw new NullPointerException("Can't add null Bid");
        }
        if (bid.getItem() != null) {
            throw new IllegalStateException("Bid is already assigned to an Item");
        }
        getBids().add(bid);
        bid.setItem(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(Date auctionEnd) {
        this.auctionEnd = auctionEnd;
    }
}
