package vn.hoidanit.jobhunter.util.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.hoidanit.jobhunter.domain.RestResponse;

// @RestControllerAdvice là một annotation trong Spring được sử dụng để xử lý các ngoại lệ (exceptions) toàn cục 
// cho các RESTful APIs. Nó là một phần mở rộng của @ControllerAdvice nhưng được tối ưu hóa để làm việc với các REST controllers 
// (thường được đánh dấu bởi @RestController).
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            IdInvalidException.class,
            BadCredentialsException.class })
    // khi muốn custome trả ra lỗi thì để class đó vô đây
    public ResponseEntity<RestResponse<Object>> handleAuthException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("403 For Bidden or 401 Unauthorized");
        // cứ để trả về thẳng thế này luôn khi có lỗi xảy ra, lúc này, data = null
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    // thích truyền class nào để nhận định lỗi thì truyền
    @ExceptionHandler(value = {
            NoResourceFoundException.class })
    // khi muốn custome trả ra lỗi thì để class đó vô đây
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError(ex.getMessage());
        res.setMessage("404 Not found");
        // cứ để trả về thẳng thế này luôn khi có lỗi xảy ra, lúc này, data = null
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> listErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = listErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

}