package taxi.share.back.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "ROUTES")
@Setter
@Getter
@Schema(description = "Details about the route")
public class Routes implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique ID of the routes", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private int routeNO;

    @Column(nullable = false)
    @Schema(description = "The map's origin", example = "서울 동대문구")
    private String origin;

    @Column(nullable = false)
    @Schema(description = "The map's destination", example = "서울 서대문구")
    private String destination;




    // getters and setters
}