package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Services.impl.TrainService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/train")
@AllArgsConstructor
public class TrainApiController {

    private TrainService trainService;

}