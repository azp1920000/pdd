package com.leading.noticeservice.domain.notice.service;

import com.leading.common.page.PageInfo;
import com.leading.noticeservice.domain.notice.dto.NoticePushListDTO;
import com.leading.noticeservice.domain.notice.dto.NoticeInfoDTO;
import com.leading.noticeservice.domain.notice.dto.NoticeListDTO;
import com.leading.noticeservice.domain.notice.model.Notice;

import java.time.LocalDate;
import java.util.Set;

/**
 * @Author: liukun
 * @Date: 2019/4/25 11:25
 * @Description:
 */
public interface NoticeService {
    /**
     * 根据查询条件查询公告消息列表
     * @param startTime
     * @param endTime
     * @return
     */
    PageInfo<NoticeListDTO> findAllNoticeList(LocalDate startTime, LocalDate endTime, PageInfo<NoticeListDTO> pageInfo, Long userId);

    /**
     * 根据id删除或者撤回公告信息
     * @param id
     * @param userId
     * @return
     */
    int deleteAndRevokeNoticeById(Long id, Long userId,int status);
    /**
     * 创建公告信息
     * @param notice
     * @return
     */
    Long createNoticeInfo(Notice notice , Set<Long> receiveUsers, int status);

    /**
     * 公告消息设置为已读
     * @param noticePushId
     * @return
     */
    int readNoticeByNoticePushId(Long noticePushId);

    /**
     * 推送
     * @param receiveUsers
     * @return
     */
    Long push(Long id ,Set<Long> receiveUsers );

    /**
     * 首页公告信息
     * @param pageInfo
     * @param userId
     * @return
     */
    PageInfo<NoticePushListDTO> findNoticePushList(PageInfo<NoticePushListDTO> pageInfo, Long userId);

    /**
     * 详细信息
     * @param id
     * @return
     */
    NoticeInfoDTO findNoticeDetail(Long id);

    /**
     * 编辑公告信息
     * @param notice
     * @return
     */
    Long updateNoticeInfo(Notice notice,Set<Long> receiveUsers,int status);
}
