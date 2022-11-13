package lec.projects.chat.service;

import lec.projects.chat.dto.LoginResponse;
import lec.projects.chat.dto.NameAndAvatar;
import lec.projects.chat.entity.User;
import lec.projects.chat.form.LoginForm;
import lec.projects.chat.form.SetAvatarForm;
import lec.projects.chat.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {
    Result<LoginResponse> checkLogin(LoginForm loginForm);

    Result<?> logout();

    List<NameAndAvatar> getAvatarsByNames(Set<String> names);

    Result<?> setAvatar(String id, MultipartFile multipartFile);
}
