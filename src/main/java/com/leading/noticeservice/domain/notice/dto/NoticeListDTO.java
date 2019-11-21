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
@ApiModel(value = "NoticeListDTO", description = "公告消息列表信息")
@Data
public class NoticeListDTO {
    @ApiModelProperty("编号")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty("状态")
    private Integer status;
}
