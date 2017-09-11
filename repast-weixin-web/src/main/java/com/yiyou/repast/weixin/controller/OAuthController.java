package com.yiyou.repast.weixin.controller;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dw.weixin.sdk.openapi.IWeixinBasisAPI;
import com.dw.weixin.sdk.request.oauth.OAuthGetAccessTokenByCodeRequest;
import com.dw.weixin.sdk.request.oauth.OAuthGetCodeRequest;
import com.dw.weixin.sdk.request.oauth.OAuthGetUserInfoRequest;
import com.dw.weixin.sdk.response.oauth.OAuthGetAccessTokenByCodeResponse;
import com.dw.weixin.sdk.response.oauth.OAuthGetUserInfoResponse;
import com.yiyou.repast.merchant.model.User;
import com.yiyou.repast.weixin.base.Constants;
import com.yiyou.repast.weixin.compent.WechatProperties;

/**
 * 微信用户授权认证
 */
@Controller
@RequestMapping("/wx/oauth")
public class OAuthController {

	@Resource
	private IWeixinBasisAPI weixinBasisAPI;
	@Resource
	private WechatProperties wechatProperties;
	private final String SESSION_OAUTH_STATE = "session_oauth_state";

	/**
	 * 用户登入进行网页授权
	 */
	@GetMapping()
	public String oauth(HttpServletRequest request) throws Exception {
		String callbackUrl = wechatProperties.getDomain() + request.getContextPath() + "/wx/oauth/callback";
		String sessionState = RandomStringUtils.random(10, true, true);
		request.getSession().setAttribute(SESSION_OAUTH_STATE, sessionState);
		
		OAuthGetCodeRequest oauthReq = new OAuthGetCodeRequest();
		oauthReq.setAppid(wechatProperties.getAppId());
		oauthReq.setScope("snsapi_userinfo");// snsapi_base为静默
		oauthReq.setState(sessionState);
		oauthReq.setRedirect_uri(callbackUrl);
		String params="?r=yiyou";
		for(Map.Entry<String, String> entry: oauthReq.getTextParams().entrySet()) {
			params += "&"+entry.getKey()+"="+entry.getValue();
		}
		return "redirect:"+oauthReq.getApiMethodUrl()+params;
	}

	/**
	 * 授权回调结果
	 */
	@GetMapping("/callback")
	public String callback(HttpServletRequest request, String code, String state) throws Exception {
        String sessionState = Objects.toString(request.getSession().getAttribute(SESSION_OAUTH_STATE));
        if(StringUtils.isEmpty(sessionState) || StringUtils.isEmpty(state) || !sessionState.equals(state)){
            throw new RuntimeException("非法请求");
        }
        if(StringUtils.isEmpty(code)){
            throw new RuntimeException("请授权对微信账号的访问权限");
        }
		OAuthGetAccessTokenByCodeRequest tokenReq = new OAuthGetAccessTokenByCodeRequest();
		tokenReq.setCode(code);
		tokenReq.setAppid(wechatProperties.getAppId());
		tokenReq.setSecret(wechatProperties.getAppSecret());
		OAuthGetAccessTokenByCodeResponse rsp = (OAuthGetAccessTokenByCodeResponse) weixinBasisAPI.sendReq(tokenReq,
				null);
		if(rsp.isSuccess()) {
			//授权成功，开始获取用户信息
			String openId=rsp.getOpenid();
			OAuthGetUserInfoRequest req1=new OAuthGetUserInfoRequest();
        	req1.setOpenid(openId);
        	req1.setAccess_token(rsp.getAccess_token());
        	OAuthGetUserInfoResponse rsp1=(OAuthGetUserInfoResponse) this.weixinBasisAPI.sendReq(req1, null);
        	if(rsp1.isSuccess()){
        		User user=new User();
        		user.setOpenId(openId);
        		user.setAvatar(rsp1.getHeadimgurl());
        		user.setCreateTime(new Date());
        		user.setNickName(rsp1.getNickname());
        		request.getSession().setAttribute(Constants.SESSION_ACCOUNT, user);
        		return "/index";
        	}
		}
		return "/fail";
	}

}