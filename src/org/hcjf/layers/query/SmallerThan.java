package org.hcjf.layers.query;

import org.hcjf.utils.Introspection;

/**
 * @author javaito
 * @mail javaito@gmail.com
 */
public class SmallerThan extends Evaluator {

    private final boolean orEquals;

    protected SmallerThan(String fieldName, Object value, boolean orEquals) {
        super(fieldName, value);
        this.orEquals = orEquals;
    }

    public SmallerThan(String fieldName, Object value) {
        this(fieldName, value, false);
    }

    @Override
    protected boolean evaluate(Object object) {
        boolean result;
        Introspection.Getter getter = Introspection.getGetters(object.getClass()).get(getFieldName());
        try {
            Object fieldValue = getter.invoke(object);
            if(Comparable.class.isAssignableFrom(getValue().getClass())) {
                if(fieldValue.getClass().isAssignableFrom(getValue().getClass()) ||
                        getValue().getClass().isAssignableFrom(fieldValue.getClass())) {
                    if(orEquals) {
                        result = ((Comparable)fieldValue).compareTo(getValue()) <= 0;
                    } else {
                        result = ((Comparable)fieldValue).compareTo(getValue()) < 0;
                    }
                } else {
                    throw new IllegalArgumentException("Incompatible types between value and field value ("
                            + getFieldName() + "): " + getValue().getClass() + " != " + fieldValue.getClass());
                }
            } else {
                throw new IllegalArgumentException("Unsupported evaluator type: " + getValue().getClass());
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Smaller than evaluator fail", ex);
        }
        return result;
    }
}
