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
 * 定期查询dump_record入库数据时间，与历史入库时间比对，以此判断数据读取程序是否掉线，存在误差
 * </p>
 *
 * @author luo
 * @since 2023-03-16
 */
@Getter
@Setter
  @TableName("offline_record")
@ApiModel(value = "OfflineRecord对象", description = "定期查询dump_record入库数据时间，与历史入库时间比对，以此判断数据读取程序是否掉线，存在误差")
public class OfflineRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String siteName;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    private LocalDateTime exactDate;

    private String timeInterval;


}
