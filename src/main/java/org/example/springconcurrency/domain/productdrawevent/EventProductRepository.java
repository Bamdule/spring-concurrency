package org.example.springconcurrency.domain.productdrawevent;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventProductRepository extends JpaRepository<EventProduct, Long> {
}
