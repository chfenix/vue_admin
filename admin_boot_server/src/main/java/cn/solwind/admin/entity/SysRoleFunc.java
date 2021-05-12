package cn.solwind.admin.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SysRoleFunc {

    /**
     * 
     * SYS_ROLE_FUNC.ROLE_ID
     *
     * @mbg.generated
     */
    private Long roleId;

    /**
     * 
     * SYS_ROLE_FUNC.FUNC_ID
     *
     * @mbg.generated
     */
    private Long funcId;
}
