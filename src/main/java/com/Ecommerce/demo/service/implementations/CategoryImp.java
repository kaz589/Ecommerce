package com.Ecommerce.demo.service.implementations;

import com.Ecommerce.demo.exception.BusinessException;
import com.Ecommerce.demo.repository.CategoryRepository;
import com.Ecommerce.demo.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryImp  implements CategoryService {
    private  final CategoryRepository categoryRepository;

    public CategoryImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<String> findDistinctCategory() {


        return categoryRepository.findDistinctBynameNotNull();
    }
}
