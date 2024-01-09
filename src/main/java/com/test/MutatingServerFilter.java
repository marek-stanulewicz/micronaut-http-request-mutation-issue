package com.test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Filter(Filter.MATCH_ALL_PATTERN)
public class MutatingServerFilter implements HttpServerFilter {

    public volatile boolean mutateRequest = false;

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return (mutateRequest ? mutated(request) : original(request))
                .flatMap(req -> Mono.from(chain.proceed(req)));
    }

    private Mono<HttpRequest<?>> original(HttpRequest<?> request) {
        return Mono.just(request);
    }

    private Mono<HttpRequest<?>> mutated(HttpRequest<?> request) {
        return Mono.fromCallable(request::mutate);
    }

}
