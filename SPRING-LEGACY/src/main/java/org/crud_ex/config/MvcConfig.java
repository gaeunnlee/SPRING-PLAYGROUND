package org.crud_ex.config;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan(basePackages = {"org.crud_ex.mapper"}, annotationClass = Mapper.class)
@ComponentScan(basePackages = {"org.crud_ex"})
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer{

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setLocation(new ClassPathResource("application.properties"));
        return c;
    }

    @Override
    // JSP 경로 (ViewResolver)
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // prefix: /WEB-INF/view/, suffix: .jsp -> /WEB-INF/views/test.jsp
        registry.jsp("/WEB-INF/views/", ".jsp");
    }
}
