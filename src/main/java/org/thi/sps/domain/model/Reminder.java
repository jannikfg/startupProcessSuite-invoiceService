package org.thi.sps.domain.model;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

@AllArgsConstructor
@Data
@Builder
public class Reminder {

  @Setter(AccessLevel.NONE)
  private UUID id;
  private UUID invoiceId;
  private LocalDate dueDate;
  private LocalDate reminderDate;

}
