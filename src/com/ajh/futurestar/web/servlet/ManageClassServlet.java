/**
 * ManageClassServlet.java
 * Manage classes in specified school.
 */
package com.ajh.futurestar.web.servlet;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ajh.futurestar.web.common.Request;
import com.ajh.futurestar.web.common.RetCode;
import com.ajh.futurestar.web.common.RetInfo;
import com.ajh.futurestar.web.common.ReturnX;
import com.ajh.futurestar.web.utils.Util;
import com.ajh.futurestar.web.utils.Util.DbConnectionWrapper;
import com.ajh.futurestar.web.utils.Util.DbStatementWrapper;

/**
 * @author Andy Jiang H
 *
 */
@WebServlet("/manageclass.do")
public class ManageClassServlet extends ManageExServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8273769336541772993L;

	@Override
	protected void generatePageBody(PrintWriter out, ReturnX result) {
		JSONObject obj = new JSONObject();
		obj.put("actionx", result.actionx); // String
		obj.put("retcode", result.retcode.ordinal()); // Convert enumeration to integer.
		obj.put("retinfo", result.retinfo); // String
		obj.put("retobjx", result.retobjx); // JSON object
		obj.put("curuser", result.curuser); // JSON object
		out.println(obj.toString());
	}

	@Override
	protected void doBusiness(HttpServletRequest req, HttpServletResponse rsp, ReturnX retx) {
		HttpSession session = req.getSession();

		if (!Util.checkAndSetUserLoginInfo(session, retx)) {
			return;
		}

		Connection conn = null;
//		if (!Util.getDbConnection(conn, retx)) {
//			return;
//		}

		DbConnectionWrapper wrapperConn = Util.getDbConnection(retx);
		if (wrapperConn.ok) {
			conn = wrapperConn.conn;
		} else {
			return;
		}

		Statement stmt = null;
//		if (!Util.getDbStatement(conn, stmt, retx)) {
//			return;
//		}

		DbStatementWrapper wrapperStmt = Util.getDbStatement(conn, retx);
		if (wrapperStmt.ok) {
			stmt = wrapperStmt.stmt;
		} else {
			return;
		}

		// DO NOT return when performing action.
		String strAction = req.getParameter(Request.PARAM_ACTION);
		if (strAction == null || strAction.equals("")) {
			retx.retcode  = RetCode.RETCODE_KO_MANAGE_CLASS_NULL_ACTION;
			retx.retinfo += RetInfo.RETINFO_REQ_PARAM_NULL_ACTION;
		} else {
			retx.actionx += strAction;
			if (strAction.equalsIgnoreCase(Request.VALUE_ACTION_SELECT)) {
				try {
					doActionSelect(conn, stmt, req, retx);
				} catch (SQLException e) {
					e.printStackTrace();
					retx.retcode  = RetCode.RETCODE_KO_MANAGE_CLASS_SELECT_FAILED;
					retx.retinfo += e.getMessage();
				}
			} else if (strAction.equalsIgnoreCase(Request.VALUE_ACTION_CREATE)) {
				try {
					doActionCreate(conn, stmt, req, retx);
				} catch (SQLException e) {
					e.printStackTrace();
					retx.retcode  = RetCode.RETCODE_KO_MANAGE_CLASS_CREATE_FAILED;
					retx.retinfo += e.getMessage();
				}
			} else {
				// TODO
			}
		}

		Util.closeDbConnectionAndStatement(conn, stmt); // No need to update return code when exception occurs.
	}

	private void doActionCreate(Connection conn, Statement stmt, HttpServletRequest req, ReturnX retx)
		throws SQLException {
		String strSchoolId = req.getParameter(Request.PARAM_CLASS_SCHOOLID);
		String strName = req.getParameter(Request.PARAM_CLASS_NAME);
		String strEnrolment = req.getParameter(Request.PARAM_CLASS_ENROLMENT);

		if (strSchoolId == null || strSchoolId.equals("")) {
			retx.retcode  = RetCode.RETCODE_KO_MANAGE_CLASS_NULL_SCHOOLID;
			retx.retinfo += RetInfo.RETINFO_REQ_PARAM_NULL_SCHOOLID;
			return;
		}

		if (strName == null || strName.equals("")) {
			retx.retcode  = RetCode.RETCODE_KO_MANAGE_CLASS_NULL_NAME;
			retx.retinfo += RetInfo.RETINFO_REQ_PARAM_NULL_CLASS_NAME;
			return;
		}

		if (strEnrolment == null || strEnrolment.equals("")) {
			retx.retcode  = RetCode.RETCODE_KO_MANAGE_CLASS_NULL_ENROLMENT;
			retx.retinfo += RetInfo.RETINFO_REQ_PARAM_NULL_CLASS_ENROLMENT;
			return;
		}

		// TODO - check enrollment format.

		String strInsert = "insert into T_CLASS_FROM_SCHOOL_" + strSchoolId;
		strInsert += "(NAME, ENROLMENT) values(";
		strInsert += "'" + strName + "', ";
		strInsert += "'" + strEnrolment + "'";
		strInsert += ");";

		stmt.executeUpdate(strInsert);

		conn.commit();
	}

	private void doActionSelect(Connection conn, Statement stmt, HttpServletRequest req, ReturnX retx)
		throws SQLException {
		String strSchoolId = req.getParameter("schoolid");
		if (strSchoolId == null || strSchoolId.equals("")) {
			retx.retcode  = RetCode.RETCODE_KO_MANAGE_CLASS_NULL_SCHOOLID;
			retx.retinfo += RetInfo.RETINFO_REQ_PARAM_NULL_SCHOOLID;
		} else {
			JSONObject retobjx = new JSONObject();
			retobjx.put("schoolid", strSchoolId);

			// Query from view instead of table.
			String strQuery = "select * FROM V_CLASS_FROM_SCHOOL_" + strSchoolId;
			ResultSet rsClassList = stmt.executeQuery(strQuery);
			JSONArray arrayClass = new JSONArray();
			while (rsClassList.next()) {
				JSONObject obj = new JSONObject(); // Item in array.
				obj.put("ID", rsClassList.getString("ID"));
				obj.put("NAME", rsClassList.getString("NAME"));
				obj.put("ENROLMENT", rsClassList.getString("ENROLMENT"));
				obj.put("CREATION", rsClassList.getString("CREATION"));
				arrayClass.put(obj);
			}
			retobjx.put("classes", arrayClass);

			retx.retobjx = retobjx;

			rsClassList.close();
		}
	}

}
