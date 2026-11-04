package club.fiverr.auth.presentation.rest;

import club.fiverr.auth.application.dto.result.AuthAccountResult;
import club.fiverr.auth.application.dto.result.AuthTokenResult;
import club.fiverr.auth.application.port.in.ChangePasswordUseCase;
import club.fiverr.auth.application.port.in.GetAuthAccountUseCase;
import club.fiverr.auth.application.port.in.LoginAuthAccountUseCase;
import club.fiverr.auth.application.port.in.RefreshTokenUseCase;
import club.fiverr.auth.application.port.in.RegisterAuthAccountUseCase;
import club.fiverr.auth.presentation.rest.mapper.AuthAccountRestMapper;
import club.fiverr.auth.presentation.rest.request.ChangePasswordRequest;
import club.fiverr.auth.presentation.rest.request.LoginAuthAccountRequest;
import club.fiverr.auth.presentation.rest.request.RefreshTokenRequest;
import club.fiverr.auth.presentation.rest.request.RegisterAuthAccountRequest;
import club.fiverr.auth.presentation.rest.response.AuthAccountResponse;
import club.fiverr.auth.presentation.rest.response.AuthTokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthAccountController {

    private final RegisterAuthAccountUseCase registerAuthAccountUseCase;
    private final LoginAuthAccountUseCase loginAuthAccountUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final GetAuthAccountUseCase getAuthAccountUseCase;

    public AuthAccountController(
            RegisterAuthAccountUseCase registerAuthAccountUseCase,
            LoginAuthAccountUseCase loginAuthAccountUseCase,
            RefreshTokenUseCase refreshTokenUseCase,
            ChangePasswordUseCase changePasswordUseCase,
            GetAuthAccountUseCase getAuthAccountUseCase) {
        this.registerAuthAccountUseCase = registerAuthAccountUseCase;
        this.loginAuthAccountUseCase = loginAuthAccountUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.getAuthAccountUseCase = getAuthAccountUseCase;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AuthAccountResponse>> listAccounts() {
        List<AuthAccountResponse> accounts = getAuthAccountUseCase.findAll().stream()
                .map(AuthAccountRestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthTokenResponse> register(@Valid @RequestBody RegisterAuthAccountRequest request) {
        AuthTokenResult result = registerAuthAccountUseCase.execute(AuthAccountRestMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthAccountRestMapper.toResponse(result));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginAuthAccountRequest request) {
        AuthTokenResult result = loginAuthAccountUseCase.execute(AuthAccountRestMapper.toCommand(request));
        return ResponseEntity.ok(AuthAccountRestMapper.toResponse(result));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthTokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthTokenResult result = refreshTokenUseCase.execute(AuthAccountRestMapper.toCommand(request));
        return ResponseEntity.ok(AuthAccountRestMapper.toResponse(result));
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AuthAccountResponse> getAccountById(@PathVariable UUID id) {
        AuthAccountResult result = getAuthAccountUseCase.getById(id);
        return ResponseEntity.ok(AuthAccountRestMapper.toResponse(result));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthAccountResponse> getCurrentAccount(@RequestHeader("X-User-Id") UUID userId) {
        AuthAccountResult result = getAuthAccountUseCase.getById(userId);
        return ResponseEntity.ok(AuthAccountRestMapper.toResponse(result));
    }

    @PutMapping("/accounts/{id}/password")
    public ResponseEntity<AuthAccountResponse> changePassword(
            @PathVariable UUID id,
            @Valid @RequestBody ChangePasswordRequest request) {
        AuthAccountResult result = changePasswordUseCase.execute(AuthAccountRestMapper.toCommand(id, request));
        return ResponseEntity.ok(AuthAccountRestMapper.toResponse(result));
    }
}
