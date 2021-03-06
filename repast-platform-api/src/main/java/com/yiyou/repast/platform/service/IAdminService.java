package com.yiyou.repast.platform.service;

import java.util.List;

import com.yiyou.repast.platform.model.Admin;

import repast.yiyou.common.util.DataGrid;

public interface IAdminService {
	
	Admin save(Admin obj);
	
	Admin update(Admin obj);

	Admin getById(Integer id);

	Admin find(final String name, final String password);

	boolean validateLoginName(String loginName);
	
	void removeAdmin(Integer id);
	
	void modifyStatus(Integer id,String status);
	
	List<Admin> getByGroupId(Integer groupId);

	DataGrid<Admin> getAdminList(String loginName, String nickName, String status,int page,int size);
}
