package io.github.v1bh0r;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Flux;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;


public class FluxCsvLogger {

    private final Logger logger;

    FluxCsvLogger(Logger logger) {
        this.logger = logger;
    }

    public <T> Flux<T> log(Flux<T> flux, Class<T> clazz) {
        // 1. Print a header with the field names
        // 2. Print each object as a CSV line

        return flux.doOnNext(t -> {
            Field[] fields = FieldUtils.getAllFields(clazz);
            String header = Arrays.stream(fields)
                    .filter(field -> !field.isSynthetic())
                    .map(Field::getName).collect(Collectors.joining(","));
            logger.info(header);
            String line = Arrays.stream(fields)
                    .filter(field -> !field.isSynthetic())
                    .map(field -> {
                        try {
                            return FieldUtils.readField(field, t, true).toString();
                        } catch (IllegalAccessException e) {
                            return "N/A";
                        }
                    }).collect(Collectors.joining(","));
            logger.info(line);
        });
    }
}
