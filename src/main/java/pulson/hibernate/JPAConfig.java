package pulson.hibernate;

//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@EnableTransactionManagement
//public class JPAConfig {
//
//    @Bean
//    public DataSource dataSource(){
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/HIBERNATE");
//        dataSource.setUsername("");
//        dataSource.setPassword("");
//        return dataSource;
//    }
////    @Bean(name = "dataSource")
////    public DataSource dataSource() {
////        return DataSourceBuilder.create()
////            .url("jdbc:postgresql://localhost:5432/HIBERNATE")
////            .username("")
////            .password("")
////            .driverClassName("org.postgresql.Driver")
////            .build();
////    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dataSource());
//        emf.setPackagesToScan("LearningHibernate");
//
//        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        emf.setJpaVendorAdapter(vendorAdapter);
//
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.hbm2ddl.auto", "create");
//        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
//        properties.setProperty("hibernate.show_sql", "true");
//        properties.setProperty("hibernate.format_sql", "true");
//        emf.setJpaProperties(properties);
//        return emf;
//    }
//
//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        //.getNativeEntityManagerFactory() działa nieprawidłowo z Repository
//        transactionManager.setDataSource(dataSource());
//        return transactionManager;
//    }
//
//    @Bean
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
//        return new PersistenceExceptionTranslationPostProcessor();
//    }
//}
