package com.jcondotta.bank_account_transfers.interfaces.kinesis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcondotta.bank_account_transfers.application.usecases.create_internal_transfer.CreateInternalTransferUseCase;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.kinesis.KinesisClient;
import software.amazon.awssdk.services.kinesis.model.*;
import software.amazon.awssdk.services.kinesis.model.Record;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BankTransferKinesisListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankTransferKinesisListener.class);
    private static final String STREAM_NAME = "request-internal-transfers";

    private final KinesisClient kinesisClient;
    private final CreateInternalTransferUseCase useCase;
    private final ObjectMapper objectMapper;

    private String shardIterator;

    @PostConstruct
    public void init() {
        String shardId = kinesisClient.describeStream(DescribeStreamRequest.builder()
                .streamName(STREAM_NAME)
                .build())
            .streamDescription()
            .shards()
            .get(0)
            .shardId();

        GetShardIteratorResponse iteratorResponse = kinesisClient.getShardIterator(GetShardIteratorRequest.builder()
            .streamName(STREAM_NAME)
            .shardId(shardId)
            .shardIteratorType(ShardIteratorType.LATEST) // ou TRIM_HORIZON se quiser todos os eventos
            .build());

        shardIterator = iteratorResponse.shardIterator();
        LOGGER.info("Initialized Kinesis shard iterator");
    }



    @Scheduled(fixedDelay = 10000) // a cada 3 segundos
    public void pollKinesisStream() {
        if (shardIterator == null) return;

        GetRecordsResponse response = kinesisClient.getRecords(GetRecordsRequest.builder()
            .shardIterator(shardIterator)
            .limit(10)
            .build());

        List<Record> records = response.records();
        shardIterator = response.nextShardIterator();

        for (Record record : records) {
            ByteBuffer buffer = record.data().asByteBuffer();
            String message = StandardCharsets.UTF_8.decode(buffer).toString();
            LOGGER.info("Received Kinesis event: {}", message);

            try {
                CreateInternalTransferKinesisEvent createInternalTransferKinesisEvent = objectMapper.readValue(message, CreateInternalTransferKinesisEvent.class);
                useCase.execute(createInternalTransferKinesisEvent.toCommand());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            // aqui você pode processar o JSON: usar Jackson, etc.
        }
    }
}
