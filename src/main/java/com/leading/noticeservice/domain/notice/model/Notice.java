package com.leading.noticeservice.domain.notice.model;

import lombok.Data;

import java.util.Date;

/**
 * @Author: liukun
 * @Date: 2019/4/25 13:17
 * @Description:
 */
@Data
public class Notice {
    //主键
    private Long id;
    //消息标题
    private String title;
    //消息内容
    private String content;
    //消息状态（0：草稿，1：推送，2：撤回）
    private Integer status;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
    //是否删除（0：否，1：是）
    private Boolean deleted;
}
