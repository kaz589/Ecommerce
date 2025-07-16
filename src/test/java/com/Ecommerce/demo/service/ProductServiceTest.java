package com.Ecommerce.demo.service;

import com.Ecommerce.demo.exception.BusinessException;
import com.Ecommerce.demo.model.entity.ProductEntity;
import com.Ecommerce.demo.model.entity.UsersEntity;
import com.Ecommerce.demo.repository.ProductRepository;
import com.Ecommerce.demo.repository.UsersRepository;
import com.Ecommerce.demo.service.implementations.ProductImp;
import com.Ecommerce.demo.service.implementations.UsersImp;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UsersService usersService;

    private ProductService productService ;

    private ProductEntity oproduct;
    private UsersEntity ouser;


    @BeforeEach
    void setUp() {

        productService = new ProductImp(productRepository,usersService); // 手動初始化
        oproduct = createProduct(1, 5, 100);
        ouser = createUser("testUser", 500);
    }
    private ProductEntity createProduct(int id, int stock, int price) {
        return Instancio.of(ProductEntity.class)
                .set(field(ProductEntity::getProductId), id)
                .set(field(ProductEntity::getStockQuantity), stock)
                .set(field(ProductEntity::getPrice), price)
                .create();
    }

    private UsersEntity createUser(String username, int balance) {
        return Instancio.of(UsersEntity.class)
                .set(field(UsersEntity::getUsername), username)
                .set(field(UsersEntity::getBalance), balance)
                .create();
    }
    //找到產品
    @Test
    void findProductById_Found() {

        // 模擬返回值
        ProductEntity insproduct = Instancio.create(ProductEntity.class);


        Mockito.when(productRepository.findById(1))
                .thenReturn(Optional.of(oproduct));

        // 測試服務層
        Optional<ProductEntity> product = productService.findProductById(1);

        // 驗證結果
        assertTrue(product.isPresent(), "Product should be present");
        assertEquals(product.get(),oproduct);
    }

    //找不到產品
    @Test
    void findProductById_NotFound() {
        // 模擬 Repository 行為：返回空的 Optional
        Mockito.when(productRepository.findById(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        // 測試方法：期望拋出 BusinessException
        BusinessException exception = assertThrows(BusinessException.class, () -> productService.findProductById(1));

        // 驗證例外訊息是否符合預期
        assertEquals("商品不存在", exception.getMessage());
    }

    @Test
    void findAllProduct_Found() {
        // 模擬返回值
        // 假資料 1
        ProductEntity product1 = Instancio.create(ProductEntity.class);

        // 假資料 2
        ProductEntity product2 =Instancio.create(ProductEntity.class);

        // 包裝成 Page
        List<ProductEntity> mockProducts = Arrays.asList(product1, product2);
        Page<ProductEntity> mockPage = new PageImpl<>(mockProducts);

        // 模擬 Repository 的行為
        Pageable pageable =  PageRequest.of(0, 10, Sort.by("productId").ascending());

        Mockito.when(productRepository.findAll(pageable)).thenReturn(mockPage);

        // 測試方法
        Page<ProductEntity> products = productService.findAllProduct(0, 10);

        // 驗證結果
        assertNotNull(products);
        assertEquals(2, products.getTotalElements());

    }


    @Test
    void findAllProduct_NotFound() {
        // 模擬 Repository 行為：返回空的 Page
        Pageable pageable = PageRequest.of(0, 10, Sort.by("productId").ascending());
        Mockito.when(productRepository.findAll(pageable))
                .thenReturn(Page.empty());

        // 測試方法：期望拋出 BusinessException
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productService.findAllProduct(0,10));

        // 驗證例外訊息
        assertEquals("查無商品", exception.getMessage());
    }

    @Test
    void ValidateAndFetchProduct_InsufficientStock() {
        // 模擬商品存在但庫存不足
        int productId = 1;
        int quantity = 10;

        ProductEntity product = new ProductEntity();
        product.setProductId(1);
        product.setStockQuantity(5); // 庫存為 5

        Mockito.when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

        // 驗證拋出 BusinessException
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productService.validateAndFetchProduct(productId, quantity));

        assertEquals("商品庫存不足", exception.getMessage());
    }
    @Test
    void purchase() {


    }
    @Test
    void purchase_ProductNotFound() {
        // 模擬商品不存在
        Mockito.when(productRepository.findById(Mockito.eq(1)))
                .thenReturn(Optional.empty());

        // 驗證拋出異常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productService.purchase("testUser", 1, 2));

        // 驗證異常訊息
        assertEquals("商品不存在", exception.getMessage());
    }
    @Test
    public void Purchase_UserNotFound() {
        ProductEntity product = new ProductEntity();
        product.setProductId(1);
        product.setStockQuantity(5);
        product.setPrice(100);
        // 模擬用戶不存在
        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Mockito.when(usersService.validateAndFetchUser("testUser", 200))
                .thenThrow(new BusinessException("用戶不存在"));

        // 驗證拋出異常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productService.purchase("testUser", 1, 2));

        assertEquals("用戶不存在", exception.getMessage());
    }
    @Test
    void purchase_ProductStockNotEnough() {
        // 模擬商品存在但庫存不足
        ProductEntity product = new ProductEntity();
        product.setProductId(1);
        product.setStockQuantity(1); // 庫存不足
        product.setPrice(100);

        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // 驗證拋出異常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productService.purchase("testUser", 1, 2));

        assertEquals("商品庫存不足", exception.getMessage());
    }
    // 模擬餘額不足
    @Test
    void purchase_UserBalanceNotEnough() {
        ProductEntity product = new ProductEntity();
        product.setProductId(1);
        product.setStockQuantity(5);
        product.setPrice(100);

        UsersEntity user = new UsersEntity();
        user.setUsername("testUser");
        user.setBalance(150); // 餘額不足

        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Mockito.when(usersService.validateAndFetchUser("testUser", 200))
                .thenThrow(new BusinessException("用戶餘額不足"));

        // 驗證拋出異常
        BusinessException exception = assertThrows(BusinessException.class,
                () -> productService.purchase("testUser", 1, 2));

        assertEquals("用戶餘額不足", exception.getMessage());
    }
    //模擬正常支付
    @Test
    void purchase_Success() {
        ProductEntity product = new ProductEntity();
        product.setProductId(1);
        product.setStockQuantity(5);
        product.setPrice(100);

        UsersEntity user = new UsersEntity();
        user.setUsername("testUser");
        user.setBalance(500);

        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Mockito.when(usersService.validateAndFetchUser("testUser", 200)).thenReturn(user);
        Mockito.when(productRepository.save(Mockito.any(ProductEntity.class))).thenReturn(product);
        Mockito.doNothing().when(usersService).updateUsers(Mockito.any(UsersEntity.class));

        // 執行購買操作
        productService.purchase("testUser", 1, 2);

        // 驗證商品庫存更新
        assertEquals(3, product.getStockQuantity());

        // 驗證用戶餘額更新
        assertEquals(300, user.getBalance());
    }
    @Test
    void createProduct() {
        // 使用 Instancio 生成測試用的產品資料
        ProductEntity newProduct = Instancio.of(ProductEntity.class)
                .set(field(ProductEntity::getName), "New Product")
                .set(field(ProductEntity::getPrice), 200)
                .set(field(ProductEntity::getStockQuantity), 50)
                .create();

        // 模擬 Repository 行為：保存產品並返回
        Mockito.when(productRepository.save(Mockito.any(ProductEntity.class)))
                .thenReturn(newProduct);

        // 測試方法
        ProductEntity createdProduct = productService.createProduct(newProduct);

        // 驗證 Repository 的 save 方法是否被正確調用
        Mockito.verify(productRepository, Mockito.times(1)).save(newProduct);

        // 驗證返回值
        assertNotNull(createdProduct, "Created product should not be null");
        assertEquals("New Product", createdProduct.getName());
        assertEquals(200, createdProduct.getPrice());
        assertEquals(50, createdProduct.getStockQuantity());
    }


}