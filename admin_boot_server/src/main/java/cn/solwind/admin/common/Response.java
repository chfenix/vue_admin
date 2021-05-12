package cn.solwind.admin.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 接口调用反馈类
 * @param <T>
 */
@Getter
@Setter
public class Response<T> {

    private Integer code;       // 返回码
    private String message;     // 返回信息

    private T data;

    @Override
    public String toString() {
        return "Response{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
