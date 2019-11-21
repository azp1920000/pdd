package com.leading.noticeservice.domain.noticetest.controllertest.paramtest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by lenovo on 2019/11/20.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "PushNoticeInfoParam",description = "推送公告信息")
public class FindNoticeListParamtest {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("标题")
    private String title;
}
