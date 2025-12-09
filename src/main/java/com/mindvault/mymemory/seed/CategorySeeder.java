package com.mindvault.mymemory.seed;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.mindvault.mymemory.entity.category;
import com.mindvault.mymemory.repository.CategoryRepository;


@Component
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {

            List<String> categories = List.of(
                "Tour",
                "Party",
                "Love",
                "Family",
                "Friends",
                "Study",
                "Personal"
            );

            categories.forEach(name -> {
                category c = new category();
                c.setName(name);
                categoryRepository.save(c);
            });

            System.out.println("🌱 Categories seeded successfully.");
        }
    }
}








