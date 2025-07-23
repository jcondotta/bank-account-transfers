package com.jcondotta.bank_account_transfers.application.ports.output;

import java.util.Collection;
import java.util.function.Function;

@FunctionalInterface
public interface ValidationErrorConverterPort<T, R> extends Function<Collection<T>, Collection<R>> {

}
