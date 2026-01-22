package dev.danvega.qbe.config;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import dev.danvega.qbe.employee.Employee;
import dev.danvega.qbe.employee.EmployeeRepository;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackageClasses = EmployeeRepository.class,
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"
)
public class PostgresJpaConfig {

    // Bind primary (default) datasource properties from spring.datasource.*.
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties postgresDataSourceProperties() {
        return new DataSourceProperties();
    }

    // Build the primary DataSource; marked @Primary so autowiring defaults to Postgres.
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource postgresDataSource(
            @Qualifier("postgresDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    // EntityManagerFactory for Postgres entities; tells JPA where to scan and which props to use.
    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("postgresDataSource") DataSource dataSource,
            JpaProperties jpaProperties) {
        // Qualifier pins this factory to the Postgres DataSource when multiple DataSources exist.
        Map<String, Object> properties = new HashMap<>(jpaProperties.getProperties());
        properties.putIfAbsent("hibernate.hbm2ddl.auto", "create-drop");
        properties.putIfAbsent("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return builder
                .dataSource(dataSource)
                .packages(Employee.class)
                .persistenceUnit("postgres")
                .properties(properties)
                .build();
    }

    // Transaction manager for Postgres; @Primary makes it the default for @Transactional.
    @Bean
    @Primary
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
