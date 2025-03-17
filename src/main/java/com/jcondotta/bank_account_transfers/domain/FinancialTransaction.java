package com.jcondotta.bank_account_transfers.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "financial_transactions")
public class FinancialTransaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "financial_transaction_id", updatable = false, nullable = false)
    private UUID financialTransactionId;

    @Column(nullable = false)
    private UUID bankAccountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "bank_transfer_id", nullable = false)
    private BankTransfer bankTransfer;

    protected FinancialTransaction() {}

    public FinancialTransaction(BankTransfer bankTransfer, UUID bankAccountId, BigDecimal amount, String currency, TransactionType transactionType) {
        this.bankTransfer = bankTransfer;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
    }

    public UUID getFinancialTransactionId() {
        return financialTransactionId;
    }

    public void setFinancialTransactionId(UUID financialTransactionId) {
        this.financialTransactionId = financialTransactionId;
    }

    public BankTransfer getBankTransfer() {
        return bankTransfer;
    }

    public void setBankTransfer(BankTransfer bankTransfer) {
        this.bankTransfer = bankTransfer;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(UUID bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
}
