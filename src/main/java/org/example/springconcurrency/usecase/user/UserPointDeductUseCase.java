package org.example.springconcurrency.usecase.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.springconcurrency.domain.user.UserPoint;
import org.example.springconcurrency.domain.user.UserPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserPointDeductUseCase {
    private final UserPointRepository userPointRepository;

    public Long getPoints(Long userId) {
        return userPointRepository.findByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."))
            .getPoints();
    }

    @Transactional
    public Long usePoints(Long userId, Long usePoints) {

        UserPoint userPoint = userPointRepository.findWithExclusiveLockByUserId(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."));
//        UserPoint userPoint = userPointRepository.findByUserId(userId)
//            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고객입니다."));


        userPoint.use(usePoints);
        Long remainingPoints = userPoint.getPoints();

        log.info("userId : {} usePoints  : {}, remainingPoints : {}", userId, usePoints, remainingPoints);

        return remainingPoints;
    }
}
