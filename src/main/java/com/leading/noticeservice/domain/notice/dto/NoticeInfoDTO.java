package com.leading.noticeservice.domain.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zhangwenyu
 * @Date: 2019/4/25 14:03
 * @Description:
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "NoticeInfoDTO", description = "公告信息")
public class NoticeInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("推送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
