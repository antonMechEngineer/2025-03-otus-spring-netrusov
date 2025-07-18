package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Service;
import ru.otus.hw.repositories.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final UserRepository userRepository;

    private final MutableAclService mutableAclService;

    @Override
    public void createPermission(Object object, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        List<PrincipalSid> adminUsers = userRepository.findByRole("ROLE_ADMIN").stream()
                .map(u -> new PrincipalSid(u.getUsername()))
                .toList();
        List<PrincipalSid> usualUsers = userRepository.findByRole("ROLE_USER").stream()
                .map(u -> new PrincipalSid(u.getUsername()))
                .toList();
        MutableAcl acl = mutableAclService.createAcl(oid);
        adminUsers.forEach(s -> {
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, s, true);
            acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, s, true);
            acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, s, true);
            acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, s, true);
        });
        usualUsers.forEach(s -> acl.insertAce(acl.getEntries().size(), permission, s, true));
        mutableAclService.updateAcl(acl);
    }
}
