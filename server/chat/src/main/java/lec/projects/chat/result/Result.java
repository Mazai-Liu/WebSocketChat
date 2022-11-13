package lec.projects.chat.result;

import lec.projects.chat.enums.MessageAndCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private String code;
    private String msg;
    private T data;

    public static <T> Result<T> success(MessageAndCode info,T data){
        return new Result<>(info.getCode(),info.getMessage(),data);
    }
    public static <T> Result<T> fail(MessageAndCode info){
        return new Result<>(info.getCode(),info.getMessage(),null);
    }
}
