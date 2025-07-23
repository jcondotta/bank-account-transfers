package com.jcondotta.bank_account_transfers.application;

public enum TestAccountDetails {

    JEFFERSON("Jefferson Condotta", "ES3801283316232166447417"),
    PATRIZIO("Patrizio Condotta", "IT93 Q030 0203 2801 7517 1887 19 3"),
    VIRGINIO("Virginio Condotta", "IT49 W030 0203 2801 1452 4628 85 7");

    private final String recipientName;
    private final String recipientIban;

    TestAccountDetails(String name, String iban) {
        this.recipientName = name;
        this.recipientIban = iban;
    }

    public String getName() {
        return recipientName;
    }

    public String getIban() {
        return recipientIban;
    }

    @Override
    public String toString() {
        return "TestRecipient{" +
                "name='" + recipientName + '\'' +
                ", iban='" + recipientIban + '\'' +
                '}';
    }
}