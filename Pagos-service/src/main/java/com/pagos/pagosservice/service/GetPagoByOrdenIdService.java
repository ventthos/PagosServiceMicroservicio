package com.pagos.pagosservice.service;

import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Slf4j
public class GetPagoByOrdenIdService {

    private final PagoRepository pagoRepository;

    public List<Pago> execute(String ordenId) {
        log.info("Consultando historial de pagos para la Orden ID: {}", ordenId);

        try {
            List<Pago> pagos = pagoRepository.findByOrdenId(ordenId);

            if (pagos.isEmpty()) {
                log.warn("No se encontraron registros de pago para la Orden ID: {}", ordenId);
                throw new NoSuchElementException("No se encontraron pagos asociados a la orden: " + ordenId);
            }

            log.info("Consulta exitosa: se encontraron {} registros de pago para la orden {}.",
                    pagos.size(), ordenId);

            return pagos;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error crítico al recuperar pagos de la orden {}: {}", ordenId, e.getMessage(), e);
            throw new RuntimeException("Error interno al consultar el historial de pagos", e);
        }
    }
}