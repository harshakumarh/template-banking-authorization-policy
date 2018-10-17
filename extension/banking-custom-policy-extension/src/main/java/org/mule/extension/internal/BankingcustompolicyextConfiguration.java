/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package org.mule.extension.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(BankingcustompolicyextOperations.class)
public class BankingcustompolicyextConfiguration {

    @Parameter
    private String issuer;
    @Parameter
    private String jwksUrl;
    @Parameter
    private String signingAlgorithm;
    @Parameter
    private String encryptionKey;

    public String getIssuer() {
        return issuer;
    }

    public String getJwksUrl() {
        return jwksUrl;
    }

    public String getSigningAlgorithm() {
        return signingAlgorithm;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }
}
