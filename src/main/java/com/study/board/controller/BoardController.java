package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class BoardController {
@Autowired
  private BoardService boardService; // BoardService를 사용하기 위해 의존성 주입

  // 글작성 페이지 불러오기
  @GetMapping("/board/write") // http://localhost:8080/board/write -> boardWriteForm() 실행
public String boardWriteForm(){

    return "boardwrite";
}
//글작성

@PostMapping("/board/writepro") // http://localhost:8080/board/writepro -> boardWritePro() 실행
public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception{ // Board 객체와 Model 객체, MultipartFile 객체를 매개변수로 받음
    boardService.write(board, file); // BoardService의 write() 메소드 호출

    model.addAttribute("message","게시글이 등록되었습니다."); // Model 객체에 message를 담음
    model.addAttribute("searchUrl","/board/list"); // Model 객체에 searchUrl을 담음 ("/board/list"로 이동)

  return "message";
  }
//게시물 리스트
  @GetMapping("/board/list")              //페이징 처리 0~10까지 id를 기준으로 내림차순
  public String boardList(Model model,
                          @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                          String searchKeyword){ // Model 객체를 매개변수로 받음 (게시글 목록) -> @PageableDefault 어노테이션을 사용하여 페이징 처리
    Page<Board> list = null; // Page 객체를 생성
    if(searchKeyword == null){
      list = boardService.boardList(pageable); // BoardService의 boardList() 메소드 호출 (페이징 처리) : 검색안된 list반환
    }else {
      list = boardService.boardSearchList(searchKeyword, pageable); // BoardService의 boardSearchList() 메소드 호출 (페이징 처리) : 검색기능이 포함된 list반환
    }


    int nowPage = list.getPageable().getPageNumber() + 1; // 현재 페이지 번호 (0부터 시작) -> 1부터 시작으로 변경 (nowPage = 0 -> 1)
    int startPage = Math.max(nowPage - 4, 1); // 시작 페이지 번호 (1부터 시작)
    int endPage = Math.min(nowPage + 5, list.getTotalPages()); // 끝 페이지 번호

    model.addAttribute("list",list); // Model 객체에 list를 담음 (게시글 목록)
    model.addAttribute("nowPage",nowPage); // Model 객체에 nowPage를 담음
    model.addAttribute("startPage",startPage); // Model 객체에 startPage를 담음
    model.addAttribute("endPage",endPage); // Model 객체에 endPage를 담음


    return "boardlist";
  }
//글보기
  @GetMapping("/board/view") // http://localhost:8080/board/view?id=1 -> boardView() 실행 (id는 @GetMapping의 파라미터로 받음)
  public String boardView(Model model, Integer id){ // Model 객체와 id를 매개변수로 받음 (id는 @GetMapping의 파라미터로 받음)
    model.addAttribute("board", boardService.boardView(id)); // BoardService의 boardView() 메소드 호출 -> Model 객체에 board를 담음
    return "boardview";
  }
//글삭제
  @GetMapping("board/delete") // http://localhost:8080/board/delete?id=1 -> boardDelete() 실행 (id는 @GetMapping의 파라미터로 받음)
  public String boardDelete(Integer id){
    boardService.boardDelete(id);
    return "redirect:/board/list"; // http://localhost:8080/board/list로 리다이렉트 (boardList() 실행) -> 삭제 후 게시글 목록으로 이동
  }
//수정게시물 불러오기
  @GetMapping("/board/modify/{id}") // http://localhost:8080/board/modify/1 -> boardModifyForm() 실행 (id는 @GetMapping의 파라미터로 받음)
  public String boardModify(@PathVariable("id")Integer id, Model model){ // id를 매개변수로 받음  @PathVariable("id")를 통해 id를 받음
    model.addAttribute("board", boardService.boardView(id)); // BoardService의 boardView() 메소드 호출 -> Model 객체에 board를 담음
    return "boardmodify";
  }
  //수정처리
  @PostMapping("/board/update/{id}")
  public String boardUpdate(@PathVariable("id") Integer id, Board board, MultipartFile file) throws Exception { // id와 Board 객체를 매개변수로 받음 @PathVariable("id")를 통해 id를 받음

    Board boardTemp = boardService.boardView(id); // 기존에있던 객체 불러오기
    boardTemp.setTitle(board.getTitle()); // boardTemp의 title을 board의 title로 변경 (새로 작성한 객체 내용 덮어 씌우기)
    boardTemp.setContent(board.getContent()); // boardTemp의 content를 board의 content로 변경

    boardService.write(boardTemp, file); // BoardService의 write() 메소드 호출 -> 수정된 boardTemp를 DB에 저장

    return "redirect:/board/list";
  }
}
