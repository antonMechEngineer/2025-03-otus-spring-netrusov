package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Payment;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AclServiceWrapperServiceImpl implements AclServiceWrapperService {

    private final MutableAclService mutableAclService;

    @Override
    public void createPaymentPermission(Payment payment) {
        var systemAuth = new UsernamePasswordAuthenticationToken(
                "system", "N/A", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(systemAuth);
        SecurityContextHolder.setContext(securityContext);
        ObjectIdentity oid = new ObjectIdentityImpl(payment);
        MutableAcl acl;
        try {
            acl = mutableAclService.createAcl(oid);
            Sid adminSid = new GrantedAuthoritySid("ROLE_ADMIN");
            Sid userSid = new PrincipalSid(payment.getUser().getUsername());
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, userSid, true);
            acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, userSid, true);
            acl.insertAce(acl.getEntries().size(), BasePermission.READ, adminSid, true);
            acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, adminSid, true);
            acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, adminSid, true);
            acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, adminSid, true);
            mutableAclService.updateAcl(acl);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}
