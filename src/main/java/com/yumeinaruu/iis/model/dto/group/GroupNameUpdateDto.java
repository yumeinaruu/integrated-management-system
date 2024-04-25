package com.yumeinaruu.iis.model.dto.group;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupNameUpdateDto {
    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;
}
