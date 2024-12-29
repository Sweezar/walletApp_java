package org.berneick.service;

import org.berneick.model.Wallet;
import org.berneick.exception.WalletNotFoundException;
import org.berneick.exception.InsufficientFundsException;
import org.berneick.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public void performOperation(UUID walletId, String operationType, double amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        if("DEPOSIT".equals(operationType)) {
            wallet.setBalance(wallet.getBalance() + amount);
        } else if("WITHDRAW".equals(operationType)) {
            if(wallet.getBalance() < amount) {
                throw new InsufficientFundsException("Insufficient funds");
            }
            wallet.setBalance(wallet.getBalance() - amount);
        } else {
            throw new IllegalArgumentException("Invalid operation type");
        }

        walletRepository.save(wallet);
    }

    public double getBalance(UUID walletId) {
        return walletRepository.findById(walletId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
    }
}
