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
public class GetPagoByIdService {

    private final PagoRepository pagoRepository;

    public Pago execute(String id) {
        log.info("Consultando comprobante de pago con ID: {}", id);

        return pagoRepository.findById(id)
                .map(pago -> {
                    log.info("Pago recuperado. Orden asociada: {}, Estado: {}, Monto: ${}",
                            pago.getOrdenId(), pago.getStatus(), pago.getAmount());
                    return pago;
                })
                .orElseThrow(() -> {
                    log.warn("Fallo de consulta: El pago con ID {} no existe en la base de datos.", id);
                    return new NoSuchElementException("No se encontró el pago con el ID: " + id);
                });
    }
}