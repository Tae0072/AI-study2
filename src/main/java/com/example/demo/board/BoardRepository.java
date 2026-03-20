package com.example.demo.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    void deleteByUserId(Integer userId); // 유저 아이디로 게시글 삭제
    List<Board> findByUserId(Integer userId);   // 유저 아이디로 게시글 목록 조회

    @Query(value = "SELECT * FROM board_tb ORDER BY id DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Board> findAllWithLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset); // 페이징된 게시글 목록 조회
}
