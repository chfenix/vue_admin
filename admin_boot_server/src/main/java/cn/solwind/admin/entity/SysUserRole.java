package cn.solwind.admin.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysUserRole {

    /**
     * 用户ID
     * SYS_USER_ROLE.USER_ID
     *
     * @mbg.generated
     */
    private Long userId;

    /**
     * 角色ID
     * SYS_USER_ROLE.ROLE_ID
     *
     * @mbg.generated
     */
    private Long roleId;
}
