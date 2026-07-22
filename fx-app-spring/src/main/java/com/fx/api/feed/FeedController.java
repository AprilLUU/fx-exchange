package com.fx.api.feed;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** Receives incoming rate batches from fx-orchestrator. */
@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feed;

    public FeedController(FeedService feed) { this.feed = feed; }

    @PostMapping("/rates")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void receive(@RequestBody IncomingBatch batch) {
        feed.handle(batch);
    }
}
