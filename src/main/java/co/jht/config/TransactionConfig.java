package co.jht.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Role;
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

@Configuration
@Component
@EnableTransactionManagement
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class TransactionConfig implements TransactionManagementConfigurer {

    private final PlatformTransactionManager transactionManager;

    public TransactionConfig(@Lazy PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @NonNull
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager;
    }

    @Bean
    @Lazy
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public TransactionInterceptor customTransactionInterceptor() {
        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        transactionInterceptor.setTransactionManager(transactionManager);
        return transactionInterceptor;
    }
}