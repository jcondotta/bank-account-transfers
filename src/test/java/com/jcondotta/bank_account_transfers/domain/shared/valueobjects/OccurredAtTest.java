package com.jcondotta.bank_account_transfers.domain.shared.valueobjects;

import com.jcondotta.bank_account_transfers.domain.shared.DomainErrorsMessages;
import com.jcondotta.bank_account_transfers.infrastructure.config.TestClockConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OccurredAtTest {

    private static final Instant FIXED_INSTANT = TestClockConfig.testFixedClock.instant();

    @ParameterizedTest
    @ValueSource(strings = {"Europe/Madrid", "UTC", "America/Sao_Paulo"})
    void shouldBuildCreatedAt_whenUsingFactoryNowMethodWithClock(ZoneId zoneId) {
        var fixedClock = Clock.fixed(FIXED_INSTANT, zoneId);

        assertThat(com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt.now(fixedClock))
            .satisfies(createdAt -> {
                assertThat(createdAt.value()).isEqualTo(FIXED_INSTANT);
                assertThat(createdAt.zoneId()).isEqualTo(zoneId);
                assertThat(createdAt.asZonedDateTime()).isEqualTo(ZonedDateTime.ofInstant(FIXED_INSTANT, zoneId));
            });
    }

    @Test
    void shouldThrowNullPointerException_whenUsingFactoryNowMethodWithClockNull() {
        assertThatThrownBy(() -> com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt.now(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(OccurredAt.CLOCK_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Europe/Madrid", "UTC", "America/Sao_Paulo"})
    void shouldBuildCreatedAt_whenUsingFactoryOfMethodWithInstantAndZone(ZoneId zoneId) {
        assertThat(com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt.of(FIXED_INSTANT, zoneId))
            .satisfies(createdAt -> {
                assertThat(createdAt.value()).isEqualTo(FIXED_INSTANT);
                assertThat(createdAt.zoneId()).isEqualTo(zoneId);
                assertThat(createdAt.asZonedDateTime()).isEqualTo(ZonedDateTime.ofInstant(FIXED_INSTANT, zoneId));
            });
    }

    @ParameterizedTest
    @ValueSource(strings = {"Europe/Madrid", "UTC", "America/Sao_Paulo"})
    void shouldBuildCreatedAt_whenUsingFactoryOfMethodWithZonedDateTime(ZoneId zoneId) {
        var zonedDateTime = ZonedDateTime.ofInstant(FIXED_INSTANT, zoneId);

        assertThat(com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt.of(zonedDateTime))
            .satisfies(createdAt -> {
                assertThat(createdAt.value()).isEqualTo(FIXED_INSTANT);
                assertThat(createdAt.zoneId()).isEqualTo(zoneId);
                assertThat(createdAt.asZonedDateTime()).isEqualTo(zonedDateTime);
            });
    }

    @Test
    void shouldThrowNullPointerException_whenUsingFactoryOfMethodWithZonedDateTimeNull() {
        assertThatThrownBy(() -> com.jcondotta.bank_account_transfers.domain.shared.valueobjects.OccurredAt.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(OccurredAt.ZONED_DATE_TIME_NOT_NULL_MESSAGE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Europe/Madrid", "UTC", "America/Sao_Paulo"})
    void shouldThrowNullPointerException_whenInstantIsNull(ZoneId zoneId) {
        assertThatThrownBy(() -> new OccurredAt(null, zoneId))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(OccurredAt.INSTANT_VALUE_NOT_NULL_MESSAGE);
    }

    @Test
    void shouldThrowNullPointerException_whenZoneIdIsNull() {
        assertThatThrownBy(() -> new OccurredAt(FIXED_INSTANT, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessage(OccurredAt.ZONE_ID_NOT_NULL_MESSAGE);
    }

//    @Test
//    void shouldBeEqualWhenSameInstantAndZone() {
//        var a = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);
//        var b = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);
//
//        assertThat(a).isEqualTo(b);
//        assertThat(a.hashCode()).isEqualTo(b.hashCode());
//    }
//
//    @Test
//    void shouldNotBeEqualWhenDifferentZoneId() {
//        var a = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);
//        var b = CreatedAt.of(FIXED_INSTANT, UTC_ZONE);
//
//        assertThat(a).isNotEqualTo(b);
//    }
//
//    @Test
//    void shouldNotBeEqualWhenDifferentInstant() {
//        var a = CreatedAt.of(FIXED_INSTANT, MADRID_ZONE);
//        var b = CreatedAt.of(FIXED_INSTANT.plusSeconds(1), MADRID_ZONE);
//
//        assertThat(a).isNotEqualTo(b);
//    }
}
