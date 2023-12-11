package com.wms.mapper;

import com.wms.entity.OutStore;
import com.wms.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OutStoreMapper {

    // 添加出库单的方法
    public int insertOutStore(OutStore outStore);

    // 查询出库单行数的方法
    public Integer findOutStoreCount(OutStore outStore);

    // 分页查询出库单的方法
    public List<OutStore> findOutStoreByPage(@Param("page") Page page, @Param("outStore") OutStore outStore);

    // 根据id修改出库单状态为已出库的方法
    public int setIsOutById(Integer outStoreId);

}
