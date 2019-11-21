package com.leading.noticeservice.domain.notice.controller.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

/**
 * @Author: zhangwenyu
 * @Date: 2019/4/25 13:27
 * @Description:
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "NoticeInfoParam", description = "公告信息参数")
public class NoticeInfoParam implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("接收人")
    private Set<Long> receiveUsers;
    @ApiModelProperty("消息内容")
    private String content;
}
