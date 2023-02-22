package com.test.pmu.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class DatabaseConfigUtils {

    public static DataSource makeDataSource(String name, String password, String jdbcUrl,
                                            String driverClassName, boolean isReadOnly,
                                            int maxPoolSize, String poolName, String connectionTestQuery) {
        final HikariDataSource datasource = new HikariDataSource();
        datasource.setUsername(name);
        datasource.setPassword(password);
        datasource.setJdbcUrl(jdbcUrl);
        datasource.setDriverClassName(driverClassName);
        datasource.setReadOnly(isReadOnly);
        datasource.setMaximumPoolSize(maxPoolSize);
        datasource.setConnectionTestQuery(connectionTestQuery);


        return datasource;
    }

    public static Map<String, String> makeJpaPropertiesMap(String ddlAuto, String showSql, String formatSql, String dialect, String slowQueryThreshold) {
        final Map<String, String> properties = new HashMap<>();

        if(Objects.nonNull(ddlAuto)) {
            properties.put("hibernate.hbm2ddl.auto", ddlAuto);
        }

        properties.put("hibernate.show_sql", showSql);
        properties.put("hibernate.format_sql", formatSql);
        properties.put("hibernate.dialect", dialect);
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        properties.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class.getName());
        properties.put("hibernate.session.events.log.LOG_QUERIES_SLOWER_THAN_MS", slowQueryThreshold);

        return properties;
    }
}
