package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.dto.kafka.UserCreatedEvent;
import org.example.coursemanagementsystem.dto.request.LoginRequest;
import org.example.coursemanagementsystem.dto.request.PasswordUpdateRequest;
import org.example.coursemanagementsystem.dto.request.StudentRegistrationRequest;
import org.example.coursemanagementsystem.dto.request.TeacherRegistrationRequest;
import org.example.coursemanagementsystem.dto.response.AuthResponse;
import org.example.coursemanagementsystem.entity.Student;
import org.example.coursemanagementsystem.entity.Teacher;
import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.exception.*;
import org.example.coursemanagementsystem.mapper.StudentMapper;
import org.example.coursemanagementsystem.mapper.TeacherMapper;
import org.example.coursemanagementsystem.repository.StudentRepository;
import org.example.coursemanagementsystem.repository.TeacherRepository;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.example.coursemanagementsystem.security.JwtTokenProvider;
import org.example.coursemanagementsystem.service.AuthService;
import org.example.coursemanagementsystem.service.EmailService;
import org.example.coursemanagementsystem.service.OtpService;
import org.example.coursemanagementsystem.util.Role;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final OtpService otpService;
    private final EmailService emailService;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    private static final String USER_CREATED_TOPIC = "user-created-topic";

    @Override
    @Transactional
    public void registerStudent(StudentRegistrationRequest request) {
        // Email unikal yoxlanışı
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Bu email artıq qeydiyyatdan keçib");
        }
        // FİN kod unikal yoxlanışı
        if (userRepository.existsByFinCode(request.getFinCode())) {
            throw new FinCodeAlreadyExistsException("Bu FİN kod artıq istifadə olunur");
        }

        // OTP yarat və saxla
        String otp = otpService.generateAndSaveOtp(request.getEmail());

        // User obyekti yarat
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(otp))
                .finCode(request.getFinCode())
                .role(Role.STUDENT)
                .isFirstLogin(true)
                .isActive(true)
                .build();
        user = userRepository.save(user);

        // Student obyekti yarat və əlaqələndir
        Student student = studentMapper.toEntity(request);
        student.setUser(user);
        studentRepository.save(student);

        // OTP-ni email ilə göndər
        emailService.sendOtpEmail(request.getEmail(), otp);

        // Kafka event-i göndər
        UserCreatedEvent event = new UserCreatedEvent(
                user.getId(),
                user.getEmail(),
                user.getFinCode(),
                user.getRole(),
                request.getName(),
                request.getSurname()
        );
        kafkaTemplate.send(USER_CREATED_TOPIC, event);
    }

    @Override
    @Transactional
    public void registerTeacher(TeacherRegistrationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Bu email artıq qeydiyyatdan keçib");
        }
        if (userRepository.existsByFinCode(request.getFinCode())) {
            throw new FinCodeAlreadyExistsException("Bu FİN kod artıq istifadə olunur");
        }

        String otp = otpService.generateAndSaveOtp(request.getEmail());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(otp))
                .finCode(request.getFinCode())
                .role(Role.TEACHER)
                .isFirstLogin(true)
                .isActive(true)
                .build();
        user = userRepository.save(user);

        Teacher teacher = teacherMapper.toEntity(request);
        teacher.setUser(user);
        teacherRepository.save(teacher);

        emailService.sendOtpEmail(request.getEmail(), otp);

        UserCreatedEvent event = new UserCreatedEvent(
                user.getId(),
                user.getEmail(),
                user.getFinCode(),
                user.getRole(),
                request.getName(),
                request.getSurname()
        );
        kafkaTemplate.send(USER_CREATED_TOPIC, event);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new InvalidPasswordException("Email və ya şifrə yanlışdır"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Email və ya şifrə yanlışdır");
        }

        String accessToken = tokenProvider.generateAccessToken(user);
        String refreshToken = tokenProvider.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .isFirstLogin(user.isFirstLogin())
                .userId(user.getId())
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) {
            throw new InvalidPasswordException("Yeniləmə tokeni etibarsızdır");
        }
        String email = tokenProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı"));

        String newAccessToken = tokenProvider.generateAccessToken(user);
        String newRefreshToken = tokenProvider.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .role(user.getRole())
                .isFirstLogin(user.isFirstLogin())
                .userId(user.getId())
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        // Refresh token-i qara siyahıya əlavə etmək olar (məsələn, Redis-də)
        // tokenProvider.blacklistToken(refreshToken);
        // Client tərəfdə token silinir.
    }

    @Override
    @Transactional
    public void verifyOtpAndSetPassword(String email, String otp, String newPassword) {
        boolean valid = otpService.validateOtp(email, otp);
        if (!valid) {
            throw new OtpExpiredException("OTP kodu yanlış və ya vaxtı keçib");
        }

        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false);
        userRepository.save(user);

        otpService.clearOtp(email);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, PasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Köhnə şifrə yanlışdır");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setFirstLogin(false);
        userRepository.save(user);
    }

    @Override
    public Long getUserIdFromToken(String token) {
        return tokenProvider.getUserIdFromToken(token);
    }
}