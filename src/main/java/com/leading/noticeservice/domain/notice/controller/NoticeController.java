package com.leading.noticeservice.domain.notice.controller;

import com.leading.common.exception.LeadingServiceException;
import com.leading.common.page.PageInfo;
import com.leading.domain.notice.constant.NoticeOperationLogStatusEnum;
import com.leading.domain.notice.constant.NoticeStatusEnum;
import com.leading.domain.notice.error.NoticeError;
import com.leading.noticeservice.domain.notice.controller.param.FindNoticeListParam;
import com.leading.noticeservice.domain.notice.controller.param.NoticeInfoParam;
import com.leading.noticeservice.domain.notice.controller.param.NoticePushListParam;
import com.leading.noticeservice.domain.notice.controller.param.PushNoticeInfoParam;
import com.leading.noticeservice.domain.notice.dto.NoticeInfoDTO;
import com.leading.noticeservice.domain.notice.dto.NoticeListDTO;
import com.leading.noticeservice.domain.notice.dto.NoticePushListDTO;
import com.leading.noticeservice.domain.notice.model.Notice;
import com.leading.noticeservice.domain.notice.service.NoticeService;
import com.leading.resource.util.LeadingSecurityContextUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: liukun
 * @Date: 2019/4/24 15:31
 * @Description:公告服务接口
 */
