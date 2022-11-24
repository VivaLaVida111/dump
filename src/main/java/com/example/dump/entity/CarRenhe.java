package com.example.dump.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
  @TableName("car_renhe")
@ApiModel(value = "CarRenhe对象", description = "")
public class CarRenhe implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("仁和系统中的vid")
        private Integer id;

      @ApiModelProperty("车牌号信息")
      private String name;

    private String gprs;

      @ApiModelProperty("获取车辆信息所需的vkey")
      private String vkey;


}
