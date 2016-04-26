package jp.gr.java_conf.pekokun.awss3_filer.model;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

@SessionScoped
public class User implements Serializable {

    private String id;
    private String name;
    private boolean login = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return String.format("User{id: %s, name: %s, login: %s}", id, name, login);
    }

}
