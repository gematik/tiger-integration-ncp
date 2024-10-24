#
# Copyright (c) 2024. gematik GmbH
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

#language: de
#noinspection NonAsciiCharacters,SpellCheckingInspection
@PRODUKT:NCPeH_FD
Funktionalität: NCPeH PSA Use Case 3 - Versichertendatensatz abrufen
  AF_10122 - Versichertendatensatz abrufen
    In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
    Leistungserbringer ("LE-EU"), um sich von ihm behandeln zu lassen (Behandlungskontext).
    Die versicherte Person möchte im Rahmen der Behandlung dem LE-EU ihre elektronische
    Patientenkurzakte in Form eines Patient Summaries zu Verfügung stellen.
      (eHDSI Anwendungsszenario "Patient Summary Country A")
    Die versicherte Person wurde vom LE-EU bereits erfolgreich identifiziert (Use Case 1 - siehe
    patientIdentification.feature) und die Liste der verfügbaren Dokumente wurde bereits abgerufen
    (Use Case 2 - siehe psaFindDocuments.feature).
    In hier betrachteten Use Case 3 soll der LE-EU das ePKA-Dokument im Aktenkonto der versicherten
    Person als Patient Summary in Form eines strukturierten Dokumentes (CDA Level 3) abrufen.
    Aktivität des NCPeH-FD:
    Anhand der im Request des NCPeH Land B übergebenen Daten lädt NCPeH-FD das ePKA-Dokument aus dem
    ePA-Aktenkonto der versicherten Person, transformiert das Dokument in das von der eHDSI/myHealth@EU
    definierte Pivot-Dokument für CDA Level 3 und gibt dieses in seiner Antwort an den NCPeH des Landes
    B zurück.

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

    Hinweis: es erfolgt kein umfassender Test der Transformation und Transkodierung,
      Es wird nur rudimentär das Vorhandensein der persönliche Daten des/der Versicherten im Ergebnisdokument geprüft.

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und ein LE-DE hat ein ePKA-Dokument in das ePA-Konto der versicherten Person eingestellt, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff des EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Und der LE-EU führt danach erfolgreich eine Suche des Patient Summary der versicherten Person durch
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU ein Dokument der versicherten Person zurück
    Und das Dokument ist im Format CDA Level 3
    Und das strukturierte Dokument enthält die persönlichen Daten der versicherten Person

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_010
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Negativ
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
    der verfügbaren Dokumente das ePKA Dokument geändert. (zeitliche Koinzidenz, von einem befugten LE-DE)
    Das Schema des ePKA Dokumentes ist jedoch fehlerhaft (das Pflicht-Element "gender" im NFD-Datensatz fehlt)
    und das Dokument darf nicht verwertet werden.
    Der NCPeH gibt lt. TUC_NCPeH_014#TAB_NCPeH_Abruf_ePKA-MIO_Fehlerbehandlung_Zusammenhang_PS
    den Fehler ERROR_GENERIC_DOCUMENT_MISSING zurück.
      (siehe https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.2.2)

    Hinweis: reduzierte Testfallpriorität: 3, wegen der zeitlichen Rahmenbedingungen, die ein solches
      Fehlerauftreten erschweren.

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und ein LE-DE hat ein ePKA-Dokument in das ePA-Konto der versicherten Person eingestellt, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff des EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Wenn das ePKA Dokument der versicherten Person von einem LE-DE ersetzt wird, dass nicht konform zum FHIR Schema des ePKA MIO ist
    Und der LE-EU führt danach erfolgreich eine Suche des Patient Summary der versicherten Person durch
    Und der LE-EU ruft das Patient Summary Dokument der versicherten Person als CDA Level 3 ab
    Dann erhält der LE-EU kein Dokument der versicherten Person zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_GENERIC_DOCUMENT_MISSING

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_011
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
    Der NCPeH gibt lt. TUC_NCPeH_014#TAB_NCPeH_Abruf_ePKA-MIO_Fehlerbehandlung_Zusammenhang_PS
    den Fehler ERROR_GENERIC_DOCUMENT_MISSING zurück.
      (siehe https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.2.2)

    Hinweis: reduzierte Testfallpriorität: 2. wegen der zeitlichen Rahmenbedingungen, die ein solches
      Auftreten erschweren.
      Da für diesen Fall des Dokumentenabrufs an anderer Schnittstelle (XCA, nicht XCPD) stattfindet
      und gemäß TUC_NCPeH_014 ein eigener Fehlercode vorgesehen wurde, kann die Priorität nicht weiter
      gesenkt werden.

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und ein LE-DE hat ein ePKA-Dokument in das ePA-Konto der versicherten Person eingestellt, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff des EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Wenn das ePKA Dokument der versicherten Person von einem LE-DE ersetzt wird, in dem keine NFD Composition enthalten ist
    Und der LE-EU führt danach erfolgreich eine Suche des Patient Summary der versicherten Person durch
    Und der LE-EU ruft das Patient Summary Dokument der versicherten Person als CDA Level 3 ab
    Dann erhält der LE-EU kein Dokument der versicherten Person zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_GENERIC_DOCUMENT_MISSING

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
    trat eine Verfälschung oder Änderung der DocumentUniqueId auf (z. B. Update des Dokumentes von einem
    Arzt in DE). Alternativ könnte das Dokument zwischenzeitlich gelöscht worden sein, so das die
    DocumentUniqueId nicht mehr gefunden wird.
    Das ePA Aktensystem meldet ResponseStatus Failure und gibt den Fehlercode "XDSDocumentUniqueIdError" zurück.
    Situationen mit vergleichbarer Reaktion des Aktensystems:
    - Das Dokument wurde vor dem Abruf gelöscht
    - Das Dokument wurde vor dem Abruf überschrieben
    Der NCPeH gibt lt. TUC_NCPeH_014#TAB_NCPeH_Abruf_ePKA-MIO_Fehlerbehandlung_Zusammenhang_PS
    den Fehler ERROR_GENERIC_DOCUMENT_MISSING zurück.
      (siehe https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.2.2)

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und ein LE-DE hat ein ePKA-Dokument in das ePA-Konto der versicherten Person eingestellt, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff des EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Und der LE-EU hat erfolgreich eine Suche des Patient Summary der versicherten Person durchgeführt
    Wenn ein LE-DE das ePKA Dokument der versicherten Person überschreibt
    Und der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU kein Dokument der versicherten Person zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_GENERIC_DOCUMENT_MISSING

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_021
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Negativ
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
    im Use Case #3 am NCPeH-FD trat eine Verfälschung der RepositoryUniqueId auf.
    Der NCPeH-FD ruft den NCPeH_TUC_014 zum Abrufen des ePKA Dokumentes auf. Abhängig von der Nutzung
    einer internen Aktenkontosession muss das bereitstellende Aktensystem ermittelt werden.
    Der Abruf beim ePA Aktensystem schlägt auf Grund der falschen RepositoryUniqueId fehl.
    Erwartete Fehlermeldung aus dem NCPeH_TUC_014 ist ERROR_GENERIC_DOCUMENT_MISSING
      (siehe https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/#6.2.2,
      TAB_NCPeH_Abruf_ePKA-MIO_Fehlerbehandlung_Zusammenhang_PS)
    Evtl. wird durch die interne Aktenkontosession (bei Caching der RepositoryUniqueId) ohne Interaktion
    mit dem Aktensystem bereits die Diskrepanz zu den Daten der vorher durchlaufenen Use Cases erkannt.
    In diesem Fall wird trotzdem die gleiche Fehlermeldung erwartet.
    Vom ePA Aktensystem wird ein RegistryError Element mit errorCode XDSUnknownRepositoryId erwartet.
      (https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4.1)

    Hinweis: reduzierte Testfallpriorität: 3, da die Wahrscheinlichkeit des Auftretens gering ist

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und ein LE-DE hat ein ePKA-Dokument in das ePA-Konto der versicherten Person eingestellt, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff des EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Und der LE-EU hat erfolgreich eine Suche des Patient Summary der versicherten Person durchgeführt
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 mit einer falschen RepositoryUniqueId abruft
    Dann erhält der LE-EU kein Dokument der versicherten Person zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_GENERIC_DOCUMENT_MISSING

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_030
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10107 @AF-ID:AF_1012
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  @CI
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - fehlende ePKA Dokumentsignatur
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Die Signatur des ansonsten korrekten ePKA Dokumentes fehlt. Das Dokument muss trotzdem erfolgreich
    verarbeitet werden.

    Hinweis: prüft die Datenvariante sowohl für Patient Identification als auf DocumentRetrieve im gleichen Szenario

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und ein LE-DE hat ein ePKA-Dokument ohne Signatur in das ePA-Konto der versicherten Person eingestellt
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff des EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Und der LE-EU hat erfolgreich eine Suche des Patient Summary der versicherten Person durchgeführt
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU ein Dokument der versicherten Person zurück
    Und das Dokument ist im Format CDA Level 3
    Und das strukturierte Dokument enthält die persönlichen Daten der versicherten Person

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC3_031
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv
  @AF-ID:AF_10107 @AF-ID:AF_1012
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  @CI
  Szenario: NCPeH - PSA UC3 Patient Summary als strukturiertes Dokument abrufen - mathematisch fehlerhafte ePKA Dokumentsignatur
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Die Signatur des ansonsten korrekten ePKA Dokumentes ist mathematisch nicht korrekt. Das Dokument
    muss trotzdem erfolgreich verarbeitet werden.

    Hinweis: prüft die Datenvariante sowohl für Patient Identification als auf DocumentRetrieve im gleichen Szenario

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und ein LE-DE hat ein ePKA-Dokument mit einer mathematisch ungültigen Signatur in das ePA-Konto der versicherten Person eingestellt
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff des EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Und der LE-EU hat erfolgreich eine Suche des Patient Summary der versicherten Person durchgeführt
    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU ein Dokument der versicherten Person zurück
    Und das Dokument ist im Format CDA Level 3
    Und das strukturierte Dokument enthält die persönlichen Daten der versicherten Person

