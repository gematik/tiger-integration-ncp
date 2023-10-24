# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#language: de
#noinspection NonAsciiCharacters,SpellCheckingInspection
@PRODUKT:NCPeH_FD
Funktionalität: NCPeH PSA Rohdaten Performance Reporting
  Die folgend benannten NCPeH-Anwendungsfälle basieren auf dem Anwendungsszenario "Patient Summary Land A."
  Betrachtet werden die Themen zum Reporting der Performance Rohdaten des NCPeH-FD an den Dienst der gematik
  nach [gemSpec_Perf#Rohdaten-Performance-Reporting Spezifika NCPeH-Fachdienst]:
    1. Prüfung der gemeldeten Anzahl und gemessenen Dauer der vier Operationen im Report
    2. Prüfung des gemeldeten Status der vier Operationen im Report
    3. Implizite Prüfung der Einhaltung der Vorgaben zur Rohdatenerfassung v.02 (durch erfolgreichen Import ins Data Warehouse)
    4. Prüfung der Anzeige in Grafana (Basis: erfolgreicher Import ins Data Warehouse)
  Involvierte Anwendungsfälle:
    AF_10107 - Versicherten im Behandlungsland identifizieren
      gemSpec_Perf: NCPeH.UC_1, Operation Cross_Gateway_Patient_Discovery::findIdentityByTraits
    AF_10121 - Verfügbare Versichertendatensätze des ePKA MIO auflisten
      gemSpec_Perf: NCPeH.UC_2, Operation Cross_Gateway_Query::FindDocuments
    AF_10122 - Versicherten im Behandlungsland identifizieren
      gemSpec_Perf: NCPeH.UC_3, Operation Cross_Gateway_Retrieve::RetrieveDocument
    AF_10123 - Versichertendatensatz als PDF abrufen
      gemSpec_Perf: NCPeH.UC_4, Operation Cross_Gateway_Retrieve::RetrieveDocumentPDF
  Involvierte Anforderungen:
    [gemSpec_Perf#Rohdaten-Performance-Reporting Spezifika NCPeH-Fachdienst]
      A_22482 Performance - Rohdaten - Erfassung von Rohdaten (Rohdatenerfassung v.02)
      A_23012 Performance - Rohdaten - Spezifika NCPeH-Fachdienst - Duration (Rohdatenerfassung v.02)
      A_23013 Performance - Rohdaten - Spezifika NCPeH-Fachdienst - Status (Rohdatenerfassung v.02)
      A_23118-1 Performance - Rohdaten - Spezifika NCPeH-Fachdienst - Message (Rohdatenerfassung v.02)
    [gemSpec_Perf#Rohdaten-Performance-Reporting (Rohdatenerfassung v.02)] -> implizite Prüfung
      A_22000 Performance - Rohdaten - zu liefernde Dateien (Rohdatenerfassung v.02)
      A_22002 Performance - Rohdaten - Übermittlung (Rohdatenerfassung v.02)
      A_22429 Performance - Rohdaten - Inhalt der Selbstauskunft (Rohdatenerfassung v.02)
      A_22004 Performance - Rohdaten - Korrektheit (Rohdatenerfassung v.02)
      A_21975 Performance - Rohdaten - Default-Werte für Lieferintervalle (Rohdatenerfassung v.02)
      A_21980 Performance - Rohdaten - Leerlieferung (Rohdatenerfassung v.02)
      A_22001-* Performance - Rohdaten - Name der Berichte (Rohdatenerfassung v.02)
      A_21981-* Performance - Rohdaten - Format des Rohdaten-Performance-Berichtes (Rohdatenerfassung v.02)
      A_22500-* Performance - Rohdaten - Status-Block (Rohdatenerfassung v.02)
      A_21982-* Performance - Rohdaten - Message-Block (Rohdatenerfassung v.02)
      A_22513-* Performance - Rohdaten - Message-Block im Fehlerfall (Rohdatenerfassung v.02)


  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_010
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AFO-ID:A_21980
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Nachrichtenstruktur für einen Leer-Report
    Der NCPeH sendet alle 5 Minuten einen Report an den BDEv2 Dienst der gematik. Es wird nur die
    korrekte Nachrichtenstruktur überprüft, wenn keine Messwerte vorhanden sind (Leer-Nachricht).
    Es wird eine funktionierende Anbindung an den BDEv2 Dienst sichergestellt.

    Hinweis: Diese Prüfung ist Voraussetzung für die Durchführung der weiteren Testfälle bzgl. Reporting.

    Wenn der NCPeH für 10 Minuten Idle ist
    Dann liefert er an den BDEv2 Dienst mindestens einen Performance Report mit einer Leer-Information
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_011
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AFO-ID:A_22429
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Selbstauskunft
  Der NCPeH sendet alle 60 Minuten eine Selbstauskunft an den BDEv2 Dienst der gematik. Es wird geprüft,
  dass die gelieferten Daten importiert werden können (indirekte Strukturprüfung), und die gelieferten Daten
  mit den erwarteten Werten übereinstimmen.
  Die erwarteten Daten müssen zum Teil innerhalb gematik und zum Teil vom Betreiber erfragt werden.
  Zu prüfen sind folgende Werte aus der Selbstauskunft:
  1. InformationDate (60 Minuten Lieferintervall)
  2. ProductType (gematik Betrieb)
  3. ProductTypeVersion (gematik Testmanagement bzw. Produktteam)
  4. ProductVersion (Betreiber)
  Die restlichen Werte unterliegen keinen festen, kalkulierbaren Regeln und werden deshalb nicht geprüft.

    Angenommen die erwarteten Daten zur Selbstauskunft sind bekannt
    Wenn der NCPeH im 60 Minuten Intervall eine Selbstauskunft an den BDEv2 Dienst sendet
    Dann enthält er die mit gematik Operations und dem Betreiber abgestimmten Informationen über den NCPeH Fachdienst
    Und in Grafana können die erwarteten Selbstauskunft Daten nachvollzogen werden

  # nur Testentwurf
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_020
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107 @AF-ID:AF_10121 @AF-ID:AF_10122 @AF-ID:AF_10123
  @AFO-ID:A_23012 @AFO-ID:A_23013 @AFO-ID:A_23118
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Messung bei einem Behandlungsvorgang mit CDA L3
    Es wird geprüft, dass bei einem vollständigen, erfolgreichen Behandlungsvorgang mit Ausführung der drei
    Performance Use Cases NCPeH.UC_1, NCPeH.UC_2 und NCPeH.UC_3 die Anzahl der UseCases jeweils 1 ist,
    die Startzeiten konsistent und die gemessenen Bearbeitungszeiten > 0 und < der Antwortzeit des NCPeH Simulators sind .
    Es wird geprüft, dass kein Fehlerstatus der Performance Use Cases enthalten ist.
    Es wird die Struktur und die Einträge des Reports mit seinen Messwerten geprüft.
    Der Testfall NCP1_E2E_PSA_UC3_001 "NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen"
    wird hierbei zu Hilfe genommen.
    Die gemessenen Werte wurden in das Auswertungssystem übernommen und können in Grafana angezeigt werden.

    Angenommen der Testfall NCP1_E2E_PSA_UC3_001 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country     | PerformanceUsecases              |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen korrekten Statuscode
      | Statuscode |
      | 2xx        |
    Und die Startzeiten der erwarteten Einträge des Performance Reports liegen im erwarteten Ausführungsintervall
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld das korrekte anfragende Land
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden

  # nur Testentwurf
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_021
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107 @AF-ID:AF_10121 @AF-ID:AF_10122 @AF-ID:AF_10123
  @AFO-ID:A_23012 @AFO-ID:A_23013 @AFO-ID:A_23118
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Messung bei einem Behandlungsvorgang mit CDA L1
  Es wird geprüft, dass bei einem vollständigen, erfolgreichen Behandlungsvorgang mit Ausführung der drei
  Performance Use Cases NCPeH.UC_1, NCPeH.UC_2 und NCPeH.UC_4 die Anzahl der UseCases jeweils 1 ist,
  die Startzeiten konsistent und die gemessenen Bearbeitungszeiten > 0 und < der Antwortzeit des NCPeH Simulators sind .
  Es wird geprüft, dass kein Fehlerstatus der Performance Use Cases enthalten ist.
  Es wird die Struktur und die Einträge des Reports mit seinen Messwerten geprüft.
  Der Testfall NCP1_E2E_PSA_UC4_001 "NCPeH - PSA UC4 Patient Summary als PDF-Dokument (CDA1) abrufen"
  wird hierbei zu Hilfe genommen.
  Die gemessenen Werte wurden in das Auswertungssystem übernommen und können in Grafana angezeigt werden.

    Angenommen der Testfall NCP1_E2E_PSA_UC4_001 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country     | PerformanceUsecases              |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen korrekten Statuscode
      | Statuscode |
      | 2xx        |
    Und die Startzeiten der erwarteten Einträge des Performance Reports liegen im erwarteten Ausführungsintervall
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld das korrekte anfragende Land
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden

  # nur Testentwurf
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_030
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107 @AF-ID:AF_10121 @AF-ID:AF_10122 @AF-ID:AF_10123
  @AFO-ID:A_23012 @AFO-ID:A_23013 @AFO-ID:A_23118
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Messung bei mehreren Behandlungsvorgängen
    Es wird geprüft, dass bei 20 vollständigen und erfolgreichen Behandlungsvorgängen innerhalb von 10 Minuten
    die Anzahl der UseCases jeweils 20 ist und die gemessenen Zeiten > 0 und < der Antwortzeit
    des NCPeH Simulators sind. Es wird geprüft, dass die korrekten Status übertragen wurden.
    Die gemessenen Werte wurden in das Auswertungssystem übernommen und können in Grafana angezeigt werden.

    Angenommen es wurden in einem 10 Minuten Zeitintervall mehrere NCPeH Testfälle ausgeführt
      | Testfallindex | Testfall             | PerformanceUsecases              |
      | 1             | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 2             | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 3             | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 4             | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 5             | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 6             | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 7             | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 8             | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 9             | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 10            | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 11            | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 12            | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 13            | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 14            | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 15            | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 16            | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 17            | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 18            | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
      | 19            | NCP1_E2E_PSA_UC3_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | 20            | NCP1_E2E_PSA_UC4_001 | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
    Und es ist eine Übersicht der entsprechend durchgeführten Performance Use Cases vorhanden
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthalten die Reports insgesamt je einen Eintrag für jeden erwarteten Performance Use Case
    Und die Startzeiten der erwarteten Einträge der Performance Reports liegen im erwarteten Ausführungsintervall
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden

  # nur Testentwurf
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_040
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107
  @AFO-ID:A_23012 @AFO-ID:A_23013 @AFO-ID:A_23118-01
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Messung bei findIdentityByTraits (NCPeH.UC_1) mit Fehler NCPeH.UC_1
    Für das Reporting des NCPeH.UC_1 wird der Testfall NCP1_E2E_PSA_UC1_010
      "NCPeH - PSA UC1 Identifikation eines Versicherten - Suche findet kein ePKA Dokument"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_PI_GENERIC" geprüft (entsprechend dem acknowledgementDetail der Response des Referenz-Testfalls).

    Angenommen der Testfall NCP1_E2E_PSA_UC1_010 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases | ehdsiErrorcodes  |
      | NETHERLANDS | NCPeH.UC_1          | ERROR_PI_GENERIC |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen korrekten Statuscode
      | Statuscode |
      | 2xx        |
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld das korrekte anfragende Land
    Und die Startzeiten der erwarteten Einträge des Performance Reports liegen im erwarteten Ausführungsintervall
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden

  # nur Testentwurf
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_041
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107
  @AFO-ID:A_23012 @AFO-ID:A_23013 @AFO-ID:A_23118
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Messung bei findIdentityByTraits (NCPeH.UC_1) mit Fehler ERROR_PI_NO_MATCH
    Für das Reporting des NCPeH.UC_1 wird der Testfall NCP1_IOP_PSA_EPA_010
      "NCPeH - PSA ePA-IOP Identifikation eines Versicherten - KVNR nicht gefunden"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_PI_NO_MATCH" geprüft (entsprechend dem acknowledgementDetail der Response des Referenz-Testfalls).

    Angenommen der Testfall NCP1_IOP_PSA_EPA_010 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases | ehdsiErrorcodes   |
      | NETHERLANDS | NCPeH.UC_1          | ERROR_PI_NO_MATCH |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen korrekten Statuscode
      | Statuscode |
      | 2xx        |
    Und die Startzeiten der erwarteten Einträge des Performance Reports liegen im erwarteten Ausführungsintervall
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld das korrekte anfragende Land
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden

  # nur Testentwurf
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_042
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10121
  @AFO-ID:A_23012 @AFO-ID:A_23013 @AFO-ID:A_23118
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Messung bei FindDocuments (NCPeH.UC_2) mit Fehler ERROR_GENERIC_DOCUMENT_MISSING
    Für das Reporting des NCPeH.UC_2 wird der Testfall NCP1_IOP_PSA_UC2_010
      "NCPeH - PSA UC2 Suche nach den verfügbaren Patient Summary Dokumenten - es wird kein ePKA Dokument gefunden"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_GENERIC_DOCUMENT_MISSING" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP1_IOP_PSA_UC2_010 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases   | ehdsiErrorcodes                     |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2 | null,ERROR_GENERIC_DOCUMENT_MISSING |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen korrekten Statuscode
      | Statuscode |
      | 2xx        |
    Und die Startzeiten der erwarteten Einträge des Performance Reports liegen im erwarteten Ausführungsintervall
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld das korrekte anfragende Land
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden

  # nur Testentwurf
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_043
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10121
  @AFO-ID:A_23012 @AFO-ID:A_23013 @AFO-ID:A_23118
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Messung bei FindDocuments (NCPeH.UC_2) mit Fehler ERROR_PS_GENERIC
    Für das Reporting des NCPeH.UC_2 wird der Testfall NCP1_IOP_PSA_EPA_061
      "NCPeH - PSA ePA-IOP Berechtigung für EU Mitgliedsstaat fehlt bei FindDocuments ohne offene Session"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_PS_GENERIC" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP1_IOP_PSA_EPA_061 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases   | ehdsiErrorcodes       |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2 | null,ERROR_PS_GENERIC |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen korrekten Statuscode
      | Statuscode |
      | 2xx        |
    Und die Startzeiten der erwarteten Einträge des Performance Reports liegen im erwarteten Ausführungsintervall
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld das korrekte anfragende Land
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden

  # nur Testentwurf
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_044
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10122
  @AFO-ID:A_23012 @AFO-ID:A_23013 @AFO-ID:A_23118
  # Weitere Tags, team-intern
  @IOP
  Szenario: NCPeH - PSA PerfRep Messung bei RetrieveDocuments (NCPeH.UC_3) mit Fehler ERROR_PS_GENERIC
    Für das Reporting des NCPeH.UC_3 wird der Testfall NCP1_IOP_PSA_UC3_012
      "NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - fehlende ePKA Dokumentsignatur"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_PS_GENERIC" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP1_IOP_PSA_UC3_011 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases              | ehdsiErrorcodes            |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 | null,null,ERROR_PS_GENERIC |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen korrekten Statuscode
      | Statuscode |
      | 2xx        |
    Und die Startzeiten der erwarteten Einträge des Performance Reports liegen im erwarteten Ausführungsintervall
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld das korrekte anfragende Land
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und in Grafana können die erwarteten Report Daten nachvollzogen werden
