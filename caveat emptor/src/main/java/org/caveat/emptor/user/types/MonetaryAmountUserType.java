package org.caveat.emptor.user.types;

import org.caveat.emptor.model.MonetaryAmount;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Properties;

/**
 * Выполняется в моменты загрузки и сохранения значений
 */
public class MonetaryAmountUserType implements CompositeUserType, DynamicParameterizedType {

    private Currency convertTo;

    @Override
    public Class returnedClass() {
        return MonetaryAmount.class;
    }

    /**
     * Определение факта изменения и необходимости записи в базу
     */
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y || !(x == null || y == null) && x.equals(y);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return 0;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet,
                              String[] names,
                              SharedSessionContractImplementor sharedSessionContractImplementor,
                              Object o) throws HibernateException, SQLException {
        BigDecimal amount = resultSet.getBigDecimal(names[0]);
        if (resultSet.wasNull()) return null;
        Currency currency = Currency.getInstance(resultSet.getString(names[1]));
        return new MonetaryAmount(amount, currency);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement,
                            Object value,
                            int index,
                            SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (value == null) {
            preparedStatement.setNull(index, StandardBasicTypes.BIG_DECIMAL.sqlType());
            preparedStatement.setNull(index + 1, StandardBasicTypes.CURRENCY.sqlType());
        } else {
            MonetaryAmount amount = (MonetaryAmount) value;
            MonetaryAmount dbAmount = convert(amount, convertTo);
            preparedStatement.setBigDecimal(index, dbAmount.getValue());
            preparedStatement.setString(index + 1, convertTo.getCurrencyCode());
        }
    }

    private MonetaryAmount convert(MonetaryAmount amount, Currency toCurrency) {
        return new MonetaryAmount(amount.getValue().multiply(new BigDecimal(2)), toCurrency);
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        return o;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException {
        return o.toString();
    }

    @Override
    public Object assemble(Serializable serializable, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return MonetaryAmount.fromString((String) serializable);
    }

    /**
     * Возвращает объект после слияния в {@link javax.persistence.EntityManager#merge(Object)}
     * <p>
     * В данном случае объект неизменяемый поэтому возвращаем оригинал
     */
    @Override
    public Object replace(Object original, Object target, SharedSessionContractImplementor sharedSessionContractImplementor, Object o2) throws HibernateException {
        return original;
    }

    @Override
    public void setParameterValues(Properties properties) {
        ParameterType parameterType = (ParameterType) properties.get(PARAMETER_TYPE);
        String[] colums = parameterType.getColumns();
        String table = parameterType.getTable();
        Annotation[] annotations = parameterType.getAnnotationsMethod();

        String convertToParameter = properties.getProperty("convertTo");
        this.convertTo = Currency.getInstance(convertToParameter != null ? convertToParameter : "USD");
    }

    //методы чтобы Hibernate интегрировал MonetaryAmount в механизм запросов
    @Override
    public String[] getPropertyNames() {
        return new String[]{"value", "currency"};
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{StandardBasicTypes.BIG_DECIMAL, StandardBasicTypes.CURRENCY};
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        MonetaryAmount monetaryAmount = (MonetaryAmount) component;
        if (property == 0) {
            return monetaryAmount.getValue();
        } else {
            return monetaryAmount.getCurrency();
        }
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        throw new UnsupportedOperationException("MonetaryAmount is immutable");
    }
}
