package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    @Override
    public void createPermission(Object object) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var oid = new ObjectIdentityImpl(object);
        var acl = mutableAclService.createAcl(oid);
        var adminSid = new GrantedAuthoritySid("ROLE_ADMIN");
        var userSid = new PrincipalSid(authentication);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, userSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, userSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, adminSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, adminSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, adminSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, adminSid, true);
        mutableAclService.updateAcl(acl);
    }
}
