package lec.projects.chat.entity;

import lec.projects.chat.form.LoginForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String avatar;
    private String phone;

    public User(LoginForm loginForm){
        this.username = loginForm.getUsername();
        this.password = loginForm.getPassword();
    }
}

