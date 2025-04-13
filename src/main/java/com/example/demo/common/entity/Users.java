package com.example.demo.common.entity;

import java.time.LocalDate;

import lombok.Data;

/** ユーザエンティティ */
@Data
public class Users {
    private Integer id;
    private String name;
    private String department;
    private LocalDate createdAt;
}
