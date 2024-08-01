package org.example.springconcurrency.controller;

import lombok.RequiredArgsConstructor;
import org.example.springconcurrency.service.ticket.TicketEventApplyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/ticket-events")
@RestController
public class TicketEventApplyController {
    private static final int MIN = 1;
    private static final int MAX = 10000000;
    private final TicketEventApplyUseCase ticketEventApplyUseCase;

    @PostMapping(value = "/{ticketEventId}/apply")
    ResponseEntity<Void> applyTicketEvent(
        @PathVariable(value = "ticketEventId") Long ticketEventId) {
        ticketEventApplyUseCase.apply(generateRandomNumber(), ticketEventId);
        return ResponseEntity.noContent().build();
    }

    public long generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(MAX - MIN + 1) + MIN;
    }
}
