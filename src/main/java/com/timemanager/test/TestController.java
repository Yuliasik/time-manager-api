package com.timemanager.test;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {

    private final TestService testService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String getTime() {
        return testService.getTime();
    }

}

@Service
@NoArgsConstructor
class TestService {

    public String getTime() {
        return LocalTime.now().toString();
    }
}
