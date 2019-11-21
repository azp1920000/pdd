package com.leading.noticeservice.domain.noticetest.mappertest;

import com.leading.noticeservice.domain.notice.dtotest.NoticePushListDTOtest;
import com.leading.noticeservice.domain.notice.modeltest.NoticePushtest;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by lenovo on 2019/11/20.
 */
public interface NoticePushMappertest {
    @Insert(
            {
                    "<script>",
                    "insert into `NoticePush`(userId, noticeId, `read` , createTime , updateTime) values ",
                    "<foreach collection='noticePushList' item='noticePush' index='index' separator=','>",
                    "(#{noticePush.userId}, #{noticePush.noticeId}, #{noticePush.read},now(), now())",
                    "</foreach>",
                    "</script>",
            }
    )
    Long bacthCreateNoticePush(@Param("noticePushList")List<NoticePushtest> noticePushList);

    @Delete({
            "update NoticePush set `read` = 1 where id = #{noticePushId}"
    })
    int deleteByNoticeId(@Param("id") Long id);

    @Update({
            "update NoticePush set `read` = 1 where id = #{noticePushId}"
    })
    int readNoticeByNoticePushId(@Param("noticePushId")Long noticePushId);

    @Select("select count(1) from `NoticePush` n where userId = #{userId} ")
    int countNotices( @Param("userId") Long userId);

    @Select({
            "<script>",
            "select",
            " np.id as pushId,n.id,n.title,np.createTime,np.read,n.content",
            " from `NoticePush` np join `Notice` n on np.noticeId = n.id ",
            "where np.userId = #{userId} ",
            "order by np.createTime desc LIMIT  #{offset},#{pageSize} ",
            "</script>"
    })
    List<NoticePushListDTOtest> findNoticedList(
            @Param("offset") Integer offset, @Param("pageSize") Integer pageSize,@Param("userId") Long userId);

}
