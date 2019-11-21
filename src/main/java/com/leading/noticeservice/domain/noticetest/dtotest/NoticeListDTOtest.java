package com.leading.noticeservice.domain.noticetest.dtotest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by lenovo on 2019/11/20.
 */
@Data
@ApiModel(value =   "NoticeListDTO",description = "公告消息列表信息")
public class NoticeListDTOtest {
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
