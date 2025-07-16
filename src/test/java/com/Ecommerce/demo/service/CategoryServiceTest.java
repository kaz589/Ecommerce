package com.Ecommerce.demo.service;

import com.Ecommerce.demo.repository.CategoryRepository;
import com.Ecommerce.demo.service.implementations.CategoryImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {

    @Test
    void testFindDistinctCategory() {
        // 模擬依賴
        CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
        Mockito.when(categoryRepository.findDistinctBynameNotNull())
                .thenReturn(List.of("Electronics", "Books"));

        // 測試服務層
        CategoryService categoryService = new CategoryImp(categoryRepository);
        List<String> categories = categoryService.findDistinctCategory();

        // 驗證結果
        assertEquals(2, categories.size());
        assertEquals("Electronics", categories.get(0));
        assertEquals("Books", categories.get(1));
    }

}