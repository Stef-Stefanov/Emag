package com.example.emag.model.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode
public class OrderProductKey implements Serializable {
    //todo
    @Column(name = "order_id")
    Long orderId;
    @Column(name = "product_id")
    Long productId;
}
