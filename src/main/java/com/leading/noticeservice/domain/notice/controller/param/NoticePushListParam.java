package com.leading.noticeservice.domain.notice.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: liukun
 * @Date: 2019/4/25 11:12
 * @Description:
 */

@Data
@ApiModel(value = "NoticePushListParam", description = "首页公告消息参数")
public class NoticePushListParam {

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("每页条数")
    private Integer pageSize;
}
