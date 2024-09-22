package org.thi.sps.adapter.jpa.Entities;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.thi.sps.domain.model.Reminder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "invoice_reminders")
public class ReminderEntity {

  @Id
  private String id;
  private String reminderLevel;

  @ElementCollection
  private List<String> paymentIds;
  private LocalDate reminderDate;
  private int dueInDays;
  private double totalOutstanding;

  public Reminder toReminder() {
    return Reminder.builder()
        .id(this.id)
        .reminderLevel(this.reminderLevel)
        .paymentIds(this.paymentIds)
        .reminderDate(this.reminderDate)
        .dueInDays(this.dueInDays)
        .totalOutstanding(this.totalOutstanding)
        .build();
  }

  public static ReminderEntity fromReminder(Reminder reminder) {
    ReminderEntity reminderEntity = new ReminderEntity();
    reminderEntity.setId(reminder.getId());
    reminderEntity.setReminderLevel(reminder.getReminderLevel());
    reminderEntity.setPaymentIds(reminder.getPaymentIds());
    reminderEntity.setReminderDate(reminder.getReminderDate());
    reminderEntity.setDueInDays(reminder.getDueInDays());
    reminderEntity.setTotalOutstanding(reminder.getTotalOutstanding());
    return reminderEntity;
  }

}
