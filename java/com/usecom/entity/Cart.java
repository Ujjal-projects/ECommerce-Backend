package com.usecom.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItems> cartItems = new HashSet<>();

    private double totalSellingPrice;

    private double totalMrpPrice;  // Changed from int to double for accuracy

    private double discount;  // Changed from int to double

    private int totalItem;

    @Column(nullable = true)
    private String couponCode;
}
