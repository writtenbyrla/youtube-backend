package com.kh.youtube.service;

import com.kh.youtube.domain.Category;
import com.kh.youtube.repo.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// service: CRUD 로직

@Service
public class CategoryService {

    @Autowired
    private CategoryDAO dao;

    public List<Category> showAll(){
        return dao.findAll(); // 메소드가 정해져 있음
    }

    public Category show(int code) {
        //return dao.findById(code); // 반환값이 Optional<Category>라서 바로 return 하면 오류남
        return dao.findById(code).orElse(null); // 반환값 없으면 null로 지정
    }

    public Category create(Category category){
        return dao.save(category);
    }

    public Category update(Category category){
        return dao.save(category); // 수정도 save, 메서드 하나로 하는 경우도 있음(기존에 없으면 추가, 있으면 수정)
    }

    public Category delete(int code){
        Category data = dao.findById(code).orElse(null);
        dao.delete(data); // delete는 return값이 void/ delete()에 Category Entity가 들어가야 하므로 data에 담아서 넘김
        return data;
    }
}
