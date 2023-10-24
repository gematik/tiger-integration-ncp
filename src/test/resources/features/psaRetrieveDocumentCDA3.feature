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
Funktionalität: NCPeH PSA Use Case 3 - Versichertendatensatz abrufen
  AF_10122 - Versichertendatensatz abrufen
  In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
  Leistungserbringer ("LE-EU"), um sich von ihm behandeln zu lassen (Behandlungskontext).
  Der Versicherte möchte im Rahmen der Behandlung dem LE-EU seine elektronische Patientenkurzakte in
  zu Form eines Patient Summaries zu Verfügung stellen.
  (eHDSI Anwendungsszenario "Patient Summary Country A")
  Im Geradeaus-Fall wurde der Versicherte vom LE-EU bereits erfolgreich identifiziert (Use Case #1 -
  siehe patientIdentification.feature) und die Liste der verfügbaren Dokumente wurde bereits abgerufen
  (Use Case #2 - siehe psaFindDocuments.feature).
  In folgenden Use Case #3 soll der LE-EU das ePKA-Dokument im Aktenkonto der versicherten Person als
  Patient Summary in Form eines strukturierten Dokumentes (CDA Level 3) abrufen.

  Grundlage:
    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Und der LE-EU hat erfolgreich Find Documents zum Patient Summary der versicherten Person durchgeführt

  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_UC3_001
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10122
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @E2E
  @CI
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen
    Im Aktenkonto des Versicherten ist ein ePKA Dokument (als Datenbasis für das Patient Summary) vorhanden.
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Der LE-EU ruft das verfügbare, strukturierte Patient Summary Dokument ab. Als Antwort erhält der LE-EU
    ein Patient Summary Dokument im Format CDA Level 3.

    Hinweis: es erfolgt kein ausreichender Test der Transformation und Transkodierung,
      Es wird nur rudimentär das Vorhandensein der persönliche Daten des/der Versicherten im Ergebnisdokument geprüft.
      Falls automatisiert möglich, wird ein Schematron des CDA-Dokumentes durchgeführt (Gazelle-Tools,
      z.B. schematron validator)
    # Falls weitergehende Prüfungen von Transformation und Transkodierung erfolgen sollten, so sind
    # diese in einem separaten feature-File abzuhandeln

    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU ein Dokument der versicherten Person zurück
    Und das Dokument ist im Format CDA Level 3
    Und das strukturierte Dokument enthält die persönlichen Daten der versicherten Person

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_010
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ
  @AF-ID:AF_10122
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - mathematisch ungültige ePKA Dokumentsignatur
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Im Aktenkonto des Versicherten wurde im Zeitraum zwischen der Patient Identification und dem Auflisten
    der verfügbaren Dokumente das ePKA Dokument geändert. (zeitliche Koinzidenz, wer geändert hat ist egal)
    Dabei wurde ein ePKA-Dokument mit fehlerhafter Signatur erzeugt und in der ePA eingestellt.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_014 und TUC_NCPeH_009 mit Fehlercode ERROR_PS_GENERIC zurück.

    Hinweis: reduzierte Testfallpriorität: 2. wegen der zeitlichen Rahmenbedingungen, die ein solches
      Auftreten erschweren.
      Da für diesen Fall des Dokumentenabrufs an anderer Schnittstelle (XCA, nicht XCPD) stattfindet
      und gemäß TUC_NCPeH_009 ein eigener Fehlercode vorgesehen wurde, kann die Priorität nicht weiter
      gesenkt werden.

    Angenommen die Signatur des ePKA Dokumentes der versicherten Person ist mathematisch nicht gültig
    Und der LE-EU hat erfolgreich Find Documents zum Patient Summary der versicherten Person durchgeführt
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU kein Dokument der Versicherten zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_PS_GENERIC

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_011
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ
  @AF-ID:AF_10122
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - fehlende ePKA Dokumentsignatur
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Im Aktenkonto des Versicherten wurde im Zeitraum zwischen der Patient Identification und dem Auflisten
    der verfügbaren Dokumente das ePKA Dokument geändert. (zeitliche Koinzidenz, wer geändert hat ist egal)
    Dabei wurde ein ePKA-Dokument ohne Signatur erzeugt und in der ePA eingestellt.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_014 und TUC_NCPeH_009 mit Fehlercode ERROR_PS_GENERIC zurück.

    Hinweis: reduzierte Testfallpriorität: 2, wegen der zeitlichen Rahmenbedingungen, die ein solches
      Auftreten erschweren.
      Da für diesen Fall des Dokumentenabrufs an anderer Schnittstelle (XCA, nicht XCPD) stattfindet
      und gemäß TUC_NCPeH_009 ein eigener Fehlercode vorgesehen wurde, kann die Priorität nicht weiter
      gesenkt werden.

    Angenommen die Signatur des ePKA Dokumentes der versicherten Person fehlt
    Und der LE-EU hat erfolgreich Find Documents zum Patient Summary der versicherten Person durchgeführt
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU kein Dokument der Versicherten zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_PS_GENERIC

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_012
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10122
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - nonQES ePKA Dokumentsignatur
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Im Aktenkonto des Versicherten wurde im Zeitraum zwischen der Patient Identification und dem Auflisten
    der verfügbaren Dokumente das ePKA Dokument geändert. (zeitliche Koinzidenz, wer geändert hat ist egal)
    Dabei wurde ein ePKA-Dokument mit nonQES-Signatur erzeugt und in der ePA eingestellt.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_014 und TUC_NCPeH_009 mit Fehlercode ERROR_PS_GENERIC zurück.

    Hinweis: reduzierte Testfallpriorität: 2, wegen der zeitlichen Rahmenbedingungen, die ein solches
      Auftreten erschweren.
      Da für diesen Fall des Dokumentenabrufs an anderer Schnittstelle (XCA, nicht XCPD) stattfindet
      und gemäß TUC_NCPeH_009 ein eigener Fehlercode vorgesehen wurde, kann die Priorität nicht weiter
      gesenkt werden.

    Angenommen das ePKA-Dokument wurde mit einem nonQES-Zertifikat des LE-DE signiert
    Und der LE-EU hat erfolgreich Find Documents zum Patient Summary der versicherten Person durchgeführt
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU kein Dokument der Versicherten zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_PS_GENERIC

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_013
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ
  @AF-ID:AF_10122
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - ePKA Dokumentenschema verletzt
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Im Aktenkonto des Versicherten wurde im Zeitraum zwischen der Patient Identification und dem Auflisten
    der verfügbaren Dokumente das ePKA Dokument geändert. (zeitliche Koinzidenz, wer geändert hat ist egal)
    Dabei wurde ein ePKA-Dokument ohne Signatur erzeugt und in der ePA eingestellt.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_014 und TUC_NCPeH_009 mit Fehlercode ERROR_PS_GENERIC zurück.

    Hinweis: reduzierte Testfallpriorität: 2, wegen der zeitlichen Rahmenbedingungen, die ein solches
      Auftreten erschweren.
      Da für diesen Fall des Dokumentenabrufs an anderer Schnittstelle (XCA, nicht XCPD) stattfindet
      und gemäß TUC_NCPeH_009 ein eigener Fehlercode vorgesehen wurde, kann die Priorität nicht weiter
      gesenkt werden.

    Angenommen das ePKA Dokument der versicherten Person ist nicht konform zum FHIR Schema des ePKA MIO
    Und der LE-EU hat erfolgreich Find Documents zum Patient Summary der versicherten Person durchgeführt
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU kein Dokument der Versicherten zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_PS_GENERIC

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_014
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10122
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - ePKA Dokument ohne NFD
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Im Aktenkonto des Versicherten wurde im Zeitraum zwischen der Patient Identification und dem Auflisten
    der verfügbaren Dokumente das ePKA Dokument geändert. (zeitliche Koinzidenz, wer geändert hat ist egal)
    Dabei wurde ein ePKA-Dokument ohne NFD-Inhalt erzeugt und in der ePA eingestellt.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_009 und eHDSI_NCPeH_Components#6.4 mit dem Fehlercode
    ERROR_PS_GENERIC zurück.

    Hinweis: reduzierte Testfallpriorität: 2. wegen der zeitlichen Rahmenbedingungen, die ein solches
      Auftreten erschweren.
      Da für diesen Fall des Dokumentenabrufs an anderer Schnittstelle (XCA, nicht XCPD) stattfindet
      und gemäß TUC_NCPeH_009 ein eigener Fehlercode vorgesehen wurde, kann die Priorität nicht weiter
      gesenkt werden.

    Angenommen in dem Konto befindet sich ein valides ePKA-Dokument, in dem nur eine DPE Composition enthalten ist
    Und der LE-EU hat erfolgreich Find Documents zum Patient Summary der versicherten Person durchgeführt
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU kein Dokument der Versicherten zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_PS_GENERIC

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_020
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10122
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - falsche DocumentUniqueId
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Auf dem Weg der Bereitstellung der Liste der verfügbaren Dokumente bis zum Abruf eines Dokumentes am NCPeH-FD
    trat eine Verfälschung der DocumentUniqueId auf (z. B. die DocumentUniqueId eines anderen Versicherten).
    Alternativ könnte das Dokument zwischenzeitlich gelöscht worden sein, so das die DocumentUniqueId
    nicht gefunden wird.
    Das ePA Aktensystem meldet ResponseStatus Failure und gibt den Fehlercode "XDSDocumentUniqueIdError" zurück.
    (Die Ursache der Fehlermeldung ist für den NCPeH nicht erkenntlich).
    Situationen mit vergleichbarer Reaktion des Aktensystems:
      Dokument fehlt
      DocumentUniqueId ist falsch (Dokument wurde z. B. aktualisiert)
      Berechtigung ist abgelaufen (siehe gemSpec_Dokumentenverwaltung#A_14548-*)
      AccessCode ist falsch (bzw. mit der Berechtigung abgelaufen) (siehe gemSpec_Dokumentenverwaltung#A_14548-*)
    Der NCPeH gibt entweder eine Antwort gemäß eHDSI_NCPeH_Components#6.4 mit dem Fehlercode WARNING_PS_GENERIC
    oder gemäß eHDSI_XCA_Profile#4.2.2 mit der Warnung "1102" (No Data) zurück.
    (NCPeH-FD-Intern müsste der Fehler als Sicherheitsvorfall behandelt werden.)

    Hinweis: Solange ein Widerspruch zwischen der Vorgabe in
      1. eHDSI_NCPeH_Components_Specification#6.4 (Verbot der Nutzung der Tabellen 2, 3 und 4 für neue
         Implementierungen)
      2. eHDSI_XCA_Profile#4.2.2 - Nutzung der Warnung "No Data" bei Sicherheitsvorfall, die den Fehlercode
         1102 nutzt eHDSI_NCPeH_Components_Specification#6.4 Tabelle 1 liefert dabei keine passendere
         Alternative als eine generische Warnung
    besteht, werden beide Fehlercode-Varianten als zulässig akzeptiert

    Hinweis: Alle beschriebenen Situationen sind Edge-Cases. Da aber das eHDSI_XCA_Profile hier eine
      besondere Behandlung aus Sicherheitsgründen erwartet, bleibt die Priorität auf 1.

    Wenn ein LE-DE das ePKA Dokument der versicherten Person überschreibt
    Und der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU kein Dokument der Versicherten zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit einer Warnung WARNING_PS_GENERIC oder 1102

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_021
  @STATUS:Zurückgestellt @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10122
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - falsche RepositoryUniqueId
    Im Aktenkonto des Versicherten ist ein ePKA Dokument (als Datenbasis für das Patient Summary) vorhanden.
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt. Die Session zum ePA Aktensystem ist noch geöffnet.
    Auf dem Weg der Bereitstellung der Liste der verfügbaren Dokumente bis zum Abruf eines strukturierten Dokumentes
    am NCPeH-FD trat eine Verfälschung der RepositoryUniqueId (== HomeCommunityId des Aktensystems) auf.
    Der NCPeH erkennt anhand seines Session-Handlings mit dem ePA Aktensystem, der Inhalte der TRC-Assertion
    und der RepositoryUniqueId im empfangenen CrossGateway-Retrieve Request die falsche HomeCommunityId
    und liefert eine Fehlermeldung nach TUC_NCPeH_005 mit ERROR_PS_GENERIC zurück.

    Hinweis: Zurückgestellt, da das Fehlerbild direkt an der eHDSI-Schnittstelle abgefangen werden muss.

