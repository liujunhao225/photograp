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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			logger.error("没有数据库驱动");
		} catch (SQLException e) {
			logger.error("数据库连接不正确");
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

		JSONObject job = findPhoto(id);
		String insertSql = "insert into tb_photo (id) values('%s')";

		if (job == null) {
			insertSql = String.format(insertSql, id);
			Statement statement = getStatement();
			if (statement != null) {
				try {
					statement.executeUpdate(insertSql);
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
				logger.error("tb_photo 插入数据--statement为空");
			}
		} else {
			if (job.getAsString("photoname1") == null || job.getAsString("photoname1").length() < 1) {
				String updatesql = "update tb_photo set status='A' where id='" + id + "'";
				Statement statement = getStatement();
				if (statement != null) {
					try {
						statement.executeUpdate(updatesql);
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
					logger.error("tb_photo 插入数据--statement为空");
				}
			}
		}
	}

	/**
	 * 更新tb_photo中图片地址
	 * 
	 * @param photos
	 * @param id
	 */
	public static void updatePhotoAddress(String[] photos, String id) {

		String sql = "update tb_photo set photourl1=?,photo_name1=?,photourl2=?,photo_name2=?,photourl3=?,photo_name3=?  where id=?";
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
			logger.error("更新图片地址--插入语句错误");
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
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
		String sql = "SELECT id,photo_name1,photo_name2,photo_name3,select_photo FROM tb_photo WHERE id='%s'";
		sql = String.format(sql, id);
		Statement statement = getStatement();
		JSONObject job = null;
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					job = new JSONObject();
					job.put("photoname3", rs.getObject("photo_name3"));
					job.put("photoname2", rs.getObject("photo_name2"));
					job.put("photoname1", rs.getObject("photo_name1"));
					job.put("selectPhoto", rs.getString("select_photo"));
					job.put("id", id);
				}
			} catch (SQLException e) {
				logger.error("查询商品号记录--插入语句错误");
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
	public static void insertFileTask(String fileName, String state) {
		String sql = "INSERT INTO tb_task (file_name,task_state) VALUES('%s','B')";
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
			logger.error("插入抓取任务--statement为空");
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
				logger.error("取得所有文件--sql出错");
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
		String sql = "SELECT * FROM tb_task where task_state='" + state + "'";
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
				logger.error("根据状态取得文件出错");
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
		String sql = "SELECT file_name FROM tb_task where task_id='" + taskId + "'";
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
				logger.error("根据taskid取得文件出错");
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
		}
		return job;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static JSONObject getTaskBaseFileName(String fileName) {
		String sql = "SELECT task_id FROM tb_task where file_name='" + fileName + "'";
		Statement statement = getStatement();
		JSONObject job = new JSONObject();
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					job.put("taskId", rs.getString("task_id"));
				}
			} catch (SQLException e) {
				logger.error("根据文件名取得文件出错");
				e.printStackTrace();
			} finally {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
		}
		return job;
	}

	/**
	 * 更新任务状态
	 * 
	 * @param taskId
	 */
	public static void updateTask(String taskId, String state) {
		String sql = "update tb_task set task_state ='%s' where task_id='%s'";
		sql = String.format(sql, state, taskId);
		Statement statement = getStatement();
		if (statement != null) {
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				logger.error("更新任务状态SQL错误");
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
	public static int getListCount(String productid, String state) {

		String condition = " where 1=1 ";
		if (!StringUtil.isEmpty(productid)) {
			condition = condition + " and id like" + "'%" + productid + "%'";
		}
		if (!StringUtil.isEmpty(state)) {
			condition = condition + "and state =" + "'" + state + "'";
		}
		String sql = "select count(*) as count from tb_photo " + condition;
		Statement statement = getStatement();
		int result = 0;
		if (statement != null) {
			try {
				ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					result = rs.getInt(1);
				}
			} catch (SQLException e) {
				logger.error("取得图片总数出错");
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
	public static List<JSONObject> getPhotolist(int pageSize, int page, String productid, String state) {

		List<JSONObject> jobList = new ArrayList<JSONObject>();
		if (page <= 0) {
			page = 1;
		}
		int start = pageSize * (page - 1);

		String condition = " where 1=1 ";

		if (!StringUtil.isEmpty(productid)) {
			condition = condition + " and id like" + "'%" + productid + "%'";
		}
		if (!StringUtil.isEmpty(state)) {
			condition = condition + "and state" + "'" + state + "'";
		}
		String sql = "SELECT t2.id,t2.photourl1,t2.photourl2,t2.photourl3,t2.photo_name1,t2.photo_name2,"
				+ "t2.photo_name3,t2.select_photo,t2.status FROM tb_photo t2  " + condition
				+ " order by status limit ?,? ";
		PreparedStatement preparedStatement = getPrepareStatement(sql);
		String photoUrl1 = "";
		String photoUrl2 = "";
		String photoUrl3 = "";
		try {
			preparedStatement.setInt(1, start);
			preparedStatement.setInt(2, pageSize);

			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String id = rs.getString("id");
				photoUrl1 = rs.getString("photo_name1");
				photoUrl2 = rs.getString("photo_name2");
				photoUrl3 = rs.getString("photo_name3");
				String status = rs.getString("status");
				String selectPhoto = rs.getString("select_photo");
				JSONObject job = new JSONObject();
				job.put("id", id);
				job.put("selectPhoto", selectPhoto);
				if (photoUrl1 != null && photoUrl1.length() > 0) {
					job.put("photoUrl1", photoUrl1);
				}
				if (photoUrl2 != null && photoUrl2.length() > 0 && !"null".equals(photoUrl2)) {
					job.put("photoUrl2", photoUrl2);
				}
				if (photoUrl3 != null && photoUrl3.length() > 0 && !"null".equals(photoUrl3)) {
					job.put("photoUrl3", photoUrl3);
				}
				job.put("status", status);
				jobList.add(job);
			}
			return jobList;
		} catch (SQLException e) {
			System.out.println(photoUrl1);
			System.out.println(photoUrl2);
			System.out.println(photoUrl3);
			System.out.println(e.getMessage());
			logger.error("取得商品图片列表出错");
			return null;
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					
				}
			}
		}

	}

	/**
	 * 将某图片选中为商品默认图片
	 * 
	 * @param pictureId
	 * @param selectPicture
	 */
	public static void selectPicture(String pictureId, String selectPicture) {
		String sql = "UPDATE tb_photo SET select_photo =%s,status='C' WHERE id='%s'";
		sql = String.format(sql, selectPicture, pictureId);
		Statement statement = getStatement();
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			logger.error("选取默认图片出错");
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
	 * 更新图片状态
	 * 
	 * @param pictureId
	 * @param selectPicture
	 */
	public static void updateProductStatus(String status, String productId) {
		String sql = "UPDATE tb_photo SET status=? WHERE id=?";
		PreparedStatement statement = getPrepareStatement(sql);
		try {
			statement.setString(1, status);
			statement.setString(2, productId);
			statement.executeUpdate();
		} catch (SQLException e) {
			logger.error("更新图片状态出错");
			e.printStackTrace();
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
	 * 
	 * @param pictureId
	 * @param pictureName
	 */
	public static void updateSelfPhoto(String pictureId, String pictureName) {
		String sql = "UPDATE tb_photo SET select_photo='%s',status='B' WHERE id='%s'";
		// String sql = "UPDATE tb_photo SET self_photo
		// ='%s',select_photo='%s',status='B' WHERE id='%s'";
		sql = String.format(sql, pictureName, pictureId);
		Statement statement = getStatement();
		try {
			statement.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("自己上传图片时出错");
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
	public static void insertTaskProMap(String taskId, String productId) {
		if (taskId == null || taskId.length() < 1) {
			return;
		}
		if (productId == null || productId.length() < 1) {
			return;
		}

		String sql = "insert into tb_task_pro_mapper (task_id,product_id) values('%s','%s')";

		sql = String.format(sql, taskId, productId);
		Statement statement = getStatement();
		if (statement != null) {
			try {
				statement.executeUpdate(sql);
			} catch (SQLException e) {
				logger.error("向tb_photo插入数据时出错！");
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
	 * get product auto_id,be used for proxy
	 * 
	 * @param productId
	 * @return
	 */
	public static int getProxyAutoId(String productId) {

		String sql = "select auto_id from tb_photo where id=? ";
		PreparedStatement preparedStatement = getPrepareStatement(sql);
		try {
			preparedStatement.setString(1, productId);
			ResultSet rs = preparedStatement.executeQuery();
			if (rs != null) {
				logger.error("获取商品自动新增id出错");
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null)
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				}

		}

		return 0;

	}

	public static String getNewPhotoRecord() {

		logger.info("获取一条未采集图片的记录");
		String sql = "SELECT id FROM tb_photo WHERE STATUS ='A' limit 0,1";
		Statement st = getStatement();
		try {
			ResultSet rs = st.executeQuery(sql);
			String productid = "";
			while (rs.next()) {
				productid = rs.getString("id");
				break;
			}
			return productid;
		} catch (SQLException e) {
			logger.error("获取一条未采集图片出错");
			return null;
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void updatePhotoImageStatus(String status, String productId) {
		String sql = " update tb_photo set status = ? where id=?";
		PreparedStatement st = getPrepareStatement(sql);
		try {
			st.setString(1, status);
			st.setString(2, productId);
			int result = st.executeUpdate();
		} catch (SQLException e) {
			logger.error("更新图片状态出错");
			e.printStackTrace();

		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static PreparedStatement getPrepareStatement(String sql) {

		try {
			if (conn != null && !conn.isClosed() && conn.isValid(5)) {

			} else {
				conn = DriverManager.getConnection(SystemConfig.DB_DRIVER, SystemConfig.DB_USERNAME,
						SystemConfig.DB_PASSWORD);
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
				conn = DriverManager.getConnection(SystemConfig.DB_DRIVER, SystemConfig.DB_USERNAME,
						SystemConfig.DB_PASSWORD);
			}
			statement = conn.createStatement();
			return statement;
		} catch (SQLException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(findPhoto("649322-133").toJSONString());
	}
}
