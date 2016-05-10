package jp.gr.java_conf.pekokun.awss3_filer.model;

import java.util.Objects;

public abstract class Grantee {

    private final String expression;

    protected Grantee(String expression) {
        this.expression = expression;
    }

    public final String getExpression() {
        return expression;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.expression);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Grantee other = (Grantee) obj;
        return Objects.equals(this.expression, other.expression);
    }

}
