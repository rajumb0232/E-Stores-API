package com.devb.estores.serviceimpl;

import com.devb.estores.exceptions.InvalidOperationException;
import com.devb.estores.exceptions.InvalidVariantGroupException;
import com.devb.estores.mapper.VariantMapper;
import com.devb.estores.model.SimpleProduct;
import com.devb.estores.model.Variant;
import com.devb.estores.model.VaryingProduct;
import com.devb.estores.repository.ProductRepo;
import com.devb.estores.repository.VariantRepo;
import com.devb.estores.requestdto.VariantRequest;
import com.devb.estores.responsedto.VariantResponse;
import com.devb.estores.util.ResponseStructure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VariantServiceImplTest {

    @Mock
    private VariantRepo variantRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private VariantMapper variantMapper;

    @InjectMocks
    private VariantServiceImpl variantServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateVariants_Success() {
        // Arrange
        String productId = "123";

        VaryingProduct varyingProduct = new VaryingProduct();
        varyingProduct.setProductId("123");
        varyingProduct.setVariantBy(new HashSet<>(Arrays.asList("size", "color")));

        List<VariantRequest> variantRequests = new ArrayList<>();
        VariantRequest variantRequest = new VariantRequest();
        variantRequest.setSpecifications(Map.of("size", "M", "color", "red"));
        variantRequests.add(variantRequest);

        Variant variant = new Variant();
        Set<Variant> variants = new HashSet<>(Collections.singletonList(variant));
        varyingProduct.setVariants(variants);

        List<VariantResponse> variantResponses = new ArrayList<>(Collections.singletonList(new VariantResponse()));

        when(productRepo.findById(productId)).thenReturn(Optional.of(varyingProduct));
        when(variantRepo.save(any(Variant.class))).thenReturn(variant);
        when(variantMapper.mapToVariantEntity(any(VariantRequest.class))).thenReturn(variant);
        when(variantMapper.mapToVariantResponseList(anySet())).thenReturn(variantResponses);

        // Act
        ResponseEntity<ResponseStructure<List<VariantResponse>>> response = variantServiceImpl.updateVariants(variantRequests, productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Variants added successfully", response.getBody().getMessage());
        assertEquals(variantResponses, response.getBody().getData());
    }

    @Test
    void testUpdateVariants_InvalidVariantGroupException() {
        // Arrange
        String productId = "product-id";
        VaryingProduct varyingProduct = new VaryingProduct();
        varyingProduct.setVariantBy(new HashSet<>(Arrays.asList("size", "color")));
        List<VariantRequest> variantRequests = new ArrayList<>();
        VariantRequest variantRequest = new VariantRequest();
        variantRequest.setSpecifications(Map.of("size", "M"));
        variantRequests.add(variantRequest);

        when(productRepo.findById(productId)).thenReturn(Optional.of(varyingProduct));

        // Act & Assert
        assertThrows(InvalidVariantGroupException.class, () -> variantServiceImpl.updateVariants(variantRequests, productId));
    }

    @Test
    void testUpdateVariants_InvalidOperationException() {
        // Arrange
        String productId = "product-id";
        SimpleProduct simpleProduct = new SimpleProduct();  // This is to simulate a non-varying product

        when(productRepo.findById(productId)).thenReturn(Optional.of(simpleProduct));

        // Act & Assert
        assertThrows(InvalidOperationException.class, () -> variantServiceImpl.updateVariants(new ArrayList<>(), productId));
    }

    @Test
    void testUpdateVariants_ProductNotFoundException() {
        // Arrange
        String productId = "product-id";

        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> variantServiceImpl.updateVariants(new ArrayList<>(), productId));

    }
}
