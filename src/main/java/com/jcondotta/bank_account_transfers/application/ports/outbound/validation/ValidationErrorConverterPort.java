package com.jcondotta.bank_account_transfers.application.ports.outbound.validation;

import java.util.Collection;
import java.util.function.Function;

@FunctionalInterface
public interface ValidationErrorConverterPort<T, R> extends Function<Collection<T>, Collection<R>> {

}
