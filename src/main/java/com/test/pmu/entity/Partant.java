package com.test.pmu.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@Table(name = "partant_t")
public class Partant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private Integer number;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partant)) return false;
        Partant partant = (Partant) o;
        return nom.equals(partant.nom) && number.equals(partant.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, number);
    }
}
