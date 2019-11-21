package com.leading.noticeservice.domain.notice.mapper;

import com.leading.noticeservice.domain.notice.dto.NoticePushListDTO;
import com.leading.noticeservice.domain.notice.model.NoticePush;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @Author: zhangwenyu
 * @Date: 2019/4/25 14:41
 * @Description:
 */
public interface NoticePushMapper {

    /**
     * 创建推送关系
     * @param noticePushList
     * @return
     */
    @Insert({
            "<script>",
            "insert into `NoticePush`(userId, noticeId, `read` , createTime , updateTime) values ",
            "<foreach collection='noticePushList' item='noticePush' index='index' separator=','>",
            "(#{noticePush.userId}, #{noticePush.noticeId}, #{noticePush.read},now(), now())",
            "</foreach>",
            "</script>",
    })
    Long bacthCreateNoticePush(@Param("noticePushList") List<NoticePush> noticePushList );

    @Delete({
            "delete from NoticePush where noticeId = #{id}"
    })
    int deleteByNoticeId(@Param("id") Long id);

    @Update({
            "update NoticePush set `read` = 1 where id = #{noticePushId}"
    })
    int readNoticeByNoticePushId(@Param("noticePushId")Long noticePushId);

    /**
     * 已推送公告数量
     * @param userId
     * @return
     */
    @Select("select count(1) from `NoticePush` n where userId = #{userId} ")
    int countNotices( @Param("userId") Long userId);

    /**
     * 已推送公告数据
     * @param offset
     * @param pageSize
     * @param userId
     * @return
     */
    @Select({
            "<script>",
            "select",
            " np.id as pushId,n.id,n.title,np.createTime,np.read,n.content",
            " from `NoticePush` np join `Notice` n on np.noticeId = n.id ",
            "where np.userId = #{userId} ",
            "order by np.createTime desc LIMIT  #{offset},#{pageSize} ",
            "</script>"
    })
    List<NoticePushListDTO> findNoticeList(
            @Param("offset") Integer offset, @Param("pageSize") Integer pageSize,@Param("userId") Long userId);
}
