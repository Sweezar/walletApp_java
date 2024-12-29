package org.berneick.service;

import org.berneick.exception.InsufficientFundsException;
import org.berneick.exception.WalletNotFoundException;
import org.berneick.model.Wallet;
import org.berneick.repository.WalletRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WalletServiceTest {
    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;


    @Test
    public void testGetBalance_success(){
        UUID walletId = UUID.randomUUID();
        Mockito.when(walletRepository.findById(walletId)).thenReturn(Optional.of(new Wallet(walletId, 100)));

        double balance = walletService.getBalance(walletId);
        assertEquals(100.0, balance);
    }

    @Test
    public void testGetBalance_fail() {
        UUID walletId = UUID.randomUUID();
        Mockito.when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> {
            walletService.getBalance(walletId);
        });
    }

    @Test
    public void testPerformOperation_success() {
        UUID walletId = UUID.randomUUID();
        Mockito.when(walletRepository.findById(walletId)).thenReturn(Optional.of(new Wallet(walletId, 100)));

        walletService.performOperation(walletId, "DEPOSIT", 150);
        double balance = walletService.getBalance(walletId);

        assertEquals(250, balance);

        walletService.performOperation(walletId, "WITHDRAW", 200);
        balance = walletService.getBalance(walletId);

        assertEquals(50, balance);
    }

    @Test
    public void testPerformOperation_fail() {
        UUID walletId = UUID.randomUUID();
        Mockito.when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () -> {
            walletService.performOperation(walletId, "WITHDRAW", 200);
        });

        Mockito.when(walletRepository.findById(walletId)).thenReturn(Optional.of(new Wallet(walletId, 100)));

        assertThrows(InsufficientFundsException.class, () -> {
            walletService.performOperation(walletId, "WITHDRAW", 200);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            walletService.performOperation(walletId, "NULL", 10);
        });
    }
}

