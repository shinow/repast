package com.yiyou.repast.merchant.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Service;
import com.yiyou.repast.merchant.dao.GoodsRepository;
import com.yiyou.repast.merchant.model.Goods;
import com.yiyou.repast.merchant.service.IGoodsService;

import repast.yiyou.common.util.DataGrid;

@Service
public class GoodsServiceImpl implements IGoodsService {

    @Resource
    private GoodsRepository goodsRepository;

    @Override
    public Goods findById(Long merchantId, Long goodsId) {
    	Goods obj=goodsRepository.findOne(goodsId);
    	return obj;
    }

    @Override
    public List<Goods> findShelves(Long maerchanId) {
        return goodsRepository.findAllByMerchantIdAndShelvesTrue(maerchanId);
    }

    @Override
    public Goods findByGoodsName(Long merchantId, String goodsName) {
        return null;
    }

    @Override
    public List<Goods> findByIds(Long merchantId, List<Long> ids) {
        return goodsRepository.findByIds(merchantId, ids);
    }

    @Override
    public Goods save(Long merchantId, Goods obj) {
        obj.setCreateTime(new Date());
        obj.setMerchantId(merchantId);
        return goodsRepository.save(obj);
    }

    @Override
    public void remove(Long merchantId, Long goodsId) {
        Goods obj = goodsRepository.findByMerchantIdAndId(merchantId, goodsId);
        obj.setShelves(false);
        goodsRepository.save(obj);
    }

    @Override
    public Goods update(Long merchantId, Goods obj) {
        return goodsRepository.save(obj);
    }

    @Override
    public List<Goods> findAll(Long merchantId) {
        return goodsRepository.findAllByMerchantId(merchantId);
    }

    @Override
    public DataGrid<Goods> findList(Long merchantId, int page, int pageSize) {
        // TODO: 2017/9/12 分页
        return null;
    }

}
