package org.example.springconcurrency.domain.ticket;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Ticket {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    protected Ticket() {
    }

    public Ticket(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
