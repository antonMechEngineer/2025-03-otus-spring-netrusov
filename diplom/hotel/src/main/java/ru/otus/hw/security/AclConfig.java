package ru.otus.hw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@SuppressWarnings("unused")
@Configuration
public class AclConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CacheManager cacheManager;

    @Bean
    public SpringCacheBasedAclCache aclCache() {
        return new SpringCacheBasedAclCache(cacheManager.getCache("aclCache"), permissionGrantingStrategy(),
                aclAuthorizationStrategy());
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public AclPermissionEvaluator permissionEvaluator(MutableAclService aclService) {
        return new AclPermissionEvaluator(aclService);
    }

    @Bean
    public BasicLookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
    }
    
    @Bean
    public JdbcMutableAclService jdbcMutableAclService() {
        return new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(AclPermissionEvaluator evaluator) {
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(evaluator);
        return handler;
    }
}
