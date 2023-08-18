package com.study.board.repository;

import com.study.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> { // JpaRepository<엔티티, PK의 타입> -> CRUD를 자동으로 생성해줌


  Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable); // 제목으로 검색하는 메소드 (findBy + 컬럼명 + Containing)
}
