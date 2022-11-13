package lec.projects.chat.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SetAvatarForm {
    private Integer id;
    private String username;
    private String avatar;
    private MultipartFile multipartFile;
    private String token;
}
