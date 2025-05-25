package com.jcondotta.bank_account_transfers.application.ports.outbound.cache;

public enum CacheAction {

    PUT("put"),
    PUT_IF_ABSENT("putIfAbsent"),
    PUT_IF_ABSENT_OR_STALE("putIfAbsentOrStale");

    private final String display;

    CacheAction(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
