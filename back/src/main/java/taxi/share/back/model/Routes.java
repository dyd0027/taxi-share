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

    @Column(nullable = false)
    @Schema(description = "The map's origin latitude", example = "30.1235")
    private double originLatitude;

    @Column(nullable = false)
    @Schema(description = "The map's origin longitude", example = "130.335")
    private double originLongitude;

    @Column(nullable = false)
    @Schema(description = "The map's destination latitude", example = "30.1535")
    private double destinationLatitude;

    @Column(nullable = false)
    @Schema(description = "The map's destination longitude", example = "130.3235")
    private double destinationLongitude;


    // getters and setters
}