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
 * 报警记录
 * </p>
 *
 * @author luo
 * @since 2023-03-31
 */
@Getter
@Setter
  @TableName("alarm_record")
@ApiModel(value = "AlarmRecord对象", description = "报警记录")
public class AlarmRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private LocalDateTime exactDate;

    private String timeInterval;

      @ApiModelProperty("报警类型")
      private String category;


}
