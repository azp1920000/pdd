package com.leading.noticeservice.domain.notice.mapper;

import com.leading.noticeservice.domain.notice.dto.NoticeListDTO;
import com.leading.noticeservice.domain.notice.model.Notice;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @Author: liukun
 * @Date: 2019/4/25 11:37
 * @Description:
 */
public interface NoticeMapper {
    String BASE_COLUMN_LIST = " id,title,content,status,createTime,updateTime ";
    /**
     * 根据id查询公告消息
     * @param id
     * @return
     */
    @Select(
            {"select ", BASE_COLUMN_LIST,
            " from `Notice` where id = #{id} and deleted = 0"}
    )
    Notice getNoticeById(Long id);

    @Insert({
            "<script>",
            " INSERT INTO `Notice` ",
            " (title, content, status, createTime, updateTime)",
            " <trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\" >",
            " #{title}, #{content},#{status}, now(), now()",
            " </trim>",
            "</script>",
    })
    @Options(useGeneratedKeys=true, keyProperty="id")
    Long createNoticeInfo(Notice notice);

    /**
     * 查询有效的公告消息总数
     * @return
     */
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
    Integer countAllNotices(@Param("startTime") LocalDate startTime, @Param("endTime")LocalDate endTime);

    /**
     * 查询有效的公告消息信息
     * @param startTime
     * @param endTime
     * @param offset
     * @param pageSize
     * @return
     */
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
    List<NoticeListDTO> findAllNoticeList(@Param("startTime") LocalDate startTime,@Param("endTime")LocalDate endTime,@Param("offset")Integer offset,@Param("pageSize")Integer pageSize);

    /**
     * 根据id将公告消息改为删除状态
     * @param id
     * @return
     */
    @Update({
            "update Notice n set n.deleted = 1 where n.status in (0,2) and n.id = #{id}"
    })
    int deleteNoticeById(@Param("id") Long id);

    /**
     * 根据id将公告消息改为撤回状态
     * @param id
     * @return
     */
    @Update({
            "update Notice n set n.status = 2 where n.status = 1 and n.deleted = 0 and n.id = #{id}"
    })
    int revokeNoticeById(@Param("id") Long id);

    @Update({
            "update Notice n set n.status = 1 where deleted = 0 and n.id = #{id}"
    })
    Long updatePushNoticeStatus(@Param("id") Long id);

    /**
     * 修改
     * @param notice
     * @return
     */
    @Update({
            "update Notice n set n.status = #{status} , n.title = #{title} , n.content = #{content} "
            +   "where n.id = #{id} and n.status in(0,2) and n.deleted = 0"
    })
    Long updateNotice(Notice notice);
}
