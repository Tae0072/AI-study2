package com.example.demo.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    void deleteByUserId(Integer userId);
    List<Board> findByUserId(Integer userId);
}
