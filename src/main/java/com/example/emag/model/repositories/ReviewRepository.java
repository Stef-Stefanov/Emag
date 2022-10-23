package com.example.emag.model.repositories;

import com.example.emag.model.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
