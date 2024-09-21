package org.thi.sps.adapter.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReminderAdditionRequest {

  private String invoiceId;
  private int dueInDays;
  private String reminderLevel;

}
