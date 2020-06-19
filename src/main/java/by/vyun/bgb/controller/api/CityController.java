package by.vyun.bgb.controller.api;

import by.vyun.bgb.dto.city.CityPreviewDto;
import by.vyun.bgb.dto.city.CityRequestDto;
import by.vyun.bgb.dto.game.GameRequestDto;
import by.vyun.bgb.dto.game.GameDto;
import by.vyun.bgb.dto.game.GamePreviewDto;

import by.vyun.bgb.service.CityService;
import by.vyun.bgb.service.GamesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    //todo extend with paging ans soring
    //todo extend with filter
    @GetMapping
    //return only active games
    public ResponseEntity<List<CityPreviewDto>> getCities() {
        return ResponseEntity.ok(cityService.getCities());
    }

    @GetMapping("/{cityId}")
    public ResponseEntity<CityPreviewDto> getCity(@PathVariable("cityId") Long cityId) {
        return ResponseEntity.ok(cityService.getCity(cityId));
    }

    @PostMapping
    public ResponseEntity<CityPreviewDto> createCity(@Valid @RequestBody CityRequestDto createGameRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cityService.createCity(createGameRequest));
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<CityPreviewDto> updateCity(@PathVariable("cityId") Long cityId,
                                              @Valid @RequestBody CityRequestDto updateCityRequest) {
        return ResponseEntity.ok(cityService.updateCity(cityId, updateCityRequest));
    }

}


