package com.maumjido.generate.mybatis.source.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Query {
    private static Logger logger = LoggerFactory.getLogger(Transaction.class);

    @SuppressWarnings({ "unchecked" })
    public static List<Map<String, Object>> select(final String sql) {
        return (List<Map<String, Object>>) new Transaction(sql, null, null, null, null) {
            @Override
            public Object doTransaction(Connection con) {
                List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
                try {
                    Statement stmt = null;
                    ResultSet rs = null;
                    try {
                        stmt = con.createStatement();
                        rs = stmt.executeQuery(sql);
                    } catch (SQLException e) {
                        logger.error(sql, e);
                    }
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    List<Map<String, Object>> columTypeList = new ArrayList<Map<String, Object>>();
                    for (int i = 1; i <= columnCount; i++) {
                        Map<String, Object> columType = new HashMap<String, Object>();
                        columType.put("type", rsmd.getColumnType(i));
                        columType.put("name", rsmd.getColumnName(i));
                        columTypeList.add(columType);
                    }

                    while (rs.next()) {
                        Map<String, Object> result = new HashMap<String, Object>();
                        for (Map<String, Object> columType : columTypeList) {
                            String key = (String) columType.get("name");
                            switch ((Integer) columType.get("type")) {
                            case Types.VARCHAR:
                                result.put(key, rs.getString(key));
                                break;
                            case Types.INTEGER:
                                result.put(key, rs.getInt(key));
                                break;
                            default:
                                result.put(key, rs.getObject(key));
                                break;
                            }
                        }
                        logger.info(result.toString());
                        resultList.add(result);
                    }
                } catch (SQLException e) {
                    logger.error("SQL ERROR", e);
                }
                return resultList;
            }
        }.getResultList(new HashMap<String, Object>().getClass());
    }

    public static void insert(final String sql) {
        new Transaction(sql, null, null, null, null) {
            @Override
            public Object doTransaction(Connection con) {
                Statement stmt = null;
                try {
                    stmt = con.createStatement();
                    logger.info("성공", stmt.executeUpdate(sql));
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
                return null;
            }
        };
    }

    public static void insertBatch(final String sql, final List<String> parameters) {
        new Transaction("insert ", null, null, null, null) {
            @Override
            public Object doTransaction(Connection con) {
                PreparedStatement pstmt = null;
                try {
                    pstmt = con.prepareStatement(sql);
                    con.setAutoCommit(false);
                    for (int i = 0; i < parameters.size(); i++) {
                        String parameter = parameters.get(i);
                        pstmt.setString(i + 1, parameter);
                    }
                    pstmt.addBatch();
                    pstmt.executeBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    public static void main(String[] args) throws SQLException {
    }
}
