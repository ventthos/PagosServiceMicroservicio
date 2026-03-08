package com.pagos.pagosservice.service;

import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetPagoByIdService {

    private final PagoRepository pagoRepository;

    public Pago execute(String id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pago con el ID: " + id));
    }
}