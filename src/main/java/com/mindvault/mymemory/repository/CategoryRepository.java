package com.mindvault.mymemory.repository;
import com.mindvault.mymemory.entity.category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends JpaRepository<category, Long> {
    boolean existsByName(String name);
}
