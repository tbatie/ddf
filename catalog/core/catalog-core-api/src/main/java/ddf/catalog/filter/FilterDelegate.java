/**
 * Copyright (c) Codice Foundation
 * <p/>
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package ddf.catalog.filter;

import org.opengis.filter.*;
import org.opengis.filter.spatial.Beyond;

import java.util.Date;
import java.util.List;


/***
 * FilterDelegate is the target delegate of the {@link FilterAdapter}. The FilterAdapter will visit
 * a {@link org.opengis.filter.Filter} and call the corresponding delegate methods after type conversion and input
 * normalization.
 * <p>
 * Not all org.opengis.filter.Filter and {@link org.opengis.filter.expression.Expression} types are supported by the delegate. FilterAdapter and
 * FilterDelegate would need to be extended to support other org.opengis.filter.Filter and org.opengis.filter.expression.Expression types.
 * Alternatively, other types could be supported by implementing the {@link org.opengis.filter.FilterVisitor} directly.
 * <p>
 * A reference implementation is provided with the DDF Core in the org.opengis.filter.Filter Proxy bundle.
 *
 * @author ddf.isgs@lmco.com
 *
 * @param <T>
 *            Generic type that the FilterDelegate will return as a final result
 *
 * @see FilterAdapter
 */
public abstract class FilterDelegate<T> {

    /**
     * Normalized wildcard character used to match zero or more characters.
     */
    public static final String WILDCARD_CHAR = "*";

    /**
     * Normalized single wildcard character used to match exactly one character
     */
    public static final String SINGLE_CHAR = "?";

    /**
     * Normalized escape character used to escape the meaning of the wildCard, singleChar, and the
     * escapeChar itself
     */
    public static final String ESCAPE_CHAR = "\\";

    // Logical operators

