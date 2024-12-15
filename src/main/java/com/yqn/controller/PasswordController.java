package com.yqn.controller;

import com.yqn.service.UserService;
import com.yqn.common.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private UserService userService;

    private final Map<String, String> verificationCodes = new HashMap<>();
    private final Map<String, Long> verificationCodeTimestamps = new HashMap<>();

    private static final long CODE_EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(5); // 验证码有效期5分钟

    @PostMapping("/send-code")
    public ResponseEntity<Map<String, Object>> sendCode(@RequestParam String studentId, @RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        // 校验学号和邮箱是否匹配
        if (!userService.isValidStudentIdAndEmail(studentId, email)) {
            response.put("status", false);
            response.put("msg", "学号或邮箱不正确");
            return ResponseEntity.badRequest().body(response);
        }

        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        verificationCodes.put(email, code);

        try {
            // 发送邮件
            mailUtil.sendEmail(email, "找回密码验证码", "您的验证码是：" + code);
            response.put("status", true);
            response.put("msg", "验证码发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", false);
            response.put("msg", "验证码发送失败，请检查邮箱配置");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register/send-code")
    public ResponseEntity<Map<String, Object>> sendCodeRegister(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        // 生成6位随机验证码
        String code = String.format("%06d", new Random().nextInt(999999));
        verificationCodes.put(email, code);

        try {
            // 发送邮件
            mailUtil.sendEmail(email, "注册验证码", "您的验证码是：" + code);
            response.put("status", true);
            response.put("msg", "验证码发送成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", false);
            response.put("msg", "验证码发送失败，请检查邮箱配置");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register/verifyCodes")
    public boolean verifyCodes(@RequestParam String email, @RequestParam String code) {
        return verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code);
    }

    /**
     * 重置密码
     */
    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam String studentId, @RequestParam String email,
                                                             @RequestParam String code, @RequestParam String newPassword) {
        Map<String, Object> response = new HashMap<>();

        // 校验参数
        if (studentId == null || studentId.isEmpty() || email == null || email.isEmpty() || code == null || code.isEmpty()) {
            response.put("status", false);
            response.put("msg", "学号、邮箱和验证码不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        // 检查验证码是否存在且匹配
        if (!verificationCodes.containsKey(email) || !verificationCodes.get(email).equals(code)) {
            response.put("status", false);
            response.put("msg", "验证码错误或已过期");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 调用服务层重置密码
            boolean success = userService.resetPassword(studentId, email, newPassword);
            if (success) {
                verificationCodes.remove(email); // 密码重置成功后清除验证码
                response.put("status", true);
                response.put("msg", "密码重置成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("status", false);
                response.put("msg", "重置密码失败，请检查学号或邮箱");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("status", false);
            response.put("msg", "密码重置失败，请稍后再试");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
