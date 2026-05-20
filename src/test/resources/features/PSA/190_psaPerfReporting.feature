#
# Copyright 2024-2025 gematik GmbH
#
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
# ******
#
# For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
#

#language: de
#noinspection NonAsciiCharacters,SpellCheckingInspection
@PRODUKT:NCPeH_FD
# Weitere Tags, team-intern
@eHDSI-Szenario:PSA_PerfRep
Funktionalität: NCPeH PSA Rohdaten Performance Reporting
  Die folgend benannten NCPeH-Anwendungsfälle basieren auf dem Anwendungsszenario "Patient Summary Land A."
  Betrachtet werden die Themen zum Reporting der Performance Rohdaten des NCPeH-FD an den Dienst der gematik
  nach [gemSpec_Perf#Rohdaten-Performance-Reporting Spezifika NCPeH-Fachdienst]:
    1. Prüfung der gemeldeten Anzahl und Plausibilität der gemessenen Dauer der Operationen NCPeH.UC_1-NCPeH.UC_4 im Report
      - Exaktheit von Zeiten im Report kann nicht geprüft werden (nur Plausibilität anhand der eigenen Messungen)
      - NCPeH.UC_VAU1 kann nicht geprüft werden, da keine Kontrolle darüber besteht, ob ein einem Szenario ein VAU-Kanal
      aufgebaut wird
    2. Prüfung des gemeldeten Status der vier Operationen im Report
    3. Implizite Prüfung der Einhaltung der Vorgaben zum Reporting (durch erfolgreichen Import ins Data Warehouse)
  Involvierte Anwendungsfälle:
    AF_10107-* - Versicherten im Behandlungsland identifizieren
      gemSpec_Perf: NCPeH.UC_1, Operation "Cross_Gateway_Patient_Discovery::findIdentityByTraits (PS-A)"
    AF_10121-* - Verfügbare Versichertendatensätze des ePKA MIO auflisten
      gemSpec_Perf: NCPeH.UC_2, Operation "Cross_Gateway_Query::FindDocuments (PS-A)"
    AF_10122-* - Versicherten im Behandlungsland identifizieren     und
    AF_10123-* - Versichertendatensatz als PDF abrufen
      gemSpec_Perf: NCPeH.UC_3, Operation "Cross_Gateway_Retrieve::RetrieveDocument (PS-A)"
    (NCPeH.UC_VAU1 ist kein eigentlicher UseCase und wird nur hilfsweise so in gemSpec_Perf bezeichnet)
  Involvierte Anforderungen:
    [gemSpec_Perf#Bearbeitungszeiten NCPeH-Fachdienst] -> Plausibilitätsprüfung (keine exakte Gleichheit)
      A_23016-* Performance - NCPeH-Fachdienst - Last- und Bearbeitungszeiten
    [gemSpec_Perf#Rohdaten-Performance-Reporting Spezifika NCPeH-Fachdienst]
      A_23011-* Performance - Betriebsdatenlieferung v2 - Spezifika NCPeH-Fachdienst - Operation
      A_23012-* Performance - Betriebsdatenlieferung v2 - Spezifika NCPeH-Fachdienst - Duration
      A_23118-* Performance - Betriebsdatenlieferung v2 - Spezifika NCPeH-Fachdienst - Message
    [gemSpec_Perf#Betriebsdatenlieferung Version 2] -> implizite Prüfung
      A_22002-* Performance - Betriebsdatenlieferung v2 - Übermittlung
      A_22429-* Performance - Selbstauskunft v1 - Inhalt
      A_22004-* Performance - Betriebsdatenlieferung v2 - Korrektheit
      A_21975-* Performance - Betriebsdatenlieferung v2 - Default-Wert des Lieferintervalls
      A_21980-* Performance - Betriebsdatenlieferung v2 - Leerlieferung
      A_22001-* Performance - Betriebsdatenlieferung v2 - Dateiname der Lieferung
      A_21981-* Performance - Betriebsdatenlieferung v2 - Format
      A_22500-* Performance - Betriebsdatenlieferung v2 - Status-Block
      A_21982-* Performance - Betriebsdatenlieferung v2 - Message-Block
      A_22513-* Performance - Betriebsdatenlieferung v2 - Message-Block im Fehlerfall

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_010
  @STATUS:Zurückgestellt @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21980-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @LongRunning
  Szenario: NCPeH - PSA PerfRep Nachrichtenstruktur für einen Leer-Report
    Der NCPeH sendet alle 5 Minuten einen Report an den BDEv2 Dienst der gematik. Es wird nur die
    korrekte Nachrichtenstruktur überprüft, wenn keine Messwerte vorhanden sind (Leer-Nachricht).
    Es wird eine funktionierende Anbindung an den BDEv2 Dienst sichergestellt.

    Hinweis: Diese Prüfung ist Voraussetzung für die Durchführung der weiteren Testfälle bzgl. Reporting.

    Wenn der NCPeH für 10 Minuten Idle ist
    Dann liefert er an den BDEv2 Dienst mindestens einen leeren Performance Report

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_011
  @STATUS:Zurückgestellt @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_22429
  @DESCRIPTION
  # Weitere Tags, team-intern
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

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_020
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01 @AF-ID:AF_10122-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  @CI
  Szenario: NCPeH - PSA PerfRep Messung bei einem Behandlungsvorgang mit CDA L3
    Es wird geprüft, dass bei einem vollständigen, erfolgreichen Behandlungsvorgang mit Ausführung der drei
    Performance Use Cases NCPeH.UC_1, NCPeH.UC_2 und NCPeH.UC_3 die Anzahl der UseCases jeweils 1 ist,
    die Startzeiten konsistent und die gemessenen Bearbeitungszeiten > 0 und < der Antwortzeit des NCPeH Simulators sind.
    Es wird geprüft, dass kein Fehlerstatus der Performance Use Cases enthalten ist.
    Es werden die Struktur und die Einträge des Reports mit seinen Messwerten anhand der Informationen aus dem
    Operations-Datawarehouse geprüft.
    Der Testfall NCP1_E2E_PSA_UC3_001 "NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen"
    wird hierbei zu Hilfe genommen.

    Angenommen der Testfall NCP1_E2E_PSA_UC3_001 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country     | PerformanceUsecases              |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_021
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01 @AF-ID:AF_10123-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21981-02 @AFO-ID:A_22500-01 @AFO-ID:A_21982-01
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - PSA PerfRep Messung bei einem Behandlungsvorgang mit CDA L1
    Es wird geprüft, dass bei einem vollständigen, erfolgreichen Behandlungsvorgang mit Ausführung der drei
    Performance Use Cases NCPeH.UC_1, NCPeH.UC_2 und NCPeH.UC_4 die Anzahl der UseCases jeweils 1 ist,
    die Startzeiten konsistent und die gemessenen Bearbeitungszeiten > 0 und < der Antwortzeit des NCPeH Simulators sind .
    Es wird geprüft, dass kein Fehlerstatus der Performance Use Cases enthalten ist.
    Es werden die Struktur und die Einträge des Reports mit seinen Messwerten anhand der Informationen aus dem
    Operations-Datawarehouse geprüft.
    Der Testfall NCP1_E2E_PSA_UC4_001 "NCPeH - PSA UC4 Patient Summary als PDF-Dokument (CDA1) abrufen"
    wird hierbei zu Hilfe genommen.

    Angenommen der Testfall NCP1_E2E_PSA_UC4_001 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country     | PerformanceUsecases              |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_4 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # nur Testentwurf
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_022
  @STATUS:Zurückgestellt @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01 @AF-ID:AF_10123-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21981-02 @AFO-ID:A_22500-01 @AFO-ID:A_21982-01
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - PSA PerfRep Messung für Aufbau VAU-Kanal
    Es wird geprüft, dass bei einem Aufbau eines VAU-Kanals zum ePA-AS ein Report für NCPeH.UC_VAU1 geliefert wird,
    die Startzeiten konsistent und die gemessenen Bearbeitungszeiten > 0 und < der Antwortzeit des NCPeH Simulators sind.
    Es wird geprüft, dass kein Fehlerstatus der Performance Use Cases enthalten ist.
    Es werden die Struktur und die Einträge des Reports mit seinen Messwerten anhand der Informationen aus dem
    Operations-Datawarehouse geprüft.
    Der Testfall NCP1_IOP_PSA_EPA_060 "NCPeH - PSA ePA-IOP UC2 Neuaufbau eines VAU-Kanals nach 19 Minuten idle timeout"
    wird hierbei zu Hilfe genommen. Dabei muss zusätzlich zum Report für NCPeH.UC_2 auch ein Report zu NCPeH.UC_VAU1
    geliefert werden

    Angenommen der Testfall NCP1_IOP_PSA_EPA_040 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country     | PerformanceUsecases                            |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_VAU1,NCPeH.UC_2 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration


  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_PerfRep_030
  @STATUS:Zurückgestellt @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10107-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenariogrundriss: NCPeH - PSA PerfRep Datengenerierung zu mehreren Behandlungsvorgängen
    Es wird geprüft, dass der NCPeH für verschiedene EU Mitgliedsstaaten mit jedem Aktensystem und entsprechend vorhandener
    Berechtigungen das vollständige Anwendungsszenario Patient Summary Land A durchführen kann.

    Angenommen eine versicherte Person hat ein aktives ePA Konto im Aktensystem des Betreibers <Aktensystem>
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land <Mitgliedsstaat> bei LE-EU <Leistungserbringer> in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Wenn der LE-EU erfolgreich die Patient Identification der versicherten Person durchführt
    Und der LE-EU führt danach erfolgreich eine Suche des Patient Summary der versicherten Person durch
    Und der LE-EU ruft das Patient Summary Dokument der versicherten Person als CDA Level <CDA_Level> ab
    Dann erhält der LE-EU ein Dokument der versicherten Person zurück

    # Todo: add second Aktensystem, when it becomes available, to generate load split over both systems
    # Todo: if possible change countries as well (and do not forget to update NCP1_E2E_PSA_PerfRep_031 accordingly)
    # Testschleife über 20 vollständige PS-A Abläufe, abwechselnd CDA L3 und L1, noch über genau ein Aktensystem
    Beispiele:
      | Aktensystem | Mitgliedsstaat | Leistungserbringer | CDA_Level |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |
      | IBM         | NETHERLANDS    | van der Meer       | 3         |
      | IBM         | NETHERLANDS    | van der Meer       | 1         |

  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_PerfRep_031
  @STATUS:Zurückgestellt @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01 @AF-ID:AF_10122-01 @AF-ID:AF_10123-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21981-02 @AFO-ID:A_22500-01 @AFO-ID:A_21982-01
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - PSA PerfRep Messung bei mehreren Behandlungsvorgängen
    Es wird geprüft, dass bei 20 vollständigen und erfolgreichen Behandlungsvorgängen innerhalb von 10 Minuten
    die Anzahl der UseCases jeweils 20 ist und die gemessenen Zeiten > 0 und < der Antwortzeit
    des NCPeH Simulators sind. Es wird geprüft, dass die korrekten Status übertragen wurden.
    Die gemessenen Werte wurden in das Auswertungssystem übernommen und können in Grafana angezeigt werden.

    # Hinweis: in NCPeH v2.0.x wurden im Reporting zu PS-A die UC_3 (XML-Abruf) und UC_4 (PDF-Abruf) zusammengezogen
    # siehe https://gemspec.gematik.de/docs/gemSpec/gemSpec_Perf/latest/#3.7

    Angenommen der Testfall NCP1_E2E_PSA_PerfRep_030 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country     | PerformanceUsecases              |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration


  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_040
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21981-02 @AFO-ID:A_22500-01 @AFO-ID:A_21982-01
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_22513-02
  @DESCRIPTION
  # Weitere Tags, team-intern
  @CI
  Szenario: NCPeH - PSA PerfRep Messung für PSA_RespondingGateway (NCPeH.UC_1) mit Fehler NCPeH.UC_1
    Für das Reporting des NCPeH.UC_1 wird der Testfall NCP1_E2E_PSA_UC1_010
      "NCPeH - PSA UC1 Identifikation eines Versicherten - Suche findet kein ePKA Dokument"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_PI_GENERIC" geprüft (entsprechend dem acknowledgementDetail der Response des Referenz-Testfalls).

    Angenommen der Testfall NCP1_IOP_PSA_UC1_011 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases | ehdsiErrorcodes  |
      | NETHERLANDS | NCPeH.UC_1          | ERROR_PI_GENERIC |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration


  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_041
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21981-02 @AFO-ID:A_22500-01 @AFO-ID:A_21982-01
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_22513-02
  @DESCRIPTION
  # Weitere Tags, team-intern
  @CI
  Szenario: NCPeH - PSA PerfRep Messung für PSA_RespondingGateway (NCPeH.UC_1) mit Fehler ERROR_PI_NO_MATCH
    Für das Reporting des NCPeH.UC_1 wird der Testfall NCP1_E2E_PSA_UC1_010
      "NCPeH - PSA UC1 Identifikation eines Versicherten - Suche findet kein ePKA Dokument"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_PI_NO_MATCH" geprüft (entsprechend dem acknowledgementDetail der Response des Referenz-Testfalls).

    Angenommen der Testfall NCP1_E2E_PSA_UC1_010 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases | ehdsiErrorcodes   |
      | NETHERLANDS | NCPeH.UC_1          | ERROR_PI_NO_MATCH |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_042
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21981-02 @AFO-ID:A_22500-01 @AFO-ID:A_21982-01
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_22513-02
  @DESCRIPTION
  # Weitere Tags, team-intern
  @CI
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
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_043
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21981-02 @AFO-ID:A_22500-01 @AFO-ID:A_21982-01
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_22513-02
  @DESCRIPTION
  # Weitere Tags, team-intern
  @CI
  Szenario: NCPeH - PSA PerfRep Messung bei FindDocuments (NCPeH.UC_2) mit Fehler ERROR_NO_CONSENT
    Für das Reporting des NCPeH.UC_2 wird der Testfall NCP1_IOP_PSA_EPA_031
      "NCPeH - PSA ePA-IOP UC2 Befugnis für EU Mitgliedsstaat fehlt bei FindDocuments"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_NO_CONSENT" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP1_IOP_PSA_EPA_031 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases   | ehdsiErrorcodes       |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2 | null,ERROR_NO_CONSENT |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_PerfRep_044
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01 @AF-ID:AF_10122-01
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21981-02 @AFO-ID:A_22500-01 @AFO-ID:A_21982-01
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  @CI
  Szenario: NCPeH - PSA PerfRep Messung bei RetrieveDocuments (NCPeH.UC_3) mit Fehler ERROR_GENERIC_DOCUMENT_MISSING
    Für das Reporting des NCPeH.UC_3 wird der Testfall NCP1_IOP_PSA_UC3_020
      "NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - falsche DocumentUniqueId"
    bei Bedarf ausgeführt bzw. dessen bereits erfolgte Ausführung wird nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element eCode im Message Feld auf den Fehlercode
    "ERROR_GENERIC_DOCUMENT_MISSING" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP1_IOP_PSA_UC3_020 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country     | PerformanceUsecases              | ehdsiErrorcodes                          |
      | NETHERLANDS | NCPeH.UC_1,NCPeH.UC_2,NCPeH.UC_3 | null,null,ERROR_GENERIC_DOCUMENT_MISSING |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration
