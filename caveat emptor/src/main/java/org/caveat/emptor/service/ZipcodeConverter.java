package org.caveat.emptor.service;

import org.caveat.emptor.model.GermanZipcode;
import org.caveat.emptor.model.SwissZipcode;
import org.caveat.emptor.model.Zipcode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ZipcodeConverter implements AttributeConverter<Zipcode, String> {
    @Override
    public String convertToDatabaseColumn(Zipcode zipcode) {
        return zipcode.getValue();
    }

    @Override
    public Zipcode convertToEntityAttribute(String s) {
        if (s.length() == 5) {
            return new GermanZipcode(s);
        } else if (s.length() == 4) {
            return new SwissZipcode(s);
        }
        throw new IllegalArgumentException("Unsupported zipcode in database: " + s);
    }
}
