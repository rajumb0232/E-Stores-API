package com.devb.estores.security;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class JwtModel {
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_BROWSER_NAME = "browser";
    public static final String CLAIM_SEC_CH_UA_PLATFORM = "secChUaPlatform";
    public static final String CLAIM_SEC_CH_UA_MOBILE = "secChUaMobile";
    public static final String CLAIM_JWT_ID = "jti";
    public static final String CLAIM_USER_AGENT = "userAgent";

    private Map<String, Object> claims;
    private Date issuedAt;
    private Date expiration;
    private String subject;

    public static JwtBuilder create() {
        return new JwtBuilder(new HashMap<>());
    }

    public static class JwtBuilder {
        private final Map<String, Object> claims;

        private Date issuedAt;
        private Date expiration;
        private String subject;

        JwtBuilder(Map<String, Object> claims) {
            this.claims = claims;
        }

        public JwtBuilder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public JwtBuilder setIssuedAt(Date issuedAt) {
            this.issuedAt = issuedAt;
            return this;
        }

        public JwtBuilder setExpiration(Date expiration) {
            this.expiration = expiration;
            return this;
        }

        public JwtBuilder roles(List<String> roles) {
            this.claims.put(CLAIM_ROLES, roles);
            return this;
        }

        public JwtBuilder jwtId(String jwtId) {
            this.claims.put(CLAIM_JWT_ID, jwtId);
            return this;
        }

        public JwtBuilder browserName(String browserName) {
            this.claims.put(CLAIM_BROWSER_NAME, browserName);
            return this;
        }

        public JwtBuilder secChaUaPlatform(String secChaUaPlatform) {
            this.claims.put(CLAIM_SEC_CH_UA_PLATFORM, secChaUaPlatform);
            return this;
        }

        public JwtBuilder secChaUaMobile(String secChaUaMobile) {
            this.claims.put(CLAIM_SEC_CH_UA_MOBILE, secChaUaMobile);
            return this;
        }

        public JwtBuilder userAgent(String userAgent) {
            this.claims.put(CLAIM_USER_AGENT, userAgent);
            return this;
        }

        public JwtModel build() {
            JwtModel jwtModel = new JwtModel();
            jwtModel.claims = this.claims;
            jwtModel.subject = this.subject;
            jwtModel.issuedAt = this.issuedAt;
            jwtModel.expiration = this.expiration;

            return jwtModel;
        }
    }

}
