package com.pagos.pagosservice.service;

import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetPagoByOrdenIdService {

    private final PagoRepository pagoRepository;

    public List<Pago> execute(String ordenId) {
        List<Pago> pagos = pagoRepository.findByOrdenId(ordenId);

        if (pagos.isEmpty()) {
            throw new RuntimeException("No se encontraron pagos asociados a la orden: " + ordenId);
        }

        return pagos;
    }
}