package jp.gr.java_conf.pekokun.awss3_filer.model;

import java.util.Objects;

public class Path {

    private final String value;
    private final Owner owner;
    
    public Path(String value, Owner owner) {
        this.value = value;
        this.owner = owner;
    }

    public String getValue() {
        return value;
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.value);
        hash = 53 * hash + Objects.hashCode(this.owner);
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
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (!Objects.equals(this.owner, other.owner)) {
            return false;
        }
        return true;
    }

    public static final class Owner {
        private final String id;

        public Owner(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + Objects.hashCode(this.id);
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
            final Owner other = (Owner) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            return true;
        }

    }
}
