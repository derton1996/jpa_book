package org.hello.world.transaction.manager;

import bitronix.tm.resource.jdbc.PoolingDataSource;

import java.util.Properties;

public enum DatabaseProduct {

    MYSQL(
            new DataSourceConfiguration() {
                @Override
                public void configure(PoolingDataSource ds, String connectionURL) {
                    // TODO: MySQL XA support is completely broken, we use the BTM XA wrapper
                    //ds.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
                    ds.setClassName("bitronix.tm.resource.jdbc.lrc.LrcXADataSource");
                    ds.getDriverProperties().put("url", connectionURL);
                    ds.getDriverProperties().put("driverClassName", "com.mysql.jdbc.Driver");
                    ds.getDriverProperties().put("user", "root");
                    ds.getDriverProperties().put("password", "root");
                }
            },
            // Yes, this should work with 5.6, no idea why Gail named it 5.7
            org.hibernate.dialect.MySQL57InnoDBDialect.class.getName()
    );

    public DataSourceConfiguration configuration;
    public String hibernateDialect;

    private DatabaseProduct(DataSourceConfiguration configuration,
                            String hibernateDialect) {
        this.configuration = configuration;
        this.hibernateDialect = hibernateDialect;
    }

    public interface DataSourceConfiguration {

        void configure(PoolingDataSource ds, String connectionURL);
    }

}
