package com.branchsales;

import com.branchsales.repository.MainItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationContextTest {

    @Autowired
    private MainItemRepository mainItemRepository;

    @Test
    void contextLoads() {
        // Assert that the context and repository load correctly
        mainItemRepository.findAllWithCategory();
    }
}
