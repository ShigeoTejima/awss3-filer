package jp.gr.java_conf.pekokun.awss3_filer.api;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DemoResource.class);
        classes.add(FileResource.class);
        classes.add(MultiPartFeature.class);
        return classes;
    }

}
