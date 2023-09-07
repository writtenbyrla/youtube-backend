package com.kh.youtube.repo;

import com.kh.youtube.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDAO extends JpaRepository<Category, Integer> { // <상속받을 도메인 , primary key에 대한 데이터 타입>
}
