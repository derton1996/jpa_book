package org.caveat.emptor;

import org.caveat.emptor.model.Item;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ModelOperations {

    @Test
    public void testItemFieldsValidation() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Item item = new Item();
        item.setName("Some Item");
        item.setAuctionEnd(new Date());

        Set<ConstraintViolation<Item>> violations = validator.validate(item);
        assertEquals(1, violations.size());

        ConstraintViolation violation = violations.iterator().next();
        String failedPropertyName = violation.getPropertyPath().iterator().next().getName();

        assertEquals(failedPropertyName, "auctionEnd");

    }

}
