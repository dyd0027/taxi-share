package taxi.share.back.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER")
@Setter
@Getter
@Schema(description = "Details about the user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique ID of the user", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int userNo;

    @Column(nullable = false, unique = true)
    @Schema(description = "The user's ID", example = "user123")
    private String userId;

    @Column(nullable = false)
    @Schema(description = "The user's password", example = "password123")
    private String userPassword;

    @Column(nullable = false)
    @Schema(description = "The user's name", example = "John Doe")
    private String userName;

    @Column(nullable = false)
    @Schema(description = "The user's type", example = "1")
    private int userType;

    @Column(nullable = false, unique = true)
    @Schema(description = "The user's phone number", example = "010-1111-1111")
    private String phoneNum;

    // char가 아닌 String인 이유는 SpringBoot는 JSON 데이터를 객체로 변환할 때 char를 처리하기 힘듦.
    @Column(nullable = false)
    @Pattern(regexp = "[MF]", message = "Sex must be M or F")
    @Schema(description = "The user's sex", example = "M")
    private String userSex;


    // getters and setters
}