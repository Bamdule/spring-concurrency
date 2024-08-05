package org.example.springconcurrency.controller;

import lombok.RequiredArgsConstructor;
import org.example.springconcurrency.usecase.user.UserPointDeductUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/user-points")
@RestController
public class UserPointController {


    private final UserPointDeductUseCase userPointDeductUseCase;
    private static final int MIN = 1;
    private static final int MAX = 10000000;
    @GetMapping
    public ResponseEntity<Long> getUserPoints(@RequestParam(value = "userId") Long userId) {
        return ResponseEntity.ok().body(userPointDeductUseCase.getPoints(userId));
    }

    @GetMapping(value = "/points-use")
    public ResponseEntity<Long> usePoints(@RequestParam(value = "userId") Long userId, @RequestParam(value = "usePoints") Long usePoints) {
        return ResponseEntity.ok().body(userPointDeductUseCase.usePoints(generateRandomNumber(), usePoints));
    }

    public long generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(MAX - MIN + 1) + MIN;
    }
}
