package app.homsai.engine.forecast.application.http.controllers;

import app.homsai.engine.forecast.application.services.ForecastQueriesApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Giacomo Agostini on 03/10/2022
 */

@RestController
public class ForecastQueriesController {

    @Autowired
    ForecastQueriesApplicationService forecastQueriesApplicationService;

    @RequestMapping(value = "/forecast/photovoltaic/self-consumption", method = RequestMethod.GET)
    public ResponseEntity getOptimizerForecast() {
        return ResponseEntity.status(HttpStatus.OK).body(
                forecastQueriesApplicationService.getOptimizerForecast());
    }
}
