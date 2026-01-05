package ee.mihkel.veebipood.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    // @ManyToMany xx, sest siis peaks olema siin List<Product>
    // @ManyToOne --> kellelgi teisel OrderRow-l võib olla sama toode
    // @OneToMany xx, sest siis peaks olema siin List<Product>
    // @OneToOne --> kellelgi teisel ei tohi olla, üksühene seos
    @ManyToOne
    private Product product;
}
