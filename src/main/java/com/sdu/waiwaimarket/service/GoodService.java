package com.sdu.waiwaimarket.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sdu.waiwaimarket.pojo.*;

import java.util.List;

public interface GoodService {
    public boolean goodInsert(GoodInsertDTO goodInsertDTO);
    public boolean goodInsertImg(GoodInsertImgDTO insertImgDTO);
    public boolean goodUpdate(GoodUpdateDTO goodUpdateDTO);
    public boolean goodDelete(Integer id);
    public List<GoodVO> goodSelectByCategory(Integer id);
    public GoodVO goodSelectById(Integer id);
    public List<GoodVO> goodSelectByNew(Integer num);
    public List<GoodVO> goodSelectByName(String name);
    public IPage<GoodDAO> goodSelectAll(Integer pageNum, Integer pageSize, String goodsName);
}
