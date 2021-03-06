package com.yiyou.repast.merchant.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yiyou.repast.merchant.base.RBeanUtils;
import com.yiyou.repast.merchant.base.RspResult;
import com.yiyou.repast.merchant.base.ThreadContextHolder;
import com.yiyou.repast.merchant.model.User;
import com.yiyou.repast.merchant.model.UserAuthorizeApply;
import com.yiyou.repast.merchant.model.UserWhite;
import com.yiyou.repast.merchant.service.IUserAuthorizeApplyService;
import com.yiyou.repast.merchant.service.IUserService;
import com.yiyou.repast.merchant.service.IUserWhiteService;

import repast.yiyou.common.base.EnumDefinition.AuthorizeAuditStaus;
import repast.yiyou.common.base.EnumDefinition.UserWhiteStaus;

/**
 * 前端用户数据处理
 * */
@Controller
@RequestMapping("/user")
public class UserController {
	
	@Reference
	private IUserWhiteService userWhiteService;
	@Reference
	private IUserAuthorizeApplyService authorizeApplyService;
	@Reference
	private IUserService userService;
	
	/**
	 * 用户列表
	 * */
	@GetMapping("list")
	public String list(Model model) {
		return "/user/list";
	}
	
	@ResponseBody
	@PostMapping("/listData.do")
	public List<User> listData(Integer page,Integer pageSize) {
		page=page==null?page=0:page;
		pageSize=Integer.MAX_VALUE;//客户端分页，服务端查询所有数据
		List<User> data=userService.findAll(ThreadContextHolder.getCurrentMerchantId());
		return data;
	}

	@GetMapping("/edit")
	public String edit(Long id,Model model) {
		if(id==null) {
			return "/user/add";
		}
		model.addAttribute("obj",this.userService.findById(ThreadContextHolder.getCurrentMerchantId(), id));
		return "/user/edit";
	}
	
	@ResponseBody
	@PostMapping("/save.do")
	public RspResult save(User obj,Long roleId) {
		if(obj.getId()==null) {
			//obj.setMerchantId((ThreadContextHolder.getCurrentMerchantId()));
			userService.save(obj);
		}else {
			User pojo=userService.findById(ThreadContextHolder.getCurrentMerchantId(),obj.getId());
			RBeanUtils.copyProperties(obj, pojo);
			userService.update(pojo);
		}
		return new RspResult();
	}

	/**
	 * 白名单列表
	 * */
	@GetMapping("white")
	public String whiteList(Model model) {
		return "/white/list";
	}
	
	@ResponseBody
	@PostMapping("/white/listData.do")
	public List<UserWhite> whiteListData(Integer page,Integer pageSize) {
		page=page==null?page=0:page;
		pageSize=Integer.MAX_VALUE;//客户端分页，服务端查询所有数据
		List<UserWhite> data=userWhiteService.findAll(ThreadContextHolder.getCurrentMerchantId());
		return data;
	}

	@GetMapping("/white/edit")
	public String editWhite(Long id,Model model) {
		if(id==null) {
			return "/white/add";
		}
		model.addAttribute("obj",this.userWhiteService.findById(ThreadContextHolder.getCurrentMerchantId(), id));
		return "/white/edit";
	}
	
	@ResponseBody
	@PostMapping("/white/save.do")
	public RspResult saveWhite(UserWhite obj) {
		if(obj.getId()==null) {
			obj.setMerchantId((ThreadContextHolder.getCurrentMerchantId()));
			obj.setStatus(UserWhiteStaus.normal);
			userWhiteService.save(ThreadContextHolder.getCurrentMerchantId(), obj);
		}else {
			UserWhite pojo=userWhiteService.findById(null,obj.getId());
			RBeanUtils.copyProperties(obj, pojo);
			userWhiteService.update(ThreadContextHolder.getCurrentMerchantId(),pojo);
		}
		return new RspResult();
	}
	
	@ResponseBody
	@PostMapping("/white/toggle.do")
	public RspResult removeWhite(Long id,String status) {
		UserWhite obj= userWhiteService.findById(null,id);
		UserWhiteStaus as=UserWhiteStaus.valueOf(UserWhiteStaus.class, status);
		obj.setStatus(as);
		userWhiteService.update(null,obj);
		return new RspResult();
	}
	
	
	/**
	 * 授权申请记录列表
	 * */
	@GetMapping("auth")
	public String authList(Model model) {
		return "/auth/list";
	}
	
	@ResponseBody
	@PostMapping("/auth/listData.do")
	public List<UserAuthorizeApply> authListData(Integer page,Integer pageSize) {
		page=page==null?page=0:page;
		pageSize=Integer.MAX_VALUE;//客户端分页，服务端查询所有数据
		List<UserAuthorizeApply> data=authorizeApplyService.findAll(ThreadContextHolder.getCurrentMerchantId());
		return data;
	}

	@GetMapping("/auth/edit")
	public String editAuth(Long id,Model model) {
		if(id==null) {
			return "/auth/add";
		}
		model.addAttribute("obj",this.authorizeApplyService.findById(ThreadContextHolder.getCurrentMerchantId(), id));
		return "/auth/edit";
	}
	
	@ResponseBody
	@PostMapping("/auth/save.do")
	public RspResult saveAuth(UserAuthorizeApply obj,Long roleId) {
		if(obj.getId()==null) {
			obj.setMerchantId((ThreadContextHolder.getCurrentMerchantId()));
			obj.setAuditStatus(AuthorizeAuditStaus.await);
			authorizeApplyService.save(ThreadContextHolder.getCurrentMerchantId(), obj);
		}else {
			UserAuthorizeApply pojo=authorizeApplyService.findById(null,obj.getId());
			RBeanUtils.copyProperties(obj, pojo);
			authorizeApplyService.update(ThreadContextHolder.getCurrentMerchantId(),pojo);
		}
		return new RspResult();
	}
	
	@ResponseBody
	@PostMapping("/auth/remove.do")
	public RspResult removeAuth(Long id) {
		authorizeApplyService.remove(id, id);
		return new RspResult();
	}


}
