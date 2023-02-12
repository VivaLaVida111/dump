package com.example.dump.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class DumpDataOfCar implements Serializable {
    private static final long serialVersionUID = 1L;
    private String siteName;
    private String carNumber;
    private Integer frequency;
    private Integer totalWeight;
    private Double avgWeight;
}
