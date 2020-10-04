package com.yasinbee.libman.hex.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LibmanHexagonalController {

    @GetMapping("")
    public String getAppRoot() {
        return "Elasticbeanstalk demo";
    }
}
