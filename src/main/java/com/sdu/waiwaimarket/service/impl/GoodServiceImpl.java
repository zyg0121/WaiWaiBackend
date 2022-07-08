package com.sdu.waiwaimarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdu.waiwaimarket.mapper.CategoryMapper;
import com.sdu.waiwaimarket.mapper.GoodMapper;
import com.sdu.waiwaimarket.mapper.UserMapper;
import com.sdu.waiwaimarket.pojo.*;
import com.sdu.waiwaimarket.service.GoodService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodServiceImpl implements GoodService {
    @Autowired
    GoodMapper goodMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    UserMapper userMapper;

    //添加商品
    @Override
    public boolean goodInsert(GoodInsertDTO goodInsertDTO) {
        GoodDAO goodDAO = new GoodDAO();
        BeanUtils.copyProperties(goodInsertDTO, goodDAO);
        goodDAO.setGoodsstatus(0);          //商品状态默认0正常
        Integer num = goodMapper.insert(goodDAO);

        return num >= 1 ? true : false;
    }

    @Override
    public boolean goodInsertImg(GoodInsertImgDTO insertImgDTO) {
        Integer id = insertImgDTO.getGoodsid();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsid", id);

        GoodDAO goodDAO = new GoodDAO();
        goodDAO.setGoodsimg(insertImgDTO.getGoodsimg());

        Integer num = goodMapper.update(goodDAO, queryWrapper);

        return num >= 1 ? true : false;
    }

    //修改商品
    @Override
    public boolean goodUpdate(GoodUpdateDTO goodUpdateDTO) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsid", goodUpdateDTO.getGoodsid());

        GoodDAO goodDAO = new GoodDAO();
        BeanUtils.copyProperties(goodUpdateDTO, goodDAO);

        Integer num = goodMapper.update(goodDAO, queryWrapper);

        return num >= 1 ? true : false;
    }


    //删除商品
    @Override
    public boolean goodDelete(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsid", id);
        Integer num = goodMapper.delete(queryWrapper);

        return num >= 1 ? true : false;
    }

    //改变商品状态
    @Override
    public boolean goodUpdateStatus(Integer goodsid, Integer status) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsid", goodsid);


        GoodDAO goodDAO = new GoodDAO();
        goodDAO.setGoodsstatus(status);

        Integer num = goodMapper.update(goodDAO, queryWrapper);

        return num >= 1 ? true : false;
    }


    @Override
    public List<GoodVO> goodSelectByCategory(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("categoryid", id);
        queryWrapper.eq("goodsstatus", 0);

        List<GoodDAO> goodDAOS = goodMapper.selectList(queryWrapper);
        List<GoodVO> goodVOS = new ArrayList<>();

        for(GoodDAO goodDAO:goodDAOS){
            //查找分类名称
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("categoryid", goodDAO.getCategoryid());
            CategoryDAO categoryDAO = categoryMapper.selectOne(queryWrapper2);

            //查找卖家姓名
            QueryWrapper queryWrapper3 = new QueryWrapper();
            queryWrapper3.eq("userid", goodDAO.getUserid());
            UserDAO userDAO = userMapper.selectOne(queryWrapper3);

            GoodVO goodVO = new GoodVO();
            BeanUtils.copyProperties(goodDAO, goodVO);
            goodVO.setCategoryname(categoryDAO.getCategoryname());
            goodVO.setCategoryid(categoryDAO.getCategoryid());
            goodVO.setUsername(userDAO.getUsername());
            goodVOS.add(goodVO);
        }

        return goodVOS;
    }

    //按商品号搜索商品
    @Override
    public GoodVO goodSelectById(Integer id) {
        //查找商品
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsid", id);
        GoodDAO goodDAO = goodMapper.selectOne(queryWrapper);

        //查找商品分类名称
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("categoryid", goodDAO.getCategoryid());
        CategoryDAO categoryDAO = categoryMapper.selectOne(queryWrapper2);

        //查找卖家姓名
        QueryWrapper queryWrapper3 = new QueryWrapper();
        queryWrapper3.eq("userid", goodDAO.getUserid());
        UserDAO userDAO = userMapper.selectOne(queryWrapper3);

        //设置GoodVO
        GoodVO goodVO = new GoodVO();
        BeanUtils.copyProperties(goodDAO, goodVO);
        goodVO.setCategoryname(categoryDAO.getCategoryname());
        goodVO.setUsername(userDAO.getUsername());

        return goodVO;
    }

    @Override
    public List<GoodVO> goodSelectByNew(Integer num) {
        //查找相应商品
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("1", 1);
        queryWrapper.orderByDesc("goodsdate");
        queryWrapper.eq("goodsstatus", 0);
        List<GoodDAO> goodDAOS = goodMapper.selectList(queryWrapper);
        List<GoodVO> goodVOS = new ArrayList<>();
        //判断需求数量是否大于数据库数据量
        if(num > goodDAOS.size())
            num = goodDAOS.size();
        goodDAOS = goodDAOS.subList(0, num);

        for(GoodDAO goodDAO:goodDAOS){
            //查找分类名称
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("categoryid", goodDAO.getCategoryid());
            CategoryDAO categoryDAO = categoryMapper.selectOne(queryWrapper2);

            //查找卖家姓名
            QueryWrapper queryWrapper3 = new QueryWrapper();
            queryWrapper3.eq("userid", goodDAO.getUserid());
            UserDAO userDAO = userMapper.selectOne(queryWrapper3);

            GoodVO goodVO = new GoodVO();
            BeanUtils.copyProperties(goodDAO, goodVO);
            if(categoryDAO != null)
                goodVO.setCategoryname(categoryDAO.getCategoryname());
            if(categoryDAO != null)
                goodVO.setCategoryid(categoryDAO.getCategoryid());
            if(userDAO != null)
                goodVO.setUsername(userDAO.getUsername());
            goodVOS.add(goodVO);
        }

        return goodVOS;
    }

    @Override
    public List<GoodVO> goodSelectByPrice(Integer num) {
        //查找相应商品
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsstatus", 0);
        queryWrapper.eq("1", 1);
        queryWrapper.orderByAsc("`goodsprice`");

        List<GoodDAO> goodDAOS = goodMapper.selectList(queryWrapper);
        List<GoodVO> goodVOS = new ArrayList<>();
        //判断需求数量是否大于数据库数据量
        if(num > goodDAOS.size())
            num = goodDAOS.size();
        goodDAOS = goodDAOS.subList(0, num);

        for(GoodDAO goodDAO:goodDAOS){
            //查找分类名称
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("categoryid", goodDAO.getCategoryid());
            CategoryDAO categoryDAO = categoryMapper.selectOne(queryWrapper2);

            //查找卖家姓名
            QueryWrapper queryWrapper3 = new QueryWrapper();
            queryWrapper3.eq("userid", goodDAO.getUserid());
            UserDAO userDAO = userMapper.selectOne(queryWrapper3);

            GoodVO goodVO = new GoodVO();
            BeanUtils.copyProperties(goodDAO, goodVO);
            if(categoryDAO != null)
                goodVO.setCategoryname(categoryDAO.getCategoryname());
            if(categoryDAO != null)
                goodVO.setCategoryid(categoryDAO.getCategoryid());
            if(userDAO != null)
                goodVO.setUsername(userDAO.getUsername());
            goodVOS.add(goodVO);
        }

        return goodVOS;
    }

    //模糊查询
    @Override
    public List<GoodVO> goodSelectByName(String name) {
        List<GoodDAO> goodDAOS = goodMapper.goodSelectByName(name);
        List<GoodVO> goodVOS  = new ArrayList<>();

        for(GoodDAO goodDAO:goodDAOS){
            //查找分类名称
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("categoryid", goodDAO.getCategoryid());
            CategoryDAO categoryDAO = categoryMapper.selectOne(queryWrapper2);

            //查找卖家姓名
            QueryWrapper queryWrapper3 = new QueryWrapper();
            queryWrapper3.eq("userid", goodDAO.getUserid());
            UserDAO userDAO = userMapper.selectOne(queryWrapper3);

            GoodVO goodVO = new GoodVO();
            BeanUtils.copyProperties(goodDAO, goodVO);
            goodVO.setCategoryname(categoryDAO.getCategoryname());
            goodVO.setCategoryid(categoryDAO.getCategoryid());
            goodVO.setUsername(userDAO.getUsername());
            goodVOS.add(goodVO);
        }

        return goodVOS;
    }

    //分页查询
    @Override
    public IPage<GoodDAO> goodSelectAll(Integer pageNum, Integer pageSize,String goodsName) {
        Page<GoodDAO> page = new Page(pageNum , pageSize);
        if(goodsName == null) {
            // 单表查询crud，selectPage
            /*QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("goodsname", goodsName);
            IPage<GoodDAO> productIPage = goodMapper.selectPage(page , queryWrapper2);*/
            IPage<GoodDAO> productIPage = goodMapper.selectPage(page , null);
            return productIPage;
        }
        else {
            IPage<GoodDAO> productIPage = goodMapper.selectByPage(page , goodsName);
            return productIPage;
        }
    }

    @Override
    public IPage<GoodVO> goodSelectAll2(Integer pageNum, Integer pageSize, String goodsName) {
        Page<GoodVO> page = new Page(pageNum , pageSize);
        if(goodsName == null) {
            IPage<GoodVO> productIPage = goodMapper.selectByPage2(page , null);
            return productIPage;
        }
        else {
            IPage<GoodVO> productIPage = goodMapper.selectByPage2(page , goodsName);
            return productIPage;
        }
    }

    @Override
    public IPage<GoodVO> goodSelectByCategoryPage(Integer pageNum, Integer pageSize, Integer id) {
        Page<GoodVO> page = new Page(pageNum , pageSize);
        IPage<GoodVO> productIPage = goodMapper.goodSelectByCategoryPage(page, id);

        return productIPage;
    }
}
