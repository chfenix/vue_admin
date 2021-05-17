package cn.solwind.admin.mapper;

import cn.solwind.admin.entity.Sysbook;
import cn.solwind.admin.pojo.sys.SysbookQuery;
import cn.solwind.admin.pojo.sys.SysbookVO;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SysbookMapper extends Mapper<Sysbook> {

    @Select({"<script>",
            "SELECT * FROM SYSBOOK WHERE STATUS=1 ",
            " <if test='typeCode != null'>AND TYPE_CODE = #{typeCode}</if>",
            " ORDER BY TYPE_CODE,LIST_CODE",
            "</script>"
    })
    List<SysbookVO> selectByQuery(SysbookQuery sysbookQuery);
}
