package com.yiyou.repast.weixin.service;

public interface CartBusinessService {
	
	/**
	 * 添加商品到购物车
	 * */
	void addCart(Long userId,String deskNum,Long goodsId,String auxIds,Integer count,String amount);
	
	/**
	 * 修改购物车商品数量
	 * */
	void updateCartItem(Long cartId,Long cartItemId,Integer count);
	
	/**
	 * 删除购物车项
	 * */
	void removeCartItem(Long cartId,Long cartItemId);

}
