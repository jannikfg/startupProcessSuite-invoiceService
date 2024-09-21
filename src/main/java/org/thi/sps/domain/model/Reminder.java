package org.thi.sps.domain.model;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Reminder {

  private String id;
  private String reminderLevel;
  private List<String> paymentIds;
  private LocalDate reminderDate;
  private int dueInDays;
  private double totalOutstanding;

}
