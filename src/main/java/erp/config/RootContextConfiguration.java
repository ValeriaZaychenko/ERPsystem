package erp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
import java.util.Hashtable;
import java.util.Map;

@Configuration
public class RootContextConfiguration extends BaseRootContextConfiguration
{
    @Bean
    public DataSource getDataSource()
    {
        JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("jdbc/erp_postgresql");
    }

    @Bean
    @Override
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
}
