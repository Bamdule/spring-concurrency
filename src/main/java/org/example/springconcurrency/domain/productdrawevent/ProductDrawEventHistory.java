package org.example.springconcurrency.domain.productdrawevent;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "product_draw_event_history",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_product_draw_event_history_product_draw_event_id_user_id",
            columnNames = {"product_draw_event_id", "user_id"}
        )
    }, indexes = @Index(name = "idx_product_draw_event_history_user_id", columnList = "user_id"))
@Entity
public class ProductDrawEventHistory {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_draw_event_id", nullable = false)
    private ProductDrawEvent productDrawEvent;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "is_winner")
    private boolean isWinner;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    public static ProductDrawEventHistory of(Long userId, ProductDrawEvent productDrawEvent) {
        return new ProductDrawEventHistory(null, productDrawEvent, userId, false, LocalDateTime.now());
    }
}
