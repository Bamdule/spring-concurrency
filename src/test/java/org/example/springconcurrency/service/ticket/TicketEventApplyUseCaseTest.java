package org.example.springconcurrency.service.ticket;

import ch.qos.logback.core.testUtil.RandomUtil;
import org.assertj.core.api.Assertions;
import org.example.springconcurrency.domain.ticket.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
class TicketEventApplyUseCaseTest {

    @Autowired
    public TicketEventRepository ticketEventRepository;
    @Autowired
    public TicketEventApplyUseCase ticketEventApplyUseCase;
    @Autowired
    public TicketRepository ticketRepository;

    @Autowired
    public TicketEventApplicantRepository ticketEventApplicantRepository;

    @BeforeEach
    public void setup() {
        ticketEventApplicantRepository.deleteAll();
        ticketEventRepository.deleteAll();
        ticketRepository.deleteAll();

    }

    @Test
    public void testConcurrentTicketApplication() throws InterruptedException {
        Ticket ticket = ticketRepository.save(new Ticket(null, "TEST"));
        TicketEvent event = new TicketEvent(null, ticket, 10000L);
        ticketEventRepository.save(event);

//        ticketEventRepository.flush();


        int numberOfThreads = 200; // 동시 신청할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(10000);

        for (int i = 0; i < 10000; i++) {
            long userId = i + 1; // userId는 1부터 시작하여 중복되지 않게
            executorService.execute(() -> {
                try {
                    startLatch.await();
                    ticketEventApplyUseCase.apply((long) RandomUtil.getPositiveInt(), event.getId());
                } catch (Exception e) {
                } finally {
                    doneLatch.countDown();
                }
            });
        }
        // 모든 스레드가 동시에 시작되도록 시작 래치를 내려줌
        startLatch.countDown();
        // 모든 스레드가 작업을 마칠 때까지 대기
        boolean completed = doneLatch.await(100, TimeUnit.SECONDS);
        Assertions.assertThat(completed).isTrue();
//        executorService.shutdown();
//        ticketEventApplicantRepository.findAll();
    }


    @Test
    public void test2() throws InterruptedException, ExecutionException {
        Ticket ticket = ticketRepository.save(new Ticket(null, "TEST"));
        TicketEvent event = new TicketEvent(null, ticket, 5L);
        ticketEventRepository.save(event);

        int taskCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < taskCount; i++) {
            tasks.add((Callable) () -> {
                ticketEventApplyUseCase.apply((long) RandomUtil.getPositiveInt(), event.getId());
                return null;
            });
        }

        System.out.println("-------작업 실행------ ");
        List<Future<Void>> futures = executorService.invokeAll(tasks);
        System.out.println("-------작업 종료------\n");

        System.out.println("-------결과 출력------ ");
        for (Future<Void> future : futures) {
            System.out.println(future.get());

            Assertions.assertThatThrownBy(() -> future.get()).isInstanceOf(IllegalStateException.class);
        }

        executorService.shutdown();

//
//        int numberOfThreads = 200; // 동시 신청할 스레드 수
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch startLatch = new CountDownLatch(1);
//        CountDownLatch doneLatch = new CountDownLatch(10000);
//
//        for (int i = 0; i < 10000; i++) {
//            long userId = i + 1; // userId는 1부터 시작하여 중복되지 않게
//            executorService.execute(() -> {
//                try {
//                    startLatch.await();
//                    ticketEventApplyUseCase.apply((long) RandomUtil.getPositiveInt(), event.getId());
//                } catch (Exception e) {
//                } finally {
//                    doneLatch.countDown();
//                }
//            });
//        }
//        // 모든 스레드가 동시에 시작되도록 시작 래치를 내려줌
//        startLatch.countDown();
//        // 모든 스레드가 작업을 마칠 때까지 대기
//        boolean completed = doneLatch.await(100, TimeUnit.SECONDS);
//        Assertions.assertThat(completed).isTrue();
//        executorService.shutdown();
//        ticketEventApplicantRepository.findAll();
    }


}