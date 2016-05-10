package jp.gr.java_conf.pekokun.awss3_filer.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import jp.gr.java_conf.pekokun.awss3_filer.api.repository.AclRepository;
import jp.gr.java_conf.pekokun.awss3_filer.model.AccessRule;
import jp.gr.java_conf.pekokun.awss3_filer.model.Path;
import jp.gr.java_conf.pekokun.awss3_filer.model.User;

@Dependent
@Named
public class AclService {

    @Inject
    private AclRepository aclRepository;

    public boolean isAllow(Path path, User user, AccessRule.Control control) {
        List<AccessRule> accessRules = aclRepository.getAccessRules(path);
        if (accessRules.isEmpty()) {
            return (control == AccessRule.Control.READ);
        }

        List<AccessRule> allowAccessRules =
            accessRules.stream()
                .filter(r -> r.getType() == AccessRule.Type.ALLOW)
                .collect(Collectors.toList());

        if (!allowAccessRules.isEmpty()) {
            return allowAccessRules.stream()
                .anyMatch(r -> r.getControl() == control && r.getGrantee().getExpression().equals(user.getId()));
        }

        List<AccessRule> denyAccessRules =
            accessRules.stream()
                .filter(r -> r.getType() == AccessRule.Type.DENY)
                .collect(Collectors.toList());

        return !denyAccessRules.stream()
                    .anyMatch(r -> r.getControl() == control && r.getGrantee().getExpression().equals(user.getId()));
    }

}
