package org.example.springconcurrency.domain.productdrawevent;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "product_draw_event")
@Entity
public class ProductDrawEvent {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_product_id", nullable = false)
    private EventProduct eventProduct;

    @Column(name = "product_quantity", nullable = false)
    private Long productQuantity;

    @Column(name = "draw_quantity", nullable = false)
    private Long drawQuantity;

    public boolean hasEventProductDrawQuantities() {
        return this.drawQuantity > 0;
    }

    public ProductDrawEventHistory draw(Long userId) {
        if (hasEventProductDrawQuantities()) {
            drawQuantity--;
            return ProductDrawEventHistory.of(userId, this);
        }

        throw new IllegalStateException("상품 응모 개수가 모두 소진되었습니다.");
    }

    public static ProductDrawEvent of(EventProduct eventProduct, Long productQuantity, Long drawQuantity) {
        return new ProductDrawEvent(null, eventProduct, productQuantity, drawQuantity);
    }
}
