package pl.byczazagroda.trackexpensesappbackend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class HelloWorldServiceTest {

    @Mock
    private HelloWorldService helloWorldService;

    @BeforeEach
    void beforeAll() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should say 'Hello World!'")
    void shouldSayHello() {
        // given

        // when
        when(helloWorldService.sayHello()).thenReturn("Hello World!");

        // then
        assertEquals("Hello World!", helloWorldService.sayHello());
    }

    /**
     * Should fail --> used only to test CI/CD
     */
    @Test
    void shouldFail() {
        assert false;
    }
}