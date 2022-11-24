package com.example.dump.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 记录所有垃圾归集系统的相关车辆，包括仁和以及天府环境的车辆
 * </p>
 *
 * @author xavi
 * @since 2022-11-21
 */
@Getter
@Setter
  @TableName("car_all")
@ApiModel(value = "CarAll对象", description = "记录所有垃圾归集系统的相关车辆，包括仁和以及天府环境的车辆")
public class CarAll implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("车牌号")
        private String carNumber;

      @ApiModelProperty("该车辆归属的公司")
      private String company;

      @ApiModelProperty("车辆所属的垃圾站")
      private String region;


}
