package com.yumeinaruu.iis.model.dto.users;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersFindByNameDto {
    @NotNull
    @Size(min = 1, max = 50)
    private String username;
}
