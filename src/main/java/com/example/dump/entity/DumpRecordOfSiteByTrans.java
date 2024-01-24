package com.example.dump.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class DumpRecordOfSiteByTrans implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty("站点")
    @ColumnWidth(20)
    private String siteName;
    @ExcelProperty("街道")
    @ColumnWidth(20)
    private String transporter;
    @ExcelProperty("日期/时间")
    @ColumnWidth(25)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exactDate;
    @ExcelProperty("车牌号")
    @ColumnWidth(20)
    private String carNumber;
    @ExcelProperty("毛重/kg")
    @ColumnWidth(20)
    private Integer grossWeight;
    @ExcelProperty("皮重/kg")
    @ColumnWidth(20)
    private Integer tareWeight;
    @ExcelProperty("净重/kg")
    @ColumnWidth(20)
    private Integer netWeight;

}
