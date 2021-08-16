package com.example.shirodemo.controller;

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
    public String doPostLogin(User user, Model model) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/";
        }
        String username = user.getUsername();
        String password = user.getPassword();
        if ("".equals(username) || "".equals(password)) {
            model.addAttribute("msg", "用户名或密码不能为空");
            return "login";
        }
        if (username.length() > 10 || username.length() < 2) {
            model.addAttribute("msg", "用户名长度必须在2~10之间");
            return "login";
        }
        if (password.length() > 16 || password.length() < 8) {
            model.addAttribute("msg", "密码长度必须在8~16之间");
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
    public String doPostRegister(User user, Model model) {
        Subject subject = SecurityUtils.getSubject();
        String username = user.getUsername();
        String password = user.getPassword();
        if (subject.isAuthenticated()) {
            return "redirect:/";
        }
        if ("".equals(username)) {
            model.addAttribute("msg", "用户名不能为空");
            return "register";
        }
        if ("".equals(password)) {
            model.addAttribute("msg", "密码不能为空");
            return "register";
        }
        if (username.length() > 10 || username.length() < 2) {
            model.addAttribute("msg", "用户名长度必须在2~10之间");
            return "login";
        }
        if (password.length() > 16 || password.length() < 8) {
            model.addAttribute("msg", "密码长度必须在8~16之间");
            return "login";
        }
        User userSaved = userService.register(user);
        if (userSaved != null) {
            return "redirect:/login";
        }
        model.addAttribute("msg", "该用户名已存在");
        return "register";
    }
}
