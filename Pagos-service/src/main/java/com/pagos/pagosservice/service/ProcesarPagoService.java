package com.pagos.pagosservice.service;

import com.pagos.pagosservice.clients.OrderClient;
import com.pagos.pagosservice.dto.ProcesarPagoDto;
import com.pagos.pagosservice.models.Order;
import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.repository.PagoRepository;
import com.pagos.pagosservice.response.GeneralResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcesarPagoService {

    private final PagoRepository pagoRepository;
    private final OrderClient orderClient;

    public Pago execute(ProcesarPagoDto data) {
        try {
            ResponseEntity<GeneralResponse<Order>> response = orderClient.getOrderById(data.getOrdenId());

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new RuntimeException("La orden no existe o el servicio de órdenes falló.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio de órdenes: " + e.getMessage());
        }

        Pago pago = Pago.builder()
                .ordenId(data.getOrdenId())
                .amount(data.getAmount())
                .paymentMethod(data.getPaymentMethod())
                .status("PROCESSED")
                .transactionDate(java.time.LocalDateTime.now().toString())
                .build();

        return pagoRepository.save(pago);
    }
}