package com.tena.untact2021.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private Integer id;
    private String regDate;
    private String updateDate;
    private String code;
    private String name;
}