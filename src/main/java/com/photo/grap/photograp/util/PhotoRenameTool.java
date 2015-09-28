package com.photo.grap.photograp.util;

public class PhotoRenameTool {

	public static String getPhotoName(String photoUrl) {
		try {
			String md5Source = photoUrl.substring(0, photoUrl.lastIndexOf("."));
			String prePhotoName = MD5Util.MD5(md5Source);
			String endPhotoName = "";
			if (photoUrl != null && photoUrl.length() > 1) {
				if (photoUrl.endsWith(".jpg")) {
					endPhotoName = ".jpg";
				} else if (photoUrl.endsWith(".JPG")) {
					endPhotoName = ".JPG";
				} else if (photoUrl.endsWith(".jpeg")) {
					endPhotoName = "jpeg";
				} else if (photoUrl.endsWith(".JPEG")) {
					endPhotoName = ".JPEG";
				} else if (photoUrl.endsWith(".png")) {
					endPhotoName = ".png";
				} else if (photoUrl.endsWith(".PNG")) {
					endPhotoName = ".PNG";
				} else {
					return "";
				}
				return prePhotoName + endPhotoName;
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

}
