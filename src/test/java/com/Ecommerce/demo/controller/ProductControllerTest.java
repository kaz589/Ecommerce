package com.Ecommerce.demo.controller;

import com.Ecommerce.demo.exception.BusinessException;
import com.Ecommerce.demo.exception.GlobalExceptionHandler;
import com.Ecommerce.demo.model.entity.ProductEntity;
import com.Ecommerce.demo.service.ProductService;
import com.Ecommerce.demo.utils.SecurityUtils;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.*;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.Ecommerce.demo.utils.SecurityUtils.getAuthenticatedUsername;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.client.match.ContentRequestMatchers;

import java.util.List;

class ProductControllerTest {

    // 創建 ObjectMapper 實例
    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private ProductService productService; // 模擬 Service 層

    @InjectMocks
    private ProductController productController; // 將 Mock 注入到 Controller 中

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler()) // 啟用全局異常處理
                .build();
        


    }


    @Test
    void findAllProduct_NotFound() throws Exception {
        // 模擬異常
        Mockito.doThrow(new BusinessException("ANY_MESSAGE"))
                .when(productService)
                .findAllProduct(0, 10);

        // 構建 GET 請求
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/api/product/All")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON);

        // 執行請求並驗證響應
        mockMvc.perform(request)
                .andExpect(status().isBadRequest()) // 驗證 HTTP 400 狀態碼
                .andExpect(content().json("{\"status\":400,\"message\":\"ANY_MESSAGE\"}"));
    }
    @Test
    void findAllProduct_Found() throws Exception {


        // 構建測試數據
        ProductEntity product1 = new ProductEntity();
        product1.setProductId(1);
        product1.setName("Product 1");
        product1.setPrice(100);

        ProductEntity product2 = new ProductEntity();
        product2.setProductId(2);
        product2.setName("Product 2");
        product2.setPrice(200);

        List<ProductEntity> productList = List.of(product1, product2);
        Page<ProductEntity> products = new PageImpl<>(productList, PageRequest.of(0, 10), productList.size());

        // 模擬 Service 層返回值
        Mockito.when(productService.findAllProduct(0, 10)).thenReturn(products);

        // 預期的 JSON 響應
        String expectedJson = objectMapper.writeValueAsString(products);

        // 執行請求並驗證響應
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/All")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 驗證 HTTP 200 狀態碼
                .andExpect(content().json(expectedJson)); // 驗證 JSON 響應

    }



    // 測試商品不存在
    @Test
    void purchase_ProductNotFound() throws Exception {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getAuthenticatedUsername).thenReturn("testUser");

            // 模擬 Service 層拋出異常
            Mockito.doThrow(new BusinessException("商品不存在"))
                    .when(productService).purchase("testUser", 1, 2);

            // 執行 POST 請求
            mockMvc.perform(MockMvcRequestBuilders.post("/api/product/purchase")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"productId\":1, \"quantity\":2}"))
                    .andExpect(status().isBadRequest()) // 驗證 HTTP 狀態碼
                    .andExpect(jsonPath("$.message").value("商品不存在")) // 驗證 JSON 中的 message 字段
                    .andDo(result -> {
                        System.out.println("Response Status: " + result.getResponse().getStatus());
                        System.out.println("Response Content: " + result.getResponse().getContentAsString());
                    });

            // 驗證 productService.purchase 是否被調用
            Mockito.verify(productService).purchase("testUser", 1, 2);
        }

    }

    // 測試用戶不存在
    @Test
    void purchase_UserNotFound() throws Exception {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getAuthenticatedUsername).thenReturn("testUser");

            // 模擬 Service 層拋出異常
            Mockito.doThrow(new BusinessException("用戶不存在"))
                    .when(productService).purchase("testUser", 1, 2);

            // 執行 POST 請求
            mockMvc.perform(MockMvcRequestBuilders.post("/api/product/purchase")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"productId\":1, \"quantity\":2}"))
                    .andExpect(status().isBadRequest()) // 驗證 HTTP 狀態碼
                    .andExpect(jsonPath("$.message").value("用戶不存在")) // 驗證 JSON 中的 message 字段
                    .andDo(result -> {
                        System.out.println("Response Status: " + result.getResponse().getStatus());
                        System.out.println("Response Content: " + result.getResponse().getContentAsString());
                    });

            // 驗證 productService.purchase 是否被調用
            Mockito.verify(productService).purchase("testUser", 1, 2);
        }
    }

    // 測試商品庫存不足
    @Test
    void purchase_ProductStockNotEnough() throws Exception {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getAuthenticatedUsername).thenReturn("testUser");

            // 模擬 Service 層拋出異常
            Mockito.doThrow(new BusinessException("商品庫存不足"))
                    .when(productService).purchase("testUser", 1, 2);

            // 執行 POST 請求
            mockMvc.perform(MockMvcRequestBuilders.post("/api/product/purchase")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"productId\":1, \"quantity\":2}"))
                    .andExpect(status().isBadRequest()) // 驗證 HTTP 狀態碼
                    .andExpect(jsonPath("$.message").value("商品庫存不足")) // 驗證 JSON 中的 message 字段
                    .andDo(result -> {
                        System.out.println("Response Status: " + result.getResponse().getStatus());
                        System.out.println("Response Content: " + result.getResponse().getContentAsString());
                    });

            // 驗證 productService.purchase 是否被調用
            Mockito.verify(productService).purchase("testUser", 1, 2);
        }
    }
    @Test
    void testHandleBusinessException() throws Exception {
        // 模擬 HttpServletResponse
        MockHttpServletResponse mockResponse = new MockHttpServletResponse();

        // 創建 BusinessException
        BusinessException ex = new BusinessException("商品庫存不足");

        // 調用異常處理器
        new GlobalExceptionHandler().handleBusinessException(ex, mockResponse);

        // 驗證 HTTP 狀態碼
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mockResponse.getStatus());

        // 驗證響應內容
        Assertions.assertEquals("{\"status\":400,\"message\":\"商品庫存不足\"}", mockResponse.getContentAsString());
    }




    // 測試用戶餘額不足
    @Test
    void purchase_UserBalanceNotEnough() throws Exception {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getAuthenticatedUsername).thenReturn("testUser");

            // 模擬 Service 層拋出異常
            Mockito.doThrow(new BusinessException("用戶餘額不足"))
                    .when(productService).purchase("testUser", 1, 2);

            // 執行 POST 請求
            mockMvc.perform(MockMvcRequestBuilders.post("/api/product/purchase")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"productId\":1, \"quantity\":2}"))
                    .andExpect(status().isBadRequest()) // 驗證 HTTP 狀態碼
                    .andExpect(jsonPath("$.message").value("用戶餘額不足")) // 驗證 JSON 中的 message 字段
                    .andDo(result -> {
                        System.out.println("Response Status: " + result.getResponse().getStatus());
                        System.out.println("Response Content: " + result.getResponse().getContentAsString());
                    });

            // 驗證 productService.purchase 是否被調用
            Mockito.verify(productService).purchase("testUser", 1, 2);
        }
    }

    // 測試正常購買
    @Test
    void purchase_Success() throws Exception {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getAuthenticatedUsername).thenReturn("testUser");

            // 模擬 Service 層行為
            Mockito.doNothing().when(productService).purchase("testUser", 1, 2);

            // 執行 POST 請求，提供正確的 JSON 請求體
            mockMvc.perform(MockMvcRequestBuilders.post("/api/product/purchase")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"productId\":1, \"quantity\":2}")) // 提供請求體
                    .andExpect(status().isOk()) // 驗證 HTTP 狀態碼為 200
                    .andDo(result -> {
                        System.out.println("Response Status: " + result.getResponse().getStatus());
                    });

            // 驗證 productService.purchase 是否被正確調用
            Mockito.verify(productService, Mockito.times(1)).purchase("testUser", 1, 2);
        }
    }


    @Test
    void createProduct_Success() throws Exception {
        // 初始化 ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 構建測試數據
        ProductEntity productRequest = new ProductEntity();
        productRequest.setName("New Product");
        productRequest.setPrice(150);

        ProductEntity productResponse = new ProductEntity();
        productResponse.setProductId(1);
        productResponse.setName("New Product");
        productResponse.setPrice(150);

        // 模擬 Service 層返回值
        Mockito.when(productService.createProduct(Mockito.any(ProductEntity.class)))
                .thenReturn(productResponse);

        // 將請求對象轉換為 JSON
        String requestJson = objectMapper.writeValueAsString(productRequest);
        String expectedResponseJson = objectMapper.writeValueAsString(productResponse);

        // 執行 POST 請求並驗證響應
        mockMvc.perform(MockMvcRequestBuilders.post("/api/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)) // 傳入 JSON 請求體
                .andExpect(status().isOk()) // 驗證 HTTP 200 狀態碼
                .andExpect(content().json(expectedResponseJson)); // 驗證返回的 JSON 是否正確
    }
}