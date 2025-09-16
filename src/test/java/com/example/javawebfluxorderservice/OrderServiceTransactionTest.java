package com.example.javawebfluxorderservice;

import com.example.javawebfluxorderservice.domain.request.OrderCreateRequestDto;
import com.example.javawebfluxorderservice.domain.response.OrderResponseDto;
import com.example.javawebfluxorderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TestcontainersConfig.class)
class OrderServiceTransactionTest {
//class OrderServiceTransactionTest extends AbstractIntegrationTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    void setUp() {
        databaseClient.sql("DELETE FROM orders")
                .fetch()
                .rowsUpdated()
                .block();
    }

    @Test
    void createOrder_WithinTransaction_CommitsSuccessfully() {
        OrderCreateRequestDto request = new OrderCreateRequestDto();
        request.setOrderNumber("ORDER-TX-001");
        request.setStatus("CREATED");

        Mono<OrderResponseDto> result = orderService.createOrder(request);

        StepVerifier.create(result)
                .assertNext(order -> {
                    assertNotNull(order);
                    assertEquals("ORDER-TX-001", order.getOrderNumber());
                })
                .verifyComplete();

        Long count = databaseClient.sql("SELECT COUNT(*) FROM orders WHERE order_number = 'ORDER-TX-001'")
                .fetch()
                .first()
                .map(row -> (Long) row.get("count"))
                .block();
        assertEquals(1L, count);
    }
}