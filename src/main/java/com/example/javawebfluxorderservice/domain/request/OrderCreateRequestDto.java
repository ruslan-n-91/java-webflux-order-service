package com.example.javawebfluxorderservice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {
    private String orderNumber;
    private String status;
}
