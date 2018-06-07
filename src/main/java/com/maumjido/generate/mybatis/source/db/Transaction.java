package com.maumjido.generate.mybatis.source.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maumjido.generate.mybatis.source.db.Connect.DriverClass;

public abstract class Transaction {
    private static Logger logger = LoggerFactory.getLogger(Transaction.class);
    private Object result;

    public Transaction(String tag, String url, String id, String pwd, DriverClass driverClass) {
        long start = System.currentTimeMillis();
        Connection con = Connect.getConnection(url, id, pwd, driverClass);
        try {
            con.setAutoCommit(false);
            setResult(this.doTransaction(con));
            con.commit();
        } catch (Exception e) {
            logger.error("{}", e.getMessage());
            try {
                con.rollback();
            } catch (SQLException e1) {
                logger.error("Connection Rollback Error : {}", e1.getMessage());
            }
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("Connection Close Error : {}", e.getMessage());
            }
        }
        logger.info("{}:{}ms", tag, System.currentTimeMillis() - start);
    }

    public abstract Object doTransaction(Connection con);

    public void setResult(Object result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getResult(Class<T> returnType) {
        return (T) result;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getResultList(Class<T> returnType) {
        return (List<T>) result;
    }

}