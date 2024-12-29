package org.berneick.controller;

import org.berneick.model.WalletOperationRequest;
import org.berneick.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class WalletControllerTest {
    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(walletController).build();
    }

    @Test
    public void testPerformOperation_success() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(UUID.randomUUID(), "DEPOSIT", 100);

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType("application/json")
                        .content("{\"walletId\":\"" + request.getWalletId() + "\",\"operationType\":\"DEPOSIT\",\"amount\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Operation successful"));

        verify(walletService, times(1)).performOperation(request.getWalletId(), request.getOperationType(), request.getAmount());
    }

    @Test
    public void testPerformOperation_fail() throws Exception {
        UUID walletId = UUID.randomUUID();
        String errorMessage = "Invalid operation";

        doThrow(new RuntimeException(errorMessage))
                .when(walletService).performOperation(walletId, "DEPOSIT", 100);

        mockMvc.perform(post("/api/v1/wallet")
                .contentType("application/json")
                .content("{\"walletId\":\"" + walletId + "\",\"operationType\":\"DEPOSIT\",\"amount\":100.0}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));

        verify(walletService, times(1)).performOperation(walletId, "DEPOSIT", 100);
    }

    @Test
    public void testGetBalance_success() throws Exception {
        UUID walletId = UUID.randomUUID();
        double expectedBalance = 100.0;

        Mockito.when(walletService.getBalance(walletId)).thenReturn(expectedBalance);

        mockMvc.perform(get("/api/v1/wallet/" + walletId))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedBalance)));

        verify(walletService, times(1)).getBalance(walletId);
    }

    @Test
    public  void testGetBalance_fail() throws Exception {
        UUID walletId = UUID.randomUUID();

        Mockito.when(walletService.getBalance(walletId)).thenThrow(new RuntimeException("Wallet not found"));

        mockMvc.perform(get("/api/v1/wallet/" + walletId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(walletService, times(1)).getBalance(walletId);
    }
}