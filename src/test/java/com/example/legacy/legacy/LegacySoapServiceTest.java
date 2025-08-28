package com.example.legacy.legacy;

import com.example.legacy.model.Order;
import com.example.legacy.service.OrderService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LegacySoapServiceTest {

    @Test
    void givenName_whenSayHello_thenReturnsGreeting() {
        OrderService svc = mock(OrderService.class);
        LegacySoapService soap = new LegacySoapService(svc);
        String out = soap.sayHello("Renzo");
        assertTrue(out.contains("Renzo"));
    }

    @Test
    void givenCustomerAndAmount_whenCreateSampleOrder_thenReturnsCountFromService() {
        // Given: mock OrderService to avoid time dependency and assert interaction
        OrderService svc = mock(OrderService.class);
        when(svc.listByCustomer(anyString())).thenReturn(List.of(
                new Order("1", UUID.randomUUID().toString(), 1.0, Instant.EPOCH)
        ));
        LegacySoapService soap = new LegacySoapService(svc);

        // When
        int count = soap.createSampleOrder("Alice", 12.3);

        // Then
        assertEquals(1, count);
        verify(svc, times(1)).createOrder(any(), eq(12.3), any(Instant.class));
        verify(svc, times(1)).listByCustomer(anyString());
    }
}

