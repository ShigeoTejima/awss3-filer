package jp.gr.java_conf.pekokun.awss3_filer.api.repository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;
import jp.gr.java_conf.pekokun.awss3_filer.model.AccessRule;
import jp.gr.java_conf.pekokun.awss3_filer.model.Path;

@Singleton
@Named
public class AclRepository {

    private Map<Path, List<AccessRule>> stored;

    @PostConstruct
    void postConstruct() {
        stored = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    public List<AccessRule> getAccessRules(Path path) {
        if (stored == null) {
            throw new IllegalStateException("repository not initialized yet.");
        }

        return stored.containsKey(path) ?
              Collections.unmodifiableList(stored.get(path))
            : Collections.emptyList();
    }

    public synchronized void save(Path path, List<AccessRule> accessRules) {
        if (stored == null) {
            throw new IllegalStateException("repository not initialized yet.");
        }

        if (path == null) {
            throw new IllegalArgumentException("path is required.");
        }
        if (accessRules == null) {
            throw new IllegalArgumentException("accessRules is required.");
        }

        stored.put(path, accessRules);
    }

    public synchronized void addAccessRules(Path path, List<AccessRule> accessRules) {
        if (stored == null) {
            throw new IllegalStateException("repository not initialized yet.");
        }

        if (path == null) {
            throw new IllegalArgumentException("path is required.");
        }
        if (accessRules == null || accessRules.isEmpty()) {
            throw new IllegalArgumentException("accessRules is required.");
        }
        if (!stored.containsKey(path)) {
            throw new IllegalArgumentException("path does not found in repository.");
        }

        stored.get(path).addAll(accessRules);
    }

}
