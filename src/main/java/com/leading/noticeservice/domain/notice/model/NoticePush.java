package com.leading.noticeservice.domain.notice.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zhangwenyu
 * @Date: 2019/4/26 16:17
 * @Description:
 */
@Data
@Accessors(chain = true)
public class NoticePush implements Serializable{
    private static final long serialVersionUID = 1L;

    //主键id
    private Long id;
    //公告id
    private Long noticeId;
    //接收人
    private Long userId;
    //是否已读
    private Integer read;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}
