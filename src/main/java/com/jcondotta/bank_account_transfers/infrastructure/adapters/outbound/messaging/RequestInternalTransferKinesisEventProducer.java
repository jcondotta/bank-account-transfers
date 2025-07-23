package com.jcondotta.bank_account_transfers.infrastructure.adapters.outbound.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bank_account_transfers.application.ports.output.messaging.RequestInternalTransferEventProducer;
import com.jcondotta.bank_account_transfers.domain.bank_transfer.events.InternalTransferRequestedEvent;
import com.jcondotta.bank_account_transfers.interfaces.kinesis.request_internal_transfer.model.RequestInternalTransferEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.PutRecordRequest;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestInternalTransferKinesisEventProducer implements RequestInternalTransferEventProducer {

    private static final String STREAM_NAME = "request-internal-transfers";

    private final KinesisAsyncClient kinesisClient;
    private final ObjectMapper objectMapper;
    private final InternalTransferRequestedEventMapper mapper;

    @Override
    public void publish(InternalTransferRequestedEvent internalTransferRequestedEvent) {
        try {
            RequestInternalTransferEvent kinesisEvent = mapper.toKinesisEvent(internalTransferRequestedEvent);
            String json = objectMapper.writeValueAsString(kinesisEvent);

            PutRecordRequest request = PutRecordRequest.builder()
                .streamName(STREAM_NAME)
                .partitionKey(internalTransferRequestedEvent.internalPartySender().identifier().asString())
                .data(SdkBytes.fromString(json, StandardCharsets.UTF_8))
                .build();

            kinesisClient.putRecord(request)
                .thenAccept(response ->
                    log.info("Published internal transfer event to Kinesis: sequenceNumber={}, partitionKey={}",
                        response.sequenceNumber(),
                        request.partitionKey()
                    )
                )
                .exceptionally(ex -> {
                    log.error("Failed to publish internal transfer event to Kinesis", ex);
                    return null;
                });

        } catch (Exception e) {
            log.error("Error serializing internal transfer event", e);
        }
    }
}
