package com.transaction.config;

import com.transaction.entity.Category;
import com.transaction.entity.CategoryType;
import com.transaction.entity.ExpenseItem;
import com.transaction.repository.CategoryRepository;
import com.transaction.repository.ExpenseItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CategoryDataInitializer implements ApplicationRunner {

    private final CategoryRepository categoryRepository;
    private final ExpenseItemRepository expenseItemRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        try {
            // 1. Ensure "Buttermilk Stock" category exists
            Category buttermilkStockCategory = categoryRepository.findAll().stream()
                    .filter(c -> c.getCategoryName() != null && c.getCategoryName().trim().equalsIgnoreCase("Buttermilk Stock"))
                    .findFirst()
                    .orElseGet(() -> {
                        Category newCat = new Category();
                        newCat.setCategoryName("Buttermilk Stock");
                        newCat.setCategoryType(CategoryType.EXPENSE);
                        newCat.setRecurring(false);
                        newCat.setDescription("Buttermilk stock and milk purchases");
                        Category saved = categoryRepository.save(newCat);
                        log.info("Created 'Buttermilk Stock' expense category with id: {}", saved.getId());
                        return saved;
                    });

            // 2. Map existing milk/curd expense items to "Buttermilk Stock" category
            List<ExpenseItem> allItems = expenseItemRepository.findAll();
            int mappedCount = 0;
            for (ExpenseItem item : allItems) {
                String name = (item.getName() != null ? item.getName() : "").toLowerCase();
                String tamilName = (item.getTamilName() != null ? item.getTamilName() : "").toLowerCase();
                boolean isMilkItem = name.contains("milk") || name.contains("curd") || name.contains("paal")
                        || tamilName.contains("பால்") || tamilName.contains("தயிர்");
                boolean isNotTeaOrCoffee = !name.contains("tea") && !name.contains("coffee") && !name.contains("sweet");

                if (isMilkItem && isNotTeaOrCoffee) {
                    if (item.getCategory() == null || !item.getCategory().getId().equals(buttermilkStockCategory.getId())) {
                        item.setCategory(buttermilkStockCategory);
                        expenseItemRepository.save(item);
                        mappedCount++;
                        log.info("Mapped expense item '{}' to Buttermilk Stock category", item.getName());
                    }
                }
            }

            // 3. If no milk item exists at all, create a default "Milk" expense item mapped to "Buttermilk Stock"
            boolean hasMilkItem = allItems.stream().anyMatch(i -> {
                String n = (i.getName() != null ? i.getName() : "").toLowerCase();
                return n.contains("milk") || n.contains("paal");
            });
            if (!hasMilkItem) {
                ExpenseItem defaultMilk = new ExpenseItem();
                defaultMilk.setName("Milk");
                defaultMilk.setTamilName("பால்");
                defaultMilk.setCategory(buttermilkStockCategory);
                defaultMilk.setIsActive(true);
                expenseItemRepository.save(defaultMilk);
                log.info("Created default 'Milk' expense item mapped to 'Buttermilk Stock' category");
            }

            log.info("CategoryDataInitializer finished. 'Buttermilk Stock' verified and {} items mapped.", mappedCount);
        } catch (Exception e) {
            log.error("Error in CategoryDataInitializer: {}", e.getMessage(), e);
        }
    }
}
