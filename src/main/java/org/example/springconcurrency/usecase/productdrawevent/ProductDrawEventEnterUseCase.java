package org.example.springconcurrency.usecase.productdrawevent;

import lombok.RequiredArgsConstructor;
import org.example.springconcurrency.domain.productdrawevent.ProductDrawEvent;
import org.example.springconcurrency.domain.productdrawevent.ProductDrawEventHistoryRepository;
import org.example.springconcurrency.domain.productdrawevent.ProductDrawEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductDrawEventEnterUseCase {
    private final ProductDrawEventRepository productDrawEventRepository;
    private final ProductDrawEventHistoryRepository productDrawEventHistoryRepository;

    @Transactional
    public Long draw(Long userId, Long productDrawEventId) {
        ProductDrawEvent productDrawEvent = productDrawEventRepository.findById(productDrawEventId)
            .orElseThrow(() -> new IllegalStateException("존재하지 않는 이벤트 입니다."));

        return productDrawEventHistoryRepository.save(productDrawEvent.draw(userId))
            .getId();
    }
}
