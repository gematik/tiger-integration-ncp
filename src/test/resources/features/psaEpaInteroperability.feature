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
Funktionalität: NCPeH PSA Interoperabilität mit der Anbindung an die Anwendung ePA
  AF_10107 - Versicherten im Behandlungsland identifizieren
  AF_10121 - Verfügbare Versichertendatensätze des ePKA MIO auflisten
  Die benannten Anwendungsfälle basieren auf der Anbindung an das ePA Aktensystem und der Nutzung
  der Anwendung ePA und dem darin hinterlegten ePKA-Dokument. Betrachtet werden dabei folgende Themen
  1. Das Fehlen des ePA Kontos zu einer KVNR
  2. Verschiedene Zustände eines ePA Kontos, auf das der NCPeH treffen kann
  3. Nutzbarkeit aller drei verschiedenen ePA Aktensysteme
  4. ePKA Dokumente von verschiedenen Konnektoren signiert und in der ePA eingestellt
  5. Der Umgang mit der Berechtigung des NCPeH für EU Mitgliedsstaaten in der ePA
  6. Session-Handling zum ePA Aktensystem
  Entsprechende Interoperabilitätsszenarien sind zu testen.
  Nicht betrachtet wird die Transformation und Transkodierung der medizinischen Daten des ePKA-Dokumentes.

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_010
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10107
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  Szenario: NCPeH - PSA ePA-IOP UC1 Identifikation eines Versicherten - KVNR nicht gefunden
    Der LE-EU ruft über den NCPeH die persönlichen Informationen der Versicherten aus der ePKA ab, wobei
    er eine falsche KVNR angibt und diese in keinem Aktensystem gefunden wird.
    Der NCPeH gibt eine Antwort entsprechend gemSpec_NCPeH_FD#TAB_NCPeH_EPKA_LOCALIZATION_ERRORS zurück.
    Technischer Hintergrund:
      Ein ePA Konto kann verschiedene Zustände durchlaufen, die der NCPeH verarbeiten können muss.
      Die folgenden Zustände <> Activated sind entsprechend gemSpec_Aktensystem und gemSpec_Autorisierung
      in diesem Szenario zu betrachten:
        "Registered_for_Migration" - Das Konto wurde beantragt und initialisiert, es können aber noch keine
        medizinischen Dokumente gespeichert werden.
        "DL_in_Progress" - Der Download des Migrationspakets wurde gestartet.
        "Ready_for_Import" - Der Download des Migrationspaketes wurde erfolgreich abgeschlossen.
        "Start_Migration" - Die Erstellung des Migrationspakets wurde gestartet.
        "Suspended" - Die Daten des Kontos des Versicherten wurden exportiert, um sie zu einem neuen Anbieter
        zu migrieren.
        "Key_Change" - Für das Konto wird eine Umschlüsselung vorgenommen. Während der Umschlüsselung sind
        alle Operationen verboten, die nicht explizit im Rahmen der Umschlüsselung erlaubt sind.
        "UNKNOWN" - Der Versicherte ist unbekannt, es existiert für diesen kein Konto (mehr).
      Die Operation "I_Authorization_Management::checkRecordExists" liefert bei der Option AllMandators=true
      für die oben genannten Zustände den Wert "UNKNOWN" zurück. Für das Szenario reicht es dabei aus, dass einer
      der gelisteten Zustände Auslöser für den Wert "UNKNOWN" ist. (Deshalb wird der Status "Key_Change"
      hier vereinfacht mit betrachtet, obwohl er nicht mit einer Kontenmigration verknüpft ist.)

    Hinweis: Die Test-KVNR Z999999999 ist fehlerhaft: ungültiger KVNR-Wertebereich für Testkarten nach
      Definition in gemSpec_TK_FD#7.3. Sie entspricht aber den vorgegebenen Prüfungen für den NCPeH.
      So kann sichergestellt werden, dass sie auf jeden Fall in keinem Aktensystem gefunden werden kann.
      Alternativ muss für alle Aktensysteme gemeinsam eine KVNR reserviert werden, die nicht als Konto
      angelegt werden darf.

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Wenn die versicherte Person die falsche KVNR Z999999999 und ihren AccessCode an den LE-EU übergibt
    Und der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_NO_MATCH
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "Patient's record account could not be determined."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_011
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP @Connectathon
  Szenario: NCPeH - PSA ePA-IOP UC1 Status des ePA Kontos ist Registered
    Der folgende Zustand ist entsprechend gemSpec_Aktensystem in diesem Szenario zu betrachten:
      Registered - Das Konto wurde beantragt und initialisiert, es können aber noch keine medizinischen
      Dokumente gespeichert werden.
    Die versicherte Person hat also das Konto noch nicht aktivieren lassen.
    Der NCPeH gibt eine Antwort entsprechend gemSpec_NCPeH_FD#TAB_NCPeH_EPKA_LOCALIZATION_ERRORS zurück.

    Angenommen der Versicherte Freiherr Franz von Nirgendwo hat ein registriertes ePA Konto
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_NO_MATCH
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "Patient's record account could not be determined."

  # unzureichende Implementierung (Patientendaten werden nicht zurückgeliefert), deshalb ist das Szenario noch im Build zu ignorieren
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_012
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP @Connectathon
  Szenario: NCPeH - PSA ePA-IOP UC1 Status des ePA Kontos ist Dismissed
    Die versicherte Person hat ein ePA Konto mit ePKA Dokument in einem ersten Aktensystem, das sie mit
    Migrationswunsch gekündigt hat. Sie möchte den Anbieter bzw. die Krankenversicherung wechseln.
    Bei ihrem neuen Anbieter ist bereits ein Konto angelegt.
    Bei ihrem alten Anbieter wurde die Kündigung registriert, aber die Migration der Daten noch nicht gestartet.
    Es wird ein Konto im Status "Dismissed" gefunden und deshalb erfolgreich eine Patient Identification
    durchgeführt.
    Technischer Hintergrund:
      Ein ePA Konto kann verschiedene Zustände durchlaufen, die der NCPeH verarbeiten können muss.
      Der folgende Zustand <> Activated ist entsprechend gemSpec_Aktensystem und gemSpec_Autorisierung
      in diesem Szenario zu betrachten:
        Dismissed - Das Konto wurde beim Anbieter gekündigt, kann aber weiterhin genutzt werden bis zum
        Ende einer möglichen Kündigungsfrist oder Start der Migration der Daten des Versicherten.

    Angenommen die Versicherte Franziska Musterfrau hat ein ePA Aktenkonto im Status Dismissed
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU als Antwort die KVNR mit dem AccessCode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem ePKA-Dokument der versicherten Person zurück

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_030
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
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
      | Rise        | AUSTRIA        | Dr. Baumgartner    |
      | Rise        | FRANCE         | Prof. Chevalier    |
    # TODO: konkrete Werte sind mit den zu besorgenden Testidentitäten abzugleichen, ein Mapping kann auch über die Konfiguration erfolgen

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_040
  # TODO: Check, ob der Test wirklich halbautomatisch ist, oder automatisiert in der Umgebung laufen kann
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  Szenariogrundriss: NCPeH - PSA ePA-IOP Nutzbarkeit QES-Signaturen verschiedener Konnektoren
  Es gibt verschiedene Konnektor-Produkte in der TI, deren Signaturen mit dem NCPeH interoperabel sein müssen.
  Deshalb ist eine erfolgreiche Patient Identification mit verschiedenen Konnektor-Produkten durchzuführen.
    x Konnektorversionen im aktuellen Zulassungsverfahren

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein vom Konnektor <Konnektor> mit <Signatur> signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Wenn der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU als Antwort die KVNR mit dem AccessCode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem ePKA-Dokument der versicherten Person zurück

    Beispiele:
      | Konnektor | Signatur |
      | Koco      | ECC      |
      | Koco      | RSA      |
      | Rise      | ECC      |
      | Rise      | RSA      |
      | Secunet   | ECC      |
      | Secunet   | RSA      |

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_050
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  Szenario: NCPeH - PSA ePA-IOP UC1 Berechtigung für EU Mitgliedsstaat fehlt bei Start der Patient Identification
    Es wird geprüft, dass der NCPeH mit dem Fehlen einer Zugriffsberechtigung für ein bestimmten
    EU Mitgliedsstaat im ePA Konto umgeht.
    Der NCPeH-FD sucht zuerst erfolgreich das ePA Aktensystem, dass das Konto des Versicherten bereitstellt
    (Operation I_Authorization::getAuthorizationKey)
    Daraufhin versucht sich der NCPeH-FD an der Komponente Autorisierung zu autorisieren. Auf Grund der
    fehlenden Berechtigung erhält der NCPeH-FD die Fehlermeldung KEY_ERROR zurück.
    Der NCPeH-FD beantwortet den XCPD-Request entsprechend gemSpec_NCPeH_FD#TAB_NCPeH_Authorisierung_Fehlercodes
    mit einem Fehlercode InsufficientRights und dem acknowledgementDetail ERROR_PI_GENERIC.
    Hinweis: bei der Autorisierung erfolgt noch keine Prüfung des AccesCodes.

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto NICHT berechtigt
    # Wenn die Berechtigung fehlt, muss automatisch auch der AccessCode ein falscher sein
    Wenn die versicherte Person ihre KVNR und einen falschen AccessCode an den LE-EU übergibt
    Und der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding InsufficientRights
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "Please ask the patient for access authorisation."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_051
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  Szenario: NCPeH - PSA ePA-IOP UC1 Dokument bei Cross-Gateway Query für die Patient Identification nicht gefunden
    Es wird geprüft, wie der NCPeH bei der XCA Query für die Patient Identification mit dem Fehlen
    des Dokumentes oder fehlender Zugriffsberechtigung im ePA Konto umgeht, nachdem bereits erfolgreich
    eine Verbindung zur Dokumentenverwaltung aufgebaut werden konnte.
    Dabei ist der Zeitpunkt zwischen Verbindungsaufbau zur Dokumentenverwaltung und XCA-Query
    derart gelegen, dass das Dokument verschwindet oder Zugriff darauf nicht mehr möglich ist (siehe TUC_NCPeH_013).
    Die ePA Session zur Dokumentenverwaltung konnte zuvor aufgebaut und der Kontext geöffnet werden.
    Der NCPeH-FD erhält einen ResponseStatus Success mit einer leeren Dokumentenliste zurück.
    Situationen mit vergleichbarer Reaktion des Aktensystems:
      Dokument fehlt (siehe NCP1_E2E_PSA_UC1_010)
      Berechtigung ist abgelaufen oder wurde entzogen (Timing-Edgecase, ePA-AS Reaktion siehe gemSpec_Dokumentenverwaltung#A_13585)
      AccessCode ist falsch (bzw. mit der Berechtigung abgelaufen) (siehe gemSpec_Dokumentenverwaltung#A_13585)
    Der NCPeH-FD beantwortet den XCPD-Request entsprechend TUC_NCPeH_013 mit einem Fehlercode AnswerNotAvailable
    und dem acknowledgementDetail ERROR_PI_GENERIC.
    Dieses Szenario legt hier Wert auf die korrekte Prüfung des AccessCodes durch das ePA Aktensystem.

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Wenn die versicherte Person ihre KVNR und einen falschen AccessCode an den LE-EU übergibt
    Und der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "Patient identity information is not available or accessible for European Member States. Please ask the patient for access authorisation."

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_052
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ
  @AF-ID:AF_10107
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification
  @IOP
  Szenario: NCPeH - PSA ePA-IOP UC1 Dokument bei bei Cross-Gateway Retrieve für die Patient Identification nicht gefunden
    Es wird geprüft, wie der NCPeH beim XCA Retrieve für die Patient Identification mit dem Fehlen
    des Dokumentes oder fehlender Zugriffsberechtigung im ePA Konto umgeht, nachdem bereits erfolgreich
    eine Verbindung zur Dokumentenverwaltung aufgebaut werden konnte.
    Dabei ist der Zeitpunkt zwischen XCA-Query und XCA-Retrieve derart gelegen, dass das Dokument verschwindet
    oder Zugriff darauf nicht mehr möglich ist (siehe TUC_NCPeH_017).
    Die ePA Session besteht dabei (noch), so dass direkt an die ePA Dokumentenverwaltung das XCA-Retrieve
    geschickt wird.
    Der NCPeH-FD erhält eine Fehlermeldung "XDSDocumentUniqueIdError" zurück.
    Situationen mit vergleichbarer Reaktion des Aktensystems:
      Dokument fehlt oder wurde aktualisiert (mit dadurch resultierender neuer DocumentUniqueId)
      Berechtigung ist abgelaufen oder wurde entzogen (siehe gemSpec_Dokumentenverwaltung#A_14548-*)
      AccessCode ist falsch (bzw. mit der Berechtigung abgelaufen) (siehe gemSpec_Dokumentenverwaltung#A_14548-*)
    Der NCPeH-FD beantwortet die XCPD-Suche entsprechend TUC_NCPeH_017 mit einem Fehlercode AnswerNotAvailable
    und dem acknowledgementDetail ERROR_PI_GENERIC.

    Hinweis: reduzierte Priorität, da alle beschriebenen Situationen Timing-Edge-Cases sind.
      Umsetzung des Szenarios mittels fehlendem Dokument

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Wenn das ePKA Dokument der versicherten Person gelöscht wurde
    Und der LE-EU mit KVNR und AccessCode der versicherten Person die Identifikation aufruft
    Dann erhält der LE-EU ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis enthält Reason Encoding AnswerNotAvailable
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Fehlercode ERROR_PI_GENERIC
    Und das Identifikationsergebnis enthält im acknowledgementDetail den Locationtext "Patient identity information is not available or accessible for European Member States. Please ask the patient for access authorisation."


  # Nur Testentwurf, deshalb ist das Szenario im Build zu ignorieren
  @Ignore
  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_060
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10107 @AF-ID:AF_10121
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification @eHDSI-Szenario:PSA_FindDocuments
  @IOP @LongRunning
  Szenario: NCPeH - PSA ePA-IOP UC2 Neu-Aufbau einer ePA Session nach 20 Minuten Session-timeout
    Der LE-EU hat eine Patient Identification erfolgreich durchgeführt und es wurde eine TRC-Assertion
    vom NCP-B ausgestellt. Der Usecase 2 zum Auflisten der Dokumente wird aber erst mehr als 20 Minuten
    nach der Identifikation des Patienten aufgerufen. Dadurch wurde die Session mit dem ePA Aktensystem
    im NCPeH-FD abgebaut und muss erfolgreich neu initiiert werden.
    Das EU Land ist noch für mindestens 'Zeitpunkt der Patient Identification'+30 Minuten in der ePA berechtigt.
    Das Auflisten der Dokumente muss auch >20 Minuten nach der Patient Identification erfolgreich ausgeführt werden.

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und der NCPeH Fachdienst ist für den Zugriff dieses EU-Landes auf das Konto der versicherten Person noch für mindestens 30 Minuten berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Wenn der LE-EU 21 Minuten anderweitig beschäftigt ist
    Und danach der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft
    Dann erhält der LE-EU eine Liste mit zwei Dokumentreferenzen zurück
    Und ein Dokument in der Liste referenziert das Patient Summary der versicherten Person mit CDA Level 1
    Und ein Dokument in der Liste referenziert das Patient Summary der versicherten Person mit CDA Level 3

  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_EPA_061
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ
  @AF-ID:AF_10107 @AF-ID:AF_10121
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_PatIdentification @eHDSI-Szenario:PSA_FindDocuments
  @IOP @LongRunning
  Szenario: NCPeH - PSA ePA-IOP UC2 Berechtigung für EU Mitgliedsstaat fehlt bei FindDocuments ohne offene Session
    Es wird geprüft, dass der NCPeH mit dem Fehlen einer Zugriffsberechtigung für ein bestimmten
    EU Mitgliedsstaat im ePA Konto umgeht, nachdem bereits erfolgreich eine Patient Identification und
    entsprechend eine TRC Assertion ausgestellt wurde.
    Dabei wird entweder vom Versicherten zwischen den Usecases Patient Identification und Find Documents
    die Berechtigung entzogen oder die Zeitpunkte von Usecase 1 und 2 sind derart gelegen sind, dass die
    Berechtigung zwischen beiden Aktionen in der ePA abläuft.
    Die ePA Session besteht dabei nicht mehr, so dass der NCPeH-FD eine neue Session aufbauen muss.
    Auf Grund der fehlenden Berechtigung erhält der NCPeH-FD die Fehlermeldung KEY_ERROR zurück.
    Der NCPeH-FD beantwortet den CrossGatewayQuery-Request mit dem Fehlercode ERROR_PS_GENERIC entsprechend
    gemSpec_NCPeH_FD#TAB_NCPeH_Authorisierung_Fehlercodes.

    Hinweis: Realisierung mittels Wartezeit von 21 Minuten und Entzug der Berechtigung.
      Die Alternative wäre sonst eine Wartezeit von >60 Minuten ab Erteilung der Zugriffsberechtigung.

    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Wenn die versicherte Person dem EU-Land die Zugriffsberechtigung entzieht
    Und der LE-EU 21 Minuten anderweitig beschäftigt ist
    Und danach der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft
    Dann erhält der LE-EU keine Liste von Dokumentreferenzen
    Und der LE-EU erhält einen FindDocuments RegistryError mit errorCode ERROR_PS_GENERIC

