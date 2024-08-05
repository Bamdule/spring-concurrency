package org.example.springconcurrency.usecase;

import org.assertj.core.api.Assertions;
import org.example.springconcurrency.domain.user.UserPoint;
import org.example.springconcurrency.domain.user.UserPointRepository;
import org.example.springconcurrency.usecase.user.UserPointDeductUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThatCode;

@ActiveProfiles("test")
@SpringBootTest
class UserPointDeductUseCaseTest {

    @Autowired
    public UserPointDeductUseCase userPointDeductUseCase;

    @Autowired
    public UserPointRepository userPointRepository;

    @BeforeEach
    public void setup() {
        userPointRepository.deleteAll();
    }

    @Test
    public void 포인트_차감_테스트() throws InterruptedException {
        final int numberOfThreads = 200; // 동시 신청할 스레드 수
        int taskCount = 10000;
        UserPoint userPoint = userPointRepository.save(new UserPoint(null, 10000L, 1L));


        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(taskCount);
        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < taskCount; i++) {
            executorService.execute(() -> {
                try {
                    startLatch.await();
                    userPointDeductUseCase.usePoints(userPoint.getUserId(), 1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        boolean completed = doneLatch.await(100, TimeUnit.SECONDS);
    }

    @Test
    public void 포인트_차감_테스트2() throws InterruptedException {
        final int numberOfThreads = 200; // 동시 신청할 스레드 수
        int taskCount = 9000;

        UserPoint userPoint = userPointRepository.save(new UserPoint(null, 10000L, 1L));

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch doneLatch = new CountDownLatch(taskCount);
        Callable<Void> task = () -> {
            try {
                userPointDeductUseCase.usePoints(userPoint.getUserId(), 1L);
            } catch (Exception e) {
                throw e;
            } finally {
                doneLatch.countDown(); // 작업 완료
            }
            return null;
        };

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            tasks.add(task);
        }


        executorService.invokeAll(tasks);
        boolean completed = doneLatch.await(100, TimeUnit.SECONDS);
        UserPoint resultUserPoint = userPointRepository.findByUserId(1L)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않은 회원"));


        Assertions.assertThat(completed).isTrue();
        Assertions.assertThat(resultUserPoint.getPoints()).isEqualTo(1000L);
    }

    @Test
    public void 동시에_포인트_차감_테스트() throws InterruptedException {
        // given
        long userId = 1;
        long remainingPoints = 110L;
        int taskCount = 100;
        long usePoints = 1L;
        int threads = 10;

        // 스레드 실행기 생성
        ExecutorService executorService = Executors.newScheduledThreadPool(threads);

        // 미리 회원 포인트 엔티티를 생성한다.
        userPointRepository.save(new UserPoint(null, remainingPoints, userId));

        List<Callable<Long>> tasks = new ArrayList<>();

        //태스크를 미리 정의한다.
        for (int index = 0; index < taskCount; index++) {
            tasks.add((() -> userPointDeductUseCase.usePoints(userId, usePoints)));
        }

        // when
        //회원 포인트 차감 태스크 호출
        List<Future<Long>> futures = executorService.invokeAll(tasks);


        //then
        // 결과 확인
        for (Future<Long> future : futures) {
            //예외가 발생하지 않았는지 확인
            // 포인트가 부족하면 예외가 발생한다.
            assertThatCode(future::get)
                .doesNotThrowAnyException();
        }

        // 스레드 실행기 종료
        executorService.shutdown();

        UserPoint resultsUserPoint = userPointRepository.findByUserId(userId).get();
        Assertions.assertThat(resultsUserPoint.getPoints()).isEqualTo(remainingPoints - (taskCount * usePoints));
    }
}