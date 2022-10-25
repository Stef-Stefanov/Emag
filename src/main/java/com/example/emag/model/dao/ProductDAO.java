package com.example.emag.model.dao;

import com.example.emag.model.dto.product.ProductQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ProductQueryDTO> filterMinMaxPrice(int min, int max, boolean desc) {
        if (desc) {
            return jdbcTemplate.query("SELECT * FROM products WHERE regular_price BETWEEN ? AND ?" +
                            " ORDER BY regular_price desc",
                    (rs, rowNum) -> new ProductQueryDTO(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("regular_price"),
                            rs.getInt("quantity"),
                            rs.getLong("category_id"),
                            rs.getLong("discount_id")), min, max);
        }else{
            return jdbcTemplate.query("SELECT * FROM products WHERE regular_price BETWEEN ? AND ? ORDER BY regular_price",
                    (rs, rowNum) -> new ProductQueryDTO(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getDouble("regular_price"),
                            rs.getInt("quantity"),
                            rs.getLong("category_id"),
                            rs.getLong("discount_id")), min, max);
        }
    }

}
