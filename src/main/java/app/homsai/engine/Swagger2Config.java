package app.homsai.engine;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.classmate.TypeResolver;
import app.homsai.engine.common.domain.models.DocsConsts;
import app.homsai.engine.common.domain.models.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ConditionalOnExpression(value = "${useSwagger:false}")
@Configuration
@EnableSwagger2
public class Swagger2Config {

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";

    @Autowired
    private BuildProperties buildProperties;

    @Bean
    public Docket api(TypeResolver typeResolver) {
        final List<ResponseMessage> responseMessageList = new ArrayList<ResponseMessage>();
        responseMessageList
                .add(new ResponseMessageBuilder().code(500).message("Internal Error").build());
        responseMessageList
                .add(new ResponseMessageBuilder().code(400).message("Bad Request").build());
        responseMessageList
                .add(new ResponseMessageBuilder().code(401).message("Unauthorized").build());
        responseMessageList
                .add(new ResponseMessageBuilder().code(403).message("Forbidden").build());
        responseMessageList
                .add(new ResponseMessageBuilder().code(404).message("Not Found").build());

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("app.homsai.engine"))
                .paths(PathSelectors.regex("/.*")).build()
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apiKey()))
                .ignoredParameterTypes(Pageable.class, PagedResourcesAssembler.class, Errors.class,
                        URL.class, URI.class)
                .globalOperationParameters(
                        Arrays.asList(new ParameterBuilder().name("Authorization")
                                .description("Authorization token").modelRef(new ModelRef("string"))
                                .parameterType("header").required(true).build()))
                .tags(new Tag(DocsConsts.DOCS_TAGS_COMMON, "Common domain APIs"))
                .tags(new Tag(DocsConsts.DOCS_TAGS_AUTH, "Authentication related APIs"))
                .tags(new Tag(DocsConsts.DOCS_TAGS_MEDIA, "Media domain APIs"))
                .tags(new Tag(DocsConsts.DOCS_TAGS_USERS, "User domain APIs"))
                .apiInfo(apiEndPointsInfo())
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList).additionalModels(
                        typeResolver.resolve(FormLoginOperations.AuthenticationRequest.class),
                        typeResolver.resolve(FormLoginOperations.AuthenticationResponse.class),
                        typeResolver.resolve(Page.class), typeResolver.resolve(ErrorInfo.class));
    }

    private ApiInfo apiEndPointsInfo() {
        return new ApiInfoBuilder().title(DocsConsts.DOCS_TITLE)
                .description(DocsConsts.DOCS_DESCRIPTION).version(buildProperties.getVersion())
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey(DocsConsts.DOCS_HEADER_SECURITY_REF, DocsConsts.DOCS_HEADER_AUTHORIZATION,
                "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(DEFAULT_INCLUDE_PATTERN)).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope =
                new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(
                new SecurityReference(DocsConsts.DOCS_HEADER_SECURITY_REF, authorizationScopes));
    }
}
