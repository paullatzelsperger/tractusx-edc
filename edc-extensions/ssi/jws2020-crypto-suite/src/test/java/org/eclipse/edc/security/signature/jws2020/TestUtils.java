package org.eclipse.edc.security.signature.jws2020;

import com.apicatalog.jsonld.JsonLd;
import com.apicatalog.jsonld.JsonLdError;
import com.apicatalog.jsonld.document.JsonDocument;
import com.apicatalog.jsonld.loader.DocumentLoader;
import com.apicatalog.ld.signature.key.KeyPair;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import jakarta.json.JsonObject;
import org.eclipse.edc.jsonld.util.JacksonJsonLd;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

public class TestUtils {

    private static final ObjectMapper mapper = JacksonJsonLd.createObjectMapper();

    public static KeyPair createKeyPair(JWK jwk) {
        var id = URI.create("https://org.eclipse.tractusx/keys/" + UUID.randomUUID());
        var type = URI.create("https://w3id.org/security#JsonWebKey2020");
        return new JwkMethod(id, type, null, jwk);
    }

    public static JsonObject readResourceAsJson(String name) {
        try {
            return mapper.readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(name), JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject expand(JsonObject object){
        try {
            return JsonLd.expand(JsonDocument.of(object)).get().getJsonObject(0);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject expand(JsonObject object, DocumentLoader loader){
        try {
            return JsonLd.expand(JsonDocument.of(object)).loader(loader).get().getJsonObject(0);
        } catch (JsonLdError e) {
            throw new RuntimeException(e);
        }
    }

    public static String readResourceAsString(String name) {
        try (var stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(name)) {
            return new String(Objects.requireNonNull(stream).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
