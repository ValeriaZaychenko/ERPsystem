package erp.config;


import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.Executor;

@Configuration
@ComponentScan(
        basePackages = { "erp.service.impl", "erp.repository" },
        excludeFilters = @ComponentScan.Filter( Controller.class )
)
@EnableTransactionManagement(
        mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.LOWEST_PRECEDENCE
)
@EnableJpaRepositories(basePackages="erp.repository")
public class RootContextConfiguration
{
    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean()
    {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor()
    {
        MethodValidationPostProcessor processor =
                new MethodValidationPostProcessor();
        processor.setValidator(this.localValidatorFactoryBean());
        return processor;
    }

    @Bean
    public DataSource getDataSource()
    {
/*
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName("org.postgresql.Driver");
        driver.setUrl("jdbc:postgresql://127.0.0.1:5432/erp_postgresql");
        driver.setUsername("erp_user");
        driver.setPassword("erp_user");
        return driver;
*/
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("jdbc/erp_postgresql");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory ()
    {
        Map< String, Object> properties = new Hashtable<>();
        properties.put( "javax.persistence.schema-generation.database.action", "drop-and-create" );
        properties.put( "hibernate.show_sql", "true" );

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform( "org.hibernate.dialect.PostgreSQLDialect" );

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(adapter);
        factory.setDataSource( this.getDataSource() );
        factory.setPackagesToScan( "erp.domain" );
        factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        factory.setValidationMode(ValidationMode.NONE);
        factory.setJpaPropertyMap(properties);
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager ()
    {
        return new JpaTransactionManager(
                this.entityManagerFactory().getObject()
        );
    }
}
