package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    @Override
    public void createPermission(Object object) {
        ObjectIdentity oid = new ObjectIdentityImpl(object);
        MutableAcl acl = mutableAclService.createAcl(oid);
        Sid adminSid = new GrantedAuthoritySid("ROLE_ADMIN");
        Sid userSid = new GrantedAuthoritySid("ROLE_USER");
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, userSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.READ, adminSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, adminSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, adminSid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, adminSid, true);
        mutableAclService.updateAcl(acl);
    }
}
