package com.demo.redisbeanstalkdqueues.controller;

import com.demo.redisbeanstalkdqueues.service.QueueNameEnum;
import com.demo.redisbeanstalkdqueues.service.QueuesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class QueuesController {

    private final QueuesService service;

    @GetMapping("/publish")
    public ResponseEntity<String> produce(@RequestParam QueueNameEnum queue) {
        service.produceMessages(queue);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/consume")
    public ResponseEntity<String> consume(@RequestParam QueueNameEnum queue) {
        service.consumeMessages(queue);
        return ResponseEntity.ok("OK");
    }

}
