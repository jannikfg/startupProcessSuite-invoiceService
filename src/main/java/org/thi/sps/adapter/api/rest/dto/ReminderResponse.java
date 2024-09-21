package org.thi.sps.adapter.api.rest.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.thi.sps.domain.model.Reminder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReminderResponse {

  private String id;
  private String reminderLevel;
  private List<String> paymentIds;
  private LocalDate reminderDate;
  private int dueInDays;
  private double totalOutstanding;

  public static ReminderResponse fromReminder(Reminder reminder){
    return ReminderResponse.builder()
        .id(reminder.getId())
        .reminderLevel(reminder.getReminderLevel())
        .paymentIds(reminder.getPaymentIds())
        .reminderDate(reminder.getReminderDate())
        .dueInDays(reminder.getDueInDays())
        .totalOutstanding(reminder.getTotalOutstanding())
        .build();
  }
}
