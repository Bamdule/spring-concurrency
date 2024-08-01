package org.example.springconcurrency.domain.ticket;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "ticket_event_applicant",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_ticket_event_applicant_ticket_event_id_user_id",
            columnNames = {"ticket_event_id", "user_id"}
        )
    }, indexes = @Index(name = "idx_ticket_event_applicant_user_id", columnList = "user_id"))
@Entity
public class TicketEventApplicant {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "ticket_event_id", nullable = false)
    private Long ticketEventId;

    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    protected TicketEventApplicant() {
    }

    public TicketEventApplicant(Long id, Long userId, Long ticketEventId, Long ticketId) {
        this.id = id;
        this.userId = userId;
        this.ticketEventId = ticketEventId;
        this.ticketId = ticketId;
    }

    public static TicketEventApplicant apply(Long userId, TicketEvent ticketEvent) {
        return new TicketEventApplicant(null, userId, ticketEvent.getId(), ticketEvent.getTicket().getId());
    }
}
