## Oauth 2.0 JWE access token enforcement policy

This policy enforces use of signed and encrypted JWT access token within Authorization header in order to access the API on which the policy is applied. The policy decrypts the JWE using shared AES key, then validates signature by using provided URL with RSA JWK key set and finally verifies JWT claims: iss, exp, nbf.
If any of this fails, the policy rejects access to the API and returns 401 Unauthorized status code.
The policy depends on jose4j library (org.bitbucket.b_c:jose4j:0.5.2). This is not part of Mule runtime so it cannot be applied to API running on CloudHub. It can be used on Mule Standalone by adding the jose4j JAR to the user libs folder.



###Supported encryption algorithms:
 + AES_128/GCM/NoPadding  (A128GCM in JWT terminology)
 + AES_256/GCM/NoPadding  (A256GCM in JWT terminology)

###Supported signature algorithms:
+ SHA256withRSA (RS256 in JWT terminology)
+ SHA384withRSA (RS384 in JWT terminology)
+ SHA512withRSA (RS512 in JWT terminology)

###Required token properties:
HTTP Authorization header must have the following form: `Bearer <token>`

  The token consist of 5 Base64url encoded fields concatenated with "dot" : `header.key.iv.ciphertext.tag`
    
 + **header** - Contains info about JWE
 + **key** - Is empty, because direct content encryption is assumed
 + **iv** - Initialization vector for encryption algorithm (usually random bytes)
 + **ciphertext** - Encrypted JWS in compact serialization (header.payload.signature)
 + **tag** - Authentication tag. Product of authenticated encryption.
    
####Example of real token:
`eyJhbGciOiJkaXIiLCJlbmMiOiJBMTI4R0NNIiwiY3R5IjoiSldUIn0..2SZPFRt0CL0DMyJ7.iX_AOT0BhGznfU0AaqJmM_-gE_kGJwrxuCAxSFhpEFMzVeyWHlUMw9HS6U8iCMyX8PdMzfmeTDD-t7eHLhoBggkq5L3thpNEIoNHEZBmr9BxXF9mpuONWUfSZLn_VOWTKN_zm17-tC2dLuLbkU8Pc5tzRaRkXY3fXu6DexJdoQYBxLrJAt4YNR6XM-eGe2RwVgMQ-n1FQnNPBkkkKYFfq9d3ZoaeGGrRp4qtcuhEssx_RWHeNc7N8xXtmTK8xEx1jMqVtyjX2XzXDZIAajmmJC3NEwe1i1vDePRiPKRlzH4WwapOshGDgUFWQqqhemvB1hr5kfqcV0jqIwSqtUx2V1uFRli52SGVi3K2gXw2MDQgMjekcYMVpjLQj3xNEPd-XBUgRroYb5jFOUrzVdCgVlIS2gFQCeDuu7iEGMuSSZomPovLJJYd_Yoc8qmc6xrjYwEXTDd-Shu4NzdPJ-q4XhTF9Z5am6ReD2DqAudkc4sxEQAJtgu_dOKBm01llmfnioF0cBuXVIb3UfaB8M0EF_bP13bu1Sc12ssPr4VbtUZNgt6wAWCpPV-58KSxs3ZXVoyIictsAhaxlQPqxfQnAm1TYqv6lURMaDg1bDNsBtUJPxNSJPSkrOn7ISfLieSOJI4AMqhVFQK23hjFzhTS5ax2BOOek1pAZS516cF2P-x3fItqyiZEKOsGKcxqtnXAnD94hWDiDsPKsDF7TOcCuRBtQLQ8KrZOS7vtzGRQ9qWrec8kC3O4Zju08Vl7gQAmOb2fomTtoBLl3Foe_0wB129UjIzrGw.kkND4E7F3mFGAuiiuda51w`


####JWE header fields must be as follows:
  + alg `dir`
  + cty `JWT`
  + enc `A128GCM` or `A256GCM`


Example JWE header:

	{
	  "alg" : "dir",
	  "enc" : "A128GCM",
	  "cty" : "JWT"
	}

####Nested JWS header fields must be as follows:
  + alg `RS256` or `RS384` or `RS512`
  + typ`JWT`

Example JWS header:

	{
	  "alg": "RS256",
	  "typ": "JWT"
	}
	

####The following JWT claims are validated:
 + iss - must match the provided required issuer
 + exp - expiration time must be in the future. This must be present in JWT.
 + nbf - not before - if provided in JWT, it must be in the past.

Example JWT claims:

	{
	  "sub": "12345",
	  "nbf": 1479124625,
	  "iss": "https://issuer.example.com",
	  "exp": 1481716745,
	  "iat": 1479124745,
	  "ssn": "13245-324-543"
	}


### Configuration
The policy configuration contains several input parameters:
	  
+  **Issuer** - defines an issuer of JWT token. It is usually in URI/URL form.
+  **JWKs URL** - URL pointing to the JSON Web Key Set containing RSA public keys.
+  **Signature algorithm** - Algorithm to use to verify signature of nested JWS
+  **Encryption algorithm** - Algorithm to use to decrypt the JWE
+  **Encryption key** - Base64 URL encoded AES key. If the AES key is in JWK form, it is the value of the 'k' key. 


Example Configuration:
 + Issuer `https://issuer.example.com`
 + JWKS URL `https://issuer.example.com/oauth2/keys.jwks`
 + Signature algorithm `RS256`
 + Encryption algorithm `A128GCM`
 + Encryption key `dHLyjIik981jQZ1nafdMJc`
