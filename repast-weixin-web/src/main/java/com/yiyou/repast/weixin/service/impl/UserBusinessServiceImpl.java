package com.yiyou.repast.weixin.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yiyou.repast.merchant.model.User;
import com.yiyou.repast.merchant.model.UserAuthorizeApply;
import com.yiyou.repast.merchant.model.UserWhite;
import com.yiyou.repast.merchant.service.ISmsService;
import com.yiyou.repast.merchant.service.IUserAuthorizeApplyService;
import com.yiyou.repast.merchant.service.IUserService;
import com.yiyou.repast.merchant.service.IUserWhiteService;
import com.yiyou.repast.weixin.base.RBeanUtils;
import com.yiyou.repast.weixin.base.SessionToken;
import com.yiyou.repast.weixin.service.UserBusinessService;

import repast.yiyou.common.base.EnumDefinition.AuthorizeAuditStaus;
import repast.yiyou.common.exception.BusinessException;

@Service
public class UserBusinessServiceImpl implements UserBusinessService{
	
	@Reference
	private IUserService userService;
	@Reference
	private IUserAuthorizeApplyService userAuthorizeApplyService;
	@Reference
	private ISmsService smsService;
	@Reference
	private IUserWhiteService userWhiteService;

	@Override
	public User registerUser(User user) throws BusinessException{
		if(user==null) {
			throw new BusinessException(4444, "user must not be null");
		}
		if(StringUtils.isEmpty(user.getOpenId())) {
			throw new BusinessException(4444, "user openId must not be null");
		}
		String openId=user.getOpenId();
		User obj=userService.findByOpenId(openId);
		if(obj==null) {
			userService.save(user);
		}else {
			RBeanUtils.copyProperties(user, obj);
			obj.setLoginTime(new Date());
			userService.update(obj);
		}
		return user;
	}

	@Override
	public SessionToken getSessionUser() throws BusinessException{
		if(SecurityUtils.getSubject()==null)return null;
		
		SessionToken session=(SessionToken) SecurityUtils.getSubject().getPrincipal();
	/*	session=new SessionToken();
		session.setUserId(100l);
		session.setMerchantId(1l);
		session.setDeskNum("008");
		session.setPhone("15000658445");
		session.setIndustry("catering");
		session.setOpenId("oRB9pwZZvSy08gx2fEFxZ-xTPAfc");
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		WebUtils.setSessionAttribute(request, Constants.SESSION_ACCOUNT, session);*/
		return session;
	}
	
	@Override
	public User findById(Long id) throws BusinessException{
		User obj=userService.findById(null, id);
		if(obj==null) {
			throw new BusinessException(4444, "object User must not be null");
		}
		return obj;
	}
	
	@Override
	public User update(User obj) throws BusinessException{
		if(obj==null||obj.getId()==null) {
			throw new BusinessException(4444, "object User must not be null");
		}
		return userService.update(obj);
	}

	@Override
	public UserAuthorizeApply createUserAuthorizeApply(UserAuthorizeApply obj) throws BusinessException{
		if(obj==null) {
			throw new BusinessException(4444, "object userAuthorizeApply must not be null");
		}
		obj.setAuditStatus(AuthorizeAuditStaus.await);
		obj.setCreateTime(new Date());
		return userAuthorizeApplyService.save(obj);
	}

	@Override
	public void auditUserAuthorizeApply(Long userId,Long id, boolean flag)throws BusinessException {
		if(id==null) {
			throw new BusinessException(4444, "id must not be null");
		}
		UserAuthorizeApply obj=this.userAuthorizeApplyService.findById(id);
		if(obj==null) {
			throw new BusinessException(4444, "object userAuthorizeApply must not be null");
		}
		AuthorizeAuditStaus status=flag?AuthorizeAuditStaus.pass:AuthorizeAuditStaus.refuse;
		obj.setAuditId(userId);
		obj.setAuditStatus(status);
		obj.setAuditTime(new Date());
		userAuthorizeApplyService.update(obj);
	}

	@Override
	public UserAuthorizeApply getUserAuthorizeApply(Long userId)throws BusinessException {
		if(userId==null) {
			throw new BusinessException(4444, "userId must not be null");
		}
		return userAuthorizeApplyService.findByUserId(userId);
	}
	
	@Override
	public UserAuthorizeApply getUserAuthorizeApplyByID(Long id)throws BusinessException {
		if(id==null) {
			throw new BusinessException(4444, "id must not be null");
		}
		return userAuthorizeApplyService.findById(id);
	}
	
	@Override
	public void updateUserAuthorizeApply(UserAuthorizeApply obj)throws BusinessException {
		if(obj==null||obj.getId()==null) {
			throw new BusinessException(4444, "UserAuthorizeApply must not be null");
		}
		userAuthorizeApplyService.update(obj);
	}

	@Override
	public void sendSms(String phone, String content)throws BusinessException {
		if(org.apache.commons.lang3.StringUtils.isEmpty(phone)) {
			throw new BusinessException(4444, "phone must not be null");
		}
		smsService.sendMessage(phone, content);
	}

	@Override
	public boolean validateSms(String phone, String content)throws BusinessException {
		return smsService.verifyCode(phone, content);
	}
	
	@Override
	public UserWhite getUserWhite(String phone)throws BusinessException {
		if(org.apache.commons.lang3.StringUtils.isEmpty(phone))return null;
		return this.userWhiteService.findUserWhiteByPhone(phone);
	}

	@Override
	public void updateUserWhite(UserWhite obj) throws BusinessException{
		if(obj==null||obj.getId()==null) {
			throw new BusinessException(4444, "UserWhite must not be null");
		}
		userWhiteService.update(null, obj);
	}

}
