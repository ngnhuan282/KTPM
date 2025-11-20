package com.flogin.backend;

import com.flogin.backend.dto.ProductDTO;
import com.flogin.backend.entity.Product;
import com.flogin.backend.repository.ProductRepository;
import com.flogin.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDTO productDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = Product.builder()
                .id(1L)
                .name("Coffee")
                .quantity(10L)
                .price(50000.0)
                .description("Black Coffee")
                .category("Drink")
                .build();

        productDto = new ProductDTO(1L, "Coffee", 10L, 50000.0, "Black Coffee", "Drink");
    }

    @Test
    void getProducts_shouldReturnListOfProductDTO() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductDTO> result = productService.getProducts();

        assertEquals(1, result.size());
        ProductDTO dto = result.get(0);
        assertEquals(product.getId(), dto.getId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProduct_whenIdExists_shoudReturnProductDTO() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO dto = productService.getProduct(1L);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Coffee", dto.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProduct_whenIdNotFound_shouldThrowException() {

    }

    @Test
    void createProduct_whenNameNotExists_shouldSaveAndReturnDTO() {

    }

    @Test
    void createProduct_whenNameExists_shouldThrowException() {

    }

    @Test
    void updateProduct_whenIdExistsAndNameNotDuplicated_shouldUpdateAndReturnDTO() {

    }

    @Test
    void updateProduct_whenIdNotFound_shouldThrowException() {

    }

    @Test
    void updateProduct_whenNameDuplicated_shouldThrowException() {

    }

    @Test
    void deleteProduct_whenIdExists_shouldDelete() {

    }

    @Test
    void deleteProduct_whenIdNotFound_shouldThrowException() {

    }
}
