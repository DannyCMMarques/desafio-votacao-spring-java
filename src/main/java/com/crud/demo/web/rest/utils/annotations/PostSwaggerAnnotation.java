package com.crud.demo.web.rest.utils.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
  @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
  @ApiResponse(responseCode = "400", description = "Dados inválidos")
})
public @interface PostSwaggerAnnotation {

}
