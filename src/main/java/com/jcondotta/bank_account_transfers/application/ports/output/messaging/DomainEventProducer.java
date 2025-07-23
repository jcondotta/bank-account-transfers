package com.jcondotta.bank_account_transfers.application.ports.output.messaging;

//public interface DomainEventProducer<T extends DomainEvent> {
public interface DomainEventProducer<T extends Object> {
    void publish(T event);
}