package com.example.javawebfluxorderservice;

import com.example.javawebfluxorderservice.domain.request.OrderCreateRequestDto;
import com.example.javawebfluxorderservice.domain.response.OrderResponseDto;
import com.example.javawebfluxorderservice.exception.OrderNotFoundException;
import com.example.javawebfluxorderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(TestcontainersConfig.class)
class OrderServiceImplIntegrationTest {
//class OrderServiceImplIntegrationTest extends AbstractIntegrationTest {

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
    void findAllOrders_WhenNoOrders_ReturnsEmptyFlux() {
        Flux<OrderResponseDto> result = orderService.findAllOrders();

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findAllOrders_WhenOrdersExist_ReturnsAllOrders() {
        insertTestOrder("ORDER-001", "CREATED", Instant.now());
        insertTestOrder("ORDER-002", "PROCESSING", Instant.now());

        Flux<OrderResponseDto> result = orderService.findAllOrders();

        StepVerifier.create(result.collectList())
                .assertNext(orders -> {
                    assertEquals(2, orders.size());

                    Assertions.assertTrue(orders.stream().anyMatch(o ->
                            "ORDER-001".equals(o.getOrderNumber()) && "CREATED".equals(o.getStatus())));
                    Assertions.assertTrue(orders.stream().anyMatch(o ->
                            "ORDER-002".equals(o.getOrderNumber()) && "PROCESSING".equals(o.getStatus())));
                })
                .verifyComplete();
    }

    @Test
    void findOrderById_WhenOrderExists_ReturnsOrder() {
        Long orderId = insertTestOrder("ORDER-001", "CREATED", Instant.now());

        Mono<OrderResponseDto> result = orderService.findOrderById(orderId);

        StepVerifier.create(result)
                .assertNext(order -> {
                    assertNotNull(order);
                    assertEquals(orderId, order.getId());
                    assertEquals("ORDER-001", order.getOrderNumber());
                    assertEquals("CREATED", order.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void findOrderById_WhenOrderNotExists_ThrowsOrderNotFoundException() {
        Mono<OrderResponseDto> result = orderService.findOrderById(999L);

        StepVerifier.create(result)
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    @Test
    void createOrder_WithValidData_ReturnsCreatedOrder() {
        OrderCreateRequestDto request = new OrderCreateRequestDto();
        request.setOrderNumber("ORDER-001");
        request.setStatus("CREATED");

        OrderResponseDto createdOrder = orderService.createOrder(request)
                .doOnNext(order -> {
                    assertNotNull(order);
                    assertNotNull(order.getId());
                    assertEquals("ORDER-001", order.getOrderNumber());
                    assertEquals("CREATED", order.getStatus());
                    assertNotNull(order.getCreatedAt());
                })
                .block();

        assertNotNull(createdOrder);

        StepVerifier.create(orderService.findOrderById(createdOrder.getId()))
                .assertNext(foundOrder -> {
                    assertEquals(createdOrder.getId(), foundOrder.getId());
                    assertEquals("ORDER-001", foundOrder.getOrderNumber());
                    assertEquals("CREATED", foundOrder.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void updateOrder_WhenOrderExists_ReturnsUpdatedOrder() {
        Long orderId = insertTestOrder("ORDER-001", "CREATED", Instant.now());

        OrderCreateRequestDto updateRequest = new OrderCreateRequestDto();
        updateRequest.setOrderNumber("ORDER-001-UPDATED");
        updateRequest.setStatus("PROCESSING");

        Mono<OrderResponseDto> response = orderService.updateOrder(orderId, updateRequest);

        StepVerifier.create(response)
                .assertNext(order -> {
                    assertEquals(orderId, order.getId());
                    assertEquals(updateRequest.getOrderNumber(), order.getOrderNumber());
                    assertEquals(updateRequest.getStatus(), order.getStatus());
                })
                .verifyComplete();

        Mono<OrderResponseDto> result = orderService.findOrderById(orderId);

        StepVerifier.create(result)
                .assertNext(order -> {
                    assertEquals(orderId, order.getId());
                    assertEquals(updateRequest.getOrderNumber(), order.getOrderNumber());
                    assertEquals(updateRequest.getStatus(), order.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void updateOrder_WhenOrderNotExists_ThrowsOrderNotFoundException() {
        OrderCreateRequestDto updateRequest = new OrderCreateRequestDto();
        updateRequest.setOrderNumber("ORDER-001-UPDATED");
        updateRequest.setStatus("PROCESSING");

        Mono<OrderResponseDto> result = orderService.updateOrder(999L, updateRequest);

        StepVerifier.create(result)
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    @Test
    void deleteOrder_WhenOrderExists_DeletesSuccessfully() {
        Long orderId = insertTestOrder("ORDER-001", "CREATED", Instant.now());

        Mono<Void> result = orderService.deleteOrder(orderId);

        StepVerifier.create(result)
                .verifyComplete();

        StepVerifier.create(orderService.findOrderById(orderId))
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    @Test
    void deleteOrder_WhenOrderNotExists_ThrowsOrderNotFoundException() {
        Mono<Void> result = orderService.deleteOrder(999L);

        StepVerifier.create(result)
                .expectError(OrderNotFoundException.class)
                .verify();
    }

    private Long insertTestOrder(String orderNumber, String status, Instant createdAt) {
        return databaseClient.sql(
                        "INSERT INTO orders (order_number, status, created_at) " +
                        "VALUES (:orderNumber, :status, :createdAt) " +
                        "RETURNING id"
                )
                .bind("orderNumber", orderNumber)
                .bind("status", status)
                .bind("createdAt", createdAt)
                .fetch()
                .first()
                .map(row -> (Long) row.get("id"))
                .block();
    }
}
