package id.tisnanda.allobank.allo_bank_backend_test.filter;


import id.tisnanda.allobank.allo_bank_backend_test.dto.BaseResponse;
import id.tisnanda.allobank.allo_bank_backend_test.dto.ErrorResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResponseFilter implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports (MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        int status = 200;
        if (response instanceof ServletServerHttpResponse) {
            status = ((ServletServerHttpResponse) response).getServletResponse().getStatus();
        }

        if (status >= 300 ||body instanceof BaseResponse || body instanceof ErrorResponse) {
            return body;
        }

        return BaseResponse.success(body);
    }
}
