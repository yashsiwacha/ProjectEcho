package com.projectecho.common.event;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class BaseDomainEventTest {

  // Concrete implementation for testing the abstract class
  static class TestDomainEvent extends BaseDomainEvent {}

  @Test
  void shouldAutoGenerateIdAndTimestamp() {
    TestDomainEvent event = new TestDomainEvent();

    assertNotNull(event.getEventId());
    assertNotNull(event.getOccurredAt());
  }
}
