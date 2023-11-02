package com.zosh.service;

import java.util.List;

import com.zosh.exception.ComException;
import com.zosh.exception.UserException;
import com.zosh.model.Community;
import com.zosh.model.User;

public interface ComService {

	public Community createCom(Community req, User user) throws ComException, UserException;
	
	public List<Community> findAllCom(User reqUser) throws ComException, UserException;
}
