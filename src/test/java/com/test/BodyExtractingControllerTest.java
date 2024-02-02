package com.test;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class BodyExtractingControllerTest {

    @Inject
    @Client("/extract-body")
    HttpClient httpClient;

    @ParameterizedTest
    @MethodSource("requestBodiesRawAndParsed")
    void shouldExtractBodyFromRequest(String rawBody, String parsedBody) {

        var request = HttpRequest.POST("/", rawBody).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
        var response = httpClient.toBlocking().exchange(request, String.class);
        assertEquals(parsedBody, response.body());
    }

    public static Stream<Arguments> requestBodiesRawAndParsed() {
        return Stream.of(
                Arguments.of("a=1", "{a=1}"),
                Arguments.of("a=1&b=2", "{a=1, b=2}"),

                //nothing
                Arguments.of("&a=1&b=2", "{a=1, b=2}"),
                Arguments.of("a=1&&b=2", "{a=1, b=2}"),
                Arguments.of("a=1&b=2&", "{a=1, b=2}"),

                //key equals empty value
                Arguments.of("z=&a=1&b=2", "{z=, a=1, b=2}"),
                Arguments.of("a=1&z=&b=2", "{a=1, z=, b=2}"),
                Arguments.of("a=1&b=2&z=", "{a=1, b=2, z=}"),

                //empty key equals value
                Arguments.of("=0&a=1&b=2", "{=0, a=1, b=2}"),
                Arguments.of("a=1&=0&b=2", "{a=1, =0, b=2}"),
                Arguments.of("a=1&b=2&=0", "{a=1, b=2, =0}"),

                //empty key equals empty value
                Arguments.of("=&a=1&b=2", "{=, a=1, b=2}"),
                Arguments.of("a=1&=&b=2", "{a=1, =, b=2}"),
                Arguments.of("a=1&b=2&=", "{a=1, b=2, =}"),

                //just key
                Arguments.of("z&a=1&b=2", "{z=, a=1, b=2}"),
                Arguments.of("a=1&z&b=2", "{a=1, z=, b=2}"),
                Arguments.of("a=1&b=2&z", "{a=1, b=2, z=}")
        );
    }

}
