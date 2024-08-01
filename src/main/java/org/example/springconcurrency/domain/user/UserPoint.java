package org.example.springconcurrency.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class UserPoint {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private Long points;

    @Column(name = "user_id")
    private Long userId;

    public void use(Long usePoints) {
        if (this.points < usePoints) {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }
        this.points -= usePoints;
    }
}
