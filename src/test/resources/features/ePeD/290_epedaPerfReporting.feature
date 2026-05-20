#
# Copyright 2025 gematik GmbH
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
@eHDSI-Szenario:ePeDA_PerfRep
Funktionalität: NCPeH ePeD-A Rohdaten Performance Reporting
  Die folgend benannten NCPeH-Anwendungsfälle basieren auf dem Anwendungsszenario "ePrescription/eDispensation Land A."
  Betrachtet werden die Themen zum Reporting der Performance Rohdaten des NCPeH-FD an den Dienst der gematik
  nach [gemSpec_Perf#Betriebsdatenerfassung v2 Spezifika NCPeH-Fachdienst],
    https://gemspec.gematik.de/docs/gemSpec/gemSpec_Perf/latest/#3.7.2:
    1. Prüfung der gemeldeten Anzahl und Plausibilität der gemessenen Dauer der Operationen NCPeH.UC_9-NCPeH.UC_12
      im Report
      - Exaktheit von Zeiten im Report kann nicht geprüft werden (nur Plausibilität anhand der eigenen Messungen)
      - NCPeH.UC_VAU2 kann nicht geprüft werden, da keine Kontrolle darüber besteht, ob ein einem Szenario ein VAU-Kanal
      aufgebaut wird
    2. Prüfung des gemeldeten Status der vier Operationen im Report
    3. Implizite Prüfung der Einhaltung der Vorgaben zum Reporting (durch erfolgreichen Import ins Data Warehouse)
  Involvierte Anwendungsfälle:
    AF_10379-* - ePeD-A - Versicherten im Behandlungsland für ePeD-A identifizieren
      gemSpec_Perf: NCPeH.UC_9, Operation "Cross_Gateway_Patient_Discovery::findIdentityByTraits (ePeD)"
    AF_10380-* - Einlösbare E-Rezepte des Versicherten aus ePeD-A auflisten
      gemSpec_Perf: NCPeH.UC_10, Operation "Cross_Gateway_Query::FindDocuments (ePeD)"
    AF_10400-* - Ausgewählte E-Rezepte abrufen
      gemSpec_Perf: NCPeH.UC_11, Operation "Cross_Gateway_Retrieve::RetrieveDocument (ePeD)"
    AF_10401-* - Abgabe von Arzneimitteln an Versicherte im Abgabeland
      gemSpec_Perf: NCPeH.UC_12, Operation "Enterprise_Document_Reliable_Interchange::ProvideAndRegisterDocumentSet-b"
    (NCPeH.UC_VAU2 ist kein eigentlicher UseCase und wird nur hilfsweise so in gemSpec_Perf bezeichnet)
  Involvierte Anforderungen:
    [gemSpec_Perf#Bearbeitungszeiten NCPeH-Fachdienst] -> Plausibilitätsprüfung (keine exakte Gleichheit)
      A_23016-* Performance - NCPeH-Fachdienst - Last- und Bearbeitungszeiten
    [gemSpec_Perf#Betriebsdatenerfassung v2 Spezifika NCPeH-Fachdienst]
      A_23011-* Performance - Betriebsdatenlieferung v2 - Spezifika NCPeH-Fachdienst - Operation
      A_23012-* Performance - Betriebsdatenlieferung v2 - Spezifika NCPeH-Fachdienst - Duration
      A_23118-* Performance - Betriebsdatenlieferung v2 - Spezifika NCPeH-Fachdienst - Message
    [gemSpec_Perf#Betriebsdatenlieferung] -> implizite Prüfung über Operations-Datawarehouse
      A_22482-* - Performance - Betriebsdatenlieferung - Erfassung von Betriebsdaten
    [gemSpec_Perf#Betriebsdatenlieferung Version 2] -> implizite Prüfung über Operations-Datawarehouse
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

# Hinweis: Test von Reporting zu NCPeH.UC_VAU2 nicht dediziert möglich, da der Abbau des VAU Kanals zum E-Rezept-FD als
# Voraussetzung für einen erneuten Aufbau nicht dediziert gesteuert oder abgewartet werden kann

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_001
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_21980-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @LongRunning
  Szenario: NCPeH - ePeD-A PerfRep Lieferung eines Leer-Reports
    Der NCPeH sendet alle 5 Minuten einen Report an den BDEv2 Dienst der gematik. Es wird das korrekte
    Senden und Verarbeiten eines Leer-Reports geprüft.
    Das Senden eines Leerreports setzt voraus, dass für KEIN Anwendungsszenario Aktivitäten zu reporten sind
    (weder ePeD-A noch PS-A).
    Es wird ebenfalls eine funktionierende Anbindung an den BDEv2 Dienst sichergestellt.

    Hinweis: Diese Prüfung ist Voraussetzung für die Durchführung der weiteren Testfälle bzgl. Reporting.

    Wenn der NCPeH für 10 Minuten Idle ist
    Dann liefert er an den BDEv2 Dienst mindestens einen leeren Performance Report

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_002
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_22429
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Selbstauskunft
    Der NCPeH sendet alle 60 Minuten eine Selbstauskunft an den BDEv2 Dienst der gematik. Es wird geprüft,
    dass die gelieferten Daten importiert werden können (indirekte Prüfung), und die gelieferten Daten
    mit den erwarteten Werten übereinstimmen.
    Die erwarteten Daten müssen zum Teil innerhalb gematik und zum Teil vom Betreiber erfragt werden.
    Zu prüfen sind folgende Werte aus der Selbstauskunft:
    1. InformationDate (60 Minuten Lieferintervall)
    2. ProductType (gematik Betrieb)
    3. ProductTypeVersion (gematik Testmanagement bzw. Produktteam)
    4. ProductVersion (Betreiber)
    Die restlichen Werte unterliegen keinen festen, kalkulierbaren Regeln und werden deshalb nicht geprüft.
    Die Lieferung der Selbstauskunft ist unabhängig von Anwendungsszenarien und braucht deshalb nur einmalig
    geprüft werden

    Angenommen die erwarteten Daten zur Selbstauskunft sind bekannt
    Wenn der NCPeH im 60 Minuten Intervall eine Selbstauskunft an den BDEv2 Dienst sendet
    Dann enthält er die mit gematik Operations und dem Betreiber abgestimmten Informationen über den NCPeH Fachdienst

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_010
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380 @AF-ID:AF_10400 @AF-ID:AF_10401
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei einer Rezeptabgabe mit CDA L3
    Es wird geprüft, dass bei einer vollständigen, erfolgreichen Rezeptabgabe mit Ausführung der vier
    Performance Use Cases NCPeH.UC_9, NCPeH.UC_10, NCPeH.UC_11 und NCPeH.UC_12 die Anzahl der UseCases jeweils 1 ist,
    die Startzeiten konsistent und die gemessenen Bearbeitungszeiten > 0 und < der Antwortzeit des NCPeH Simulators sind.
    Es wird geprüft, dass kein Fehlerstatus in den Performance UseCases enthalten ist.
    Es werden die Struktur und die Einträge des Reports mit seinen Messwerten anhand der Informationen aus dem
    Operations-Datawarehouse geprüft.
    Der Testfall NCP2_E2E_ePeDA_UC12_001 "NCPeH - ePeD-A UC12 Ein als CDA3 abgerufenes Rezept an eine versicherte Person abgeben"
    wird hierbei zu Hilfe genommen.

    Angenommen der Testfall NCP2_E2E_ePeDA_UC12_001 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country | PerformanceUsecases                            |
      | BELGIUM | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_011
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380 @AF-ID:AF_10400 @AF-ID:AF_10401
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - PSA PerfRep Messung bei einer Rezeptabgabe mit CDA L1
    Es wird geprüft, dass bei einer vollständigen, erfolgreichen Rezeptabgabe mit Ausführung der vier
    Performance Use Cases NCPeH.UC_9, NCPeH.UC_10, NCPeH.UC_11 und NCPeH.UC_12 die Anzahl der UseCases jeweils 1 ist,
    die Startzeiten konsistent und die gemessenen Bearbeitungszeiten > 0 und < der Antwortzeit des NCPeH Simulators sind.
    Es wird geprüft, dass kein Fehlerstatus in den Performance UseCases enthalten ist.
    Es werden die Struktur und die Einträge des Reports mit seinen Messwerten anhand der Informationen aus dem
    Operations-Datawarehouse geprüft.
    Der Testfall NCP2_E2E_ePeDA_UC12_002 "NCPeH - ePeD-A UC12 Ein als CDA1 abgerufenes Rezept an eine versicherte Person abgeben"
    wird hierbei zu Hilfe genommen.

    Angenommen der Testfall NCP2_E2E_ePeDA_UC12_002 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country | PerformanceUsecases                            |
      | BELGIUM | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  @TCID:NCP2_Rep_ePeDA_020
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380 @AF-ID:AF_10400 @AF-ID:AF_10401
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenariogrundriss: NCPeH - ePeD-A PerfRep Datengenerierung zu mehreren Rezept Abgabevorgängen
    Es wird geprüft, dass der NCPeH mehrfach für verschiedene EU Mitgliedsstaaten mit dem E-Rezept-FD und entsprechend
    vorhandener Berechtigung das vollständige Anwendungsszenario ePrescription/eDispensation Land A durchführen kann.

    Angenommen für die Versicherte <Patient> ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name             | PZN        | Substitutionsverbot |
      | <Medikamentname> | <PZNummer> | nein                |
    Und die versicherte Person begibt sich in dem EU-Land <Mitgliedsstaat> in eine Apotheke zum Apotheker <Apotheker>
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level <CDA_Level> durchgeführt
    Wenn der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet
    Dann erhält der EU-Apotheker eine Information zur erfolgreichen Verarbeitung der Dispensierinformation zurück
    Und die versicherte Person kann im FdV die Dispensierinformationen über das letzte dispensierte E-Rezept abrufen
    # Testschleife über 20 vollständige Rezeptabgaben, abwechselnd CDA L3 und L1 über 3 verschiedene Länder
    # und drei verschiedene Patienten. Die Patienten werden im Rahmen der Implementierung konfigurativ aufgelöst
    Beispiele:
      | Patient   | Mitgliedsstaat | Apotheker                    | CDA_Level | Medikamentname                                 | PZNummer |
      | versPers1 | BELGIUM        | Dr. Elisabeth Pauwels        | 3         | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
      | versPers2 | LATVIA         | J\uc481nis Liepi\uc586\uc5a1 | 1         | Grippostad C Hartkapseln                       | 00571748 |
      | versPers3 | LITHUANIA      | Urte \uc5bdili\uc6abt\uc497  | 3         | Ibuflam 600mg                                  | 06313390 |
      | versPers3 | BELGIUM        | Dr. Elisabeth Pauwels        | 1         | Etoricoxib beta 90mg Filmtabletten             | 12444251 |
      | versPers1 | LATVIA         | J\uc481nis Liepi\uc586\uc5a1 | 3         | Aspirin N 100mg                                | 05387239 |
      | versPers2 | LITHUANIA      | Urte \uc5bdili\uc6abt\uc497  | 1         | Aciclovir HEUMANN Creme                        | 06977954 |
      | versPers2 | BELGIUM        | Dr. Elisabeth Pauwels        | 3         | Paracetamol 500 mg IPA Tabletten               | 12747135 |
      | versPers3 | LATVIA         | J\uc481nis Liepi\uc586\uc5a1 | 1         | selen-Loges 200\uc2b5g                         | 17150382 |
      | versPers1 | LITHUANIA      | Urte \uc5bdili\uc6abt\uc497  | 3         | Grippostad C Hartkapseln                       | 00571748 |
      | versPers1 | BELGIUM        | Dr. Elisabeth Pauwels        | 1         | Ibuflam 600mg                                  | 06313390 |
      | versPers2 | LATVIA         | J\uc481nis Liepi\uc586\uc5a1 | 3         | Etoricoxib beta 90mg Filmtabletten             | 12444251 |
      | versPers3 | LITHUANIA      | Urte \uc5bdili\uc6abt\uc497  | 1         | Aspirin N 100mg                                | 05387239 |
      | versPers1 | BELGIUM        | Dr. Elisabeth Pauwels        | 3         | Aciclovir HEUMANN Creme                        | 06977954 |
      | versPers2 | LATVIA         | J\uc481nis Liepi\uc586\uc5a1 | 1         | Paracetamol 500 mg IPA Tabletten               | 12747135 |
      | versPers3 | LITHUANIA      | Urte \uc5bdili\uc6abt\uc497  | 3         | selen-Loges 200\uc2b5g                         | 17150382 |
      | versPers1 | BELGIUM        | Dr. Elisabeth Pauwels        | 1         | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
      | versPers2 | LATVIA         | J\uc481nis Liepi\uc586\uc5a1 | 3         | Grippostad C Hartkapseln                       | 00571748 |
      | versPers3 | LITHUANIA      | Urte \uc5bdili\uc6abt\uc497  | 1         | Ibuflam 600mg                                  | 06313390 |
      | versPers1 | BELGIUM        | Dr. Elisabeth Pauwels        | 3         | Etoricoxib beta 90mg Filmtabletten             | 12444251 |
      | versPers2 | LATVIA         | J\uc481nis Liepi\uc586\uc5a1 | 1         | Aspirin N 100mg                                | 05387239 |
    # Jānis Liepiņš
    # Urte Žiliūtė

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_021
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380 @AF-ID:AF_10400 @AF-ID:AF_10401
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei mehreren Rezept Abgabevorgängen
    Es wird geprüft, dass bei 20 vollständigen und erfolgreichen Rezept-Abgabevorgängen innerhalb von 10 Minuten
    die Anzahl der UseCases jeweils 20 ist und die gemessenen Zeiten > 0 und < der Antwortzeit
    des NCPeH Simulators sind. Es wird geprüft, dass die korrekten Status übertragen wurden.
    Die gemessenen Werte wurden in das Auswertungssystem übernommen und können in Grafana angezeigt werden.

    Angenommen der Testfall NCP1_E2E_PSA_PerfRep_030 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Testfall sind bekannt
      | country   | PerformanceUsecases                            |
      | BELGIUM   | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LATVIA    | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LITHUANIA | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | BELGIUM   | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LATVIA    | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LITHUANIA | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | BELGIUM   | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LATVIA    | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LITHUANIA | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | BELGIUM   | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LATVIA    | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LITHUANIA | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | BELGIUM   | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LATVIA    | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LITHUANIA | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | BELGIUM   | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LATVIA    | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LITHUANIA | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | BELGIUM   | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
      | LATVIA    | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld keinen eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_030
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei Patient Identification (NCPeH.UC_9) mit Fehler ERROR_PI_NO_MATCH
    Für das Reporting des NCPeH.UC_9 wird der zuvor ausgeführte Testfall NCP2_E2E_ePeDA_UC9_010
    "NCPeH - ePeD-A UC9 Identifikation eines Versicherten - Suche findet kein E-Rezept"
    über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "ERROR_PI_NO_MATCH" geprüft (entsprechend dem acknowledgementDetail.Code der Response des Referenz-Testfalls).

    Angenommen der Testfall NCP2_E2E_ePeDA_UC9_010 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases | ehdsiErrorcodes   |
      | BELGIUM | NCPeH.UC_9          | ERROR_PI_NO_MATCH |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_031
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei Patient Identification (NCPeH.UC_9) mit Fehler ERROR_PI_GENERIC
    Für das Reporting des NCPeH.UC_9 wird der zuvor ausgeführte Testfall NCP2_IOP_ePeDA_ERP_010
    "NCPeH - ePeD-A eRP-IOP Löschen einer Berechtigung vor der Patient Identification"
    über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "ERROR_PI_GENERIC" geprüft (entsprechend dem acknowledgementDetail.Code der Response des Referenz-Testfalls).

    Angenommen der Testfall NCP2_IOP_ePeDA_ERP_010 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases | ehdsiErrorcodes  |
      | BELGIUM | NCPeH.UC_9          | ERROR_PI_GENERIC |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_032
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei Patient Identification (NCPeH.UC_9) mit SOAP Fehler Invalid Security Token
    Für das Reporting des NCPeH.UC_9 wird der zuvor ausgeführte Testfall NCP2_IOP_ePeDA_NCP_060
    "NCPeH - ePeD-A NCP-IOP Patient Identification mit Mismatch CountryCode zwischen TLS-Zert und IdA-Signer-Zert"
    über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "Invalid Security Token" geprüft. (Hinweis: der Inhalt vom SubCode im SOAP Fault enthält den eHDSI Fehlercode).

    Angenommen der Testfall NCP2_IOP_ePeDA_NCP_060 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases | ehdsiErrorcodes          |
      | BELGIUM | NCPeH.UC_9          | Invalid Security Token |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    # Hinweis: erwarteter http Statuscode 4xx, weil der Client Verursacher des Fehlers ist
    # (http Client sendet inkonsistente Assertion)
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 4xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_040
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei FindDocuments (NCPeH.UC_10) mit Fehler ERROR_EP_NOT_FOUND
    Für das Reporting des NCPeH.UC_10 wird der zuvor ausgeführte Testfall NCP2_E2E_ePeDA_UC10_012
    "NCPeH - ePeD-A UC10 Auflisten eines einlösbaren E-Rezeptes - kein Rezept verfügbar"
    über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "ERROR_EP_NOT_FOUND" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP2_E2E_ePeDA_UC10_012 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases    | ehdsiErrorcodes         |
      | BELGIUM | NCPeH.UC_9,NCPeH.UC_10 | null,ERROR_EP_NOT_FOUND |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_041
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei FindDocuments (NCPeH.UC_10) mit Fehler ERROR_NO_CONSENT
    Für das Reporting des NCPeH.UC_10 wird der zuvor ausgeführte Testfall NCP2_IOP_ePeDA_ERP_021
    "NCPeH - ePeD-A eRP-IOP Erneuern einer Berechtigung vor Auflisten der Rezepte"
    über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "ERROR_NO_CONSENT" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP2_IOP_ePeDA_ERP_021 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases    | ehdsiErrorcodes       |
      | BELGIUM | NCPeH.UC_9,NCPeH.UC_10 | null,ERROR_NO_CONSENT |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_042
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei FindDocuments (NCPeH.UC_10) mit Fehler ERROR_EP_GENERIC
    Für das Reporting des NCPeH.UC_10 wird der zuvor ausgeführte Testfall NCP2_IOP_ePeDA_NCP_021
    "NCPeH - ePeD-A NCP-IOP Auflisten der Rezepte und IdA mit unzureichenden Permission Codes"
    über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "ERROR_EP_GENERIC" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP2_IOP_ePeDA_NCP_021 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases    | ehdsiErrorcodes       |
      | BELGIUM | NCPeH.UC_9,NCPeH.UC_10 | null,ERROR_EP_GENERIC |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_043
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei FindDocuments (NCPeH.UC_10) mit SOAP Fehler HP Missing Attributes
    Für das Reporting des NCPeH.UC_10 wird der zuvor ausgeführte Testfall NCP2_IOP_ePeDA_NCP_040
    "NCPeH - ePeD-A NCP-IOP Auflisten der Rezepte wobei die NameId in der TRC Assertion nicht zur Identity Assertion
      passt" über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "HP Missing Attributes" geprüft (Hinweis: der Inhalt vom SubCode im SOAP Fault enthält den eHDSI Fehlercode).

    Angenommen der Testfall NCP2_IOP_ePeDA_NCP_040 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases    | ehdsiErrorcodes              |
      | BELGIUM | NCPeH.UC_9,NCPeH.UC_10 | null,HP Missing Attributes |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    # Hinweis: erwarteter http Statuscode 4xx, weil der Client Verursacher des Fehlers ist
    # (http Client sendet inkonsistente Assertions)
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 4xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_050
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380 @AF-ID:AF_10400
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei RetrieveDocuments (NCPeH.UC_11) mit Fehler ERROR_EP_NOT_FOUND
  Für das Reporting des NCPeH.UC_11 wird der zuvor ausgeführte Testfall NCP2_E2E_ePeDA_UC11_020
  "NCPeH - ePeD-A UC11 Rezepte abrufen - ein von zwei Rezepten wurde gelöscht"
  über seine bereitgestellten Messdaten nachgenutzt.
  Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
  "ERROR_EP_NOT_FOUND" geprüft (entsprechend der erwarteten RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP2_E2E_ePeDA_UC11_020 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases                | ehdsiErrorcodes              |
      | BELGIUM | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11 | null,null,ERROR_EP_NOT_FOUND |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_060
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380 @AF-ID:AF_10400  @AF-ID:AF_10401
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei Dispense (NCPeH.UC_12) mit Fehler ERROR_ED_EPRESCRIPTION_NOT_IDENTIFIABLE
    Für das Reporting des NCPeH.UC_12 wird der zuvor ausgeführte Testfall NCP2_IOP_ePeDA_UC12_022
    "NCPeH - ePeD-A UC12 Rezepte abgeben - Versuch einer doppelten Dispensierung eines Rezeptes innerhalb einer Operation"
    über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "ERROR_ED_EPRESCRIPTION_NOT_IDENTIFIABLE" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP2_IOP_ePeDA_UC12_022 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country | PerformanceUsecases                            | ehdsiErrorcodes                                        |
      | BELGIUM | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 | null,null,null,ERROR_ED_EPRESCRIPTION_NOT_IDENTIFIABLE |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

  # PET/Polarion Tags
  @TCID:NCP2_Rep_ePeDA_061
  @PRODUKT:NCPeH_ePeDA
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379 @AF-ID:AF_10380 @AF-ID:AF_10400  @AF-ID:AF_10401
  @AFO-ID:A_22482-02 @AFO-ID:A_22002 @AFO-ID:A_22004 @AFO-ID:A_21975-01 @AFO-ID:A_22001-02
  @AFO-ID:A_23012 @AFO-ID:A_23118-03
  @AFO-ID:A_23016-03
  @DESCRIPTION
  # Weitere Tags, team-intern
  Szenario: NCPeH - ePeD-A PerfRep Messung bei Dispense (NCPeH.UC_12) mit Fehler ERROR_ED_GENERIC
    Für das Reporting des NCPeH.UC_12 wird der zuvor ausgeführte Testfall NCP2_IOP_ePeDA_NCP_013
    "NCPeH - ePeD-A eRP-IOP Dispensierung (UC12) mit einer ungültigen Prüfziffer in der KVNR in der TRC Assertion"
    über seine bereitgestellten Messdaten nachgenutzt.
    Im Reporting wird neben den restlichen Messergebnissen das Element "err" im Message Feld auf den Fehlercode
    "ERROR_ED_GENERIC" geprüft (entsprechend der RegistryResponse des Referenz-Testfalls).

    Angenommen der Testfall NCP2_IOP_ePeDA_NCP_013 wurde zu einem bekannten Zeitpunkt ausgeführt
    Und die erwarteten Daten der Performance Use Cases aus dem Negativ Testfall sind bekannt
      | country  | PerformanceUsecases                            | ehdsiErrorcodes                 |
      | BULGARIA | NCPeH.UC_9,NCPeH.UC_10,NCPeH.UC_11,NCPeH.UC_12 | null,null,null,ERROR_ED_GENERIC |
    Wenn zu dem Zeitintervall zeitlich passende Performance Reports an den BDEv2 Dienst geliefert wurden
    Dann enthält der Performance Report je einen Eintrag für jeden erwarteten Performance Use Case
    Und das Statusfeld enthält je erwartetem Eintrag einen Statuscode der Form 2xx
    Und die Bearbeitungszeiten der erwarteten Performance Report Einträge sind größer 0 und kleiner als die lokal gemessene Bearbeitungszeit
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld den erwarteten eHDSI Fehlercode
    Und jeder erwartete Performance Report Eintrag enthält im Message Feld einen konsistenten Wert zur BackendDuration

