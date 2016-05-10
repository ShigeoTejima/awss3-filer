package jp.gr.java_conf.pekokun.awss3_filer.service;

import java.util.Arrays;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import jp.gr.java_conf.pekokun.awss3_filer.api.repository.AclRepository;
import jp.gr.java_conf.pekokun.awss3_filer.model.AccessRule;
import jp.gr.java_conf.pekokun.awss3_filer.model.GranteeUser;
import jp.gr.java_conf.pekokun.awss3_filer.model.Path;

@ApplicationScoped
public class Initializer {

    @Inject
    private AclRepository aclRepository;

    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object event) {
        aclRepository.save(new Path("foo/"),
            Arrays.asList(
                new AccessRule(AccessRule.Type.ALLOW, AccessRule.Control.READ, new GranteeUser("john")),
                new AccessRule(AccessRule.Type.ALLOW, AccessRule.Control.READ, new GranteeUser("paul")),
                new AccessRule(AccessRule.Type.DENY, AccessRule.Control.READ, new GranteeUser("george"))
            )
        );

        aclRepository.save(new Path("bar/"),
            Arrays.asList(
                new AccessRule(AccessRule.Type.DENY, AccessRule.Control.WRITE, new GranteeUser("john")),
                new AccessRule(AccessRule.Type.DENY, AccessRule.Control.WRITE, new GranteeUser("paul"))
            )
        );
    }

}
