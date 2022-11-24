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
 * @since 2022-11-16
 */
@Getter
@Setter
  @TableName("gps_record")
@ApiModel(value = "GpsRecord对象", description = "")
public class GpsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("车牌号")
      private String carNumber;

      @ApiModelProperty("日期")
      private LocalDateTime day;

    private LocalDateTime exactDate;

      @ApiModelProperty("经度")
      private String longitude;

      @ApiModelProperty("纬度")
      private String latitude;


}
