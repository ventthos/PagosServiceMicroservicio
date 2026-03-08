package com.pagos.pagosservice.clients;

import com.pagos.pagosservice.models.Order;
import com.pagos.pagosservice.response.GeneralResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ordenservice")
public interface OrderClient {

    @GetMapping("/ordenes/{id}")
    ResponseEntity<GeneralResponse<Order>> getOrderById(@PathVariable("id") String id);
}