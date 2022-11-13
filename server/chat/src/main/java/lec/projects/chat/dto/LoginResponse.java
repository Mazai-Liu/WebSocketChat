package lec.projects.chat.dto;


import lombok.Data;

@Data
public class LoginResponse {
    private Integer id;
    private String username;
    private String avatar;
    private String token;
}
