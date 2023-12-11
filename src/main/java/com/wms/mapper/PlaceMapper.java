package com.wms.mapper;

import com.wms.entity.Place;

import java.util.List;

public interface PlaceMapper {

    // 查询所有产地的方法
    public List<Place> findAllPlace();
}
