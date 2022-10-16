package com.example.emag.model.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "discounts")
@Data
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private int discountPercentage;
    @Column
    private LocalDateTime startDate;
    @Column
    private LocalDateTime expireDate;

}
