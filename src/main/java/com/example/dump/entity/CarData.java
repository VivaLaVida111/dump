package com.example.dump.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CarData implements Serializable  {
    private static final long serialVersionUID = 1L;
    private String siteName;
    private String carNumber;
    private Integer todayAmount;
    private Integer predictAmount;
}
