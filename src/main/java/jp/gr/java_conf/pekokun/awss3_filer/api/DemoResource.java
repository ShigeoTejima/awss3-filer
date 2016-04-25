package jp.gr.java_conf.pekokun.awss3_filer.api;

import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("demo")
public class DemoResource {

    @GET
    public String get() {
        return "sample";
    }

    @Path("user/john")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getJohn() {
        return new User("John");
    }

    @Path("users")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers() {
        return Arrays.asList(new User("John"), new User("Paul"));
    }

    @Path("list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getList() {
        return Arrays.asList("foo", "bar");
    }
    
    public static class User {
        private String name;

        public User() {
        }
        public User(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }
}
