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
import java.util.NoSuchElementException;
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
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.getProduct(999L));

        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void createProduct_whenNameNotExists_shouldSaveAndReturnDTO() {
        when(productRepository.existsByName("Coffee")).thenReturn(false);
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                   Product product = invocation.getArgument(0);
                   product.setId(1L);
                   return product;
                });

        ProductDTO result = productService.createProduct(productDto);

        assertNotNull(result.getId());
        assertNotEquals("Coffee", result.getName());
        verify(productRepository, times(1)).existsByName("Coffee");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_whenNameExists_shouldThrowException() {
        when(productRepository.existsByName("Coffee")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(productDto)
        );
        assertEquals("Product name already exists", ex.getMessage());
        verify(productRepository, times(1)).existsByName("Coffee");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_whenIdExistsAndNameNotDuplicated_shouldUpdateAndReturnDTO() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdNot("Coffee", 1L)).thenReturn(false);
        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        ProductDTO updateDTO = new ProductDTO(
                null, "Coffee", 20L, 60000.0, "New Coffee", "Drink"
        );
        ProductDTO result = productService.updateProduct(1L, updateDTO);

        assertEquals(1L, result.getId());
        assertEquals(20L, result.getQuantity());
        assertEquals(60000.0, result.getPrice());
        assertEquals("New coffee", result.getDescription());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).existsByNameAndIdNot("Coffee", 1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_whenIdNotFound_shouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> productService.updateProduct(99L, productDto));
        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_whenNameDuplicated_shouldThrowException() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdNot("Coffee", 1L)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> productService.updateProduct(1L, productDto)
        );
        assertEquals("Product name already exists", ex.getMessage());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).existsByNameAndIdNot("Coffee", 1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_whenIdExists_shouldDelete() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_whenIdNotFound_shouldThrowException() {
        when(productRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> productService.deleteProduct(99L));
        verify(productRepository, times(1)).existsById(99L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
