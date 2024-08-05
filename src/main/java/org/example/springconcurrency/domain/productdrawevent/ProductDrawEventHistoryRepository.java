package org.example.springconcurrency.domain.productdrawevent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDrawEventHistoryRepository extends JpaRepository<ProductDrawEventHistory, Long> {

    Long countByProductDrawEventId(Long productDrawEventId);
}
