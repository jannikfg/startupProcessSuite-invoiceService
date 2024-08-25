package org.thi.sps.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {

  private Long id;
  private String firstName;
  private String lastName;
  private String company;
  private String email;
  private String phone;
  private String address;
  private String city;
  private String plz;

}
