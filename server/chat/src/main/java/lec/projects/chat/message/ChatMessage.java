package lec.projects.chat.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String time;
    private String fromName;
    private String toName;
    private String content;
    private String fromAvatar = "/avatar.png";
}
