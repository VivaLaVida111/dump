package com.example.dump.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class WarningOfSite  implements Serializable {

    private static final long serialVersionUID = 1L;
    @ExcelProperty("事件来源")
    @ColumnWidth(20)
    private String eventSource;

    @ExcelProperty("事件原因")
    @ColumnWidth(40)
    private String eventCause;

    @ExcelProperty("发生时间")
    @ColumnWidth(30)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date eventTime;


    @ExcelProperty("事件持续时长")
    @ColumnWidth(40)
    private String issueDuration;
}
