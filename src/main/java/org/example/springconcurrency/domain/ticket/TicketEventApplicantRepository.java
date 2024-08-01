package org.example.springconcurrency.domain.ticket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketEventApplicantRepository extends JpaRepository<TicketEventApplicant, Long> {
}
