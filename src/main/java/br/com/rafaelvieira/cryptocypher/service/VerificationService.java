package br.com.rafaelvieira.cryptocypher.service;

import br.com.rafaelvieira.cryptocypher.model.user.User;
import br.com.rafaelvieira.cryptocypher.repository.UserRepository;
import br.com.rafaelvieira.cryptocypher.util.CodeGeneratorService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VerificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public VerificationService(UserRepository userRepository,
                               EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Transactional
    public boolean verifyUser(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getVerificationCode().equals(code)) {
            user.setVerified(true);
            user.setVerificationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Transactional
    public void resendVerificationCode(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final CodeGeneratorService codeGeneratorService = new CodeGeneratorService();
        String newCode = codeGeneratorService.generateVerificationCode();
        user.setVerificationCode(newCode);
        userRepository.save(user);

        emailService.sendVerificationEmail(email, newCode);
    }

    @Transactional
    public boolean verifyResetCode(String email, String code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getVerificationCode().equals(code);
    }

    public boolean isFirstAccess(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.isFirstAccess();
    }

    @Transactional
    public void completeFirstAccess(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFirstAccess(false);
        userRepository.save(user);
    }

//    String generateVerificationCode() {
//        Random random = new Random();
//        StringBuilder code = new StringBuilder();
//        for (int i = 0; i < 8; i++) {
//            code.append(random.nextInt(10));
//        }
//        return code.toString();
//    }

    @Transactional
    public String generateResetCode(String email) {
        final CodeGeneratorService codeGeneratorService = new CodeGeneratorService();
        String code =  codeGeneratorService.generateVerificationCode();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setVerificationCode(code);
        userRepository.save(user);
        return code;
    }
}
