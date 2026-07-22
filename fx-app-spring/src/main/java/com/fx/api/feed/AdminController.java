package com.fx.api.feed;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** Operational toggle consumed by fx-monitor's ACCEPTING button. */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AcceptingState acceptingState;

    public AdminController(AcceptingState acceptingState) {
        this.acceptingState = acceptingState;
    }

    @GetMapping("/accepting")
    public Map<String, Boolean> getAccepting() {
        return Map.of("accepting", acceptingState.isAccepting());
    }

    @PostMapping("/accepting")
    public Map<String, Boolean> setAccepting(@RequestBody Map<String, Boolean> body) {
        boolean value = Boolean.TRUE.equals(body.get("accepting"));
        return Map.of("accepting", acceptingState.set(value));
    }
}
