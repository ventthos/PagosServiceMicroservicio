package com.pagos.pagosservice.service;

import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Slf4j
public class RefundPagoService {

    private final PagoRepository pagoRepository;

    public Pago execute(String id) {
        log.info("Iniciando proceso de reembolso para el Pago ID: {}", id);

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Fallo de reembolso: No se encontró el registro de pago con ID {}", id);
                    return new NoSuchElementException("No se encontró el pago con ID: " + id);
                });

        if ("REFUNDED".equals(pago.getStatus())) {
            log.warn("Intento de reembolso duplicado: El pago {} ya tiene estado REFUNDED.", id);
            throw new IllegalArgumentException("El pago ya ha sido reembolsado anteriormente.");
        }

        log.info("Procesando reembolso del monto ${} para la Orden {}", pago.getAmount(), pago.getOrdenId());

        try {
            pago.setStatus("REFUNDED");
            Pago updatedPago = pagoRepository.save(pago);

            log.info("Reembolso completado exitosamente. Pago ID: {}, Nuevo Estado: {}",
                    updatedPago.getId(), updatedPago.getStatus());

            return updatedPago;

        } catch (Exception e) {
            log.error("Error crítico al guardar el estado de reembolso para el pago {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Error técnico al procesar el reembolso", e);
        }
    }
}