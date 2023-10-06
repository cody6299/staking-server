package com.x.dao.config;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
 import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.apache.ibatis.plugin.Interceptor;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@MapperScan(basePackages = "com.x.dao.db.staking", sqlSessionFactoryRef = "StakingSqlSessionFactory")
public class DataSourceStaking {

    @Value("${spring.datasource.druid.staking.url:#{null}}")
    String url;

    @Bean(name="StakingDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid.staking")
    public DataSource dataSource() throws Exception {
        if (url == null) {
            Map<String, String> map = new HashMap<>();
            map.put(DruidDataSourceFactory.PROP_DRIVERCLASSNAME, "com.mysql.cj.jdbc.Driver");
            map.put(DruidDataSourceFactory.PROP_URL, "jdbc:mysql://127.0.0.1:3306/staking?charset=utf8mb4&serverTimezone=UTC&useUnicode=true&useSSL=false");
            map.put(DruidDataSourceFactory.PROP_USERNAME, "root");
            map.put(DruidDataSourceFactory.PROP_PASSWORD, "root");
            return DruidDataSourceFactory.createDataSource(map);
        } else {
            return DruidDataSourceBuilder.create().build();
        }
    }

    @Bean(name = "StakingSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("StakingDataSource") DataSource ds) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(ds);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/staking/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "StakingSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("StakingSqlSessionFactory") SqlSessionFactory sessionFactory) throws  Exception{
        return  new SqlSessionTemplate(sessionFactory);
    }

    @Bean(name = "StakingTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("StakingDataSource") DataSource ds){
        return new DataSourceTransactionManager(ds);
    }

    @Bean(name = "StakingTransactionDefinition")
    public TransactionDefinition transactionDefinition() {
        return new DefaultTransactionDefinition();
    }
}
