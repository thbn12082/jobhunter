package vn.hoidanit.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestLoginDTO {
    private String accessToken;
    // có điều cần phải lưu ý như sau: nếu mà muốn gán giá trị cho class này phải
    // khởi tạo nó đã
    private UserLoginDTO userLoginDTO;
}
