package com.yiyou.repast.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.yiyou.repast.order.dao.OrderItemRepository;
import com.yiyou.repast.order.dao.OrderProcessRepository;
import com.yiyou.repast.order.dao.OrderRepository;
import com.yiyou.repast.order.model.Order;
import com.yiyou.repast.order.model.OrderItem;
import com.yiyou.repast.order.model.OrderProcess;
import com.yiyou.repast.order.service.IOrderService;
import com.yiyou.repast.order.tools.PageConvertDataGrid;

import repast.yiyou.common.base.EnumDefinition.OrderProcessStatus;
import repast.yiyou.common.base.EnumDefinition.OrderStaus;
import repast.yiyou.common.util.CommonUtils;
import repast.yiyou.common.util.DataGrid;

@Service
public class OrderServiceImpl implements IOrderService {
	
	@Resource
	private OrderItemRepository orderItemRepository;
	@Resource
	private OrderRepository orderRepository;
	@Resource
	private OrderProcessRepository orderProcessRepository;

	@Override
	public Order save(Order obj) {
		return orderRepository.save(obj);
	}

	@Override
	public Order update(Order obj) {
		return orderRepository.save(obj);
	}

	@Override
	public OrderItem saveOrderItem(OrderItem obj) {
		return orderItemRepository.save(obj);
	}

	@Override
	public void remove(Long id) {
		orderRepository.delete(id);
	}

	@Override
	public Order findById(Long id) {
		return orderRepository.findOne(id);
	}

	@Override
	public List<Order> findByUserId(Long userId) {
		Order obj=new Order();
		obj.setUserId(userId);
	    ExampleMatcher matcher = ExampleMatcher.matching();
	    Example<Order> example = Example.of(obj, matcher); 
		return orderRepository.findAll(example);
	}
	
	@Override
	public List<Order> findByDeskNum(String deskNum){
		if(StringUtils.isEmpty(deskNum))return null;
		Order obj=new Order();
		obj.setDeskNum(deskNum);
	    ExampleMatcher matcher = ExampleMatcher.matching();
	    Example<Order> example = Example.of(obj, matcher); 
		return orderRepository.findAll(example);
	}

	@Override
	public DataGrid<Order> findOrderList(Long merchantId, String orderId, String deskNum, OrderStaus status,
			String startTime, String endTime, int page, int pageSize) {
	/*	Order obj=new Order();
		if(merchantId!=null)obj.setMerchantId(merchantId);
		if(!StringUtils.isEmpty(orderId))obj.setOrderId(orderId);
		if(!StringUtils.isEmpty(deskNum))obj.setDeskNum(deskNum);
		if(status!=null)obj.setStatus(status);
		
	    ExampleMatcher matcher = ExampleMatcher.matching();
	    Example<Order> example = Example.of(obj, matcher); 
	    
	    
		Page<Order> pages=orderRepository.findAll(example, pageable);*/
		Pageable pageable = new PageRequest(page, pageSize, Sort.Direction.DESC, "id");  
		Specification<Order> querySpecifi = new Specification<Order>() {  
            @Override  
            public Predicate toPredicate(Root<Order> root, javax.persistence.criteria.CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {  
                List<Predicate> predicates = new ArrayList<>();  
                if(merchantId!=null) {
                	predicates.add(cb.equal(root.get("merchantId").as(Long.class),merchantId)); 
                }
                Path<Date> createDate = root.get("createTime");
                if (!StringUtils.isEmpty(startTime)) {  
                    predicates.add(cb.greaterThanOrEqualTo(createDate,CommonUtils.parseDate(startTime+" 00:00:00", "yyyy-MM-dd HH:mm:ss")));  
                }  
                if (!StringUtils.isEmpty(endTime)) {  
                    predicates.add(cb.lessThanOrEqualTo(createDate, CommonUtils.parseDate(endTime+" 23:59:59", "yyyy-MM-dd HH:mm:ss")));  
                }  
                if (!StringUtils.isEmpty(orderId)) {  
                	predicates.add(cb.equal(root.get("orderId").as(String.class), orderId)); 
                }  
                if (!StringUtils.isEmpty(deskNum)) {  
                	predicates.add(cb.equal(root.get("deskNum").as(String.class), deskNum)); 
                }  
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));  
            }  
        };  
        Page<Order> pages=orderRepository.findAll(querySpecifi, pageable);
		return new PageConvertDataGrid.Bulid<Order>().page(pages).build().getData();
	}

	@Override
	public OrderItem updateOrderItem(OrderItem obj) {
		return orderItemRepository.save(obj);
	}

	@Override
	public OrderItem findItemById(Long itemId) {
		return orderItemRepository.findOne(itemId);
	}
	
	@Override
	public void removeOrderItem(OrderItem obj) {
		orderItemRepository.delete(obj);
	}

	@Override
	public OrderProcess saveOrderProcess(OrderProcess obj) {
		obj.setCreateTime(new Date());
		obj.setStatus(OrderProcessStatus.await);
		return orderProcessRepository.save(obj);
	}

	@Override
	public OrderProcess updateOrderProcess(OrderProcess obj) {
		return orderProcessRepository.save(obj);
	}

	@Override
	public OrderProcess findOrderProcessById(Long id) {
		return orderProcessRepository.findOne(id);
	}

	@Override
	public OrderProcess findOrderProcessByOrderId(Long orderId) {
		OrderProcess obj=new OrderProcess();
		obj.setOrderId(orderId);
	    ExampleMatcher matcher = ExampleMatcher.matching();
	    Example<OrderProcess> example = Example.of(obj, matcher); 
		List<OrderProcess> list=orderProcessRepository.findAll(example);
		return CollectionUtils.isEmpty(list)?null:list.get(0);
	}

	@Override
	public List<OrderProcess> findOrderProcessAwaitList() {
		OrderProcess obj=new OrderProcess();
		obj.setStatus(OrderProcessStatus.await);;
	    ExampleMatcher matcher = ExampleMatcher.matching();
	    Example<OrderProcess> example = Example.of(obj, matcher); 
		List<OrderProcess> list=orderProcessRepository.findAll(example);
		return list;
	}


}
