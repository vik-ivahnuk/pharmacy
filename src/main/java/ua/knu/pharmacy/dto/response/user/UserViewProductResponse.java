package ua.knu.pharmacy.dto.response.user;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserViewProductResponse {
  Long id;
  String name;
  String description;
  List<String> reviews;
  Integer count;
}
