package com.fx.api.feed;

import java.time.Instant;
import java.util.List;

/** Batch posted by fx-orchestrator to POST /api/feed/rates.
 *  JSON: {"batchId":12,"generatedAt":"2026-07-22T09:31:04.220Z","rates":[...]}
 */
public record IncomingBatch(long batchId, Instant generatedAt, List<IncomingRate> rates) {}
