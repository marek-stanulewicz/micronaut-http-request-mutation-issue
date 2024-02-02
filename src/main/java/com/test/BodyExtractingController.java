package com.test;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.util.Map;

@Controller
public class BodyExtractingController {

    @Post(value = "/extract-body", consumes = MediaType.ALL, produces = MediaType.ALL)
    public String extractBody(@Nullable @Body Map<String, String> body) {
        return body.toString();
    }

}
