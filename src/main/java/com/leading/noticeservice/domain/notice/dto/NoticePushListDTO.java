package com.leading.noticeservice.domain.notice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: liukun
 * @Date: 2019/4/25 11:17
 * @Description:
 */
@ApiModel(value = "NoticePushListDTO", description = "首页公告信息")
@Data
public class NoticePushListDTO {
    @ApiModelProperty("编号")
    private Long id;
    @ApiModelProperty("公告详情id")
    private Long pushId;
    @ApiModelProperty("标题")
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("推送时间")
    private Date createTime;
    @ApiModelProperty("是否已读")
    private Integer read;
}
