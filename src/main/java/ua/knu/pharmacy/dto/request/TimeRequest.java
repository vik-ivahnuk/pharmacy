package ua.knu.pharmacy.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TimeRequest {
    protected String start;
    protected String end;
}
