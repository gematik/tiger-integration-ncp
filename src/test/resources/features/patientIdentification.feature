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
Funktionalität: NCPeH PSA Use Case 1 - Versicherten im Behandlungsland identifizieren
  AF_10107 - Versicherten im Behandlungsland identifizieren
    In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
    Leistungserbringer ("LE-EU"), um sich von ihm behandeln zu lassen (Behandlungskontext).
    Der Versicherte möchte im Rahmen der Behandlung dem LE-EU seine elektronische Patientenkurzakte in
    zu Form eines Patient Summaries zu Verfügung stellen.
    (eHDSI Anwendungsszenario "Patient Summary Country A)
    Dazu identifiziert ein europäischer Leistungserbringer "LE-EU" im ersten Schritt den Patienten mit
    Hilfe seiner Versichertennummer (KVNR) und seinem Geburtsdatum. Als Antwort soll er weitere persönliche
    Daten des Patienten erhalten, die er mit Hilfe der EHIC (Rückseite der eGK) oder des Ausweises des
    Patienten überprüfen kann.

  Grundlage:
    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben

  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_UC1_001
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10107
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @E2E
  @CI
  Szenario: NCPeH - PSA UC1 Identifikation einer Versicherten bei einem LE-EU mittels KVNR und Accesscode
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der versicherten Person aus der ePKA ab, wobei
    die der Suchparameter KVNR und der Sicherheitsparameter Accesscode übergeben werden.
    Der Accesscode wird bei Erteilung der Berechtigung generiert, in der ePA hinterlegt und muss vom Versicherten
    dem LE-EU übergeben werden.
    Die zurückgegebenen Werte (in der XCPD-Response) müssen mit den konfigurierten Daten der ePKA übereinstimmen
    (KVNR, Geburtsdatum, Vorname, Nachname).

    Angenommen in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU als Antwort die KVNR mit dem AccessCode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem ePKA-Dokument der versicherten Person zurück

  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_UC1_010
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @E2E
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - Suche findet kein ePKA Dokument
    Der LE-EU startet die Patient Identification mit gültiger KVNR und erteilter Berechtigung.
    Es existiert jedoch kein ePKA-Dokument in dem Konto, so dass die Suche nach dem Dokument fehlschlägt
    (siehe TUC_NCPeH_013).
    (Grund des Fehlens des Dokumentes ist egal, hier wird es vor der Suche gelöscht)
    Der NCPeH-FD beantwortet den XCPD-Request entsprechend TUC_NCPeH_013 mit einem Fehlercode AnswerNotAvailable
    und dem acknowledgementDetail ERROR_PI_GENERIC.

    Angenommen das ePKA Dokument der versicherten Person wurde gelöscht
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "No patient demographic data could be obtained or ask the patient for access authorisation."

  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_UC1_011
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @E2E
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - ePKA Dokument nur mit DPE Composition
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA mit gültiger,
    berechtigter KVNR ab.
    Es ist jedoch nur ein Dokument mit DPE und ohne Komposition KBV_PR_MIO_NFD_Composition_NFD, so dass
    eine Schemavalidierung bzw. QES-Validierung der Teil-Komposition KBV_PR_MIO_NFD_Composition_NFD
    nicht möglich ist.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_017 und gemSpec_NCPeH_FD#TAB_NCPeH_XCPD_Fehlerbehandlung
    zurück (entsprechend CODE_QES_SCHEME_VALIDATION_INVALID aus TUC_NCPeH_014).

    Angenommen in dem Konto befindet sich ein valides ePKA-Dokument, in dem nur eine DPE Composition enthalten ist
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The patient identity information in Germany is incomplete or defective."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC1_020
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - ePKA Dokumentenschema verletzt
    Der LE-EU startet die Patient Identification mit gültiger KVNR und erteilter Berechtigung.
    Das Schema des ePKA Dokumentes ist jedoch fehlerhaft (das Pflicht-Element "gender" im NFD-Datensatz fehlt)
    und das Dokument darf nicht verwertet werden.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_017 und gemSpec_NCPeH_FD#TAB_NCPeH_XCPD_Fehlerbehandlung
    zurück (entsprechend CODE_QES_SCHEME_VALIDATION_INVALID aus TUC_NCPeH_014).

    Angenommen das ePKA Dokument der versicherten Person ist nicht konform zum FHIR Schema des ePKA MIO
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The patient identity information in Germany is incomplete or defective."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC1_021
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - mathematisch ungültige ePKA Dokumentsignatur
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA mit gültiger
    Berechtigung zu der KVNR ab.
    Die Signatur des ePKA Dokumentes ist jedoch mathematisch fehlerhaft und das Dokument darf nicht verwertet werden.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_017 und gemSpec_NCPeH_FD#TAB_NCPeH_XCPD_Fehlerbehandlung
    zurück (entsprechend CODE_QES_SCHEME_VALIDATION_INVALID aus TUC_NCPeH_014).

    Angenommen die Signatur des ePKA Dokumentes der versicherten Person ist mathematisch nicht gültig
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The patient identity information in Germany is incomplete or defective."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC1_022
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @E2E
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - nonQES ePKA Dokumentsignatur
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA mit gültiger,
    berechtigter KVNR ab.
    Die Signatur des ePKA Dokumentes ist jedoch mit einem nonQES-Zertifikat erfolgt und das Dokument darf
    nicht verwertet werden.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_017 und gemSpec_NCPeH_FD#TAB_NCPeH_XCPD_Fehlerbehandlung
    zurück (entsprechend CODE_QES_SCHEME_VALIDATION_INVALID aus TUC_NCPeH_014).

    Angenommen das ePKA-Dokument wurde mit einem nonQES-Zertifikat des LE-DE signiert
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The patient identity information in Germany is incomplete or defective."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC1_023
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @E2E
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - fehlende ePKA Dokumentsignatur
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA mit gültiger,
    berechtigter KVNR ab.
    Die Signatur des ePKA Dokumentes fehlt jedoch und das Dokument darf nicht verwertet werden.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_017 und gemSpec_NCPeH_FD#TAB_NCPeH_XCPD_Fehlerbehandlung
    zurück (entsprechend CODE_QES_SCHEME_VALIDATION_INVALID aus TUC_NCPeH_014).

    Angenommen die Signatur des ePKA Dokumentes der versicherten Person fehlt
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The patient identity information in Germany is incomplete or defective."

