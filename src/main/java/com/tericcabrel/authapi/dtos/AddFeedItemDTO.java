package com.tericcabrel.authapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFeedItemDTO {

    @NotBlank(message = "Caption cannot be blank")
    @Size(max = 255, message = "Caption cannot exceed 255 characters")
    private String caption;

    private List<String> mediaUrls;
}
