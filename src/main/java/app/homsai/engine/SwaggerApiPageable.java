package app.homsai.engine;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
        @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", defaultValue = "0",
                value = "Results page you want to retrieve (0..N)"),
        @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", defaultValue = "15",
                value = "Number of records per page."),
        @ApiImplicitParam(name = "sort", dataType = "string", paramType = "query",
                value = "Sorting criteria in the format: property,ASC|DESC. Default sort order is ascending."),
        @ApiImplicitParam(name = "search", dataType = "string", paramType = "query",
                value = "The search query in the format: property:{value} if equals"
                        + "or property<{value} or property>{value}. Multiple values can be insert using a comma as AND"
                        + "or a semicolon as OR. For example property1:{value1},property2:{value2}")})
public @interface SwaggerApiPageable {
}