@Api(tags = "公告信息API")
@RequestMapping("/notice")
@RestController
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @ApiOperation("编辑公告信息并推送")
    @PreAuthorize("hasAuthority('LEADING_AUTH_CHECK_OPEN')")
    @PutMapping("/{id}/push")
    public Long updateNoticeInfoAndPush(@PathVariable Long id ,@RequestBody NoticeInfoParam updateNoticeInfoParam ){
        return update(id,updateNoticeInfoParam,NoticeStatusEnum.PUSH.getCode());
    }

    @ApiOperation("编辑公告信息至草稿")
    @PreAuthorize("hasAuthority('LEADING_AUTH_CHECK_OPEN')")
    @PutMapping("/{id}/draft")
    public Long updateNoticeInfoToDraft(@PathVariable Long id ,@RequestBody NoticeInfoParam updateNoticeInfoParam ){
        return update(id,updateNoticeInfoParam,NoticeStatusEnum.DRAFT.getCode());
    }

    /**
     * 修改公告信息
     * @return
     */
    private Long update(Long id,NoticeInfoParam updateNoticeInfoParam,int code){
        //校验
        checkParam(updateNoticeInfoParam );
        if (id == null) {
            throw new LeadingServiceException(NoticeError.ID_IS_NULL.getCode(),
                    NoticeError.ID_IS_NULL.getMessage());
        }
        Notice notice = new Notice();
        BeanUtils.copyProperties(updateNoticeInfoParam , notice);
        notice.setId(id);
        return noticeService.updateNoticeInfo(
                notice , updateNoticeInfoParam.getReceiveUsers() ,code);
    }


    @ApiOperation("公告详情预览")
    @PreAuthorize("hasAuthority('LEADING_AUTH_CHECK_OPEN')")
    @GetMapping("/{id}/detail")
    public NoticeInfoDTO findNoticeDetail(@PathVariable Long id){
        return noticeService.findNoticeDetail(id);
    }

    @ApiOperation("首页公告消息列表")
    @PostMapping(value = "/push-list")
    public PageInfo<NoticePushListDTO> findNoticePushList(@RequestBody NoticePushListParam noticePushListParam) {
        if (noticePushListParam == null || noticePushListParam.getPageNum() == null
                || noticePushListParam.getPageSize() == null)
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(), NoticeError.PARAM_IS_NULL.getMessage());
        return noticeService.findNoticePushList(new PageInfo<>(noticePushListParam.getPageNum(), noticePushListParam.getPageSize()),
                LeadingSecurityContextUtil.getUserId());
    }

    @ApiOperation("查询公告消息列表")
    @PostMapping(value = "/all-list")
    public PageInfo<NoticeListDTO> findAllNoticeList(@RequestBody FindNoticeListParam findNoticeListParam) {

        if (findNoticeListParam == null || findNoticeListParam.getPageNum() == null
                || findNoticeListParam.getPageSize() == null)
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(), NoticeError.PARAM_IS_NULL.getMessage());

        return noticeService.findAllNoticeList(findNoticeListParam.getStartTime(),findNoticeListParam.getEndTime(),
                new PageInfo<>(findNoticeListParam.getPageNum(), findNoticeListParam.getPageSize()),
                LeadingSecurityContextUtil.getUserId());
    }

    @ApiOperation("删除")
    @ApiImplicitParam(name = "id", value = "公告消息id")
    @PreAuthorize("hasAuthority('LEADING_AUTH_CHECK_OPEN')")
    @DeleteMapping("/{id}")
    public int deleteNoticeById(@PathVariable Long id) {
        return noticeService.deleteAndRevokeNoticeById(id, LeadingSecurityContextUtil.getUserId(),
                NoticeOperationLogStatusEnum.DELETE.getCode());
    }

    @ApiOperation("撤回")
    @ApiImplicitParam(name = "id", value = "公告消息id")
    @PreAuthorize("hasAuthority('LEADING_AUTH_CHECK_OPEN')")
    @PutMapping("/{id}")
    public int revokeNoticeById(@PathVariable Long id) {
        return noticeService.deleteAndRevokeNoticeById(id, LeadingSecurityContextUtil.getUserId(),
                NoticeOperationLogStatusEnum.REVOKE.getCode());
    }

    @ApiOperation("创建公告信息至草稿")
    @PreAuthorize("hasAuthority('LEADING_AUTH_CHECK_OPEN')")
    @PostMapping("/draft")
    public Long createNoticeInfoToDraft(@RequestBody NoticeInfoParam noticeInfoParam){
        //校验
        checkParam(noticeInfoParam);
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeInfoParam, notice);

        return noticeService.createNoticeInfo(
                notice , noticeInfoParam.getReceiveUsers(), NoticeStatusEnum.DRAFT.getCode());
    }

    @ApiOperation("创建公告信息并推送")
    @PreAuthorize("hasAuthority('LEADING_AUTH_CHECK_OPEN')")
    @PostMapping("/push")
    public Long createNoticeInfoAndPush(@RequestBody NoticeInfoParam noticeInfoParam){
        //校验
        checkParam(noticeInfoParam);
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeInfoParam, notice);
        return noticeService.createNoticeInfo(
                notice , noticeInfoParam.getReceiveUsers(), NoticeStatusEnum.PUSH.getCode());
    }

    @ApiOperation("已读")
    @ApiImplicitParam(name = "noticePushId", value = "公告推送明细id")
    @PutMapping("/read/{noticePushId}")
    public int readNoticeByNoticePushId(@PathVariable Long noticePushId) {
        return noticeService.readNoticeByNoticePushId(noticePushId);
    }

    @ApiOperation("手动推送")
    @PreAuthorize("hasAuthority('LEADING_AUTH_CHECK_OPEN')")
    @PostMapping(value = "/{id}/manual-push")
    public Long pushNoticeInfo(@PathVariable Long id , @RequestBody PushNoticeInfoParam pushNoticeInfoParam ){
        checkPushNotice(id , pushNoticeInfoParam);
        return noticeService.push(id ,pushNoticeInfoParam.getReceiveUsers());
    }

    private void checkPushNotice(Long id , PushNoticeInfoParam pushNoticeInfoParam) {
        //校验
        if (pushNoticeInfoParam == null) {
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(),
                    NoticeError.PARAM_IS_NULL.getMessage());
        }

        if (id == null) {
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(),
                    NoticeError.PARAM_IS_NULL.getMessage());
        }

        if (CollectionUtils.isEmpty(pushNoticeInfoParam.getReceiveUsers())) {
            throw new LeadingServiceException(NoticeError.RECEIVE_USER_IS_NULL.getCode(),
                    NoticeError.RECEIVE_USER_IS_NULL.getMessage());
        }

        if (StringUtils.isBlank(pushNoticeInfoParam.getTitle())) {
            throw new LeadingServiceException(NoticeError.TITLE_IS_NULL.getCode(),
                    NoticeError.TITLE_IS_NULL.getMessage());
        }
    }

    private void checkParam( NoticeInfoParam noticeInfoParam ) {
        //校验
        if (noticeInfoParam == null) {
            throw new LeadingServiceException(NoticeError.PARAM_IS_NULL.getCode(),
                    NoticeError.PARAM_IS_NULL.getMessage());
        }
        if (StringUtils.isBlank(noticeInfoParam.getTitle())) {
            throw new LeadingServiceException(NoticeError.TITLE_IS_NULL.getCode(),
                    NoticeError.TITLE_IS_NULL.getMessage());
        }
    }

}
