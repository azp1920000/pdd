package com.leading.noticeservice.domain.noticetest.modeltest;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * Created by lenovo on 2019/11/20.
 */
@Data
@Accessors(chain = true)
public class NoticePushtest {
    private static  final long serialVersionUID = 1L;

    private Long id;
    private Long noticeId;
    private Long UserId;
    private Integer read;  //是否已读
    private Date createTime;
    private Date updateTime;
}
