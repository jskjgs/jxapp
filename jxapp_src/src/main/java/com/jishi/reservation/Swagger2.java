package com.jishi.reservation;

import com.jishi.reservation.util.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sloan on 2017/5/27.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {



    @Bean
    public Docket createRestApi() {
        List<ResponseMessage> responseMessageList =  new ArrayList<ResponseMessage>();
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("请求接口成功，业务正确").build());
        responseMessageList.add(new ResponseMessageBuilder().code(400).message("请求接口成功，业务失败，前端返回预先定义的统一错误信息").build());
        responseMessageList.add(new ResponseMessageBuilder().code(401).message("未授权IP，未找到登录信息，无权限访问接口").build());
        responseMessageList.add(new ResponseMessageBuilder().code(406).message("请求接口成功，业务失败，前端返回后端返回的msg信息").build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("不明错误，前端返回预先定义的统一错误信息").build());
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET,responseMessageList)
                .globalResponseMessage(RequestMethod.POST,responseMessageList)
                .globalResponseMessage(RequestMethod.PUT,responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE,responseMessageList)
                .globalOperationParameters(globalParameters())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jishi.reservation"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("预约挂号系统")
                .description("预约挂号系统")
                .version("1.0")
                .build();
    }

    private List<Parameter> globalParameters() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name(Constant.HEADER_TEST_ACCOUNT_ID).description("测试账号id").modelRef(new ModelRef("Long")).parameterType("header").required(false);
        pars.add(tokenPar.build());
        return pars;
    }
}
