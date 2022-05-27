package app.homsai.engine;


import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Multimap;
import app.homsai.engine.common.domain.models.DocsConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiListingBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiListingScanningContext;
import springfox.documentation.spring.web.scanners.ApiModelReader;

import java.util.*;


public class FormLoginOperations extends ApiListingScanner {
    @Autowired
    private TypeResolver typeResolver;

    @Autowired
    public FormLoginOperations(ApiDescriptionReader apiDescriptionReader,
            ApiModelReader apiModelReader, DocumentationPluginsManager pluginsManager) {
        super(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    @Override
    public Multimap<String, ApiListing> scan(ApiListingScanningContext context) {

        final Multimap<String, ApiListing> def = (Multimap<String, ApiListing>) super.scan(context);

        final List<ApiDescription> apis = new LinkedList<>();

        final List<Operation> operations = new ArrayList<>();
        operations.add(new OperationBuilder(new CachingOperationNameGenerator())
                .method(HttpMethod.POST).uniqueId(
                        "login")
                .parameters(
                        Arrays.asList(
                                new ParameterBuilder().name("body").required(true)
                                        .description("The request body with the credentials")
                                        .parameterType("body")
                                        .type(typeResolver.resolve(AuthenticationRequest.class))
                                        .modelRef(new ModelRef(
                                                AuthenticationRequest.class.getSimpleName()))
                                        .build(),
                                new ParameterBuilder().name("X-Requested-With")
                                        .description("XMLHttpRequest")
                                        .modelRef(new ModelRef("string")).parameterType("header")
                                        .required(true).build()))
                .responseMessages(
                        new HashSet<ResponseMessage>(Arrays.asList(
                                new ResponseMessageBuilder().code(200).message("Success")
                                        .responseModel(new ModelRef(
                                                AuthenticationResponse.class.getSimpleName()))
                                        .build(),
                                new ResponseMessageBuilder().code(401).message("Unauthorized")
                                        .build())))
                .summary("Log in with credentials")
                .tags(new HashSet<String>(Arrays.asList(DocsConsts.DOCS_TAGS_AUTH))).build());
        apis.add(new ApiDescription("/auth/login/", "Authentication documentation", operations,
                false));

        def.put("authentication",
                new ApiListingBuilder(context.getDocumentationContext().getApiDescriptionOrdering())
                        .apis(apis).description("Custom authentication").build());

        return (Multimap<String, ApiListing>) def;
    }

    public class AuthenticationRequest {

        public String email;
        public String password;
    }

    public class AuthenticationResponse {

        public String token;
        public String refreshToken;
    }

}
