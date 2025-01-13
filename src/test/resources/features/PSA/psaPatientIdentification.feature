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
Funktionalität: NCPeH PSA Use Case 1 - Versicherten im Behandlungsland identifizieren
  AF_10107 - Versicherten im Behandlungsland identifizieren
    In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
    Leistungserbringer ("LE-EU"), um sich von ihm behandeln zu lassen (Behandlungskontext).
    Die versicherte Person möchte im Rahmen einer Behandlung dem LE-EU ihre elektronische
    Patientenkurzakte in Form eines Patient Summaries zu Verfügung stellen.
      (eHDSI Anwendungsszenario "Patient Summary Country A")
    Dazu identifiziert ein europäischer Leistungserbringer "LE-EU" im ersten Schritt die versicherte
    Person mit Hilfe ihrer Versichertennummer (KVNR) und einem Zugriffscode (auch "Accesscode").
    Den Zugriffscode erhält die versicherte Person im Zuge ihrer Befugniserteilung für den Zugriff
    des EU-Landes auf ihr ePA-Aktenkonto (mit Hilfe ihres ePA-FdVs). KVNR und Zugriffscode werden
    von der versicherten Person dem LE-EU oder seiner Fachkraft in der EU-LEI (Praxis) übergeben.
    Als Antwort soll der LE-EU weitere persönliche Daten der versicherten Person erhalten, die er mit
    Hilfe der EHIC (Rückseite der eGK), elektronisch im ePA-FdV der versicherten Personn (privat versichert)
    oder des Ausweises des Patienten überprüfen kann.
    Die auszugebenden Daten sind die KVNR (konkateniert mit dem Zugriffscode), Vorname & Name der
    versicherten Person und das Geburtsdatum.
    Mit Hilfe des Abgleichs der Daten der versicherten Person muss der LE-EU sicherstellen, dass er
    in den nachfolgenden Schritten auf die korrekten Versichertendaten zugreifen wird.
    Aktivität des NCPeH-FD:
    Anhand der im Request des NCPeH Land B übergebenen Daten sucht der NCPeH-FD im ePA Aktenkonto
    das ePKA-Dokument der versicherten Person, lädt das Dokument aus der Akte und extrahiert
    von dort die persönlichen Daten der versicherten Person. Die Daten werden mit der Antwort
    an den NCPeH Land B an den LE-EU ausgegeben.
    Der NCPeH-FD kann im ePA Aktenkonto nur ein (oder kein) ePKA-Dokument finden.

  Grundlage:
    Angenommen die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY hat ein aktives ePA Konto
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
  @NCPEH-RESPONSE-UC1:PRPA_IN201306UV02_4
  @eHDSI-Szenario:PSA_PatIdentification
  @E2E
  @CI
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - Suche findet kein ePKA Dokument
    Der LE-EU startet die Patient Identification mit gültiger KVNR und erteilter Berechtigung.
    Es existiert jedoch kein ePKA-Dokument in dem Konto, so dass die Suche nach dem Dokument fehlschlägt
    (siehe TUC_NCPeH_013).
    (Grund des Fehlens des Dokumentes ist egal)
    Der NCPeH-FD beantwortet den XCPD-Request entsprechend TUC_NCPeH_013 mit einem Fehlercode AnswerNotAvailable
    und dem acknowledgementDetail ERROR_PI_NO_MATCH.
    (entsprechend Fehlerkorrektur im Change C_11967 zum NCPeH-FD v2.0)

    Angenommen in dem ePA Konto der versicherten Person ist kein ePKA Dokument vorhanden
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_NO_MATCH
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext "Patient Identification Error"
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "No match with an existing patient."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC1_011
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @NCPEH-RESPONSE-UC1:PRPA_IN201306UV02_43
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  @CI
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - ePKA Dokument nur mit DPE Composition
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA mit gültiger,
    berechtigter KVNR ab.
    Es ist jedoch nur ein Dokument mit DPE und ohne Komposition KBV_PR_MIO_NFD_Composition_NFD, so dass
    eine Schemavalidierung bzw. QES-Validierung der Teil-Komposition KBV_PR_MIO_NFD_Composition_NFD
    nicht möglich ist.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_014 und TAB_NCPeH_Abruf_ePKA-MIO_Fehlerbehandlung_Zusammenhang_XCPD
    zurück.

    Angenommen in dem Konto befindet sich ein valides ePKA-Dokument, in dem nur eine DPE Composition enthalten ist
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext "Patient Identification Error"
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The patient identity information in Germany is incomplete or defective."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC1_012
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @NCPEH-RESPONSE-UC1:PRPA_IN201306UV02_42
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  @CI
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - ePKA Dokument ohne Patient.birthDate.value
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA mit gültiger,
    berechtigter KVNR ab.
    In dem ePKA-Dokument fehlt jedoch das Geburtsdatum (KBV_PR_MIO_NFD_Patient_NFD:Patient.birthDate.value),
    was nach ePKA v1.0.0 fehlen kann (Kardinalität 0..1). Statt dessen ist die FHIR-Extension "data-absent-reason"
    im Element birthDate hinterlegt (Patient.birthDate.extension:data-absent-reason).
      (siehe https://simplifier.net/packages/kbv.mio.patientenkurzakte/1.0.0/files/611117/~details, "Patient.birthDate.value")
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_017 und gemSpec_NCPeH_FD#TAB_NCPeH_Fehlen_Geburtstag_Fehler_XCPD_Response
    zurück (entsprechend Fehlerkorrektur im Change C_11967 zum NCPeH-FD v2.0).

    Hinweis: reduzierte Priorität, da die Wahrscheinlichkeit des Auftretens als sehr gering erachtet wird.

    Angenommen in dem Konto befindet sich ein valides ePKA-Dokument, in dem das Geburtsdatum der versicherten Person fehlt
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext "Patient Identification Error"
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The patient's health record data ist not complete, the date of birth is missing."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC1_020
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @NCPEH-RESPONSE-UC1:PRPA_IN201306UV02_41
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  @CI
  Szenario: NCPeH - PSA UC1 Identifikation eines Versicherten - ePKA Dokumentenschema verletzt
    Der LE-EU startet die Patient Identification mit gültiger KVNR und erteilter Berechtigung.
    Das Schema des ePKA Dokumentes ist jedoch fehlerhaft (das Pflicht-Element "gender" im NFD-Datensatz fehlt)
    und das Dokument darf nicht verwertet werden.
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_014#TAB_NCPeH_Abruf_ePKA-MIO_Fehlerbehandlung_Zusammenhang_XCPD
    zurück.

    Angenommen das ePKA Dokument der versicherten Person ist nicht konform zum FHIR Schema des ePKA MIO
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext "Patient Identification Error"
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The patient identity information in Germany is defective."
