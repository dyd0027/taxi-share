package taxi.share.back.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private int routeNo;

    @Column(nullable = false)
    @Schema(description = "user No", example = "1")
    private int rtUserNo;

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

    @Column(name = "DISTANCE_M", nullable = true)
    @Schema(description = "출발지부터 도착지 거리", example = "12")
    private int distanceM;

    @Column(nullable = true)
    @Schema(description = "택시 탄 시간", example = "2024.03.02 22:03:10")
    private LocalDateTime startTime;

    @Column(nullable = true)
    @Schema(description = "택시 내린 시간", example = "2024.03.02 22:03:10")
    private LocalDateTime endTime;

    @Column(nullable = false)
    @Schema(description = "혼자 탔을 때 택시비", example = "6400")
    private int fare;

    @Column(nullable = false)
    @Schema(description = "톨비", example = "2000")
    private int toll;

    @Column(nullable = false)
    @Schema(description = "실제 지불한 택시비", example = "4000")
    private int paidFare;

    @Transient
    @Column(nullable = false)
    @Schema(description = "0:share찾는중, 1:share대기, 2:share잡음, 3:도착, 4:결제완료", example = "0")
    private int status;

    @Column(nullable = false)
    @Schema(description = "택시 타는 사람 수", example = "1")
    private int personCnt;
    // getters and setters
}