package cn.solwind.admin.mapper;

import cn.solwind.admin.entity.Sysbook;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

public interface SysbookMapper extends Mapper<Sysbook> {

    @Select({ "" })
    Sysbook selectTEst();
}
