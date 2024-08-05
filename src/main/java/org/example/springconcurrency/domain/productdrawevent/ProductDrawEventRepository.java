package org.example.springconcurrency.domain.productdrawevent;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ProductDrawEventRepository extends JpaRepository<ProductDrawEvent, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductDrawEvent> findWithExclusiveLockById(Long id);

    @Lock(LockModeType.OPTIMISTIC)
    Optional<ProductDrawEvent> findWithOptimisticLockById(Long id);

}
