package com.example.dump.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DBStatus implements Serializable  {
    private static final long serialVersionUID = 1L;
    private String siteName;
    private Integer predict;
    private Integer actual;
}

