package com.example.shirodemo.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.util.StrUtil;
import com.example.shirodemo.entity.User;
import com.example.shirodemo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String doGetLogin(Model model) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/";
        }
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "login";
    }

    @PostMapping("/login")
    public String doPostLogin(User user, String code, Model model, HttpSession session) {
        ShearCaptcha captcha = (ShearCaptcha) session.getAttribute("captcha");
        if (captcha != null && !captcha.verify(code)) {
            session.removeAttribute("captcha");
            model.addAttribute("msg", "验证码错误");
            return "login";
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/";
        }
        String username = user.getUsername();
        String password = user.getPassword();
        if (StrUtil.hasEmpty(username) || StrUtil.hasBlank(username)) {
            model.addAttribute("msg", "用户名不能为空，不能有空格");
            return "login";
        }
        if (StrUtil.hasEmpty(password) || StrUtil.hasBlank(password)) {
            model.addAttribute("msg", "密码不能为空，不能有空格");
            return "login";
        }
        try {
            subject.login(new UsernamePasswordToken(username, password));
            return "redirect:/";
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            model.addAttribute("msg", "该用户不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            model.addAttribute("msg", "密码错误");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String doGetRegister() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String doPostRegister(User user, String code, Model model, HttpSession session) {
        ShearCaptcha captcha = (ShearCaptcha) session.getAttribute("captcha");
        if (captcha != null && !captcha.verify(code)) {
            session.removeAttribute("captcha");
            model.addAttribute("msg", "验证码错误");
            return "register";
        }
        Subject subject = SecurityUtils.getSubject();
        String username = user.getUsername();
        String password = user.getPassword();
        if (subject.isAuthenticated()) {
            return "redirect:/";
        }
        if (StrUtil.hasEmpty(username) || StrUtil.hasBlank(username)) {
            model.addAttribute("msg", "用户名不能为空，不能有空格");
            return "login";
        }
        if (StrUtil.hasEmpty(password) || StrUtil.hasBlank(password)) {
            model.addAttribute("msg", "密码不能为空，不能有空格");
            return "login";
        }
        User userSaved = userService.register(user);
        if (userSaved != null) {
            return "redirect:/login";
        }
        model.addAttribute("msg", "该用户名已存在");
        return "register";
    }

    @GetMapping("/captcha/**")
    public void captcha(HttpSession session, HttpServletResponse response) {
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            session.setAttribute("captcha", captcha);
            response.setContentType("image/png");
            captcha.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
