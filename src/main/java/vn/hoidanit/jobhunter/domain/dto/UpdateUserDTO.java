package vn.hoidanit.jobhunter.domain.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.enumCustome.GenderEnum;

@Getter
@Setter
public class UpdateUserDTO {
    private long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private int age;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
}
