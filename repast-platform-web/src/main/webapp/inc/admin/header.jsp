<%@ page language="java" pageEncoding="UTF-8"%>
<div class="layui-header header header-demo">
	<div class="layui-main">
		<div class="admin-login-box">
			<a class="logo" style="left: 0;" href="#"> <span
				style="font-size: 22px;">平台管理中心</span>
			</a>
			<div class="admin-side-toggle">
				<i class="fa fa-bars" aria-hidden="true"></i>
			</div>
		</div>
		<ul class="layui-nav admin-header-item">
			<li class="layui-nav-item">
			    <a href="javascript:;" class="admin-header-user"> <img src="${_PATH}/res/admin/images/0.jpg" /><span>${admin_login.nickName}</span> </a>
				<dl class="layui-nav-child">
					<!-- <dd>
						<a href="javascript:;"><i class="fa fa-user-circle"
							aria-hidden="true"></i> 个人信息</a>
					</dd> -->
					<dd id="setting">
						<a href="javascript:;"><i class="fa fa-gear"
							aria-hidden="true"></i> 设置</a>
					</dd>
					<dd id="lock" data="${admin_login.loginName}">
						<a href="javascript:;"> <i class="fa fa-lock"
							aria-hidden="true" style="padding-right: 3px; padding-left: 1px;"></i>
							锁屏 (Alt+L)
						</a>
					</dd>
					<dd id="logout">
						<a href="${_PATH}/admin/logout"><i class="fa fa-sign-out" aria-hidden="true"></i> 注销</a>
					</dd>
				</dl></li>
		</ul>
		<ul class="layui-nav admin-header-item-mobile">
			<li class="layui-nav-item"><a href="${_PATH}/admin/logout"><i class="fa fa-sign-out" aria-hidden="true"></i> 注销</a></li>
		</ul>
	</div>
</div>