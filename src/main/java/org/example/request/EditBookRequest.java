package org.example.request;

import lombok.Data;

@Data
public class EditBookRequest extends CreateBookRequest {
    private Long id;
}
