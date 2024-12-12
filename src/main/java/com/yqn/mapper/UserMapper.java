package com.yqn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yqn.pojo.User;
import org.apache.ibatis.annotations.Select;

/**
 * 用户数据库访问层
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据邮箱查询用户
     *
     * @param email 用户邮箱
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE email = #{email}")
    User selectByEmail(String email);

    /**
     * 根据学号和邮箱查询用户
     *
     * @param studentId 学号
     * @param email     用户邮箱
     * @return 用户对象
     */
    @Select("SELECT * FROM user WHERE student_id = #{studentId} AND email = #{email}")
    User selectByStudentIdAndEmail(String studentId, String email);

}
