package lec.projects.chat.service.impl;

import lec.projects.chat.dto.LoginResponse;
import lec.projects.chat.dto.NameAndAvatar;
import lec.projects.chat.entity.User;
import lec.projects.chat.enums.MessageAndCode;
import lec.projects.chat.form.LoginForm;
import lec.projects.chat.form.SetAvatarForm;
import lec.projects.chat.mapper.UserMapper;
import lec.projects.chat.result.Result;
import lec.projects.chat.service.UserService;
import lec.projects.chat.utils.GithubUploader;
import lec.projects.chat.utils.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.Query;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GithubUploader githubUploader;
    @Autowired
    private RecordServiceImpl recordService;

    public static final String AVATAR_PREFIX = "img/chat/avatar/";

    /**
     * 登录验证，没有则新增
     * @param loginForm
     * @return
     */
    @Override
    public Result<LoginResponse> checkLogin(LoginForm loginForm) {
        User user = userMapper.selectByUsernameAndPassword(loginForm);
        if(user == null) {
            if(userMapper.selectUsername(loginForm.getUsername()) == 1)
                return Result.fail(MessageAndCode.USER_EXIST);

            user = new User(loginForm);
            int result = userMapper.insertUser(user);
            if(result == 0)
                return Result.fail(MessageAndCode.SIGNUPFAIL);
        }

        LoginResponse loginResponse = new LoginResponse();
        BeanUtils.copyProperties(user, loginResponse);
        loginResponse.setToken(JwtUtil.getJwtToken(loginResponse.getId(),loginResponse.getUsername()));

        return Result.success(MessageAndCode.LOGIN_SUCCESS,loginResponse);
    }

    /**
     * 退出，未做操作
     * // TODO 将token保存在redis中以保持登录状态，退出后删除token
     * @return
     */
    @Override
    public Result<?> logout() {
        return Result.success(MessageAndCode.OK, null);
    }

    /**
     * 获取若干个头像
     * @param usernames 用户名集合
     * @return （用户名，头像）的列表
     */
    @Override
    public List<NameAndAvatar> getAvatarsByNames(Set<String> usernames) {
        List<NameAndAvatar> nameAndAvatars = userMapper.getAvatarsByNames(usernames);

        return nameAndAvatars;
    }

    /**
     * 用户设置头像
     * @param id 用户id
     * @param multipartFile 上传的头像文件
     * @return
     */
    @Override
    public Result<?> setAvatar(String id, MultipartFile multipartFile) {
        // 老头像
        String oldAvatarPath = userMapper.getAvatarsById(id);
        if(!oldAvatarPath.equals(AVATAR_PREFIX + "avatar.png")) {
            githubUploader.deleteAvatar(oldAvatarPath);
        }

        // 上传新头像
        String newAvatarPath = ((LinkedHashMap)githubUploader.uploadAvatar(multipartFile).get("content")).get("path").toString();

        // 修改数据库中头像值
        int count = userMapper.setAvatarById(id,newAvatarPath);

        if(count == 1){
            User user = userMapper.selectByPrimaryKey(Integer.valueOf(id));

            // 修改历史消息中头像
            recordService.setAvatarAfterChanged(user.getUsername(),newAvatarPath);

            LoginResponse loginResponse = new LoginResponse();
            BeanUtils.copyProperties(user, loginResponse);
            loginResponse.setToken(JwtUtil.getJwtToken(loginResponse.getId(),loginResponse.getUsername()));
            return Result.success(MessageAndCode.OK, loginResponse);
        }

        return Result.fail(MessageAndCode.EDIT_FAIL);
    }
}