    /**
     * Logical "and" operation on a list of operands.
     *
     * @param operands
     *            list of operands to "and"
     * @return result of "and" operation on operands
     */
    public T and(List<T> operands) {
        throw new UnsupportedOperationException("And filter not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Logical "or" operation on a list of operands.
     *
     * @param operands
     *            list of T to "or"
     * @return result of "or" operation on operands
     */
    public T or(List<T> operands) {
        throw new UnsupportedOperationException("Or filter not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Returns a sorted list of the nearest neighbors to a property for a given WKT.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of nearest neighbor spatial operation
     */
    public T nearestNeighbor(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "Nearest neighbor filter not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Logical "not" operation on an operand.
     *
     * @param operand
     *            operand to negate
     * @return result of "not" operation on operand
     */
    public T not(T operand) {
        throw new UnsupportedOperationException("Not filter not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Include filter is equivalent to no filtering or logically {@code true}.
     *
     * @return returns the equivalent of true
     */
    public T include() {
        throw new UnsupportedOperationException("include() not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Include filter is equivalent to filter all or logically {@code false}.
     *
     * @return returns the equivalent of false
     */
    public T exclude() {
        throw new UnsupportedOperationException("exclude() not supported by org.opengis.filter.Filter Delegate.");
    }

    // Comparison operators

    // PropertyIsEqualTo

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @param isCaseSensitive
     *            case-sensitivity boolean
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, String literal, boolean isCaseSensitive) {
        return propertyIsEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, Date literal) {
       return propertyIsEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param startDate
     *            starting date of literal to compare
     * @param endDate
     *            ending date of literal to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, Date startDate, Date endDate) {
        return propertyIsEqualTo(propertyName, (Object)startDate);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, int literal) {
        return propertyIsEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, short literal) {
        return propertyIsEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, long literal) {
        return propertyIsEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, float literal) {
        return propertyIsEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, double literal) {
        return propertyIsEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, Number literal) {
            return propertyIsEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, boolean literal) {
        return propertyIsEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsEqualTo(String, Object)
     */
    public T propertyIsEqualTo(String propertyName, byte[] literal) {
        return propertyIsEqualTo(propertyName, (Object)literal);
    }

    /**
     * Compares the value associated with a property is equal to the value of a literal.
     * <p>
     * {@code propertyName == literal}
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of equals operation between {@code propertyName} and {@code literal}
     */
    public T propertyIsEqualTo(String propertyName, Object literal) {
        throw new UnsupportedOperationException(
                "propertyIsEqualTo(String,Object) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIsEqualTo(String xpath, PropertyIsEqualTo filter) {
        return propertyIsEqualTo(xpath, filter.getExpression1());
    }

    // PropertyIsNotEqualTo

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @param isCaseSensitive
     *            case-sensitivity boolean
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, String literal, boolean isCaseSensitive) {
        return propertyIsNotEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, Date literal) {
        return propertyIsNotEqualTo(propertyName, (Object) literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param startDate
     *            starting date of literal to compare
     * @param endDate
     *            ending date of literal to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, Date startDate, Date endDate) {
        return propertyIsNotEqualTo(propertyName, (Object)startDate);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, int literal) {
        return propertyIsNotEqualTo(propertyName, (Number) literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, short literal) {
        return propertyIsNotEqualTo(propertyName, (Number) literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, long literal) {
        return propertyIsNotEqualTo(propertyName, (Number) literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, float literal) {
        return propertyIsNotEqualTo(propertyName, (Number) literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, double literal) {
        return propertyIsNotEqualTo(propertyName, (Number) literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, boolean literal) {
        return propertyIsNotEqualTo(propertyName, (Object) literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, Number literal) {
        return propertyIsNotEqualTo(propertyName, (Object) literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsNotEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsNotEqualTo(String, Object)
     */
    public T propertyIsNotEqualTo(String propertyName, byte[] literal) {
        return propertyIsNotEqualTo(propertyName, (Object) literal);
    }

    /**
     * Compares the value associated with a property is not equal to the value of a literal.
     *
     * <p>
     * {@code propertyName != literal}
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     */
    public T propertyIsNotEqualTo(String propertyName, Object literal) {
        throw new UnsupportedOperationException(
                "propertyIsNotEqualTo(String,Object) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the value associated with a property is not equal to the value of a literal.
     *
     * <p>
     * {@code propertyName != literal}
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of not equals operation between {@code propertyName} and {@code literal}
     */
    public T xpathIsNotEqualTo(String xpath, PropertyIsNotEqualTo filter) {
        return propertyIsNotEqualTo(xpath, filter.getExpression1());
    }

    // PropertyIsGreaterThan

    /***
     * See {@link FilterDelegate#propertyIsGreaterThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThan(String, Object)
     */
    public T propertyIsGreaterThan(String propertyName, String literal) {
        throw new UnsupportedOperationException(
                "propertyIsGreaterThan(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThan(String, Object)
     */
    public T propertyIsGreaterThan(String propertyName, Date literal) {
        throw new UnsupportedOperationException(
                "propertyIsGreaterThan(String,Date) not supported by org.opengis.filter.Filter Delegate.");
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThan(String, Object)
     */
    public T propertyIsGreaterThan(String propertyName, int literal) {
        throw new UnsupportedOperationException(
                "propertyIsGreaterThan(String,int) not supported by org.opengis.filter.Filter Delegate.");
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThan(String, Object)
     */
    public T propertyIsGreaterThan(String propertyName, short literal) {
        throw new UnsupportedOperationException(
                "propertyIsGreaterThan(String,short) not supported by org.opengis.filter.Filter Delegate.");
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThan(String, Object)
     */
    public T propertyIsGreaterThan(String propertyName, long literal) {
        throw new UnsupportedOperationException(
                "propertyIsGreaterThan(String,long) not supported by org.opengis.filter.Filter Delegate.");
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThan(String, Object)
     */
    public T propertyIsGreaterThan(String propertyName, float literal) {
        throw new UnsupportedOperationException(
                "propertyIsGreaterThan(String,float) not supported by org.opengis.filter.Filter Delegate.");
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThan(String, Object)
     */
    public T propertyIsGreaterThan(String propertyName, double literal) {
        throw new UnsupportedOperationException(
                "propertyIsGreaterThan(String,double) not supported by org.opengis.filter.Filter Delegate.");
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThan(String, Object)
     */
    public T propertyIsGreaterThan(String propertyName, Number literal) {
        return propertyIsGreaterThan(propertyName, (Object)literal);
    }

    /**
     * Compares the value associated with a property is greater than the value of a literal.
     * <p>
     * {@code propertyName > literal}
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     */
    public T propertyIsGreaterThan(String propertyName, Object literal) {
        throw new UnsupportedOperationException(
                "propertyIsGreaterThan(String,Object) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIsGreaterThan(String xpath, PropertyIsGreaterThan filter){
        return propertyIsGreaterThan(xpath, filter.getExpression1());
    }

    // PropertyIsGreaterThanOrEqualTo

    /***
     * See {@link FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)} .
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, String literal) {
        return propertyIsGreaterThanOrEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)} .
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, Date literal) {
        return propertyIsGreaterThanOrEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)} .
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, int literal) {
        return propertyIsGreaterThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)} .
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, short literal) {
        return propertyIsGreaterThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)} .
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, long literal) {
        return propertyIsGreaterThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)} .
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, float literal) {
        return propertyIsGreaterThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)} .
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, double literal) {
        return propertyIsGreaterThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)} .
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsGreaterThanOrEqualTo(String, Object)
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, Number literal) {
        return propertyIsGreaterThanOrEqualTo(propertyName, (Object)literal);
    }

    /**
     * Compares the value associated with a property is greater than or equal to the value of a
     * literal.
     * <p>
     * {@code propertyName >= literal}
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of greater than operation between {@code propertyName} and {@code literal}
     */
    public T propertyIsGreaterThanOrEqualTo(String propertyName, Object literal) {
        throw new UnsupportedOperationException(
                "propertyIsLessThanOrEqualTo(String,Object) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIsGreaterThanOrEqualTo(String xpath, PropertyIsGreaterThanOrEqualTo filter) {
        return propertyIsGreaterThanOrEqualTo(xpath, filter.getExpression1());
    }

    // PropertyIsLessThan

    /***
     * See {@link FilterDelegate#propertyIsLessThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThan(String, Object)
     */
    public T propertyIsLessThan(String propertyName, String literal) {
        return propertyIsLessThan(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThan(String, Object)
     */
    public T propertyIsLessThan(String propertyName, Date literal) {
        return propertyIsLessThan(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThan(String, Object)
     */
    public T propertyIsLessThan(String propertyName, int literal) {
        return propertyIsLessThan(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThan(String, Object)
     */
    public T propertyIsLessThan(String propertyName, short literal) {
        return propertyIsLessThan(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThan(String, Object)
     */
    public T propertyIsLessThan(String propertyName, long literal) {
        return propertyIsLessThan(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThan(String, Object)
     */
    public T propertyIsLessThan(String propertyName, float literal) {
        return propertyIsLessThan(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThan(String, Object)
     */
    public T propertyIsLessThan(String propertyName, double literal) {
        return propertyIsLessThan(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThan(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThan(String, Object)
     */
    public T propertyIsLessThan(String propertyName, Number literal) {
        return propertyIsLessThan(propertyName, (Object)literal);
    }

    /**
     * Compares the value associated with a property is less than the value of a literal.
     * <p>
     * {@code propertyName < literal}
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than operation between {@code propertyName} and {@code literal}
     */
    public T propertyIsLessThan(String propertyName, Object literal) {
        throw new UnsupportedOperationException(
                "propertyIsLessThan(String,Object) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIsLessThan(String xpath, PropertyIsLessThan filter){
        return propertyIsLessThan(xpath, filter.getExpression1());
    }

    // PropertyIsLessThanOrEqualTo

    /***
     * See {@link FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, String literal) {
        return propertyIsLessThanOrEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, Date literal) {
        return propertyIsLessThanOrEqualTo(propertyName, (Object)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, int literal) {
        return propertyIsLessThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, short literal) {
        return propertyIsLessThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, long literal) {
        return propertyIsLessThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, float literal) {
        return propertyIsLessThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, double literal) {
        return propertyIsLessThanOrEqualTo(propertyName, (Number)literal);
    }

    /***
     * See {@link FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     *
     * @see FilterDelegate#propertyIsLessThanOrEqualTo(String, Object)
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, Number literal) {
        return propertyIsLessThanOrEqualTo(propertyName, (Object)literal);
    }

    /**
     * Compares the value associated with a property is less than or equal to the value of a
     * literal.
     * <p>
     * {@code propertyName <= literal}
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of less than or equal to operation between {@code propertyName} and
     *         {@code literal}
     */
    public T propertyIsLessThanOrEqualTo(String propertyName, Object literal) {
        throw new UnsupportedOperationException(
                "propertyIsLessThanOrEqualTo(String,Object) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIsLessThanOrEqualTo(String xpath, PropertyIsLessThanOrEqualTo filter) {
        return propertyIsLessThanOrEqualTo(xpath, filter.getExpression1());
    }

    // PropertyIsBetween

    /***
     * See {@link FilterDelegate#propertyIsBetween(String, Object, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     *
     * @see FilterDelegate#propertyIsBetween(String, Object, Object)
     */
    public T propertyIsBetween(String propertyName, String lowerBoundary, String upperBoundary) {
        return propertyIsBetween(propertyName, (Object)lowerBoundary, (Object)upperBoundary);
    }

    /***
     * See {@link FilterDelegate#propertyIsBetween(String, Object, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     *
     * @see FilterDelegate#propertyIsBetween(String, Object, Object)
     */
    public T propertyIsBetween(String propertyName, Date lowerBoundary, Date upperBoundary) {
        return propertyIsBetween(propertyName, (Object)lowerBoundary, (Object)upperBoundary);
    }

    /***
     * See {@link FilterDelegate#propertyIsBetween(String, Object, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     *
     * @see FilterDelegate#propertyIsBetween(String, Object, Object)
     */
    public T propertyIsBetween(String propertyName, int lowerBoundary, int upperBoundary) {
        return propertyIsBetween(propertyName, (Number)lowerBoundary, (Number)upperBoundary);
    }

    /***
     * See {@link FilterDelegate#propertyIsBetween(String, Object, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     *
     * @see FilterDelegate#propertyIsBetween(String, Object, Object)
     */
    public T propertyIsBetween(String propertyName, short lowerBoundary, short upperBoundary) {
        return propertyIsBetween(propertyName, (Number)lowerBoundary, (Number)upperBoundary);
    }

    /***
     * See {@link FilterDelegate#propertyIsBetween(String, Object, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     *
     * @see FilterDelegate#propertyIsBetween(String, Object, Object)
     */
    public T propertyIsBetween(String propertyName, long lowerBoundary, long upperBoundary) {
        return propertyIsBetween(propertyName, (Number)lowerBoundary, (Number)upperBoundary);
    }

    /***
     * See {@link FilterDelegate#propertyIsBetween(String, Object, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     *
     * @see FilterDelegate#propertyIsBetween(String, Object, Object)
     */
    public T propertyIsBetween(String propertyName, float lowerBoundary, float upperBoundary) {
        return propertyIsBetween(propertyName, (Number)lowerBoundary, (Number)upperBoundary);
    }

    /***
     * See {@link FilterDelegate#propertyIsBetween(String, Object, Object)}.
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     *
     * @see FilterDelegate#propertyIsBetween(String, Object, Object)
     */
    public T propertyIsBetween(String propertyName, double lowerBoundary, double upperBoundary) {
        return propertyIsBetween(propertyName, (Number)lowerBoundary, (Number)upperBoundary);
    }

    /**
     * Compares the value associated with a property is between a lower and upper boundary. This is
     * an exclusive comparison.
     * <p>
     * {@code lower < propertyName < upper}
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     */
    public T propertyIsBetween(String propertyName, Number lowerBoundary, Number upperBoundary) {
        return propertyIsBetween(propertyName, (Object)lowerBoundary, (Object)upperBoundary);
    }

    /**
     * Compares the value associated with a property is between a lower and upper boundary. This is
     * an exclusive comparison.
     * <p>
     * {@code lower < propertyName < upper}
     *
     * @param propertyName
     *            name of property to compare
     * @param lowerBoundary
     *            lower boundary to compare
     * @param upperBoundary
     *            upper boundary to compare
     * @return result of between operation
     */
    public T propertyIsBetween(String propertyName, Object lowerBoundary, Object upperBoundary) {
        throw new UnsupportedOperationException(
                "propertyIsBetween(String,Object,Object) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIsBetween(String xpath, Object lowerBoundary, Object upperBoundary) {
        return propertyIsBetween(xpath, lowerBoundary, upperBoundary);
    }

    // Additional PropertyTypes

    /**
     * Compares the value associated with a property is equal to {@code null}.
     * <p>
     * {@code propertyName == null}
     *
     * @param propertyName
     *            name of property to compare
     * @return result of null check
     */
    public T propertyIsNull(String propertyName) {
        throw new UnsupportedOperationException(
                "propertyIsNull(String) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIsNull(String xpath) {
        return propertyIsNull(xpath);
    }

    /**
     * Compares the value associated with a property to a regular expression pattern.
     * <p>
     * Regular expression special characters are defined as {@link FilterDelegate#WILDCARD_CHAR
     * WILDCARD_CHAR}, {@link FilterDelegate#SINGLE_CHAR SINGLE_CHAR}, and
     * {@link FilterDelegate#ESCAPE_CHAR ESCAPE_CHAR}.
     *
     * @param propertyName
     *            name of property to compare
     * @param pattern
     *            regular expression pattern to match
     * @param isCaseSensitive
     *            case-sensitivity boolean
     * @return result of regular expression operation
     */
    public T propertyIsLike(String propertyName, String pattern, boolean isCaseSensitive) {
        throw new UnsupportedOperationException(
                "propertyIsLike(String,String,boolean) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIsLike(String xpath, String pattern, boolean isCaseSensitive) {
        return propertyIsLike(xpath, pattern, isCaseSensitive);
    }

    /**
     * Compares the value associated with a property to the value of a literal with a fuzzy operator
     * which expands the literal to match misspellings.
     *
     * @param propertyName
     *            name of property to compare
     * @param literal
     *            value to compare
     * @return result of fuzzy operation
     */
    public T propertyIsFuzzy(String propertyName, String literal) {
        throw new UnsupportedOperationException(
                "propertyIsFuzzy(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the node expressed by XPath to the value of a literal with a fuzzy operator which
     * expands the literal to match misspellings.
     *
     * @param xpath
     *            XPath expression
     * @param literal
     *            value to compare
     * @return result of fuzzy operation on XPath node
     */
    public T xpathIsFuzzy(String xpath, String literal) {
        throw new UnsupportedOperationException(
                "xpathIsFuzzy(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    // Addtional XPath operators

    /**
     * Determines if a node expressed by XPath exists.
     *
     * @param xpath
     *            XPath expression
     * @return result of XPath node existence
     */
    public T xpathExists(String xpath) {
        throw new UnsupportedOperationException(
                "xpathExists(String) not supported by org.opengis.filter.Filter Delegate.");
    }

    // Spatial operators

    /**
     * Compares the geometry associated with a property is beyond a distance from a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @param distance
     *            distance buffer in meters
     * @return result of beyond spatial operation
     */
    public T beyond(String propertyName, String wkt, double distance) {
        throw new UnsupportedOperationException(
                "beyond(String,String,double) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathBeyond(String xpath, String wkt, double distance) {
        return beyond(xpath, wkt, distance);
    }
    /**
     * Compares the geometry associated with a property is contained by a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of contains spatial operation
     */
    public T contains(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "contains(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathContains(String xpath, String wkt) {
        return contains(xpath, wkt);
    }

    /**
     * Compares the geometry associated with a property crosses a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of crosses spatial operation
     */
    public T crosses(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "crosses(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the geometry associated with a property crosses a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of crosses spatial operation
     */
    public T xpathCrosses(String xpath, String wkt) {
     return crosses(xpath, wkt);
    }

    /**
     * Compares the geometry associated with a property is disjoint from a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of disjoint spatial operation
     */
    public T disjoint(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "disjoint(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the geometry associated with a property is disjoint from a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of disjoint spatial operation
     */
    public T xpathDisjoint(String xpath, String wkt) {
        return xpathDisjoint(xpath, wkt);
    }

    /**
     * Compares the geometry associated with a property is within a distance from a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @param distance
     *            distance buffer in meters
     * @return result of dwithin spatial operation
     */
    public T dwithin(String propertyName, String wkt, double distance) {
        throw new UnsupportedOperationException(
                "dwithin(String,String,double) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the geometry associated with a property is within a distance from a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @param distance
     *            distance buffer in meters
     * @return result of dwithin spatial operation
     */
    public T xpathDwithin(String xpath, String wkt, double distance) {
        return dwithin(xpath, wkt, distance);
    }

    /**
     * Compares the geometry associated with a property intersects a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of intersects spatial operation
     */
    public T intersects(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "intersects(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathIntersects(String xpath, String wkt) {
        return intersects(xpath, wkt);
    }
    /**
     * Compares the geometry associated with a property overlaps a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of overlaps spatial operation
     */
    public T overlaps(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "overlaps(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathOverlaps(String xpath, String wkt) {
        return overlaps(xpath, wkt);
    }

    /**
     * Compares the geometry associated with a property touches a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of touches spatial operation
     */
    public T touches(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "touches(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    public T xpathTouches(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "touches(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the geometry associated with a property is within a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of within spatial operation
     */
    public T within(String propertyName, String wkt) {
        throw new UnsupportedOperationException(
                "within(String,String) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the geometry associated with a property is within a WKT geometry.
     *
     * @param propertyName
     *            name of property to compare
     * @param wkt
     *            WKT geometry to compare
     * @return result of within spatial operation
     */
    public T xpathWithin(String xpath, String wkt) {
       return within(xpath, wkt);
    }


    // Temporal operators

    /**
     * Compares the value associated with a property is after a given {@code Date}. This is an
     * exclusive comparison.
     * <p>
     * {@code property > date}
     *
     * @param propertyName
     *            name of property to compare
     * @param date
     *            {@code Date} to compare
     * @return result of after temporal operation
     */
    public T after(String propertyName, Date date) {
        throw new UnsupportedOperationException(
                "after(String,Date) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the value associated with a property is after a given {@code Date}. This is an
     * exclusive comparison.
     * <p>
     * {@code property > date}
     *
     * @param propertyName
     *            name of property to compare
     * @param date
     *            {@code Date} to compare
     * @return result of after temporal operation
     */
    public T xpathAfter(String xpath, Date date) {
        return after(xpath, date);
    }

    /**
     * Compares the value associated with a property is before a given {@code Date}.This is an
     * exclusive comparison.
     * <p>
     * {@code property < date}
     *
     * @param propertyName
     *            name of property to compare
     * @param date
     *            {@code Date} to compare
     * @return result of before temporal operation
     */
    public T before(String propertyName, Date date) {
        throw new UnsupportedOperationException(
                "before(String,Date) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the value associated with a property is before a given {@code Date}.This is an
     * exclusive comparison.
     * <p>
     * {@code property < date}
     *
     * @param propertyName
     *            name of property to compare
     * @param date
     *            {@code Date} to compare
     * @return result of before temporal operation
     */
    public T xpathBefore(String xpath, Date date) {
        return before(xpath, date);
    }

    /**
     * Compares the value associated with a property is between a start and end date. This is an
     * exclusive comparison.
     * <p>
     * {@code startDate < property < endDate}
     *
     * @param propertyName
     *            name of property to compare
     * @param startDate
     *            start date to compare
     * @param endDate
     *            end date to compare
     * @return result of during temporal operation
     */
    public T during(String propertyName, Date startDate, Date endDate) {
        throw new UnsupportedOperationException(
                "during(String,Date,Date) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the value associated with a property is between a start and end date. This is an
     * exclusive comparison.
     * <p>
     * {@code startDate < property < endDate}
     *
     * @param propertyName
     *            name of property to compare
     * @param startDate
     *            start date to compare
     * @param endDate
     *            end date to compare
     * @return result of during temporal operation
     */
    public T xpathDuring(String xpath, Date startDate, Date endDate) {
        return during(xpath, startDate, endDate);
    }

    /**
     * Compares the value associated with a property is relatively within a duration of milliseconds
     * in the past from the current time of evaluation.
     *
     * @param propertyName
     *            name of property to compare
     * @param duration
     *            relative duration in milliseconds
     * @return result of relative temporal operation
     */
    public T relative(String propertyName, long duration) {
        throw new UnsupportedOperationException(
                "relative(String,long) not supported by org.opengis.filter.Filter Delegate.");
    }

    /**
     * Compares the value associated with a property is relatively within a duration of milliseconds
     * in the past from the current time of evaluation.
     *
     * @param propertyName
     *            name of property to compare
     * @param duration
     *            relative duration in milliseconds
     * @return result of relative temporal operation
     */
    public T xpathRelative(String xpath, long duration) {
        return relative(xpath, duration);
    }

}
