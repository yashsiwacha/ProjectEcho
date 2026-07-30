package com.projectecho.evidence.domain.model;

/** Indicates the origin and verifiability of an Evidence artifact. */
public enum Provenance {
  /** Self-reported by the Person without external verification. Low confidence. */
  SELF_REPORTED,

  /** Imported from an external trusted system (e.g., GitHub, Jira). Medium-High confidence. */
  SYSTEM_INGESTION,

  /** Validated explicitly by a peer or manager. High confidence. */
  PEER_VALIDATED
}
