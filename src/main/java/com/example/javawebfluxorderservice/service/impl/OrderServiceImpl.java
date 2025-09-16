package com.example.javawebfluxorderservice.service.impl;

import com.example.javawebfluxorderservice.domain.mapper.OrderMapper;
import com.example.javawebfluxorderservice.domain.model.Order;
import com.example.javawebfluxorderservice.domain.request.OrderCreateRequestDto;
import com.example.javawebfluxorderservice.domain.response.OrderResponseDto;
import com.example.javawebfluxorderservice.exception.OrderNotFoundException;
import com.example.javawebfluxorderservice.repository.OrderRepository;
import com.example.javawebfluxorderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    @Override
    public Flux<OrderResponseDto> findAllOrders() {
        return orderRepository.findAll()
                .map(orderMapper::mapToOrderResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<OrderResponseDto> findOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::mapToOrderResponseDto)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Order not found by id " + id)));
    }

    @Transactional
    @Override
    public Mono<OrderResponseDto> createOrder(OrderCreateRequestDto requestDto) {
        Order newOrder = orderMapper.mapToOrder(requestDto);

        return orderRepository.save(newOrder)
                .map(orderMapper::mapToOrderResponseDto);
    }

    @Transactional
    @Override
    public Mono<OrderResponseDto> updateOrder(Long id, OrderCreateRequestDto requestDto) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Order not found by id " + id)))
                .flatMap(existingOrder -> {
                    orderMapper.updateOrderFromDto(requestDto, existingOrder);
                    return orderRepository.save(existingOrder)
                            .map(orderMapper::mapToOrderResponseDto);
                });
    }

    @Transactional
    @Override
    public Mono<Void> deleteOrder(Long id) {
        return orderRepository.findById(id)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Order not found by id " + id)))
                .flatMap(existingOrder -> orderRepository.deleteById(id));
    }
}
