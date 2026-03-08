package com.pagos.pagosservice.service;

import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefundPagoService {

    private final PagoRepository pagoRepository;

    public Pago execute(String id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pago con ID: " + id));

        if ("REFUNDED".equals(pago.getStatus())) {
            throw new RuntimeException("El pago ya ha sido reembolsado anteriormente.");
        }

        pago.setStatus("REFUNDED");

        return pagoRepository.save(pago);
    }
}