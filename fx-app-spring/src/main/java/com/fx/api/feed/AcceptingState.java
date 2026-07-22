package com.fx.api.feed;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/** In-memory accepting flag. AtomicBoolean because the feed thread reads it
 *  while an HTTP thread may flip it concurrently. Resets to ON on every restart
 *  — that is the correct behaviour for an operational switch. */
@Component
public class AcceptingState {
    private final AtomicBoolean accepting = new AtomicBoolean(true);

    public boolean isAccepting() { return accepting.get(); }

    public boolean set(boolean value) {
        accepting.set(value);
        return value;
    }
}
