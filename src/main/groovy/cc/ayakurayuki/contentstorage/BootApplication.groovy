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
import org.springframework.stereotype.Repository
import org.springframework.transaction.PlatformTransactionManager

import javax.sql.DataSource

@SpringBootApplication
@MapperScan(basePackages = 'cc.ayakurayuki.contentstorage', annotationClass = Repository.class)
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
    sqlSessionFactory.dataSource = dataSource
    sqlSessionFactory.mapperLocations = resolver.getResources("/mapping/**/*.xml")
    sqlSessionFactory.getObject()
  }

  @Bean
  PlatformTransactionManager getTransactionManager() {
    new DataSourceTransactionManager(dataSource)
  }

  static main(args) {
    SpringApplication.run BootApplication.class, args
  }

}
