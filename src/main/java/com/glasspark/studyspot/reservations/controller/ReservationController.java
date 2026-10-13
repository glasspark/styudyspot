package com.glasspark.studyspot.reservations.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    @GetMapping
    public String myReservations() {
        return "reservations/list";
    }
}
