package com.test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class BodyExtractingControllerTest {

    @Inject
    @Client("/extract-body")
    HttpClient httpClient;

    @Inject
    MutatingServerFilter filter;

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void shouldExtractBodyFromRequest(boolean mutateRequestInFilter) {
        filter.mutateRequest = mutateRequestInFilter;

        var body = "x=123&text=a-message";
        var request = HttpRequest.POST("/", body);
        var response = httpClient.toBlocking().exchange(request, String.class);
        assertEquals(body, response.body());
    }

}
