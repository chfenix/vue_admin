package cn.solwind.admin.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenValue implements java.io.Serializable {

    /**
     *  请求头的值
     */
    private String header;

    /**
     * token 值
     */
    private String value;

    /**
     * token 前缀
     */
    private String prefix;

    /**
     * 过期时间（毫秒，这里默认20分钟）
     */
    private Long expiration;
}
