/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package org.mule.extension.internal;

import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.AnyType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;

public class BankingcustompolicyextMetadataResolver implements OutputTypeResolver<Object> {

    private static final AnyType ANY_TYPE = BaseTypeBuilder.create(JAVA).anyType().build();

    @Override
    public String getCategoryName() {
        return "BankingCustomPolicy";
    }

    @Override
    public MetadataType getOutputType(MetadataContext context, Object key) {
        return ANY_TYPE;
    }
}
