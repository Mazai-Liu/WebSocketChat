package lec.projects.chat.mapper;

import lec.projects.chat.dto.NameAndAvatar;
import lec.projects.chat.entity.User;
import lec.projects.chat.form.LoginForm;
import lec.projects.chat.form.SetAvatarForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByUsernameAndPassword(LoginForm loginForm);

    int insertUser(User user);

    int selectUsername(String username);

    List<NameAndAvatar> getAvatarsByNames(@Param("usernames") Set<String> usernames);

    int setAvatarById(@Param("id")String id,@Param("newAvatarPath") String newAvatarPath);

    String getAvatarsById(String id);
}
