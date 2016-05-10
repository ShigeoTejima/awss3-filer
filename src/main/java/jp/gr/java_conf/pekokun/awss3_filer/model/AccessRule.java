package jp.gr.java_conf.pekokun.awss3_filer.model;

public class AccessRule {

    public enum Type {
        ALLOW,
        DENY
    }
    
    public enum Control {
        READ,
        WRITE,
        MANAGE
    }

    private final Type type;
    private final Control control;
    private final Grantee grantee;

    public AccessRule(Type type, Control control, Grantee grantee) {
        this.type = type;
        this.control = control;
        this.grantee = grantee;
    }

    public Type getType() {
        return type;
    }

    public Control getControl() {
        return control;
    }

    public Grantee getGrantee() {
        return grantee;
    }

}
