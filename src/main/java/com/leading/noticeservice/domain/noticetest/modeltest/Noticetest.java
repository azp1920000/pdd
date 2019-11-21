package com.leading.noticeservice.domain.noticetest.modeltest;

import lombok.Data;

import java.util.Date;

/**
 * Created by lenovo on 2019/11/20.
 */
@Data
public class Noticetest {
    private Long id;
    private String title;
    private String content;
    private Integer status;   //0:草稿 1：推送 2：撤回
    private Date createTime;
    private Date updateTime;
    private Boolean deleted;   //是否删除  0：否  1： 是
}
