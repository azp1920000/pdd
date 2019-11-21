package com.leading.noticeservice.domain.noticetest.mappertest;

import com.leading.noticeservice.domain.notice.dtotest.NoticeListDTOtest;
import com.leading.noticeservice.domain.notice.modeltest.Noticetest;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lenovo on 2019/11/20.
 */
public interface NoticeMappertest {
    String BASE_COLUMN_LIST = " id,title,content,status,createTime,updateTime ";

    @Select(
            {"select ",BASE_COLUMN_LIST,
                    " from `Notice` where id = #{id} and deleted = 0"}
    )
    Noticetest getNoticeBuId(Long id);

    @Select({
            "<script>",
            "INSERT INTO 'Notice'",
            "(title, content, status,createTime, updateTIme)",
            "<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\" >",
            "#{title}, #{content},#{status}, now(), now()",
            "</trim>",
            "</script>",
    })
    @Options(useGeneratedKeys=true, keyProperty="id")
    Long createNoticeInfo(Noticetest notice);

    @Select({
            "<script>",
            "select count(1) from Notice n where deleted = 0",
            "<if test=\" null != startTime \">",
            "AND n.createTime &gt;= #{startTime}",
            "</if>",
            "<if test=\" null != endTime \">",
            "AND n.createTime &lt;= #{endTime}",
            "</if>",
            "</script>"
    })
    Integer countAllNotices(@Param("startTime")LocalDate startTime, @Param("endTime")LocalDate endtime);

    @Select({
            "<script>",
            "select",
            " id,title,createTime,status",
            " from Notice n where deleted = 0",
            "<if test=\" null != startTime \">",
            "AND n.createTime &gt;= #{startTime}",
            "</if>",
            "<if test=\" null != endTime \">",
            "AND n.createTime &lt;= #{endTime}",
            "</if>",
            " order by updateTime desc ",
            " limit #{offset},#{pageSize} ",
            "</script>"
    })
    List<NoticeListDTOtest> findAllNoticeList(@Param("startTIme")LocalDate startTime,@Param("endTime")LocalDate endTime,@Param("offset")Integer offset,@Param("pageSize")Integer pageSize);

    @Update({
            "update Notice n set n.deleted = 1 where n.status in (0,2) and n.id = #{id}"
    })
    int deleteNoticeById(@Param("id") Long id);

    @Update({
            "update Notice n set n.status = 2 where n.status = 1 and n.deleted = 0 and n.id = #{id}"
    })
    int revokeNoticeById(@Param("id") Long id);

    @Update({
            "update Notice n set n.status = 1 where deleted = 0 and n.id = #{id}"
    })
    Long updatePushNoticeStatus(@Param("id") Long id);

    @Update({
            "update Notice n set n.status = #{status} , n.title = #{title} , n.content = #{content} "
                    +   "where n.id = #{id} and n.status in(0,2) and n.deleted = 0"
    })
    Long updateNotice(Noticetest notice);
}
