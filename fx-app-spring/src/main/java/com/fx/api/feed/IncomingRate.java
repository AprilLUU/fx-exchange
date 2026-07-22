package com.fx.api.feed;

/** One rate tick in an incoming batch from fx-orchestrator.
 *  JSON: {"base":"EUR","quote":"USD","rate":1.0837}
 */
public record IncomingRate(String base, String quote, double rate) {}
