package com.pagos.pagosservice.repository;

import com.pagos.pagosservice.models.Pago;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PagoRepository extends MongoRepository<Pago, String> {
    List<Pago> findByOrdenId(String ordenId);
}