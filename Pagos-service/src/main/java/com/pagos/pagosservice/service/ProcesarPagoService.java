package com.pagos.pagosservice.service;

import com.pagos.pagosservice.clients.OrderClient;
import com.pagos.pagosservice.dto.ProcesarPagoDto;
import com.pagos.pagosservice.models.Order;
import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.repository.PagoRepository;
import com.pagos.pagosservice.response.GeneralResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProcesarPagoService {

    private final PagoRepository pagoRepository;
    private final OrderClient orderClient;

    public Pago execute(ProcesarPagoDto data) {
        log.info("Iniciando procesamiento de pago para la Orden: {}. Monto: ${}", data.getOrdenId(), data.getAmount());

        try {
            ResponseEntity<GeneralResponse<Order>> response = orderClient.getOrderById(data.getOrdenId());

            if (response.getBody() == null) {
                throw new NoSuchElementException("La orden con ID " + data.getOrdenId() + " no existe.");
            }
        } catch (feign.FeignException.NotFound e) {
            log.warn("Feign detectó que la orden {} no existe.", data.getOrdenId());
            throw new NoSuchElementException("La orden con ID " + data.getOrdenId() + " no existe.");
        } catch (Exception e) {
            log.error("Error de comunicación con Órdenes: {}", e.getMessage());
            throw new RuntimeException("Fallo en la comunicación entre microservicios: " + e.getMessage());
        }

        Pago pago = Pago.builder()
                .ordenId(data.getOrdenId())
                .amount(data.getAmount())
                .paymentMethod(data.getPaymentMethod())
                .status("PROCESSED")
                .transactionDate(java.time.LocalDateTime.now().toString())
                .build();

        try {
            Pago savedPago = pagoRepository.save(pago);
            log.info("Pago procesado y guardado exitosamente. ID de Transacción: {}", savedPago.getId());
            return savedPago;
        } catch (Exception e) {
            log.error("Error al guardar el pago en MongoDB para la orden {}: {}", data.getOrdenId(), e.getMessage());
            throw e;
        }
    }
}