package taxi.share.back.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USER")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userNo;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private int userType = 1;

    @Column(nullable = false)
    private String phoneNum;

    @Column(nullable = false)
    private char userSex;

    // getters and setters
}