package com.yqn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqn.mapper.UserMapper;
import com.yqn.pojo.User;
import com.yqn.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 重置用户密码
     *
     * @param studentId   学号
     * @param email       用户邮箱
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    @Override
    public boolean resetPassword(String studentId, String email, String newPassword) {
        // 根据学号和邮箱查询用户
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("student_id", studentId).eq("email", email); // Use student_id instead of username
        User user = userMapper.selectOne(query);

        if (user == null) {
            return false; // 用户不存在或信息不匹配
        }

        // 对新密码进行加密
        String encryptedPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        user.setPassword(newPassword); // 将加密后的密码设置到用户对象

        // 更新用户信息
        int result = userMapper.updateById(user);
        return result > 0; // 更新成功返回 true
    }

    /**
     * 校验学号和邮箱是否匹配
     *
     * @param studentId 学号
     * @param email     用户邮箱
     * @return 用户对象（若匹配）或 null
     */
    @Override
    public User findByStudentIdAndEmail(String studentId, String email) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("student_id", studentId).eq("email", email); // Use student_id instead of username
        return userMapper.selectOne(query); // 返回匹配的用户
    }

    /**
     * 校验学号和邮箱是否有效
     *
     * @param studentId 学号
     * @param email     用户邮箱
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValidStudentIdAndEmail(String studentId, String email) {
        User user = findByStudentIdAndEmail(studentId, email); // 调用 findByStudentIdAndEmail 方法
        return user != null;
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email 用户邮箱
     * @return 用户对象（若存在）或 null
     */
    @Override
    public User findByEmail(String email) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email", email);
        return userMapper.selectOne(query); // 返回匹配的用户
    }
}
