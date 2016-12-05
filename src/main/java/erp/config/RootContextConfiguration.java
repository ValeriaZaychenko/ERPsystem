package erp.config;


import org.hibernate.validator.HibernateValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.Map;

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
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setProviderClass( HibernateValidator.class );
        return validator;
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
    public MessageSource messageSource()
    {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setCacheSeconds(-1);
        messageSource.setFallbackToSystemLocale( false );
        messageSource.setDefaultEncoding( StandardCharsets.UTF_8.name());
        messageSource.setBasenames(
                 "/WEB-INF/i18n/messages"
        );
        return messageSource;
    }

    @Bean
    public DataSource getDataSource()
    {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("jdbc/erp_postgresql");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory ()
    {
        Map< String, Object> properties = new Hashtable<>();
        properties.put( "javax.persistence.schema-generation.database.action", "drop-and-create" );
        properties.put( "hibernate.show_sql", "true" );
        properties.put( "javax.persistence.sql-load-script-source" , "data.sql" );

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
