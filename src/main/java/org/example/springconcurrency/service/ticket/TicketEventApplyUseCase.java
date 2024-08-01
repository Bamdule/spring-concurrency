package org.example.springconcurrency.service.ticket;

import lombok.RequiredArgsConstructor;
import org.example.springconcurrency.domain.ticket.TicketEvent;
import org.example.springconcurrency.domain.ticket.TicketEventApplicant;
import org.example.springconcurrency.domain.ticket.TicketEventApplicantRepository;
import org.example.springconcurrency.domain.ticket.TicketEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TicketEventApplyUseCase {

    private final TicketEventRepository ticketEventRepository;
    private final TicketEventApplicantRepository ticketEventApplicantRepository;

    @Transactional
    public void apply(Long userId, Long ticketEventId) {

        TicketEvent ticketEvent = ticketEventRepository.findWithExclusiveLockById(ticketEventId)
            .orElseThrow(() -> new IllegalStateException("존재하지 않은 이벤트입니다."));

        ticketEventApplicantRepository.save(TicketEventApplicant.apply(userId, ticketEvent.provideTickets()));
    }
}
