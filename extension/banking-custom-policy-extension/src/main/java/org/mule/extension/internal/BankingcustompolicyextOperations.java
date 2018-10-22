/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package org.mule.extension.internal;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.OctetSequenceJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import org.mule.extension.exceptions.BankingcustompolicyextErrorTypeProvider;
import org.mule.extension.exceptions.InvalidJWTException;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class BankingcustompolicyextOperations {

    private final Logger LOGGER = LoggerFactory.getLogger(BankingcustompolicyextOperations.class);

    /**
     *
     * @param configuration
     * @param token
     * @return Map of jwt claims
     */
    @Summary("Validates JWT and returns claims extracted from it.")
    @Throws(BankingcustompolicyextErrorTypeProvider.class)
    @OutputResolver(output = BankingcustompolicyextMetadataResolver.class)
    public Map<String, Object> validateJWT(@Config BankingcustompolicyextConfiguration configuration, String token) {
        JwtClaims jwtClaims = null;
        try {

            HttpsJwksVerificationKeyResolver verificationKeys =
                new HttpsJwksVerificationKeyResolver(new HttpsJwks(configuration.getJwksUrl()));

            String jwkJson = String.format("{\"kty\": \"oct\", \"k\": \"%s\"}", configuration.getEncryptionKey());
            OctetSequenceJsonWebKey symmetricKey = (OctetSequenceJsonWebKey) JsonWebKey.Factory.newJwk(jwkJson);

            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setJwsAlgorithmConstraints(
                    new AlgorithmConstraints(ConstraintType.WHITELIST, configuration.getSigningAlgorithm()))
                .setJweAlgorithmConstraints(new AlgorithmConstraints(ConstraintType.WHITELIST, "dir"))
                .setMaxFutureValidityInMinutes(30 * 24 * 60) // 30 days
                .setAllowedClockSkewInSeconds(0)
                .setRequireSubject()
                .setSkipDefaultAudienceValidation()
                .setExpectedIssuer(configuration.getIssuer())
                .setVerificationKeyResolver(verificationKeys)
                .setDecryptionKey(symmetricKey.getKey())
                .build();

            jwtClaims = jwtConsumer.processToClaims(token);
            return jwtClaims.getClaimsMap();

        } catch (Exception e) {
            LOGGER.info("JWS access_token could not be verified: " + e);
            throw new InvalidJWTException(e);
        }
    }
}
