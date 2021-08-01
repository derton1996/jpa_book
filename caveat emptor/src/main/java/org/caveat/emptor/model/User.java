package org.caveat.emptor.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.StringTokenizer;

@Entity
@Table(name = "USERS")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "HOME_STREET", nullable = false)),
            @AttributeOverride(name = "zipcode", column = @Column(name = "HOME_ZIPCODE", nullable = false, length = 5)),
            @AttributeOverride(name = "city", column = @Column(name = "HOME_CITY", nullable = false)),
    })
    private Address homeAddress;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET", nullable = false)),
            @AttributeOverride(name = "zipcode", column = @Column(name = "BILLING_ZIPCODE", nullable = false, length = 5)),
            @AttributeOverride(name = "city", column = @Column(name = "BILLING_CITY", nullable = false)),
    })
    private Address billingAddress;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return firstname + " " + lastname;
    }

    public void setName(String name) {
        StringTokenizer t = new StringTokenizer(name);
        firstname = t.nextToken();
        lastname = t.nextToken();
    }

    public BigDecimal calcShippingCosts(Address fromLocation) {
        return null;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }
}
