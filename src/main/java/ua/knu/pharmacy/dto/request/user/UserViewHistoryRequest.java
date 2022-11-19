package ua.knu.pharmacy.dto.request.user;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserViewHistoryRequest {
    private String start;
    private String end;
    private Long userId;
}
