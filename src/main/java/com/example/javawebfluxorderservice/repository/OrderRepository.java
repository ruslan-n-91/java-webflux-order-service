package com.example.javawebfluxorderservice.repository;

import com.example.javawebfluxorderservice.domain.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, Long> {
}
