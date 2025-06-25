package Ast.homework.controllers;

import Ast.homework.dto.UserEvent;
import Ast.homework.services.UserEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final UserEventProducer userEventProducer;

    @Autowired
    public NotificationController(UserEventProducer userEventProducer) {
        this.userEventProducer = userEventProducer;
    }

    @PostMapping
    public ResponseEntity<String> sendNotification(
            @RequestParam String event,
            @RequestParam String email) {

        if (!Set.of("CREATED", "DELETED").contains(event.toUpperCase())) {
            return ResponseEntity.badRequest().body("Invalid event type");
        }

        userEventProducer.sendMessage(new UserEvent(event, email));

        return ResponseEntity.ok("Notification sent");
    }
}
