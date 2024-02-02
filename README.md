## Micronaut 4.2.4 application/x-www-form-urlencoded parsing issue

Parsing should conform to https://url.spec.whatwg.org/#application/x-www-form-urlencoded, but it does not support the case when a single sequence is just `key`. Moreover, if this `key` is followed by more sequences an exception is thrown and Internal Server Error is returned.
