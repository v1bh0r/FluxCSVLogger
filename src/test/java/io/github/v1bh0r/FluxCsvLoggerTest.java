package io.github.v1bh0r;

import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FluxCsvLoggerTest {

    @Test
    void log() {
        // Arrange
        Logger mockLogger = mock(Logger.class);
        var fluxCsvLogger = new FluxCsvLogger(mockLogger);

        class TestObject {
            private String field1 = "test1";
            private String field2 = "test2";
        }

        Flux<TestObject> flux = Flux.just(new TestObject(), new TestObject());

        // Act
        Flux<TestObject> result = fluxCsvLogger.log(flux, TestObject.class);

        // Assert
        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger, times(4)).info(argumentCaptor.capture());

        assertEquals("field1,field2", argumentCaptor.getAllValues().get(0));
        assertEquals("test1,test2", argumentCaptor.getAllValues().get(1));
        assertEquals("field1,field2", argumentCaptor.getAllValues().get(2));
        assertEquals("test1,test2", argumentCaptor.getAllValues().get(3));
    }
}