package vn.hoidanit.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.RestResponse;

@RestControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    // khi trả ra false thì sẽ không format xuống dưới
    // khi nào muốn ghi đè, khi nào muốn format lại API, thì ở đây muốn bất kì phản
    // hồi nào => true
    // khi nào muốn check controller theo ý muốn thì if/ else còn muốn tác động full
    // thì trả true

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            // phản hồi chưa format : body
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            // request là lời gọi từ phía client dành cho sever
            ServerHttpRequest request,
            // response là lời trả về từ phía sever dành cho phía client
            ServerHttpResponse response) {

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);

        if (body instanceof String) {
            return body;
        }

        // case error:
        if (status >= 400) {
            return body;
        }

        // case success
        else {
            res.setData(body);
            res.setMessage("CALL API SUCCESS");
            return res;
        }
        // body ở đây chính là data phản hồi của server dành cho client
    }
}
