package org.example.springconcurrency.domain.ticket;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "ticket_event")
@Entity
public class TicketEvent {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(name = "amount", nullable = false)
    private Long amount;

    public boolean hasTickets() {
        return this.amount > 0;
    }

    public TicketEvent provideTickets() {
        if (hasTickets()) {
            amount--;
            return this;
        }

        throw new IllegalStateException("남아있는 티켓이 없습니다");
    }

    protected TicketEvent() {
    }

    public TicketEvent(Long id, Ticket ticket, Long amount) {
        this.id = id;
        this.ticket = ticket;
        this.amount = amount;
    }
}
