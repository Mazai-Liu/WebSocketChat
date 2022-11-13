package lec.projects.chat.controller;

import lec.projects.chat.dto.LoginResponse;
import lec.projects.chat.entity.User;
import lec.projects.chat.form.LoginForm;
import lec.projects.chat.form.LogoutForm;
import lec.projects.chat.form.SetAvatarForm;
import lec.projects.chat.result.Result;
import lec.projects.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginForm loginForm){
        return userService.checkLogin(loginForm);
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestBody LogoutForm logoutForm){
        return userService.logout();
    }

    @PostMapping("/setAvatar")
    public Result<?> setAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest){
        String id = httpServletRequest.getParameter("id");
        return userService.setAvatar(id, file);
    }
}
