package com.example.javawebfluxorderservice.service;

import com.example.javawebfluxorderservice.domain.request.OrderCreateRequestDto;
import com.example.javawebfluxorderservice.domain.response.OrderResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {

    Flux<OrderResponseDto> findAllOrders();

    Mono<OrderResponseDto> findOrderById(Long id);

    Mono<OrderResponseDto> createOrder(OrderCreateRequestDto requestDto);

    Mono<OrderResponseDto> updateOrder(Long id, OrderCreateRequestDto requestDto);

    Mono<Void> deleteOrder(Long id);
}
