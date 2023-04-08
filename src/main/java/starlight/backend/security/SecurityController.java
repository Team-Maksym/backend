package starlight.backend.security;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import starlight.backend.security.service.SecurityServiceInterface;
import starlight.backend.security.model.request.NewUser;
import starlight.backend.security.model.response.SessionInfo;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Security", description = "Security related endpoints")
public class SecurityController {
    private SecurityServiceInterface service;

    @Operation(
            summary = "Login in system",
            description = "Login in system",
            tags = {"Security"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SessionInfo.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SessionInfo.class)
                            )
                    )
            }
    )
    @PostMapping("/talents/login")
    @ResponseStatus(HttpStatus.OK)
    public SessionInfo login(Authentication authentication) {
        return service.loginInfo(authentication.getName());
    }

    @Operation(
            summary = "Create a new talent",
            description = "Create a new talent",
            tags = {"Security"}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = SessionInfo.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation error",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = Exception.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            name = "Exception",
                                            implementation = Exception.class
                                    )
                            )
                    )
            }
    )
    @PostMapping("/talents")
    @ResponseStatus(HttpStatus.CREATED)
    public SessionInfo register(@Valid @RequestBody NewUser newUser) {
        return service.register(newUser);
    }
}
