package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {

  @Autowired // 의존성 주입
  private BoardRepository boardRepository; // BoardRepository를 사용하기 위해 의존성 주입
  //글작성 처리
  public void write(Board board, MultipartFile file) throws Exception{ // BoardService의 write() 메소드  -> BoardController의 boardWritePro() 메소드에서 호출

    String projectpath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files"; // 현재 프로젝트의 경로를 가져옴

    UUID uuid = UUID.randomUUID(); // 랜덤한 문자열을 생성

    String fileName = uuid + "_" + file.getOriginalFilename(); // 랜덤한 문자열과 파일명을 합침

    File saveFile = new File(projectpath, fileName); // 파일을 저장할 경로와 파일명을 지정

    file.transferTo(saveFile); // 파일을 저장

    board.setFilename(fileName); // 저장된 파일의 이름
    board.setFilepath("/files/" + fileName); // 저장된 파일 경로의 이름

    boardRepository.save(board); // boardRepository를 통해 DB에 저장
  }
//게시물 리스트 처리
        //Pageable: 페이징 처리를 위한 인터페이스
  public Page<Board> boardList(Pageable pageable){ // BoardService의 boardList() 메소드 -> BoardController의 boardList() 메소드에서 호출
    return boardRepository.findAll(pageable); // boardRepository를 통해 DB에서 모든 데이터를 가져옴
  }

// 게시물 검색 처리 (제목으로 검색)
  public Page<Board>boardSearchList(String searchKeyword, Pageable pageable){
    return boardRepository.findByTitleContaining(searchKeyword, pageable); // boardRepository를 통해 DB에서 제목에 searchKeyword가 포함된 데이터를 가져옴
  }

  //특정 게시글 불러오기
  public Board boardView(Integer id){
    return boardRepository.findById(id).get(); // boardRepository를 통해 DB에서 id에 해당하는 데이터를 가져옴
  }
  //특정 게시글 삭제
  public void boardDelete(Integer id){
    boardRepository.deleteById(id); // boardRepository를 통해 DB에서 id에 해당하는 데이터를 삭제
  }
}
