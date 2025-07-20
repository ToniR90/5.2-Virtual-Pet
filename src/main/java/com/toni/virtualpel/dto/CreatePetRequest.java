package com.toni.virtualpel.dto;

import com.toni.virtualpel.model.enums.Variant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePetRequest {

    @NotBlank(message = "Insert a name")
    @Size(min = 1 , max = 20 , message = "Name must be between 1 - 20 chars long")
    private String name;

    @NotNull(message = "The Dragon variant can't be empty")
    private Variant variant;
}
