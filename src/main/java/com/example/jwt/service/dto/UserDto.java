package com.example.jwt.service.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String uuid;
    @NonNull
    private Boolean isActive;
    private List<Long> roleIds;
}
