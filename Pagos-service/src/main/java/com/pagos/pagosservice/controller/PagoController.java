package com.pagos.pagosservice.controller;

import com.pagos.pagosservice.dto.ProcesarPagoDto;
import com.pagos.pagosservice.models.Pago;
import com.pagos.pagosservice.response.GeneralResponse;
import com.pagos.pagosservice.service.GetPagoByIdService;
import com.pagos.pagosservice.service.GetPagoByOrdenIdService;
import com.pagos.pagosservice.service.ProcesarPagoService;
import com.pagos.pagosservice.service.RefundPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final ProcesarPagoService procesarPagoService;
    private final GetPagoByIdService getPagoByIdService;
    private final GetPagoByOrdenIdService getPagoByOrdenIdService;
    private final RefundPagoService refundPagoService;

    @PostMapping("/procesar")
    public ResponseEntity<GeneralResponse<Pago>> procesarPago(@RequestBody ProcesarPagoDto dto) {
        Pago nuevoPago = procesarPagoService.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GeneralResponse.<Pago>builder()
                        .status("SUCCESS")
                        .message("Pago procesado correctamente")
                        .data(nuevoPago)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse<Pago>> getPagoById(@PathVariable String id) {
        Pago pago = getPagoByIdService.execute(id);
        return ResponseEntity.ok(GeneralResponse.<Pago>builder()
                .status("SUCCESS")
                .message("Información del pago recuperada")
                .data(pago)
                .build());
    }

    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<GeneralResponse<List<Pago>>> getPagoByOrdenId(@PathVariable String ordenId) {
        List<Pago> pago = getPagoByOrdenIdService.execute(ordenId);
        return ResponseEntity.ok(GeneralResponse.<List<Pago>>builder()
                .status("SUCCESS")
                .message("Pagos de la orden encontrados")
                .data(pago)
                .build());
    }

    @PutMapping("/{id}/reembolso")
    public ResponseEntity<GeneralResponse<Pago>> refundPago(@PathVariable String id) {

        Pago pagoReembolsado = refundPagoService.execute(id);

        return ResponseEntity.ok(
                GeneralResponse.<Pago>builder()
                        .status("SUCCESS")
                        .message("Reembolso procesado correctamente")
                        .data(pagoReembolsado)
                        .build()
        );
    }
}