package com.ivan.payments.controller;

import com.ivan.payments.service.PaymentService;
import com.ivan.shared.constant.HttpStatuses;
import com.ivan.shared.dto.PaymentDto;
import com.ivan.shared.entity.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Get all payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(examples = @ExampleObject(Payment.defaultJson))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @Operation(summary = "Update a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(examples = @ExampleObject(Payment.defaultJson))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @PutMapping("/update/{paymentId}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable Integer paymentId,
            @Valid @RequestBody PaymentDto paymentDto
    ) {
        return ResponseEntity.ok(paymentService.update(paymentId, paymentDto));
    }

    @Operation(summary = "Create a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = HttpStatuses.CREATED,
                    content = @Content(examples = @ExampleObject(Payment.defaultJson))),
            @ApiResponse(responseCode = "400", description = HttpStatuses.BAD_REQUEST,
                    content = @Content(examples = @ExampleObject(HttpStatuses.BAD_REQUEST))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND)))
    })
    @PostMapping
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody PaymentDto paymentDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.save(paymentDto));
    }

    @Operation(summary = "process a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.OK,
                    content = @Content(examples = @ExampleObject(Payment.defaultJson))),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND))),
            @ApiResponse(responseCode = "503", description = HttpStatuses.SERVICE_UNAVAILABLE,
                    content = @Content(examples = @ExampleObject(HttpStatuses.SERVICE_UNAVAILABLE)))

    })
    @PostMapping("/process/{paymentId}")
    public ResponseEntity<Payment> processPayment(
            @PathVariable Integer paymentId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.processPayment(paymentId));
    }

    @Operation(summary = "delete a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = HttpStatuses.NO_CONTENT),
            @ApiResponse(responseCode = "404", description = HttpStatuses.NOT_FOUND,
                    content = @Content(examples = @ExampleObject(HttpStatuses.NOT_FOUND))),
    })
    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer paymentId) {
        paymentService.deleteById(paymentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}