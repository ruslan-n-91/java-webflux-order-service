package com.example.javawebfluxorderservice.domain.mapper;

import com.example.javawebfluxorderservice.domain.model.Order;
import com.example.javawebfluxorderservice.domain.request.OrderCreateRequestDto;
import com.example.javawebfluxorderservice.domain.response.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "orderNumber", target = "orderNumber")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt")
    OrderResponseDto mapToOrderResponseDto(Order order);

    @Mapping(source = "orderNumber", target = "orderNumber")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Order mapToOrder(OrderCreateRequestDto requestDto);

    @Mapping(source = "orderNumber", target = "orderNumber")
    @Mapping(source = "status", target = "status")
    void updateOrderFromDto(OrderCreateRequestDto requestDto, @MappingTarget Order order);
}
