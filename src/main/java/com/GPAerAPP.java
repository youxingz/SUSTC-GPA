package com; /**
 * Created by Anthony on 2016/8/7.
 */
//
//import AccountAuthentic;
//import StudentTokenProvider;
//import com.auth.authentication.AccountAuthentic;
//import com.auth.authentication.StudentTokenProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableAutoConfiguration
//@EnableTransactionManagement
@ComponentScan
@MapperScan(basePackages = {
        "com.gpaer"
})
@ServletComponentScan
@SpringBootApplication
@PropertySources(value = {@PropertySource("classpath:application.properties")})
public class GPAerAPP {
    public static void main(String[] args) {
        SpringApplication.run(GPAerAPP.class,args);
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {

        return new DataSource();
    }

    //    提供SqlSeesion
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        return sqlSessionFactoryBean.getObject();
    }

    //
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}