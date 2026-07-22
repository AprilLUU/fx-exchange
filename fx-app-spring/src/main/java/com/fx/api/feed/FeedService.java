package com.fx.api.feed;

import com.fx.api.repo.RateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FeedService {

    private static final Logger log = LoggerFactory.getLogger(FeedService.class);

    private final RateRepository rates;
    private final RestTemplate http;
    private final AcceptingState acceptingState;
    private final String orchestratorUrl;

    public FeedService(RateRepository rates,
                       RestTemplate http,
                       AcceptingState acceptingState,
                       @Value("${fx.orchestrator.url}") String orchestratorUrl) {
        this.rates = rates;
        this.http = http;
        this.acceptingState = acceptingState;
        this.orchestratorUrl = orchestratorUrl;
    }

    public void handle(IncomingBatch batch) {
        String status;

        if (acceptingState.isAccepting()) {
            int stored = 0;
            for (IncomingRate r : batch.rates()) {
                rates.insert(r.base(), r.quote(), r.rate());
                stored++;
            }
            log.info("Batch #{}: stored {} rates", batch.batchId(), stored);
            status = "ACCEPTED";
        } else {
            log.info("Batch #{}: DECLINED (accepting=false)", batch.batchId());
            status = "DECLINED";
        }

        sendAck(batch.batchId(), status);
    }

    private void sendAck(long batchId, String status) {
        try {
            http.postForEntity(
                    orchestratorUrl + "/api/feed/ack",
                    Map.of("batchId", batchId, "status", status),
                    Void.class);
        } catch (Exception e) {
            // A failed callback must never break the request we're serving.
            log.warn("ACK failed for batch #{}: {}", batchId, e.getMessage());
        }
    }
}
