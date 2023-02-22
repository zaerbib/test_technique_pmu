package com.test.pmu.config;

import com.test.pmu.utils.DatabaseConfigUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = "com.test.pmu.repository", transactionManagerRef = "coursRwTransactionManager",
                        entityManagerFactoryRef = "courseRwLocalContainerEntityManagerFactoryBean")
@ConfigurationProperties(prefix = "datasource.course")
@Setter
@Getter
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class CourseRWConfig {

    private String username;
    private String password;
    private String jdbcUrl;
    private String driverClassName;
    private Integer maxSize;
    private String poolName;
    private String validationQuery;
    private String ddlAuto;
    private Boolean showSql;
    private Boolean formatSql;
    private String dialect;
    private String slowQueryThreshold;

    @Bean("courseRwDataSource")
    @Primary
    DataSource rwDataSource() {
        return DatabaseConfigUtils.makeDataSource(username, password, jdbcUrl, driverClassName, false, maxSize, poolName, validationQuery);
    }

    @Bean("courseRwLocalContainerEntityManagerFactoryBean")
    @Primary
    LocalContainerEntityManagerFactoryBean rwLocalContainerEntityManagerFactoryBean(@Qualifier("courseRwDataSource") DataSource dataSource,
                                                                                    EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("com.test.pmu.entity")
                .persistenceUnit("coursePersistenceUnit")
                .properties(DatabaseConfigUtils.makeJpaPropertiesMap(ddlAuto, String.valueOf(showSql), String.valueOf(formatSql), dialect, slowQueryThreshold))
                .build();
    }

    @Bean("coursRwTransactionManager")
    @Primary
    PlatformTransactionManager rwTransactionManager(@Qualifier("courseRwLocalContainerEntityManagerFactoryBean") LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean){
        return new JpaTransactionManager(Objects.requireNonNull(localContainerEntityManagerFactoryBean.getObject()));
    }
}
