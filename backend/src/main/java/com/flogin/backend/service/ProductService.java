package com.flogin.backend.service;

import com.flogin.backend.dto.ProductDTO;
import com.flogin.backend.entity.Product;
import com.flogin.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        return mapToDto(product);
    }

    public ProductDTO createProduct(ProductDTO dto) {
        if(productRepository.existsByName(dto.getName()))
            throw new IllegalArgumentException("Product name already exists");

        Product product = mapToEntity(dto);
        product.setId(null);
        Product saved = productRepository.save(product);
        return mapToDto(saved);
    }

    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));
        if(productRepository.existsByNameAndIdNot(dto.getName(), id))
            throw new IllegalArgumentException("Product name already exists");

        existing.setName(dto.getName());
        existing.setQuantity(dto.getQuantity());
        existing.setPrice(dto.getPrice());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());

        Product saved = productRepository.save(existing);
        return mapToDto(saved);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new NoSuchElementException("Product not found");
        }

        productRepository.deleteById(id);
    }

    private ProductDTO mapToDto(Product product) {
        if(product == null)
            return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setQuantity(product.getQuantity());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());

        return dto;
    }

    private Product mapToEntity(ProductDTO dto) {
        if(dto == null)
            return null;

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .build();
    }




}
