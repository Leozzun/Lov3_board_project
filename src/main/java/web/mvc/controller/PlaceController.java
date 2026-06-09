package web.mvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.mvc.dto.place.PlaceReqDto;
import web.mvc.dto.place.PlaceResDto;
import web.mvc.service.PlaceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/places")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<PlaceResDto>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlaceResDto>> searchPlaces(@RequestParam String keyword) {

        List<PlaceResDto> result = placeService.searchPlaces(keyword);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceResDto> getPlace(@PathVariable Long placeId) {

        PlaceResDto result = placeService.getPlace(placeId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<PlaceResDto> addPlace(@RequestBody PlaceReqDto placeReqDto) {

        PlaceResDto result = placeService.addPlace(placeReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(result) ;
    }

    @PutMapping("/{placeId}")
    public ResponseEntity<PlaceResDto> updatePlace(@PathVariable Long placeId, @RequestBody PlaceReqDto placeReqDto) {

        PlaceResDto result = placeService.updatePlace(placeReqDto, placeId);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{placeId}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long placeId) {

        placeService.deletePlace(placeId);

        return ResponseEntity.noContent().build();
    }
}
