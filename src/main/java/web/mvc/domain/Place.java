package web.mvc.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "place")
@Getter @Setter @NoArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long placeId;

    @Column(name = "place_name", nullable = false, length = 100)
    private String placeName;

    @Column(name = "place_img")
    private String placeImg;               // 장소 이미지 URL

    @Column(name = "place_info", length = 500)
    private String placeInfo;              // 장소 설명
}
