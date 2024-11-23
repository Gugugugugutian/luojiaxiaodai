package com.yqn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yqn.pojo.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 重置用户密码
     *
     * @param studentId   学号
     * @param email       用户邮箱
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    boolean resetPassword(String studentId, String email, String newPassword);

    /**
     * 校验学号和邮箱是否匹配
     *
     * @param studentId 学号
     * @param email     用户邮箱
     * @return 用户对象（若匹配）或 null
     */
    User findByStudentIdAndEmail(String studentId, String email);

    /**
     * 根据邮箱查询用户
     *
     * @param email 用户邮箱
     * @return 用户对象（若存在）或 null
     */
    User findByEmail(String email);

    /**
     * 验证学号和邮箱的有效性
     *
     * @param studentId 学号
     * @param email     邮箱
     * @return 如果有效返回 true，否则返回 false
     */
    boolean isValidStudentIdAndEmail(String studentId, String email);
}
