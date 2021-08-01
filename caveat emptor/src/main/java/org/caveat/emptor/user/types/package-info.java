@org.hibernate.annotations.TypeDefs({
        @org.hibernate.annotations.TypeDef(
                name = "monetary_amount_usd",
                typeClass = MonetaryAmount.class,
                parameters = {@org.hibernate.annotations.Parameter(name = "convertTo", value = "USD")}
        ),
        @org.hibernate.annotations.TypeDef(
                name = "monetary_amount_eur",
                typeClass = MonetaryAmount.class,
                parameters = {@org.hibernate.annotations.Parameter(name = "convertTo", value = "EUR")}
        )
})

package org.caveat.emptor.user.types;

import org.caveat.emptor.model.MonetaryAmount;