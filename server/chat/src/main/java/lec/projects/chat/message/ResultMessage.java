package lec.projects.chat.message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultMessage {
    private Boolean isSystem;
    private Boolean isListUsers;
    private Boolean isSingle;
    private String fromName;
    private Integer headcount;
    private Object message;


}
