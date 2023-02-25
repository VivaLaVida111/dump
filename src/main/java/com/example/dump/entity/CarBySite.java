package com.example.dump.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 按站点划分的汽车, 包括三轮车（针对同一车辆可能有多条记录，对应不同的站点）
 * </p>
 *
 * @author luo
 * @since 2023-02-25
 */
@Getter
@Setter
  @TableName("car_by_site")
@ApiModel(value = "CarBySite对象", description = "按站点划分的汽车, 包括三轮车（针对同一车辆可能有多条记录，对应不同的站点）")
public class CarBySite implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private String carNumber;

      @ApiModelProperty("站点名称")
      private String siteName;


}
