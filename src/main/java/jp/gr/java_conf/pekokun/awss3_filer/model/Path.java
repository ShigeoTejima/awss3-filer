package jp.gr.java_conf.pekokun.awss3_filer.model;

import java.util.Objects;

public class Path {

    private final String value;
    
    public Path(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.value);
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
        final Path other = (Path) obj;
        return Objects.equals(this.value, other.value);
    }

}
