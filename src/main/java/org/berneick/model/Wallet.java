package org.berneick.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "wallets", indexes = @Index(name = "idx_wallet_id", columnList = "id"))
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private double balance;

    public Wallet() {
    }

    public Wallet(UUID id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
