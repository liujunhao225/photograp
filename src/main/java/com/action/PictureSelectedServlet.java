package com.action;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.photo.grap.photograp.util.MysqlConnector;


public class PictureSelectedServlet extends HttpServlet {

	private static final long serialVersionUID = -5432282380053538744L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String pictureId = request.getParameter("pictureId");
		String selectPicture = request.getParameter("selectCheckBox");
		String selectName = "";
		if("1".equals(selectPicture)){
			selectName="photo_name1";
		}else if("2".equals(selectPicture)){
			selectName="photo_name2";
		}else if("3".equals(selectPicture)){
			selectName= "photo_name3";
		}else{
			return ;
		}
		MysqlConnector.selectPicture(pictureId, selectName);

	}

}
