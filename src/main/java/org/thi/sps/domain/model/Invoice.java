package org.thi.sps.domain.model;

import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Invoice {

  @Setter(AccessLevel.NONE)
  private String id; //Rechnungsnummer
  private String description; //Beschreibung
  private LocalDate createdDate; //Erstellungsdatum
  private List<InvoiceItem> invoiceItems; //Liste der Rechnungspositionen
  private LocalDate dueDate; //Fälligkeitsdatum
  private Long clientId; //Kundennummer
  private LocalDate dateOfDelivery; //Lieferdatum bzw. Datum der erbrachten Dienstleistung
  private String noticeOfTaxExemption; //Hinweis bei Steuerbefreiung
  private String noticeOfRetentionObligation; // In den Fällen des $14 b Abs. 1 Satz 5 UStF einen Hinweis auf die Aufbewahrungspflicht des Rechnungsempfängers
  private double netTotal; //Nettobetrag
  private double taxTotal; //Steuerbetrag
  private double total; //Gesamtbetrag
  private double totalOutstanding; //Offener Betrag
  private boolean paid; //Bezahlt
  private List<Payment> payments; //Liste der Zahlungen
  private List<Reminder> reminders;

}
