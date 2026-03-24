package org.example.coursemanagementsystem.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coursemanagementsystem.dto.kafka.ForgotPasswordEvent;
import org.example.coursemanagementsystem.dto.kafka.UserCreatedEvent;
import org.example.coursemanagementsystem.dto.kafka.UserCreatedProducer;
import org.example.coursemanagementsystem.dto.request.*;
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
import org.example.coursemanagementsystem.security.JwtUtils;
import org.example.coursemanagementsystem.service.AuthService;
import org.example.coursemanagementsystem.service.OtpService;
import org.example.coursemanagementsystem.util.Role;
import org.example.coursemanagementsystem.util.StudentNumberGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final OtpService otpService;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final UserCreatedProducer userCreatedProducer;
    private final RedisTemplate<String, String> redisTemplate;

    private static final long REFRESH_TOKEN_EXPIRE_DAYS = 7;

    @Override
    @Transactional
    public void registerStudent(StudentRegistrationRequest request) {
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
                .role(Role.STUDENT)
                .build();
        user = userRepository.save(user);

        Student student = studentMapper.toEntity(request);
        student.setStudentNumber(StudentNumberGenerator.generate());
        student.setUser(user);
        studentRepository.save(student);

        userCreatedProducer.sendUserCreatedEvent(UserCreatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .finCode(user.getFinCode())
                .role(user.getRole())
                .name(request.getName())
                .surname(request.getSurname())
                .otp(otp)
                .build());
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
                .build();
        user = userRepository.save(user);

        Teacher teacher = teacherMapper.toEntity(request);
        teacher.setUser(user);
        teacherRepository.save(teacher);

        userCreatedProducer.sendUserCreatedEvent(UserCreatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .finCode(user.getFinCode())
                .role(user.getRole())
                .name(request.getName())
                .surname(request.getSurname())
                .otp(otp)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                .orElseThrow(() -> new InvalidPasswordException("Email və ya şifrə yanlışdır"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Email və ya şifrə yanlışdır");
        }

        String accessToken  = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        saveRefreshToken(user.getId(), refreshToken);

        String displayName = resolveDisplayName(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .isFirstLogin(user.getIsFirstLogin())
                .userId(user.getId())
                .displayName(displayName)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new InvalidTokenException("Yeniləmə tokeni etibarsızdır");
        }

        Long userId = jwtUtils.getUserIdFromToken(refreshToken);

        String storedToken = redisTemplate.opsForValue().get(refreshTokenKey(userId));
        if (!refreshToken.equals(storedToken)) {
            throw new InvalidTokenException("Token artıq etibarsızdır");
        }

        String email = jwtUtils.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı"));

        String newAccessToken  = jwtUtils.generateAccessToken(user);
        String newRefreshToken = jwtUtils.generateRefreshToken(user);

        saveRefreshToken(user.getId(), newRefreshToken);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .role(user.getRole())
                .isFirstLogin(user.getIsFirstLogin())
                .userId(user.getId())
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        if (jwtUtils.validateToken(refreshToken)) {
            Long userId = jwtUtils.getUserIdFromToken(refreshToken);
            redisTemplate.delete(refreshTokenKey(userId));
            log.info("İstifadəçi çıxış etdi — userId: {}", userId);
        }
    }

    @Override
    @Transactional
    public void verifyOtpAndSetPassword(String email, String otp, String newPassword) {
        if (!otpService.validateAndClearOtp(email, otp)) {
            throw new OtpExpiredException("OTP kodu yanlış və ya vaxtı keçib");
        }

        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setIsFirstLogin(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, PasswordUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Köhnə şifrə yanlışdır");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Yeni şifrələr uyğun deyil");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setIsFirstLogin(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Bu email ilə aktiv istifadəçi tapılmadı"));

        String otp = otpService.generateAndSaveOtp(email);

        userCreatedProducer.sendForgotPasswordEvent(ForgotPasswordEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .otp(otp)
                .build());
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword, String confirmPassword) {

        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordMismatchException("Şifrələr uyğun deyil");
        }

        if (!jwtUtils.validateToken(token)) {
            throw new InvalidTokenException("Token etibarsızdır və ya vaxtı keçib");
        }

        String email = jwtUtils.getEmailFromToken(token);
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("İstifadəçi tapılmadı"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setIsFirstLogin(false);
        userRepository.save(user);
    }


    private void saveRefreshToken(Long userId, String token) {
        redisTemplate.opsForValue().set(
                refreshTokenKey(userId), token,
                REFRESH_TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
    }

    private String refreshTokenKey(Long userId) {
        return "refresh_token:" + userId;
    }

    private String resolveDisplayName(User user) {
        if (user.getRole() == Role.STUDENT) {
            return studentRepository.findById(user.getId())
                    .map(s -> s.getName() + " " + s.getSurname())
                    .orElse(user.getEmail());
        } else if (user.getRole() == Role.TEACHER) {
            return teacherRepository.findById(user.getId())
                    .map(t -> t.getName() + " " + t.getSurname())
                    .orElse(user.getEmail());
        }
        return user.getEmail();
    }
}
