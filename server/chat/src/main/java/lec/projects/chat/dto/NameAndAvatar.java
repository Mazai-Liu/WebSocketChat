package lec.projects.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NameAndAvatar {
    private String username;
    private String avatar;
}
