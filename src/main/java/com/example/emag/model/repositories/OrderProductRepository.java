package com.example.emag.model.repositories;

import com.example.emag.model.entities.OrderProduct;
import com.example.emag.model.entities.OrderProductKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.jaas.JaasPasswordCallbackHandler;

public interface OrderProductRepository extends JpaRepository<OrderProduct,OrderProductKey> {
}

