package cc.ayakurayuki.contentstorage

import org.apache.commons.dbcp.BasicDataSource
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager

import javax.sql.DataSource

@SpringBootApplication
@MapperScan("cc.ayakurayuki.contentstorage.dao")
class BootApplication {

    @Bean
    @ConfigurationProperties("spring.datasource")
    DataSource getDataSource() {
        new BasicDataSource()
    }

    @Bean
    SqlSessionFactory getSqlSessionFactory() {
        def sqlSessionFactory = new SqlSessionFactoryBean()
        def resolver = new PathMatchingResourcePatternResolver()
        sqlSessionFactory.dataSource = getDataSource()
        sqlSessionFactory.mapperLocations = resolver.getResources("/mapping/*.xml")
        sqlSessionFactory.getObject()
    }

    @Bean
    PlatformTransactionManager getTransactionManager() {
        new DataSourceTransactionManager(getDataSource())
    }

    static main(args) {
        SpringApplication.run BootApplication.class, args
    }

}
