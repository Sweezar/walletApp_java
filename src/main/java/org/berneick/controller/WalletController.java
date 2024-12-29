package org.berneick.controller;

import org.berneick.model.WalletOperationRequest;
import org.berneick.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping
    public ResponseEntity<String> performOperation(@RequestBody WalletOperationRequest request) {
        try {
            walletService.performOperation(request.getWalletId(), request.getOperationType(), request.getAmount());
            return ResponseEntity.ok("Operation successful");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/{walletId}")
    @Cacheable("walletBalanceResponse")
    public ResponseEntity<Double> getBalance(@PathVariable UUID walletId) {
        try {
            double balance = walletService.getBalance(walletId);
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
