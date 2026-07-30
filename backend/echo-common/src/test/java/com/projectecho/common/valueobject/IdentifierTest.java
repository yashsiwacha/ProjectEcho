package com.projectecho.common.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class IdentifierTest {

  @Test
  void shouldGenerateUniqueIdentifiers() {
    Identifier id1 = Identifier.generate();
    Identifier id2 = Identifier.generate();

    assertNotNull(id1.value());
    assertNotEquals(id1, id2);
  }

  @Test
  void shouldCreateFromString() {
    String uuidStr = "550e8400-e29b-41d4-a716-446655440000";
    Identifier id = Identifier.from(uuidStr);

    assertEquals(UUID.fromString(uuidStr), id.value());
  }

  @Test
  void shouldThrowWhenNullValueProvided() {
    assertThrows(NullPointerException.class, () -> new Identifier(null));
  }
}
