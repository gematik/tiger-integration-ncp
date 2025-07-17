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
@PRODUKT:NCPeH_FD,Aktensystem_ePA,ePA_FdV_AdV,IDP-D,VZD_FHIR
Funktionalität: NCPeH PSA Interoperabilität mit der Anbindung an die Anwendung ePA
  AF_10107-* - Versicherten im Behandlungsland identifizieren
  AF_10121-* - Verfügbare Versichertendatensätze des ePKA MIO auflisten
  Die benannten Anwendungsfälle basieren auf der Anbindung an das ePA Aktensystem und der Nutzung
  der Anwendung ePA und dem darin hinterlegten ePKA-Dokument. Betrachtet werden dabei folgende
  Interoperabilitäts-Themen
  1. Das Fehlen des ePA Kontos zu einer KVNR und verschiedene Zustände eines ePA Kontos,
     auf das der NCPeH treffen kann
  2. Nutzbarkeit aller verschiedenen ePA Aktensysteme
  3. Der Umgang mit der Befugnis des NCPeH für EU Mitgliedsstaaten in der ePA
  4. VAU-Session-Handling zum ePA Aktensystem
  Entsprechende Interoperabilitätsszenarien sind zu testen.
  Nicht betrachtet wird die Transformation und Transkodierung der medizinischen Daten des ePKA-Dokumentes.

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_010
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @NCPEH-RESPONSE-UC1:PRPA_IN201306UV02_44
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  @CI
  Szenario: NCPeH - PSA ePA-IOP UC1 Identifikation eines Versicherten - KVNR UNKNOWN
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA ab, wobei
    er eine falsche KVNR angibt und diese in keinem ePA Aktensystem gefunden wird.
    Der NCPeH gibt eine Antwort entsprechend gemSpec_NCPeH_FD#TAB_NCPeH_Lokalisierung_Akte_Fehler_XCPD_Response zurück.
      (siehe https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/#4.2.7.3)
    Technischer Hintergrund:
      Ein ePA Konto kann verschiedene Zustände durchlaufen, die der NCPeH im Rahmen der
      Operation "I_Information_Service::getRecordStatus" verarbeiten können muss.
      Die möglichen Kontozustände sind in
        https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/latest/#3.1.2
      dokumentiert.
      Der folgende Zustand <> ACTIVATED ist entsprechend gemSpec_Aktensystem_ePAfueralle
      in diesem Szenario zu betrachten:
        "UNKNOWN"
      Die Operation "I_Information_Service::getRecordStatus" liefert für den genannten Zustand
      den http Statuscode 404 zurück.
      Siehe auch https://github.com/gematik/ePA-Basic -> im Pfad src/openapi/I_Information_Service.yaml

    Hinweis: Die Test-KVNR Z999999999 ist fehlerhaft: ungültiger KVNR-Wertebereich für Testkarten nach
      Definition in https://gemspec.gematik.de/docs/gemSpec/gemSpec_TK_FD/latest/#7.3.
      Sie entspricht aber den vorgegebenen Prüfungen für den NCPeH.
      So kann sichergestellt werden, dass sie auf jeden Fall in keinem Aktensystem gefunden werden kann.
      Alternativ muss für alle Aktensysteme gemeinsam eine KVNR reserviert werden, die nicht als Konto
      angelegt werden darf.

    Angenommen die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Wenn die versicherte Person die falsche KVNR Z999999999 und ihren AccessCode an den LE-EU übergibt
    Und der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_NO_MATCH
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext "Patient Identification Error"
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "It was not possible to localise the patient's health record account in the national health record system."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_011
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP @Connectathon
  Szenario: NCPeH - PSA ePA-IOP UC1 Status des ePA Kontos ist INITIALIZED
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der versicherten Person aus der ePKA
    ab, wobei das Aktenkonto der versicherten Person noch nicht aktiviert wurde (z. B. wegen einer
    Konto-Migration).
    Der NCPeH gibt eine Antwort entsprechend gemSpec_NCPeH_FD#TAB_NCPeH_Lokalisierung_Akte_Fehler_XCPD_Response
    zurück.
    (https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/#4.2.7.3)
    Technischer Hintergrund:
      Ein ePA Konto kann verschiedene Zustände durchlaufen, die der NCPeH im Rahmen der
      Operation "I_Information_Service::getRecordStatus" verarbeiten können muss.
      Die möglichen Kontozustände sind in https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/latest/#3.1.2
      dokumentiert.
      Der folgende Zustand <> ACTIVATED aus gemSpec_Aktensystem_ePAfueralle wird in diesem Szenario
      betrachtet:
        "INITIALIZED"
      Die Operation "I_Information_Service::getRecordStatus" liefert für den genannten Zustand
      den http Statuscode 404 zurück.
      Siehe auch https://github.com/gematik/ePA-Basic -> im Pfad src/openapi/I_Information_Service.yaml

    Hinweis: Dies ist eine Testschrittanleitung zur Ausführung in einem Connectathon und wird nicht
      in der automatisierten Testsuite ausgeführt. Das gesetzte Connectathon-Tag dient auch zur
      Vermeidung der Ausführung im automatisierten Anteil der Testsuite.

    Angenommen der Versicherte Freiherr Franz von Nirgendwo hat ein ePA Aktenkonto im Status INITIALIZED
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_NO_MATCH
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext "Patient Identification Error"
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "It was not possible to localise the patient's health record account in the national health record system."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_012
  @STATUS:Spezifiziert @MODUS:Manuell @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP @Connectathon
  Szenario: NCPeH - PSA ePA-IOP UC1 Status des ePA Kontos ist SUSPENDED
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA ab, wobei
    das Aktenkonto der versicherten Person noch nicht aktiviert wurde (z. B. wegen einer Konto-Migration).
    Der NCPeH gibt eine Antwort entsprechend gemSpec_NCPeH_FD#TAB_NCPeH_Lokalisierung_Akte_Fehler_XCPD_Response
    zurück.
    (https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V1.6.0/#4.2.7.3)
    Technischer Hintergrund:
      Ein ePA Konto kann verschiedene Zustände durchlaufen, die der NCPeH im Rahmen der
      Operation "I_Information_Service::getRecordStatus" verarbeiten können muss.
      Die möglichen Kontozustände sind in https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/latest/#3.1.2
      dokumentiert.
      Der folgende Zustand <> ACTIVATED aus gemSpec_Aktensystem_ePAfueralle wird in diesem Szenario
      betrachtet:
        "SUSPENDED"
      Die Operation "I_Information_Service::getRecordStatus" liefert für den genannten Zustand
      den http Statuscode 409 zurück.
      Siehe auch https://github.com/gematik/ePA-Basic -> im Pfad src/openapi/I_Information_Service.yaml

    Hinweis: Dies ist eine Testschrittanleitung zur Ausführung in einem Connectathon und wird nicht
      in der automatisierten Testsuite ausgeführt. Das gesetzte Connectathon-Tag dient auch zur
      Vermeidung in einer automatisierten Ausführung der Testsuite.

    Angenommen die Versicherte Franziska Musterfrau hat ein ePA Aktenkonto im Status SUSPENDED
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_NO_MATCH
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext "Patient Identification Error"
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "It was not possible to localise the patient's health record account in the national health record system."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_020
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10107-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  @CI
  Szenariogrundriss: NCPeH - PSA ePA-IOP Nutzbarkeit verschiedener ePA Aktensysteme mit verschiedenen EU Mitgliedsstaaten
    Es wird geprüft, dass der NCPeH für verschiedene EU Mitgliedsstaaten mit jedem Aktensystem und entsprechend vorhandener
    Berechtigungen das vollständige Anwendungsszenario Patient Summary Land A durchführen kann.

    Angenommen eine versicherte Person hat ein aktives ePA Konto im Aktensystem des Betreibers <Aktensystem>
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land <Mitgliedsstaat> bei LE-EU <Leistungserbringer> in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU als Antwort die KVNR mit dem AccessCode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem ePKA-Dokument der versicherten Person zurück

    Beispiele:
      | Aktensystem | Mitgliedsstaat | Leistungserbringer |
      | IBM         | AUSTRIA        | Dr. Baumgartner    |
      | IBM         | FRANCE         | Prof. Chevalier    |

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_030
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @NCPEH-RESPONSE-UC1:PRPA_IN201306UV02_45
  @FDV-RESPONSE:,FAILURE-RESPONSE
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  @CI
  Szenario: NCPeH - PSA ePA-IOP UC1 Befugnis für EU Mitgliedsstaat für das ePA Konto fehlt bei Start der Patient Identification
    Es wird geprüft, dass der NCPeH mit dem Fehlen einer Zugriffsberechtigung für ein bestimmten
    EU Mitgliedsstaat im ePA Konto umgeht.
    Situationen mit vergleichbarer Reaktion des Aktensystems:
    - Befugnis wurde vom Versicherten mittels FdV entzogen
    - Befugnis wurde vom Versicherten mittels FdV erneuert, so dass ein neuer AccessCode in der Befugnis aktiv ist
    - Befugnis ist zeitlich abgelaufen
    Interner Ablauf:
    - Der NCPeH-FD sucht das Aktensystem, in dem das Aktenkonto der versicherten Person bereitgestellt wird.
    - Der NCPeH-FD authentifiziert sich erfolgreich mit dem ePA Aktensystem (inkl. VAU-Kanal) oder nutzt einen bereits
      etablierten, authentifizierten und verfügbaren VAU-Kanal zu dem Aktensystem.
    - Der NCPeH-FD sendet eine FindDocuments-Operation an das Aktensystem.
    - Das Aktensystem lehnt die Operation mangels Befugnis mit RegistryError und errorCode="NotEntitled" ab, siehe
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/gemSpec_Aktensystem_ePAfueralle_V1.3.0/#A_25683-01
      und https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/gemSpec_Aktensystem_ePAfueralle_V1.3.0/#A_23148-01
    - Der NCPeH-FD beantwortet nach TUC_NCPeH_013 den XCPD-Request mit einem Fehler mit ReasonEncoding "InsufficientRights" und
      einem AcknowledgementDetail "ERROR_PI_GENERIC" (entsprechend Fehlerkorrektur im Change C_11967 zum NCPeH-FD v2.0).

    Angenommen die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto NICHT befugt
    Wenn die versicherte Person ihre KVNR und einen falschen AccessCode an den LE-EU übergibt
    Und der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding InsufficientRights
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlertext "Patient Identification Error"
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "The requestor has insufficient rights to query for patient’s identity data. Please ask the patient for access rights."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_031
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @NCPEH-RESPONSE-UC2:AdhocQueryResponse_031
  @FDV-RESPONSE:,,FAILURE-RESPONSE
  @eHDSI-Szenario:PSA_FindDocuments
  @IOP
  @CI
  Szenario: NCPeH - PSA ePA-IOP UC2 Befugnis für EU Mitgliedsstaat fehlt bei FindDocuments
    Es wird geprüft, dass der NCPeH mit dem Fehlen einer Zugriffsbefugnis für einen bestimmten
    EU Mitgliedsstaat im ePA Konto umgeht, nachdem bereits erfolgreich eine Patient Identification und
    entsprechend eine TRC Assertion ausgestellt wurde.
    Situationen mit vergleichbarer Reaktion des Aktensystems:
    - Befugnis wurde vom Versicherten mittels FdV entzogen
    - Befugnis wurde vom Versicherten mittels FdV erneuert, so dass ein neuer AccessCode in der Befugnis aktiv ist
    - Befugnis ist zeitlich abgelaufen
    Interner Ablauf:
    - Der NCPeH-FD sucht das Aktensystem, in dem das Aktenkonto der versicherten Person bereitgestellt wird.
    - Der NCPeH-FD authentifiziert sich erfolgreich mit dem ePA Aktensystem (inkl. VAU-Kanal) oder nutzt einen bereits
      etablierten, authentifizierten und verfügbaren VAU-Kanal zu dem Aktensystem.
    - Der NCPeH-FD sendet eine FindDocuments-Operation an das Aktensystem.
    - Das Aktensystem lehnt die Operation mangels Befugnis mit RegistryError und errorCode="NotEntitled" ab, siehe
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/gemSpec_Aktensystem_ePAfueralle_V1.3.0/#A_25683-01
      und https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/gemSpec_Aktensystem_ePAfueralle_V1.3.0/#A_23148-01
    - Der NCPeH-FD beantwortet nach TUC_NCPeH_013 den XCA-Request mit einem Fehler "ERROR_NO_CONSENT".
      (entsprechend Fehlerkorrektur im Change C_11967 zum NCPeH-FD v2.0)

    Angenommen die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto befugt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Wenn die versicherte Person dem EU-Land die Befugnis entzieht
    Und danach der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft
    Dann erhält der LE-EU keine Liste von Dokumentreferenzen
    Und der LE-EU erhält einen FindDocuments RegistryError mit errorCode ERROR_NO_CONSENT

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_032
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @FDV-RESPONSE:,,FAILURE-RESPONSE
  @NCPEH-RESPONSE-UC34:RetrieveDocumentSetResponse_032
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3
  @IOP
  @CI
  Szenario: NCPeH - PSA ePA-IOP UC3 Befugnis für EU Mitgliedsstaat fehlt bei RetrieveDocuments
    Es wird geprüft, dass der NCPeH mit dem Fehlen einer Zugriffsbefugnis für einen bestimmten
    EU Mitgliedsstaat im ePA Konto umgeht, nachdem bereits erfolgreich eine Patient Identification (UC 1)
    und Find Documents (UC 2) erfolgreich durchgeführt wurden.
    Situationen mit vergleichbarer Reaktion des Aktensystems:
    - Befugnis wurde vom Versicherten mittels FdV entzogen
    - Befugnis wurde vom Versicherten mittels FdV erneuert, so dass ein neuer AccessCode in der Befugnis aktiv ist
    - Befugnis ist zeitlich abgelaufen
    Interner Ablauf:
    - Der NCPeH-FD sucht das Aktensystem, in dem das Aktenkonto der versicherten Person bereitgestellt wird.
    - Der NCPeH-FD authentifiziert sich erfolgreich mit dem ePA Aktensystem (inkl. VAU-Kanal) oder nutzt einen bereits
      etablierten, authentifizierten und verfügbaren VAU-Kanal zu dem Aktensystem.
    - Der NCPeH-FD sendet eine RetrieveDocuments-Operation an das Aktensystem.
    - Das Aktensystem lehnt die Operation mangels Befugnis mit RegistryError oder RepositoryError und
      errorCode="NotEntitled" ab, siehe
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/gemSpec_Aktensystem_ePAfueralle_V1.3.0/#A_25683-01
      und https://gemspec.gematik.de/docs/gemSpec/gemSpec_Aktensystem_ePAfueralle/gemSpec_Aktensystem_ePAfueralle_V1.3.0/#A_23148-01
    - Der NCPeH-FD beantwortet nach TUC_NCPeH_014 den XCA-Request mit einem Fehler "ERROR_NO_CONSENT".
      (entsprechend Fehlerkorrektur im Change C_11967 zum NCPeH-FD v2.0)

    Angenommen die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto befugt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Und der LE-EU hat erfolgreich eine Suche des Patient Summary der versicherten Person durchgeführt
    Wenn die versicherte Person dem EU-Land die Befugnis entzieht
    Und danach der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 abruft
    Dann erhält der LE-EU kein Dokument der versicherten Person zurück
    Und das Ergebnis der RetrieveDocument-Operation enthält einen RegistryError mit errorCode ERROR_NO_CONSENT

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_033
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @NCPEH-RESPONSE-UC2:AdhocQueryResponse_031
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  @CI
  Szenario: NCPeH - PSA ePA-IOP Wechsel der Befugnis von einem EU Mitgliedsstaat zu einem anderen
    Dieses Szenario ist eine erweiterte Variante zum Thema fehlende Befugnis im Aktensystem.
    Dabei wird geprüft, dass bei Setzen einer neuen Befugnis für ein anderes EU Land die
    alte Befugnis für das vorige Land erlischt und die neue Befugnis wirksam wird.
    In diesem Sinne ist dies eher ein Szenario zur Wirksamkeitsprüfung von EU-Befugnis-Updates in einem ePA Aktensystem.
    Die zeitliche Komponente in Bezug auf die Dauer einer Befugnis wird hier außer Acht gelassen. Realistisch wären
    hier z. B. Wechsel der EU Länder in Grenznähe.
    Der Versuch vom ersten LE-EU aus EU Land #1 eine Dokumentensuche durchzuführen, erzeugt beim
    ePA XDS Document Service eine 'NotEntitled' IHE-Fehlermeldung.
    Der zweiten LE-EU aus EU Land #2 kann erfolgreich eine Patient Identification durchführen

    Angenommen die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto befugt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Wenn die versicherte Person den LE-EU ohne Behandlung verlässt
    Und die versicherte Person begibt sich in dem weiteren EU-Land FRANCE bei LE-EU Prof. Chevalier in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto befugt
    Dann kann der LE-EU van der Meer aus dem EU-Land NETHERLANDS keine erfolgreiche Suche nach dem Patient Summary der versicherten Person durchführen
    Wenn die versicherte Person ihre KVNR und den neuen AccessCode an den LE-EU des weiteren EU-Landes übergibt
    Und der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU als Antwort die KVNR mit dem AccessCode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem ePKA-Dokument der versicherten Person zurück


  # Nur Testentwurf, deshalb ist das Szenario im Build zu ignorieren
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_040
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Positiv
  @AF-ID:AF_10107-01 @AF-ID:AF_10121-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_FindDocuments
  @IOP
  @LongRunning
  Szenario: NCPeH - PSA ePA-IOP UC2 Neuaufbau eines VAU-Kanals nach 19 Minuten idle timeout
    Der LE-EU hat eine Patient Identification erfolgreich durchgeführt und es wurde eine TRC-Assertion
    vom NCP-B ausgestellt. Der Usecase 2 zum Auflisten der Dokumente wird aber erst 20 Minuten nach der Identifikation
    des Patienten aufgerufen. Dadurch wurde der VAU-Kanal mit dem ePA Aktensystem im NCPeH-FD abgebaut (19 Minuten Idle Timeout)
    und muss neu aufgebaut und autorisiert werden.
    Das EU Land ist noch für mindestens 'Zeitpunkt der Patient Identification'+30 Minuten in der ePA berechtigt.
    Das Auflisten der Dokumente muss auch 20 Minuten nach der Patient Identification erfolgreich ausgeführt werden.

    Hinweis: Die aktuelle umgesetzte, interne Architektur des NCPeH-FD beachtet momentan nicht bereits geöffnet und
      ungenutzte VAU-Kanäle, die für das gleiche Land B autorisiert wurden. Der Aufruf für UC2 KANN in der gleichen
      NCPeH-VAU-Instanz landen, in dem zuvor ein passender VAU-Kanal etabliert war, oder aber in einer anderen.
      Die VAU-Kanäle zum ePA AS sind nicht variabel zwischen den NCPeH-VAU-Instanzen nutzbar.
      Prinzipiell muss eine Folge-UC mit 20 Minuten Abstand zum vorausgehenden UC aber erfolgreich sein.
      Die Priorität des Testfalls wurde deshalb vorerst auf 3 gesetzt.

    Angenommen die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto befugt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Wenn der LE-EU 20 Minuten anderweitig beschäftigt ist
    Und danach der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft
    Dann erhält der LE-EU eine Liste mit zwei Dokumentreferenzen zurück
    Und ein Dokument in der Liste referenziert das Patient Summary der versicherten Person mit CDA Level 1
    Und ein Dokument in der Liste referenziert das Patient Summary der versicherten Person mit CDA Level 3

