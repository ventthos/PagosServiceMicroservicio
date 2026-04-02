package com.pagos.pagosservice.service;

import com.pagos.pagosservice.clients.OrderClient;
import com.pagos.pagosservice.dto.ProcesarPagoDto;
import com.pagos.pagosservice.models.Order;
import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.repository.PagoRepository;
import com.pagos.pagosservice.response.GeneralResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private final PaymentProducer paymentProducer;

    @Autowired
    private MongoTemplate mongoTemplate; // 👈 AQUÍ

    public Pago execute(ProcesarPagoDto data) {

        log.info("Iniciando procesamiento de pago para la Orden: {}", data.getOrdenId());

        // 🔹 Validación de orden (igual que ya tienes)
        //validarOrden(data);

        Pago pago = Pago.builder()
                .ordenId(data.getOrdenId())
                .amount(data.getAmount())
                .paymentMethod(data.getPaymentMethod())
                .status("PROCESSED")
                .transactionDate(java.time.LocalDateTime.now().toString())
                .build();

        try {
            Pago savedPago = pagoRepository.save(pago);

            log.info("Pago guardado correctamente: {}", savedPago.getId());

            return savedPago;

        } catch (Exception e) {

            log.error("Error al guardar pago → Kafka");

            if (!data.isFromRetry()) {
                paymentProducer.sendToRetry(data);
            }

            throw new RuntimeException("Pago enviado a retry");
        }

    }

    // 👇 AQUÍ VA TU MÉTODO
    private boolean mongoDisponible() {
        try {
            mongoTemplate.executeCommand("{ ping: 1 }");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void validarOrden(ProcesarPagoDto data) {
        try {
            ResponseEntity<GeneralResponse<Order>> response =
                    orderClient.getOrderById(data.getOrdenId());

            if (response.getBody() == null) {
                throw new NoSuchElementException("La orden no existe.");
            }

        } catch (feign.FeignException.NotFound e) {
            throw new NoSuchElementException("La orden no existe.");
        }
    }
}