package com.leading.noticeservice.domain.notice.service;

import com.leading.common.exception.LeadingServiceException;
import com.leading.common.page.PageInfo;
import com.leading.domain.notice.constant.NoticeOperationLogStatusEnum;
import com.leading.domain.notice.constant.NoticeReadStatusEnum;
import com.leading.domain.notice.constant.NoticeStatusEnum;
import com.leading.domain.notice.error.NoticeError;
import com.leading.noticeservice.domain.notice.dto.NoticeInfoDTO;
import com.leading.noticeservice.domain.notice.dto.NoticeListDTO;
import com.leading.noticeservice.domain.notice.dto.NoticePushListDTO;
import com.leading.noticeservice.domain.notice.mapper.NoticeMapper;
import com.leading.noticeservice.domain.notice.mapper.NoticePushMapper;
import com.leading.noticeservice.domain.notice.model.Notice;
import com.leading.noticeservice.domain.notice.model.NoticePush;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: liukun
 * @Date: 2019/4/25 11:30
 * @Description:
 */
@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private NoticePushMapper noticePushMapper;

    /**
     *
     * 功能描述: 编辑公告信息
     *
     * @param: [notice]
     * @return: java.lang.Long
     * @author: zhangwenyu
     * @date: 2019/4/28 10:45
     */
    @Override
    public Long updateNoticeInfo(Notice notice ,Set<Long> receiveUsers ,int status) {
        //校验参数
        checkParam(notice);
        notice.setStatus(status);
        //update
        try {
            Long update = noticeMapper.updateNotice(notice);
            if(update == 0){
                log.error("编辑失败,{}",notice.getTitle());
                throw new LeadingServiceException(NoticeError.OPERATION_FAIL.getCode(),
                        NoticeError.OPERATION_FAIL.getMessage());
            }
            if (status == NoticeStatusEnum.PUSH.getCode() && update > 0) {
                //推送关系校验
                checkNoticeBeforeBush(notice, receiveUsers);

                return pushNoticeInfo( notice.getId() , receiveUsers);
            }
            return update;
        }catch (Exception e){
            log.error("编辑失败,{}",notice.getTitle() , e);
            throw new LeadingServiceException(NoticeError.OPERATION_FAIL.getCode(),
                    NoticeError.OPERATION_FAIL.getMessage());
        }
    }

    /**
     *
     * 功能描述: 详情信息
     *
     * @param: [id]
     * @return: com.leading.noticeservice.domain.notice.dto.NoticeInfoDTO
     * @author: zhangwenyu
     * @date: 2019/4/28 10:03
     */
    @Override
    public NoticeInfoDTO findNoticeDetail(Long id) {
        NoticeInfoDTO noticeInfoDTO = new NoticeInfoDTO();
        Notice notice = noticeMapper.getNoticeById(id);
        BeanUtils.copyProperties(notice , noticeInfoDTO);
        return noticeInfoDTO;
    }

    /**
     *
     * 功能描述: 推送
     *
     * @param: [noticeId, receiveUsers]
     * @return: java.lang.Long
     * @author: zhangwenyu
     * @date: 2019/4/26 11:38
     */
    private Long pushNoticeInfo(Long noticeId , Set<Long> receiveUsers) {

        //建立推送关系集合
        List<NoticePush> noticePushList = receiveUsers.stream().map(item -> {
            NoticePush noticePush = new NoticePush();
            noticePush.setUserId(item)
                    .setNoticeId(noticeId)
                    .setRead(NoticeReadStatusEnum.NO_READ.getCode());
            return noticePush;
        }).collect(Collectors.toList());

        //校验
        if (CollectionUtils.isEmpty(noticePushList)) {
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(), NoticeError.PARAM_IS_NULL.getMessage());
        }
        try {
            //建立关系
            return noticePushMapper.bacthCreateNoticePush(noticePushList);
        }catch (Exception e){
            log.error("推送失败" , e);
            throw new LeadingServiceException(NoticeError.OPERATION_FAIL.getCode(),
                    NoticeError.OPERATION_FAIL.getMessage());
        }

    }

    @Override
    public PageInfo<NoticeListDTO> findAllNoticeList(LocalDate startTime, LocalDate endTime, PageInfo<NoticeListDTO> pageInfo, Long userId) {
        //查询总条数
        pageInfo.setTotal(noticeMapper.countAllNotices(startTime,endTime));
        if (pageInfo.getTotal()>0){
            pageInfo.setItems(noticeMapper.findAllNoticeList(startTime,endTime,pageInfo.getOffset(),pageInfo.getPageSize()));
        }
        return pageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAndRevokeNoticeById(Long id, Long userId,int status) {
        if (null == id)
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(), NoticeError.PARAM_IS_NULL.getMessage());
        int num = 0;
        if (NoticeOperationLogStatusEnum.DELETE.getCode() == status){
            //修改公告消息表删除状态
            num = noticeMapper.deleteNoticeById(id);
        }else {
            //修改公告消息表为撤回状态
            num = noticeMapper.revokeNoticeById(id);
        }

        if (num <= 0)
            throw new LeadingServiceException(NoticeError.OPERATION_FAIL.getCode(), NoticeError.OPERATION_FAIL.getMessage());
        //删除推送关系
        noticePushMapper.deleteByNoticeId(id);
        return num;
    }

    /**
     *
     * 功能描述: 创建公告信息
     *
     * @param: [createNoticeInfoParam, status]
     * @return: java.lang.Long
     * @author: zhangwenyu
     * @date: 2019/4/26 11:39
     */
    @Override
    @Transactional
    public Long createNoticeInfo(Notice notice , Set<Long> receiveUsers ,int status) {
        checkParam(notice);
        notice.setStatus(status);
        //创建
        try {
            Long create = noticeMapper.createNoticeInfo(notice);
            if (status == NoticeStatusEnum.PUSH.getCode() && create > 0) {
                //推送关系校验
                checkNoticeBeforeBush(notice, receiveUsers);

                return pushNoticeInfo( notice.getId() , receiveUsers);
            }
            return create;
        }catch (Exception e){
            log.error("创建失败,{}",notice.getTitle() , e);
            throw new LeadingServiceException(NoticeError.OPERATION_FAIL.getCode(),
                    NoticeError.OPERATION_FAIL.getMessage());
        }

    }

    private void checkNoticeBeforeBush(Notice notice, Set<Long> receiveUsers) {
        if ( notice.getId() == null) {
            throw new LeadingServiceException(
                    NoticeError.ID_IS_NULL.getCode(),NoticeError.ID_IS_NULL.getMessage());
        }
        if (CollectionUtils.isEmpty(receiveUsers)) {
            throw new LeadingServiceException(
                    NoticeError.RECEIVE_USER_IS_NULL.getCode(), NoticeError.RECEIVE_USER_IS_NULL.getMessage());
        }
    }

    /**
     * 校验公告消息
     * @param notice
     */
    private void checkParam(Notice notice) {
        if (notice == null) {
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(),
                    NoticeError.PARAM_IS_NULL.getMessage());
        }
        if (StringUtils.isBlank(notice.getTitle())) {
            throw new LeadingServiceException(NoticeError.TITLE_IS_NULL.getCode(),
                    NoticeError.TITLE_IS_NULL.getMessage());
        }
    }

    @Override
    public int readNoticeByNoticePushId(Long noticePushId) {
        if (null == noticePushId)
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(), NoticeError.PARAM_IS_NULL.getMessage());
        return noticePushMapper.readNoticeByNoticePushId(noticePushId);
    }

    @Override
    @Transactional
    public Long push(Long id ,Set<Long> receiveUsers) {
        try {
            //修改状态
            Long update = noticeMapper.updatePushNoticeStatus(id);
            //建立关系
            return pushNoticeInfo(id , receiveUsers);
        }catch (Exception e){
            log.error("推送失败", e);
            throw new LeadingServiceException(NoticeError.OPERATION_FAIL.getCode(),
                    NoticeError.OPERATION_FAIL.getMessage());
        }
    }

    /**
     *
     * 功能描述: 公告信息简单列表
     *
     * @param: [pageInfo, userId]
     * @return: com.leading.common.page.PageInfo<com.leading.noticeservice.domain.notice.dto.NoticeSimpleListDTO>
     * @author: zhangwenyu
     * @date: 2019/4/29 9:25
     */
    @Override
    public PageInfo<NoticePushListDTO> findNoticePushList(PageInfo<NoticePushListDTO> pageInfo, Long userId) {
     //查询总条数
        pageInfo.setTotal(noticePushMapper.countNotices(userId));
        if (pageInfo.getTotal()>0){
            pageInfo.setItems(noticePushMapper.findNoticeList(pageInfo.getOffset() , pageInfo.getPageSize() , userId));
        }
        return pageInfo;
    }

}
