package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.api_clients;

import com.jcondotta.bank_account_transfers.application.ports.outbound.api_clients.BankAccountLookupPort;
import com.jcondotta.bank_account_transfers.infrastructure.config.BankAccountServiceV1Config;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.constraints.NotNull;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;

import java.util.Optional;

import static org.springframework.http.MediaType.*;

@Component
public class RestClientBankAccountLookupAdapter implements BankAccountLookupPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientBankAccountLookupAdapter.class);

    private final RestClient restClient;
    private final BankAccountServiceV1Config bankAccountServiceV1Config;

    public RestClientBankAccountLookupAdapter(RestClient restClient, BankAccountServiceV1Config bankAccountServiceV1Config) {
        this.restClient = restClient;
        this.bankAccountServiceV1Config = bankAccountServiceV1Config;
    }

    @Override
    @Timed(value = "bankAccount.findBankAccountByIban.time", description = "Time taken to fetch a bank account by IBAN",
            percentiles = {0.5, 0.9, 0.99})
    public Optional<BankAccountDTO> findBankAccountByIban(@NotNull(message = "bankAccount.iban.notNull") String bankAccountIban) {
        try {
            LOGGER.info("Fetching bank account by: {}", StructuredArguments.kv("iban", bankAccountIban));
            var bankAccountByIbanURI = bankAccountServiceV1Config.findBankAccountByIbanURI(bankAccountIban);

            return Optional.ofNullable(
                    restClient.get()
                            .uri(bankAccountByIbanURI)
                            .header(HttpHeaders.ACCEPT, APPLICATION_JSON_VALUE)
                            .retrieve()
                            .body(BankAccountDTO.class)
            ).map(bankAccountDTO -> {
                LOGGER.info("Bank account successfully retrieved: {}", StructuredArguments.f(bankAccountDTO));
                return bankAccountDTO;
            });
        }
        catch (RestClientResponseException e) {
            LOGGER.error("Bank account lookup failed: {} {} {}",
                    StructuredArguments.kv("status", e.getStatusCode()),
                    StructuredArguments.kv("iban", bankAccountIban),
                    StructuredArguments.kv("error", e.getResponseBodyAsString()), e);

        } catch (Exception e) {
            LOGGER.error("Unexpected error during bank account lookup: {}", StructuredArguments.kv("iban", bankAccountIban), e);
        }
        return Optional.empty();
    }
}
