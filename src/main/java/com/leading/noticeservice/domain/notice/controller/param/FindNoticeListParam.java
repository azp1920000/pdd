package com.leading.noticeservice.domain.notice.controller.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author: liukun
 * @Date: 2019/4/25 11:12
 * @Description:
 */
@ApiModel(value = "FindNoticeListParam", description = "查询公告消息列表参数")
@Data
public class FindNoticeListParam {
    @ApiModelProperty("创建起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;

    @ApiModelProperty("创建结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页条数")
    private Integer pageSize;
}
