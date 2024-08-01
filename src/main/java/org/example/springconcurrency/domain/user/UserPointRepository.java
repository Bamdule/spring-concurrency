package org.example.springconcurrency.domain.user;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface UserPointRepository extends JpaRepository<UserPoint, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<UserPoint> findWithExclusiveLockByUserId(Long userId);

    Optional<UserPoint> findByUserId(Long userId);

}
