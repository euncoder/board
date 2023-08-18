package com.study.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;


import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data // getter, setter, toString, equals, hashCode를 자동으로 생성해주는 어노테이션
@Entity // JPA를 사용하기 위한 어노테이션(table과 매핑)
public class Board {
  @Id // PK를 지정하기 위한 어노테이션
  @GeneratedValue (strategy = GenerationType.IDENTITY) // PK의 값을 자동으로 생성해주는 어노테이션 (IDENTITY: DB에게 PK를 위임)
  private Integer id;
  private String title;
  private String content;

  private String filename;
  private String filepath;

}
