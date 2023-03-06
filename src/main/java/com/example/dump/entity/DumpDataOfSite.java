package com.example.dump.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class DumpDataOfSite implements Serializable {
    private static final long serialVersionUID = 1L;
    @ExcelProperty("站点")
    @ColumnWidth(20)
    private String siteName;
    @ExcelProperty("日期")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd")
    private Date day;
    @ExcelProperty("垃圾总量/kg")
    @ColumnWidth(20)
    private Integer weight;
}
