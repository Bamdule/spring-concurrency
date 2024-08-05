package org.example.springconcurrency.usecase.productdrawevent;

import org.assertj.core.api.Assertions;
import org.example.springconcurrency.domain.productdrawevent.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class ProductDrawEventEnterUseCaseTest {

    @Autowired
    public EventProductRepository eventProductRepository;

    @Autowired
    public ProductDrawEventRepository productDrawEventRepository;

    @Autowired
    public ProductDrawEventHistoryRepository productDrawEventHistoryRepository;

    @Autowired
    public ProductDrawEventEnterUseCase productDrawEventEnterUseCase;

    @BeforeEach
    public void setup() {
        productDrawEventHistoryRepository.deleteAll();
        productDrawEventRepository.deleteAll();
        eventProductRepository.deleteAll();
    }

    @Test
    public void 동시에_10만번_이벤트상품_응모_요청_성공_테스트() throws InterruptedException {
        final String eventProductName = "Apple 맥북 프로 14 M2";
        final long productQuantity = 10L;
        final long drawQuantity = 1000L;

        final int nThreads = 20;

        final EventProduct eventProduct = EventProduct.of(eventProductName);

        // 미리 이벤트 상품과 상품 응모 이벤트를 생성한다.
        eventProductRepository.save(eventProduct);
        ProductDrawEvent productDrawEvent = productDrawEventRepository.save(ProductDrawEvent.of(eventProduct, productQuantity, drawQuantity));

        List<Callable<Long>> tasks = new ArrayList<>();

        // task 목록을 생성한다
        for (long index = 0; index < drawQuantity; index++) {
            final long userId = index;
            tasks.add(() -> productDrawEventEnterUseCase.draw(userId, productDrawEvent.getId()));
        }

        // 스레드 개수롤 고정적으로 nThreads 만큼 생성
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        // 멀티 스레드로 동시에 상품 응모를 진행한다.
        List<Future<Long>> futures = executorService.invokeAll(tasks);

        // 상품 응모 결과 확인
        for (Future<Long> future : futures) {
            // 예외가 발생하지 않았는지 확인
            assertThatCode(future::get)
                .doesNotThrowAnyException();
        }

        // 작업 종료
        executorService.shutdown();

        ProductDrawEvent resultProductDrawEvent = productDrawEventRepository.findById(eventProduct.getId()).get();
        long productDrawEventHistories = productDrawEventHistoryRepository.countByProductDrawEventId(eventProduct.getId());
        Assertions.assertThat(resultProductDrawEvent.getDrawQuantity()).isEqualTo(0L);
        Assertions.assertThat(productDrawEventHistories).isEqualTo(drawQuantity);
    }
}