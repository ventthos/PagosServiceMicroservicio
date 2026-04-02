package com.pagos.pagosservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagos.pagosservice.dto.ProcesarPagoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "payments_retry_jobs";

    public void sendToRetry(ProcesarPagoDto dto) {
        try {
            String message = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send(TOPIC, message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando a Kafka", e);
        }
    }
}