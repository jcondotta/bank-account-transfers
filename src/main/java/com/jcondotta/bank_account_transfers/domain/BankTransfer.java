package com.jcondotta.bank_account_transfers.domain;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bank_transfers")
public class BankTransfer {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "bank_transfer_id", updatable = false, nullable = false, unique = true)
    private UUID bankTransferId;

    @Column(name = "idempotency_key", updatable = false, nullable = false, unique = true)
    private UUID idempotencyKey;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "bankTransfer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FinancialTransaction> financialTransactions = new ArrayList<>();

    protected BankTransfer() {}

    public BankTransfer(UUID idempotencyKey, String reference, Instant createdAt) {
        this.idempotencyKey = idempotencyKey;
        this.reference = reference;
        this.createdAt = createdAt;
    }

    public UUID getBankTransferId() {
        return bankTransferId;
    }

    public UUID getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<FinancialTransaction> getFinancialTransactions() {
        return List.copyOf(financialTransactions);
    }

    public void addFinancialTransaction(FinancialTransaction transaction) {
        financialTransactions.add(transaction);
        transaction.setBankTransfer(this);
    }

    public void removeFinancialTransaction(FinancialTransaction transaction) {
        financialTransactions.remove(transaction);
        transaction.setBankTransfer(null);
    }
}
