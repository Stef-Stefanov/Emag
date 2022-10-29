package com.example.emag.model.dao;

import com.example.emag.model.dto.product.ProductQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Page<ProductQueryDTO> filterMinMaxPrice(int min, int max, boolean desc, Pageable pageable) {
        String order = desc ? "DESC" : "ASC";
        List<ProductQueryDTO> list = jdbcTemplate.query("SELECT * FROM products WHERE regular_price BETWEEN ? AND ? ORDER BY " +
                        "regular_price " + order + " LIMIT " + pageable.getPageSize() + " " +
                        "OFFSET " + pageable.getOffset(),
                (rs, rowNum) -> new ProductQueryDTO(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("regular_price"),
                        rs.getInt("quantity"),
                        rs.getLong("category_id"),
                        rs.getLong("discount_id")), min, max);
        return new PageImpl<>(list, pageable, list.size());
        }
    }
