package com.kh.youtube.controller;

import com.kh.youtube.domain.Category;
import com.kh.youtube.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/*")
@CrossOrigin(origins={"*"}, maxAge = 6000)
// Home.jsp에서 useEffect로 url 추가하면 정책위반 관련 오류뜸, 여기서 잡고감
public class CategoryController {

    @Autowired
    private CategoryService service;

    // 전체조회
    @GetMapping("/category")
    public ResponseEntity<List<Category>> showAll(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.showAll());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // (BAD_REQUEST: 400 error), build() = body를 null로 보냄
        }
    }
    
    // 상세조회
    @GetMapping("/category/{id}")
    public ResponseEntity<Category> show(@PathVariable int id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.show(id));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 카테고리 추가
    @PostMapping("/category")
    public ResponseEntity<Category> create(@RequestBody Category category){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.create(category));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // 카테고리 수정
    @PutMapping("/category")
    public ResponseEntity<Category> update(@RequestBody Category category){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.update(category));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 카테고리 삭제
    @DeleteMapping("/category/{id}")
    public ResponseEntity<Category> delete(@PathVariable int id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.delete(id));
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
