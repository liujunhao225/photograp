/**
 * 数据库接口类，所有数据库增删改查都放到此类中
 */
package com.photo.grap.photograp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.minidev.json.JSONObject;

public class MysqlConnector {
	
	private static Logger logger = Logger.getLogger(MysqlConnector.class);
	static Connection conn = null;
	static {
		try {
			Class.forName(SystemConfig.DB_DRIVER);
			conn = DriverManager.getConnection(SystemConfig.DB_URL, SystemConfig.DB_USERNAME, SystemConfig.DB_PASSWORD);
		} catch (ClassNotFoundException e) {
			System.err.println("没有mysql驱动");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("数据库连接不正确");
			e.printStackTrace();
		}
	}

	/**
	 * 向tb_photo中插入一条不存在的记录
	 * 
	 * @param id
	 */
	public static void insertPhoto(String id) {
		if (id == null || id.length() < 1) {
			return;
		}
		String sql = "insert into tb_photo (id) values('%s')";
		sql = String.format(sql, id);
		Statement statement = getStatement();
		if (statement != null) {
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}

	}

	/**
	 * 更新tb_photo中图片地址
	 * 
	 * @param photos
	 * @param id
	 */
	public static void updatePhotoAddress(String[] photos, String id) {

		String sql = "update tb_photo set photourl1=?,photo_name1=?,photourl2=?,photo_name2=?,photourl3=?,photo_name3=? where id=?";
		String photoname1 = PhotoRenameTool.getPhotoName(photos[0]);
		String photoname2 = PhotoRenameTool.getPhotoName(photos[1]);
		String photoname3 = PhotoRenameTool.getPhotoName(photos[2]);
		PreparedStatement statement = getPrepareStatement(sql);
		try {
			statement.setString(1, photos[0]);
			statement.setString(2, photoname1);
			statement.setString(3, photos[1]);
			statement.setString(4, photoname2);
			statement.setString(5, photos[2]);
			statement.setString(6, photoname3);
			statement.setString(7, id);
			statement.execute();
		} catch (SQLException e1) {
			e1.printStackTrace();
			System.err.println("插入语句错误");
		}

		finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据商品号查找记录
	 * 
	 * @param id
	 * @return
	 */
	public static JSONObject findPhoto(String id) {
		String sql = "SELECT id,photourl1,photo_name1,photourl2,photourl3,select_photo FROM tb_photo WHERE id='%s'";
		sql = String.format(sql, id);
		Statement statement = getStatement();
		JSONObject job = null;
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					job = new JSONObject();
					job.put("photourl3", rs.getObject("photourl3"));
					job.put("photourl2", rs.getObject("photourl2"));
					job.put("photourl1", rs.getObject("photourl1"));
					job.put("photoname1", rs.getObject("photo_name1"));
					job.put("selectPhoto", rs.getString("select_photo"));
					job.put("id", id);
				}
			} catch (SQLException e) {
				System.err.println("插入语句错误");
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}
		return job;
	}

	/**
	 * 插入抓取任务记录
	 * 
	 * @param fileName
	 */
	public static void insertFileTask(String fileName,String state) {
		String sql = "INSERT INTO tb_task (file_name,task_state) VALUES('%s','A')";
		sql = String.format(sql, fileName);
		Statement statement = getStatement();
		if (statement != null) {
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}
	}

	/**
	 * 取得所有文件
	 * 
	 * @param state
	 * @return
	 */
	public static List<JSONObject> getFileList() {
		String sql = "SELECT * FROM tb_task ";
		Statement statement = getStatement();
		List<JSONObject> list = new ArrayList<JSONObject>();
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					JSONObject job = new JSONObject();
					job.put("taskId", rs.getString("task_id"));
					job.put("fileName", rs.getString("file_name"));
					job.put("startTime", rs.getString("task_startTime"));
					job.put("endTime", rs.getString("task_endTime"));
					job.put("taskState", rs.getString("task_state"));
					list.add(job);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}
		return list;
	}
	/**
	 * 根据状态取得文件
	 * 
	 * @param state
	 * @return
	 */
	public static List<JSONObject> getFileList(String state) {
		String sql = "SELECT * FROM tb_task where task_state='"+state+"'";
		Statement statement = getStatement();
		List<JSONObject> list = new ArrayList<JSONObject>();
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					JSONObject job = new JSONObject();
					job.put("taskId", rs.getString("task_id"));
					job.put("fileName", rs.getString("file_name"));
					job.put("startTime", rs.getString("task_startTime"));
					job.put("endTime", rs.getString("task_endTime"));
					job.put("taskState", rs.getString("task_state"));
					list.add(job);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}
		return list;
	}

	/**
	 * 根据taskID取得文件
	 * 
	 * @param state
	 * @return
	 */
	public static JSONObject getTaskBaseTaskId(String taskId) {
		String sql = "SELECT file_name FROM tb_task where task_id='"+taskId+"'";
		Statement statement = getStatement();
		JSONObject job = new JSONObject();
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					job.put("fileName", rs.getString("file_name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}
		return job;
	}
	
	public static JSONObject getTaskBaseFileName(String fileName) {
		String sql = "SELECT task_id FROM tb_task where file_name='"+fileName+"'";
		Statement statement = getStatement();
		JSONObject job = new JSONObject();
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					job.put("taskId", rs.getString("task_id"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}
		return job;
	}

	/**
	 * 更新任务状态
	 * 
	 * @param taskId
	 */
	public static void updateTask(String taskId,String state) {
		String sql = "update tb_task set task_state ='%s' where task_id='%s'";
		sql = String.format(sql, state,taskId);
		Statement statement = getStatement();
		if (statement != null) {
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}

	}

	/**
	 * 取得 任务对应的图片列表总数
	 * 
	 * @param taskID
	 * @return
	 */
	public static int getListCount(String taskID) {
		String sql = "select count(*) as count from tb_task_pro_mapper where task_id= '"+taskID+"'";
		Statement statement = getStatement();
		int result = 0;
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					result = rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}
		return result;
	}

	/**
	 * 按页取得任务对应的列表
	 * 
	 * @param taskFileId
	 * @param pageSize
	 * @param page
	 * @return
	 */
	public static List<JSONObject> getPhotolist(String taskFileId,
			int pageSize, int page) {

		List<JSONObject> jobList = new ArrayList<JSONObject>();
		if (page <= 0) {
			page = 1;
		}
		int start = pageSize * (page - 1);
		
		String sql = "SELECT t1.task_id,t1.product_id,t2.photourl1,t2.photourl2,t2.photourl3,t2.photo_name1,"
				+ "t2.photo_name2,t2.photo_name3,t2.select_photo"
				+ " FROM tb_task_pro_mapper t1,tb_photo t2 WHERE t1.task_id =? AND t1.product_id= t2.id LIMIT ?,?";
		PreparedStatement preparedStatement = getPrepareStatement(sql);
		String photoUrl1 = "";
		String photoUrl2 = "";
		String photoUrl3 = "";
		try {
			preparedStatement.setString(1, taskFileId);
			preparedStatement.setInt(2, start);
			preparedStatement.setInt(3, pageSize);

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String id = rs.getString("product_id");
				String taskId = rs.getString("task_id");
				photoUrl1 = rs.getString("photo_name1");
				photoUrl2 = rs.getString("photo_name2");
				photoUrl3 = rs.getString("photo_name3");
				String selectPhoto = rs.getString("select_photo");
				JSONObject job = new JSONObject();
				job.put("id", id);
				job.put("taskId", taskId);
				job.put("selectPhoto", selectPhoto);
				if (photoUrl1 != null && photoUrl1.length() > 0) {
					job.put("photoUrl1", photoUrl1);
				}
				if (photoUrl2 != null && photoUrl2.length() > 0
						&& !"null".equals(photoUrl2)) {
					job.put("photoUrl2", photoUrl2);
				}
				if (photoUrl3 != null && photoUrl3.length() > 0
						&& !"null".equals(photoUrl3)) {
					job.put("photoUrl3", photoUrl3);
				}
				jobList.add(job);
			}
			return jobList;
		} catch (SQLException e) {
			System.out.println(photoUrl1);
			System.out.println(photoUrl2);
			System.out.println(photoUrl3);
			System.out.println(e.getMessage());
			return null;
		}

	}

	/**
	 * 将某图片选中为商品默认图片
	 * 
	 * @param pictureId
	 * @param selectPicture
	 */
	public static void selectPicture(String pictureId, String selectPicture) {
		String sql = "UPDATE tb_photo SET select_photo =%s WHERE id='%s'";
		sql = String.format(sql, selectPicture, pictureId);
		Statement statement = getStatement();
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			System.out.println("sql语句异常！");
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * 自己上传作为选择图片
	 * @param pictureId
	 * @param pictureName
	 */
	public static void updateSelfPhoto(String pictureId, String pictureName) {
		String sql = "UPDATE tb_photo SET self_photo ='%s',select_photo='%s' WHERE id='%s'";
		sql = String.format(sql, pictureName, pictureName,pictureId);
		Statement statement = getStatement();
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			System.out.println("sql语句异常！");
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * 向tb_photo中插入一条不存在的记录
	 * 
	 * @param id
	 */
	public static void insertTaskProMap(String taskId,String productId) {
		if (taskId == null || taskId.length() < 1) {
			return;
		}
		if(productId==null  || productId.length()<1){
			return ;
		}
		
		String sql = "insert into tb_task_pro_mapper (task_id,product_id) values('%s','%s')";
		
		sql = String.format(sql, taskId,productId);
		Statement statement = getStatement();
		if (statement != null) {
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				System.err.println("插入语句错误");
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("statement为空");
		}

	}

	/**
	 * 向proxyIp表插入数据
	 * 
	 * @param id
	 */
	public static void insertProxyIpPort(List<String> proxyIpPortList ) {
		
		if(proxyIpPortList.size()<1){
			return ;
		}
		String sql = "INSERT INTO tb_proxy_ip_port (ip,PORT) values ";
		StringBuilder sb = new StringBuilder();
		for(String ss:proxyIpPortList){
			String temp[] = ss.trim().split("\\s");
			sb.append("('"+temp[0]+"',"+temp[1]+"),");
		}
		String  endSQL = sb.substring(0, sb.toString().length()-1);
		sql = sql+endSQL;
		logger.info("sql语句是"+sql);
		Statement st = getStatement();
		try {
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 向proxyIp表插入数据
	 * 
	 * @param id
	 */
	public static void truncateProxyIp( ) {
		
		logger.info("清空代理 表数据开始");
		Statement st = getStatement();
		try {
			st.execute("TRUNCATE tb_proxy_ip_port");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		logger.info("清空代理 表数据结束");
	}
	public static List<JSONObject> getProxyIP(){
		List<JSONObject> jobList = new ArrayList<JSONObject>();
		logger.info("获取代理ip");
		String sql = "select ip,port from tb_proxy_ip_port ";
		Statement st = getPrepareStatement(sql);
		try {
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()){
				JSONObject job = new JSONObject();
				String ip= rs.getString("ip");
				String port = rs.getString("port");
				job.put("ip",ip);
				job.put("port",port);
				jobList.add(job);
			}
			return jobList;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}
	private static PreparedStatement getPrepareStatement(String sql) {

		try {
			if (conn != null && !conn.isClosed() && conn.isValid(5)) {

			} else {
				conn = DriverManager.getConnection(SystemConfig.DB_DRIVER, SystemConfig.DB_USERNAME, SystemConfig.DB_PASSWORD);
			}
			PreparedStatement preparestatement = conn.prepareStatement(sql);
			return preparestatement;
		} catch (SQLException e) {
			return null;
		}
	}
	
	

	private static Statement getStatement() {
		Statement statement = null;
		try {
			if (conn != null && !conn.isClosed() && conn.isValid(5)) {

			} else {
				conn = DriverManager.getConnection(SystemConfig.DB_DRIVER, SystemConfig.DB_USERNAME, SystemConfig.DB_PASSWORD);
			}
			statement = conn.createStatement();
			return statement;
		} catch (SQLException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		// insert("649322-133");
		// update(new String[]{"url1","url2","url3"},"649322-133");
		System.out.println(findPhoto("649322-133").toJSONString());
	}
}
