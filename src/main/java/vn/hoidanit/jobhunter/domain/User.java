package vn.hoidanit.jobhunter.domain;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.enumCustome.GenderEnum;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    // để là Long hay long thì vẫn thế thôi chẳng qua máy sẽ phải chuyển qua lại
    // giữa nguyên thủy và object mà thôi

    private String name;
    private String email;
    private String password;
    private int age;
    // private String gender; // male, female => chỉ có 2 cái này thôi, làm sao để
    // có thể hiện tùy chọn mỗi 2 cái này mà không phải nhập??
    // => viết như thế này này
    // annotation này là để giúp chuyển kiểu dữ liệu enum mà ta viết thành String
    // trong db(db không biết enum là gì, chỉ biết String)
    @Enumerated(EnumType.STRING)
    // còn nếu để là ORDINAL thì sẽ là 0, 1 trong db
    private GenderEnum gender;
    private String address;
    private String refreshToken;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }
}
