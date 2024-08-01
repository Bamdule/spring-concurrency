package org.example.springconcurrency.domain.ticket;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface TicketEventRepository extends JpaRepository<TicketEvent, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<TicketEvent> findWithExclusiveLockById(Long id);
}
