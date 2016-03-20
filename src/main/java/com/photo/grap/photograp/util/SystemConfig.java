package com.photo.grap.photograp.util;

public class SystemConfig {

	// mysql地址配置
//
	public final static String DB_DRIVER = "com.mysql.jdbc.Driver";

	public final static String DB_USERNAME = "root";

	public final static String DB_PASSWORD = "123456";
//
//	//  正式
//	public static final String SAVE_FILE_PATH = "/home/jetty/uploadfile/";
////
//	public static final String DOWNLOAD_FILE_PATH = "/home/jetty/webapps/ROOT/resource/download/";
//
//	public static final String IMAGE_FILE_PATH = "/home/jetty/webapps/ROOT/resource/images/";
//	public final static String DB_URL = "jdbc:mysql://42.51.152.223:3306/tb_photo?useUnicode=true&characterEncoding=utf-8";

	//测试 
	public final static String DB_URL = "jdbc:mysql://127.0.0.1:3306/tb_photo?useUnicode=true&characterEncoding=utf-8";
//	public static final String SAVE_FILE_PATH = "F:\\tb\\save\\";
//	public static final String DOWNLOAD_FILE_PATH = "F:\\caijitupian\\apache-tomcat-7.0.67\\webapps\\ROOT\\resource\\download\\";
//	public static final String IMAGE_FILE_PATH = "F:\\caijitupian\\apache-tomcat-7.0.67\\webapps\\ROOT\\resource\\images\\";
	public static final String SAVE_FILE_PATH = "D:\\tb\\save\\";
	public static final String DOWNLOAD_FILE_PATH = "D:\\work\\photograp\\src\\main\\webapp\\resource\\download\\";
	public static final String IMAGE_FILE_PATH = "D:\\work\\photograp\\src\\main\\webapp\\resource\\images\\";

	// 任务状态

	/**
	 * 任务初始化状态
	 */
	public final static String TASK_INIT = "A";// 任务初始化
	/**
	 * 任务下载完成状态
	 */
	public final static String TASK_FINISHED_DOWNLOAD = "B";// 任务下载完成
	/**
	 * 任务文件已生成状态
	 */
	public final static String TASK_FILE_MAKED = "C";// 任务文件已生成
	/**
	 * 文件生成中状态
	 */
	public final static String TASK_FILE_MAKING = "D";// 文件生成中
	/**
	 * 文件生成时异常
	 */
	public final static String TASK_FILE_MAKING_ERROR = "E";

}
