package com.minis.jdbc.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * 池化的数据源，用于管理连接池
 */
public class PooledDataSource implements DataSource {

    /**
     * 连接池
     * todo: 存在并发问题。可以用 SynchronizedList；或者用两个 LinkedBlockingQueue，分别存储 active 连接和 inactive 连接
     */
    private List<PooledConnection> connections = null;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    /**
     * 连接池中的连接数量，一般可以设置为并发数的十分之一
     */
    private int initialSize = 2;

    private Properties connectionProperties;

    public PooledDataSource() {
    }

    private void initPool(Properties props) {
        this.connections = new ArrayList<>(initialSize);
        try {
            for(int i = 0; i < initialSize; i++){
                Connection connect = DriverManager.getConnection(url, props);
                PooledConnection pooledConnection = new PooledConnection(connect, false);
                this.connections.add(pooledConnection);
                System.out.println("********add connection pool*********");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getConnectionFromDriver(getUsername(), getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnectionFromDriver(username, password);
    }

    protected Connection getConnectionFromDriver(String username, String password) throws SQLException {
        // 如果连接池为空，就初始化连接池
        if (this.connections == null) {
            // 拼接连接参数
            Properties mergedProps = new Properties();
            Properties connProps = getConnectionProperties();
            if (connProps != null) {
                mergedProps.putAll(connProps);
            }
            if (username != null) {
                mergedProps.setProperty("user", username);
            }
            if (password != null) {
                mergedProps.setProperty("password", password);
            }

            // 初始化连接池
            initPool(mergedProps);
        }

        // 循环等待，直到获取一个可用的 (空闲的) 连接
        PooledConnection pooledConnection = getAvailableConnection();
        while (pooledConnection == null) {
            pooledConnection = getAvailableConnection();
            if (pooledConnection == null) {
                try {
                    TimeUnit.MILLISECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return pooledConnection;
    }

    /**
     * 获取一个可用的 (空闲的) 连接
     */
    private PooledConnection getAvailableConnection() throws SQLException {
        for (PooledConnection pooledConnection : this.connections) {
            if (!pooledConnection.isActive()) {
                pooledConnection.setActive(true);
                return pooledConnection;
            }
        }
        return null;
    }

    protected Connection getConnectionFromDriverManager(String url, Properties props) throws SQLException {
        return DriverManager.getConnection(url, props);
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        try {
            Class.forName(this.driverClassName);
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", ex);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }


    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter arg0) throws SQLException {
    }

    @Override
    public void setLoginTimeout(int arg0) throws SQLException {
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return null;
    }

}
