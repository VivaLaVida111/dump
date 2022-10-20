package com.example.dump.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author xavi
 * @since 2022-10-20
 */
@Getter
@Setter
  @TableName("dump_record")
@ApiModel(value = "DumpRecord对象", description = "")
public class DumpRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("自增主键")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("站点名称")
      private String siteName;

      @ApiModelProperty("日期(精确到天)")
      private LocalDateTime day;

      @ApiModelProperty("车牌号")
      private String carNumber;

      @ApiModelProperty("垃圾净重")
      private Integer netWeight;

      @ApiModelProperty("毛重")
      private Integer grossWeight;

      @ApiModelProperty("承运单位")
      private String transporter;

      @ApiModelProperty("IC卡号")
      private String icNumber;

      @ApiModelProperty("精确时间")
      private LocalDateTime exactDate;

      @ApiModelProperty("垃圾类型")
      private String garbageType;

      @ApiModelProperty("皮重")
      private Integer tareWeight;


}
