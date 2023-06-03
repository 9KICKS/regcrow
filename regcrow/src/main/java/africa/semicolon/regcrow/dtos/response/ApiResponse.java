package africa.semicolon.regcrow.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private T data;
}
