package com.boardgame.configuration;

import com.boardgame.configuration.properties.SwaggerProperties;
import com.google.common.base.Predicates;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

    private SwaggerProperties swaggerProperties;

    public SwaggerConfiguration(SwaggerProperties swaggerProperties){
        this.swaggerProperties = swaggerProperties;
    }

    @Bean
    public Docket api(ServletContext servletContext){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(getApiInfo())
                .useDefaultResponseMessages(false)
                .host(swaggerProperties.getHost())
                .pathProvider(new RelativePathProvider(servletContext){
                    @Override
                    public String getApplicationBasePath(){ return swaggerProperties.getBasepath(); }
                });
    }

    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("Boardgames API")
                .description("Documentation of the Boardgames API.")
                .version(getClass().getPackage().getImplementationVersion())
                .contact(new Contact("Gautier & Justine", null, null))
                .build();
    }

}
