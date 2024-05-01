package com.yumeinaruu.iis.model.dto.speciality;

import com.yumeinaruu.iis.model.dto.faculty.FacultyForSpecialityDto;
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
public class SpecialityCreateDto {
    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    private FacultyForSpecialityDto faculty;
}
