package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients.bank_accounts.BankAccountDTO;
import jakarta.validation.constraints.NotNull;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class BankAccountRestClientLookupAdapter implements BankAccountLookupPort<String, BankAccountDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountRestClientLookupAdapter.class);

    private final RestClient restClient;
    private final BankAccountServiceConfig bankAccountServiceConfig;

    public BankAccountRestClientLookupAdapter(RestClient restClient, BankAccountServiceConfig bankAccountServiceConfig) {
        this.restClient = restClient;
        this.bankAccountServiceConfig = bankAccountServiceConfig;
    }

    @Override
    public Optional<BankAccountDTO> findBankAccount(@NotNull(message = "bankAccount.iban.notNull") String bankAccountIban) {
        try {
            LOGGER.atInfo().setMessage("Fetching by bank account iban: {}")
                    .addArgument(bankAccountIban)
                    .addKeyValue("bankAccountIban", bankAccountIban)
                    .log();

            var bankAccountByIbanURI = bankAccountServiceConfig.findBankAccountByIbanURI(bankAccountIban);

            return Optional.ofNullable(
                    restClient.get()
                            .uri(bankAccountByIbanURI)
                            .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                            .retrieve()
                            .body(BankAccountDTO.class)
//                    new BankAccountDTO(UUID.rand+mUUID(), bankAccountIban)
            ).map(bankAccountDTO -> {
                LOGGER.warn("Bank account successfully retrieved: {}", StructuredArguments.f(bankAccountDTO));
                return bankAccountDTO;
            });
        }
        catch (HttpClientErrorException.NotFound e) {
            LOGGER.atWarn().setMessage("Bank account not found for IBAN: {}")
                    .addArgument(bankAccountIban)
                    .addKeyValue("bankAccountIban", bankAccountIban)
                    .log();
            return Optional.empty();
        }
        catch (RestClientResponseException e) {
            LOGGER.atError().setMessage("Bank account lookup failed: {} {} {}")
                    .addArgument(e.getStatusCode())
                    .addArgument(bankAccountIban)
                    .addArgument(e.getResponseBodyAsString())
                    .setCause(e)
                    .log();
            System.out.println(e.getMessage());
//            LOGGER.error("Bank account lookup failed: {} {} {}",
//                    StructuredArguments.kv("status", e.getStatusCode()),
//                    StructuredArguments.kv("iban", bankAccountIban),
//                    StructuredArguments.kv("error", e.getResponseBodyAsString()), e);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            LOGGER.atError().setMessage("Unexpected error during lookup by bank account iban: {}")
                    .addArgument(bankAccountIban)
                    .addKeyValue("bankAccountIban", bankAccountIban)
                    .log();
        }
        return Optional.empty();
    }
}
