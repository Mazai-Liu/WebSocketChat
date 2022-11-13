package lec.projects.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    private String _id;
    private String time;
    private String content;
    private String fromName;
    private String fromAvatar;
    private String toName;
}
