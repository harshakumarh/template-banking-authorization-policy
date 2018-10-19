## Oauth 2.0 JWE access token enforcement policy extension

This policy extension decrypts and validates encrypted JWT access token. The extension decrypts the JWE using shared AES key, then validates signature by using provided URL with RSA JWK key set and finally verifies JWT claims: iss, exp, nbf. Claims are then returned as a java Map object to the policy , which uses this extension.
To decrypt and validate JWT org.bitbucket.b_c::jose4j library is used

###Supported encryption algorithms:
 + AES_128/GCM/NoPadding  (A128GCM in JWT terminology)
 + AES_256/GCM/NoPadding  (A256GCM in JWT terminology)

###Supported signature algorithms:
+ SHA256withRSA (RS256 in JWT terminology)
+ SHA384withRSA (RS384 in JWT terminology)
+ SHA512withRSA (RS512 in JWT terminology)

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
The extension configuration contains several input parameters:

+  **Issuer** - defines an issuer of JWT token. It is usually in URI/URL form.
+  **JWKs URL** - URL pointing to the JSON Web Key Set containing RSA public keys.
+  **Signature algorithm** - Algorithm to use to verify signature of nested JWS
+  **Encryption key** - Base64 URL encoded AES key. If the AES key is in JWK form, it is the value of the 'k' key.

Example Configuration:
 + Issuer `https://issuer.example.com`
 + JWKS URL `https://issuer.example.com/oauth2/keys.jwks`
 + Signature algorithm `RS256`
 + Encryption key `secret`
