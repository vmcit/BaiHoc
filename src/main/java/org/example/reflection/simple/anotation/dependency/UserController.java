package org.example.reflection.simple.anotation.dependency;

public class UserController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private MessengerService messengerService;

    public void registerUser() {
        emailService.sendEmail("Chào mừng bạn gia nhập!");
    }

    public void sendMessage() {
        messengerService.sendMesage("hello welcome!");
    }

}
