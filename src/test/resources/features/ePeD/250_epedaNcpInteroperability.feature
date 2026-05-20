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
# *******
#
# For additional notes and disclaimer from gematik and in case of changes by gematik find details in the "Readme" file.
#

#language: de
#noinspection NonAsciiCharacters,SpellCheckingInspection
@PRODUKT:NCPeH_FD @PRODUKT:NCPeH_ePeDA
@PRODUKT:eRp_FD @PRODUKT:IDP-D @PRODUKT:eRp_FdV
@PRODUKT:TSP\sX.509\snonQES\s-\sSMC-B
Funktionalität: NCPeH ePeD Interoperabilität mit der Anbindung an die Anwendung E-Rezept
  Anwendungsfälle:
  AF_10379 - ePeD-A - Versicherten im Behandlungsland für ePeD-A identifizieren - UseCase 9 (UC9)
  AF_10380 - Einlösbare E-Rezepte des Versicherten aus ePeD-A auflisten - UseCase 10 (UC10)
  AF_10400 - Ausgewählte E-Rezepte abrufen - UseCase 11 (UC11)
  AF_10401 - Abgabe von Arzneimitteln an Versicherte im Abgabeland - UseCase 12 (UC12)
  Die benannten Anwendungsfälle basieren an der Schnittstelle zur EU neben den sicherheitsrelevanten Vorgaben der eHSDI
  auf spezifischen sicherheitsrelevanten Vorgaben, die nationale Spezifika, wie z. B. Vorgaben zur KVNR und
  Zugriffsberechtigung eines LE-EU auf nationale Daten konkretisieren.
  In diesem feature-File werden dazu folgende Themen geprüft:
  1. Unbekannte / fehlerhafte Eingabedaten (KVNR, Zugriffscode)
  2. Prüfung von LE-EU Permissions Codes und Rollen Codes
  3. Prüfung auf Konsistenz zwischen Identity- und TRC-Assertion
  4. Prüfung für Zugriff eines EU-Mitgliedsstaates auf NCPeH anhand EU-Ländercode aus seinem TLS-Zertifikat
  5. Zugriffsversuch auf unzulässigen / nicht verfügbaren Service der eHDSI
  Entsprechende Interoperabilitätsszenarien sind zu testen.

########################################################################################
########                fehlerhafte Eingabedaten (KVNR, Zugriffscode)           ########
########################################################################################

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_010
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_27881 @AFO-ID:A_27877
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) mit Zahlendreher in der KVNR
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker startet die Patient Identification, aber gibt dabei die KVNR fehlerhaft ein, wobei
    die Prüfziffer der KVNR durch Zahlendreher verfälscht wurde (in die Prüfziffer geht auch die Reihenfolge der
    Zeichen/Ziffern in der KVNR ein).
    Siehe auch https://www.gkv-datenaustausch.de/media/dokumente/kvnr/Anlage_1_20230102_Pruefziffernberechnung-KVNR.pdf
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaIdentifyInvalidPatientIdentifier'
    Referenzen:
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#4.1.9
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27877
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27881

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn der EU-Apotheker die Patient Identification mit einem Zahlendreher in der KVNR aufruft
    Dann erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaIdentifyInvalidPatientIdentifier

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_011
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_27901 @AFO-ID:A_26576
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP
  Szenario: NCPeH - ePeD-A NCP-IOP Auflisten eines Rezeptes (UC10) nur mit Ziffern in der KVNR der TRC Assertion
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker startet die Patient Identification erfolgreich durchgeführt, aber bei der Verarbeitung der KVNR
    bei Erstellung der TRC trat ein Fehler auf, so dass die KVNR syntaktisch fehlerhaft ist (Ziffer anstatt Buchstabe
    am Beginn der KVNR).
    Der Apotheker startet mit der fehlerhaften TRC Assertion die Auflistung der Rezepte.
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaSearchInvalidPatientIdentifier'
    Referenzen: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_26576
       https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
       TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, Prüfung der Variable trc_kvnr

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach den E-Rezepten mit einer KVNR nur mit Ziffern in der TRC Assertion aufruft
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und das Suchergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaSearchInvalidPatientIdentifier

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_012
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_27987 @AFO-ID:A_26576
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @IOP
  Szenario: NCPeH - ePeD-A NCP-IOP Abruf eines Rezeptes (UC11) mit einer zu langen KVNR in der TRC Assertion
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker hat die Patient Identification und das Auflisten der Rezepte erfolgreich durchgeführt.
    Der Apotheker startet den Abruf der Rezepte, aber bei der Nutzung der TRC Assertion tritt ein Fehler auf, so dass
    die KVNR syntaktisch fehlerhaft ist (Ziffer anstatt Buchstabe am Beginn der KVNR). (Alternativ erfolgt ein
    missbräuchlicher Versuch der Nutzung einer falschen TRC Assertion)
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaRetrieveInvalidPatientIdentifier'
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
      TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A, Wert der Variable trc_kvnr

    Hinweis: Da zum Zeitpunkt der Operation bereits einmal die Gültigkeit der TRC Assertion erfolgreich geprüft
      wurde, ist die Wahrscheinlichkeit des Auftretens einer falschen KVNR geringer. Die Prio des Szenarios
      wird deshalb auf 2 reduziert.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker den Abruf von E-Rezepten mit einer zu langen KVNR in der TRC Assertion durchführt
    Dann erhält der EU-Apotheker kein Rezept ausgegeben
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrieveInvalidPatientIdentifier
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_013
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_27204
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP
  Szenario: NCPeH - ePeD-A eRP-IOP Dispensierung (UC12) mit einer ungültigen Prüfziffer in der KVNR in der TRC Assertion
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker hat die Patient Identification, das Auflisten und den Abruf des Rezeptes erfolgreich durchgeführt.
    Der Apotheker hat das Medikament abgegeben und startet die Dispensierung des Rezeptes, aber bei der Nutzung der
    TRC Assertion tritt ein Fehler auf, so dass die KVNR syntaktisch fehlerhaft ist (falsche Prüfziffer).
    (Alternativ erfolgt ein missbräuchlicher Versuch der Nutzung einer falschen TRC Assertion).
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaDispenseInvalidPatientIdentifier'
    Referenz: "TUC_NCPeH_031: Überprüfung des Cross-Enterprise Document Reliable Interchange Request für eD-A auf
      allgemeine Vorgaben"
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27204

    Hinweis: Da zum Zeitpunkt der Operation bereits zweimal die Gültigkeit der TRC Assertion erfolgreich geprüft
      wurde, ist die Wahrscheinlichkeit des Auftretens einer falschen KVNR geringer. Die Prio des Szenarios
      wird deshalb auf 2 reduziert.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 3 durchgeführt
    Wenn der EU-Apotheker eine Dispensierung von E-Rezepten mit falscher Prüfziffer in der KVNR der TRC Assertion durchführt
    Dann enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für epedaDispenseInvalidPatientIdentifier
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_014
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_27881 @AFO-ID:A_27879
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) mit zu kurzem Zugriffscode
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker startet die Patient Identification, aber gibt dabei den Zugriffscode fehlerhaft ein, wobei
    dem Code eine Stelle fehlt (5 anstatt 6 alphanumerische Zeichen).
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaIdentifyInvalidAccessCode'
    Referenzen:
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27879
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27881
      TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A, 'die Variable xcpd_accesscode_erp DARF NICHT leer sein.'

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn der EU-Apotheker die Patient Identification mit einem zu kurzen Zugriffscode aufruft
    Dann erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaIdentifyInvalidAccessCode

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_015
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_27926 @AFO-ID:A_27901
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Auflisten eines Rezeptes (UC10) mit zu langem Zugriffscode
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker hat die Patient Identification erfolgreich durchgeführt, aber bei der Erstellung der TRC
    trat ein Fehler auf, so dass der Zugriffscode syntaktisch fehlerhaft ist (Zugriffscode ist zu lang aber
    alphanumerisch).
    Der Apotheker startet mit der fehlerhaften TRC Assertion die Auflistung der Rezepte.
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaSearchInvalidAccesscode'
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
       TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, Prüfung von trc_accesscode

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach E-Rezepten mit einem zu langen Zugriffscode in der TRC Assertion aufruft
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und das Suchergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaSearchInvalidAccesscode

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_016
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_27926 @AFO-ID:A_27901
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Auflisten eines Rezeptes (UC10), bei der Zugriffscode in der XCA Query nicht zum Wert in der TRC passt
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker hat die Patient Identification erfolgreich durchgeführt, die TRC Assertion wurde korrekt erstellt.
    Der Apotheker startet die Auflistung der Rezepte wobei aber in der XCA Query ein anderer Wert für den
    Zugriffscode steht, als in der TRC Assertion.
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaSearchInvalidAccesscode'
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
       TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, Prüfung von trc_accesscode und XDSDocumentEntryPatientId

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker nach den Rezepten der versicherten Person sucht und dabei ein anderer Zugriffscode als in der TRC Assertion zum Einsatz kommt
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und das Suchergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaSearchInvalidAccesscode

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_017
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_27987 @AFO-ID:A_27926
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Abruf eines Rezeptes (UC11) mit Sonderzeichen im Zugriffscode der TRC Assertion
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker hat die Patient Identification und das Auflisten der Rezepte erfolgreich durchgeführt.
    Der Apotheker startet den Abruf der Rezepte, aber bei der Nutzung der TRC Assertion tritt ein Fehler auf, so dass
    die Zugriffscode syntaktisch fehlerhaft ist (kein alphanumerischer String).
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaRetrieveInvalidAccesscode'
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27926
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
      TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A, Wert der Variable trc_accesscode

    Hinweis: Da zum Zeitpunkt der Operation bereits einmal die Gültigkeit der TRC Assertion erfolgreich geprüft
      wurde, ist die Wahrscheinlichkeit des Auftretens eines falschen Accesscodes geringer. Die Prio des Szenarios
      wird deshalb auf 2 reduziert.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Rezepte der versicherten Person abruft und dabei kein alphanumerischer Zugriffscode in der TRC Assertion zum Einsatz kommt
    Dann erhält der EU-Apotheker kein Rezept ausgegeben
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrieveInvalidAccesscode
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_018
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_27206
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A eRP-IOP Dispensierung (UC12) ohne Zugriffscode in der TRC Assertion
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker hat die Patient Identification, das Auflisten und den Abruf des Rezeptes erfolgreich durchgeführt.
    Der Apotheker hat das Medikament abgegeben und startet die Dispensierung des Rezeptes, aber bei der Nutzung der
    TRC Assertion tritt ein Fehler auf, so dass der Zugriffscode komplet fehlt.
    (Alternativ erfolgt ein missbräuchlicher Versuch der Nutzung einer falschen TRC Assertion).
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine Fehlermeldung für die Situation
      'epedaDispenseInvalidAccesscode'
    Referenz: "TUC_NCPeH_031: Überprüfung des Cross-Enterprise Document Reliable Interchange Request für eD-A auf
      allgemeine Vorgaben", Afo A_27206-*
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27206

    Hinweis: Da zum Zeitpunkt der Operation bereits zweimal die Gültigkeit der TRC Assertion erfolgreich geprüft
      wurde, ist die Wahrscheinlichkeit des Auftretens eines falschen Accesscodes geringer. Die Prio des Szenarios
      wird deshalb auf 2 reduziert.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat keinen Abruf des Rezeptes durchgeführt
    Wenn der EU-Apotheker die Rezepte der versicherten Person dispensiert und dabei der Zugriffscode in der TRC Assertion fehlt
    Dann enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für epedaDispenseInvalidAccesscode
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress


########################################################################################
########           Prüfung der Auswertung von LE-EU Permissions codes           ########
########################################################################################

# Hinweis: der Standardfall - gültige Permission Codes sind vorhanden - wird in den fachlichen Tests bereits regelmäßig
# mit geprüft und deshalb hier nicht weiter betrachtet

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_020
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_27881 @AFO-ID:A_28083
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) und IdA mit unzureichenden Permission Codes
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der EU-Apotheker ruft über den NCPeH die persönlichen Informationen der versicherten Person ab, wobei
    die Identity Assertion aus dem Land B Permission Codes enthält, aber der Zugriff für "Patient Identification"
    PRD-006 nicht mit enthalten ist.
    Die Ausführung der Patient Identification Operation wird wegen fehlender Berechtigung mit der Fehlermeldung
    für die Situation 'epedaIdentifyIncompletePermissionCodes' vom NCPeH-FD abgelehnt.
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.1.1.3
      TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn der EU-Apotheker für die versicherte Person die Patient Identification aufruft und ihm das Recht für Patient Identification in der IdA fehlt
    Dann erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaIdentifyIncompletePermissionCodes

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_021
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_28083 @AFO-ID:A_27901
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Auflisten der Rezepte (UC10) und IdA mit unzureichenden Permission Codes
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der EU-Apotheker ruft über den NCPeH die persönlichen Informationen der versicherten Person ab, wobei
    die Identity Assertion aus dem Land B Permission Codes enthält, aber der Zugriff für "Request for ePrescriptions"
    PRD-004 und PRD-010 nicht mit enthalten ist.
    Die Ausführung des Auflistens von Rezepten wird wegen fehlender Berechtigung mit der Fehlermeldung für die Situation
    'epedaSearchIncompletePermissionCodes' vom NCPeH-FD abgelehnt.
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28083
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
      TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, Prüfung von ida_permissions, wenn nicht leer

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker nach den Rezepten der versicherten Person sucht und ihm das Recht für ePrescription in der IdA fehlt
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und das Suchergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaSearchIncompletePermissionCodes

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_022
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_27987 @AFO-ID:A_28083
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Abruf eines Rezeptes (UC11) und IdA mit unzureichenden Permission Codes
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der EU-Apotheker ruft über den NCPeH die persönlichen Informationen der versicherten Person ab, wobei
    in der Identity Assertion aus dem Land B Permission Codes enthält, aber der Zugriff für "Request for ePrescriptions"
    PRD-004 und PRD-010 nicht mit enthalten ist.
    Die Ausführung des Abrufs eines Rezeptes wird wegen fehlender Berechtigung mit der Fehlermeldung für die Situation
    'epedaRetrieveIncompletePermissionCodes' vom NCPeH-FD abgelehnt.
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28083
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
      TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A, Variable ida_permissions, falls sie nicht leer ist

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker die Rezepte der versicherten Person abruft und ihm das Recht für ePrescription in der IdA fehlt
    Dann erhält der EU-Apotheker kein Rezept ausgegeben
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrieveIncompletePermissionCodes
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_023
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_26652
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Dispensierung eines Rezeptes (UC12) und IdA mit unzureichenden Permission Codes
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der EU-Apotheker sendet die Dispensier-Information der versicherten Person über den NCPeH ab, wobei
    in der Identity Assertion aus dem Land B Permission Codes enthält, aber der Zugriff für "Notification on Dispensation"
    PPD-046 nicht mit enthalten ist.
    Die Ausführung der Dispensierung des Rezeptes wird wegen fehlender Berechtigung mit der Fehlermeldung für
    die Situation 'epedaDispenseIncompletePermissionCodes' vom NCPeH-FD abgelehnt.
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_26652

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 3 durchgeführt
    Wenn der EU-Apotheker die Rezepte der versicherten Person dispensiert und ihm das Recht für eDispensation in der IdA fehlt
    Dann enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für epedaDispenseIncompletePermissionCodes
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress


########################################################################################
########             Prüfung der Auswertung von LE-EU Rollen codes              ########
########################################################################################

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_030
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_28086
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  @CI
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) mit IdA ohne Permission Codes und mit gültiger Rolle "Pharmacists"
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der EU-Apotheker ruft über den NCPeH die persönlichen Informationen der versicherten Person ab, wobei
    in der Identity Assertion aus dem Land B keine Permission Codes für den Apotheker mitgeliefert wird. In der
    Identity Assertion ist als Rolle des Leistungserbringers der für DE zulässige Code für "Pharmacists" eingetragen.
    Die Ausführung der Patient Identification ist erfolgreich.
    Die zurückgegebenen Werte (in der XCPD-Response) müssen mit den im E-Rezept des Versicherten hinterlegten Werten
    übereinstimmen (KVNR konkateniert mit Zugriffscode, Geburtsdatum, Vorname, Nachname).

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn ein LE-EU mit der Rolle Pharmacists und ohne PermissionCodes die Patient Identifikation aufruft
    Dann erhält der EU-Apotheker als Antwort die KVNR mit dem Zugriffscode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem E-Rezept der versicherten Person zurück

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_031
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_28086
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  @CI
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) mit IdA ohne Permission Codes und mit gültiger Rolle "Pharmaceutical technicians"
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der EU-Apotheker ruft über den NCPeH die persönlichen Informationen der versicherten Person ab, wobei
    in der Identity Assertion aus dem Land B keine Permission Codes für den Apotheker mitgeliefert wird. In der
    Identity Assertion ist als Rolle des Leistungserbringers der für DE zulässige Code für "Pharmaceutical technicians"
    eingetragen. Die Ausführung der Patient Identification ist erfolgreich.
    Die zurückgegebenen Werte (in der XCPD-Response) müssen mit den im E-Rezept des Versicherten hinterlegten Werten
    übereinstimmen (KVNR konkateniert mit Zugriffscode, Geburtsdatum, Vorname, Nachname).

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn ein LE-EU mit der Rolle Pharmaceutical technicians und ohne PermissionCodes die Patient Identifikation aufruft
    Dann erhält der EU-Apotheker als Antwort die KVNR mit dem Zugriffscode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem E-Rezept der versicherten Person zurück

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_032
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_27881 @AFO-ID:A_28086
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) mit IdA ohne Permission Codes und mit ungültiger Rolle "Medical Doctors"
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der EU-Apotheker ruft über den NCPeH die persönlichen Informationen der versicherten Person ab, wobei
    in der Identity Assertion aus dem Land B keine Permission Codes für den Apotheker mitgeliefert wird. In der
    Identity Assertion ist als Rolle des Leistungserbringers der für DE unzulässige Code für "Medical Doctors"
    eingetragen.
    Die Ausführung der Patient Identification Operation wird wegen fehlender Berechtigung mit der Fehlermeldung
    für die Situation 'epedaIdentifyInvalidHpRoleCode' vom NCPeH-FD abgelehnt.
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#6.1.1.3
      TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn ein LE-EU mit der Rolle Medical Doctors und ohne PermissionCodes die Patient Identifikation aufruft
    Dann erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaIdentifyInvalidHpRoleCode

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_033
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_28086 @AFO-ID:A_27901
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Auflisten eines Rezeptes (UC10) mit IdA ohne Permission Codes und mit ungültiger Rolle "Dentists"
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der LE-EU hat erfolgreich die Patient Identification durchgeführt.
    Danach muss sich jedoch der LE-EU muss sich erneut im Land-B authentisieren (IdA abgelaufen), wobei in der Identity
    Assertion aus dem Land B keine Permission Codes für den Apotheker mitgeliefert wird. In der Identity Assertion ist
    als Rolle des Leistungserbringers der für DE unzulässige Code für "Dentists" eingetragen.
    Der EU-Apotheker listet über den NCPeH die einlösbaren Rezepte der versicherten Person auf.
    Die Ausführung des Auflistens von Rezepten wird wegen fehlender Berechtigung mit der Fehlermeldung für die Situation
    'epedaSearchInvalidHpRoleCode' vom NCPeH-FD abgelehnt.
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
      TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, Prüfung bei leerer Variable ida_permissions

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn ein LE-EU mit der Rolle Dentists und ohne PermissionCodes die Rezepte der versicherten Person sucht
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und das Suchergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaSearchInvalidHpRoleCode

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_034
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_27987 @AFO-ID:A_28086
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Abruf eines Rezeptes (UC11) mit IdA ohne Permission Codes und mit ungültiger Rolle "Physiotherapists"
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der LE-EU hat erfolgreich die Patient Identification und die Auflistung der einlösbaren Rezepte durchgeführt.
    Danach muss sich jedoch der LE-EU muss sich erneut im Land-B authentisieren (IdA abgelaufen), wobei in der Identity
    Assertion aus dem Land B keine Permission Codes für den Apotheker mitgeliefert wird. In der Identity Assertion ist
    als Rolle des Leistungserbringers der für DE unzulässige Code für "Physiotherapists" eingetragen.
    Der EU-Apotheker ruft über den NCPeH die einzulösenden Rezepte der versicherten Person ab.
    Die Ausführung des Abrufs eines Rezeptes wird wegen fehlender Berechtigung mit der Fehlermeldung für die Situation
    'epedaRetrieveInvalidHpRoleCode' vom NCPeH-FD abgelehnt.
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28086
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
      TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A, Die Variable ida_permissions ist leer

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn ein LE-EU mit der Rolle Physiotherapists und ohne PermissionCodes die Rezepte der versicherten Person abruft
    Dann erhält der EU-Apotheker kein Rezept ausgegeben
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrieveInvalidHpRoleCode
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_035
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_26699
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Dispensierung eines Rezeptes (UC12) mit IdA ohne Permission Codes und mit ungültiger Rolle "Nursing professionals"
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der LE-EU hat erfolgreich die Patient Identification, die Auflistung und den Abruf der einlösbaren Rezepte
    durchgeführt.
    Danach muss sich jedoch der LE-EU muss sich erneut im Land-B authentisieren (IdA abgelaufen), wobei in der Identity
    Assertion aus dem Land B keine Permission Codes für den Apotheker mitgeliefert wird. In der Identity Assertion ist
    als Rolle des Leistungserbringers der für DE unzulässige Code für "Nursing professionals" eingetragen.
    Der EU-Apotheker sendet über den NCPeH die Dispensier-Information der an die versicherte Person abgegebenen Rezepte
    ab. Die Ausführung des Abrufs eines Rezeptes wird wegen fehlender Berechtigung mit der Fehlermeldung für die
    Situation 'epedaDispenseInvalidHpRoleCode' vom NCPeH-FD abgelehnt.
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_26699

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 3 durchgeführt
    Wenn ein LE-EU mit der Rolle Nursing professionals und ohne PermissionCodes die Rezepte der versicherten Person dispensiert
    Dann enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für epedaDispenseInvalidHpRoleCode
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress

########################################################################################
######## Prüfung der Auswertung auf Konsistenz zwischen IdA- und TRC-assertion  ########
########################################################################################

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_040
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_27926
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Auflisten der Rezepte (UC10) wobei die NameId in der TRC Assertion nicht zur Identity Assertion passt
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der LE-EU hat erfolgreich die Patient Identification durchgeführt.
    Danach muss sich jedoch der LE-EU erneut im Land-B authentisieren (IdA abgelaufen, Apotheker wechselt oder
    Missbrauch einer fremden IdA), so dass in der Identity Assertion aus dem Land B eine andere NameId für den
    Apotheker mitgeliefert wird.
    Die NameId in der Identity Assertion ist demzufolge ungleich der NameId in der TRC Assertion, so dass beide
    Assertions nicht zusammen passen und der Zugriff nicht gestattet werden kann.
    Der EU-Apotheker listet über den NCPeH die einlösbaren Rezepte der versicherten Person ab.
    Die Ausführung des Auflistens von Rezepten wird wegen fehlender Übereinstimmung mit der Fehlermeldung für nicht
    übereinstimmende LE-EU Identifier vom NCPeH-FD abgelehnt.
    Referenzen: [eHDSI_SAML_Profile#4.1 "Generic Structure of the Treatment Relationship Assertion"], Element NameID;
      [eHDSI_Messaging_Profile#6.2.4 Security Header Encoding and Consistency Errors];
      [eHDSI_Messaging_Profile#6.2.1 SOAP Error Profile];
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27926,
        TAB_NCPeH_Zwischenspeicherung_Daten_TRC-Assertion, Definition Variable trc_nameid

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker nach Rezepten der versicherten Person sucht und der LE-Identifier in der TRC Assertion nicht zu dem in der Identity Assertion passt
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und der EU-Apotheker erhält eine allgemeine Fehlermeldung für noMatchingNameIDs

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_041
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_27926 @AFO-ID:A_28561
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Abruf eines Rezeptes (UC11) wobei das Format der NameId in TRC Assertion nicht zur Identity Assertion passt
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der LE-EU hat erfolgreich die Patient Identification und die Auflistung einlösbarer Rezepte durchgeführt.
    Danach muss sich jedoch der LE-EU erneut im Land-B authentisieren (IdA abgelaufen, Apotheker wechselt oder
    Missbrauch einer fremden IdA), so dass in der Identity Assertion aus dem Land B ein anderes Format der NameId für
    den Apotheker mitgeliefert wird.
    Die NameId in der Identity Assertion ist demzufolge ungleich der NameId in der TRC Assertion, so dass beide
    Assertions nicht zusammen passen und der Zugriff nicht gestattet werden kann.
    Der EU-Apotheker ruft über den NCPeH das einlösbare Rezepte der versicherten Person ab.
    Die Ausführung des Abrufs von Rezepten wird wegen fehlender Übereinstimmung mit der Fehlermeldung für nicht
    übereinstimmende LE-EU Identifier vom NCPeH-FD abgelehnt.
    Referenzen: [eHDSI_SAML_Profile#4.1 "Generic Structure of the Treatment Relationship Assertion"], Element NameID;
      [eHDSI_Messaging_Profile#6.2.4 Security Header Encoding and Consistency Errors];
      [eHDSI_Messaging_Profile#6.2.1 SOAP Error Profile];
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28561

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker die Rezepte der versicherten Person abruft und der Typ des LE-Identifiers in der TRC Assertion nicht zu dem in der Identity Assertion passt
    Dann erhält der EU-Apotheker kein Rezept ausgegeben
    Und der EU-Apotheker erhält eine allgemeine Fehlermeldung für noMatchingNameIdFormats
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_042
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_28561
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Dispensierung eines Rezeptes (UC12) wobei die NameId in der TRC Assertion nicht zur Identity Assertion passt
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der LE-EU hat erfolgreich die Patient Identification, das Auflisten einlösbarer Rezepte und den Abruf des
    einzulösenden Rezeptes durchgeführt.
    Danach muss sich jedoch der LE-EU erneut im Land-B authentisieren (IdA abgelaufen, Apotheker wechselt oder
    Missbrauch einer fremden IdA), so dass in der Identity Assertion aus dem Land B eine andere NameId für den
    Apotheker mitgeliefert wird.
    Die NameId in der Identity Assertion ist demzufolge ungleich der NameId in der TRC Assertion, so dass beide
    Assertions nicht zusammen passen und der Zugriff nicht gestattet werden kann.
    Der EU-Apotheker sendet die Dispensierinformation zum abgegebenen Rezept der versicherten Person über den NCPeH ab.
    Die Ausführung des Abrufs von Rezepten wird wegen fehlender Übereinstimmung mit der Fehlermeldung für nicht
    übereinstimmende LE-EU Identifier vom NCPeH-FD abgelehnt.
    Referenzen: [eHDSI_SAML_Profile#4.1 "Generic Structure of the Treatment Relationship Assertion"], Element NameID;
      [eHDSI_NCPeH_Components_Specifications#4.4.2.4 Security Header Encoding and Consistency Errors];
      [eHDSI_NCPeH_Components_Specifications#4.4.2.1 SOAP Error Profile];
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28561

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 3 durchgeführt
    Wenn der EU-Apotheker die Rezepte der versicherten Person dispensiert und der LE-Identifier in der TRC Assertion nicht zu dem in der Identity Assertion passt
    Und der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet
    Dann erhält der EU-Apotheker eine allgemeine Fehlermeldung für noMatchingNameIDs
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress

########################################################################################
########            Prüfung auf Auswertung des Ländercodes für ePeD-A           ########
########################################################################################

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_050
  @STATUS:Implementiert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_28067 @AFO-ID:A_27918 @AFO-ID:A_27921
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) für EU-Land mit vereinbartem Zugriff auf ePeD-A
  Eine in Deutschland versicherte Person besucht einen EU-Mitgliedsstaat, für den mit Deutschland der Austausch von
  Gesundheitsdaten für die Szenarien Patient Summary und ePrescription/eDispensation vereinbart ist.
  Die vers. Person kann in ihrem FdV die Berechtigung zum Zugriff auf seine Rezepte für das EU-Land erteilen und
  übergibt die Daten (KVNR, Zugriffscode) an den EU-Apotheker.
  Der EU-Apotheker startet eine Patient Identification für ePeD und erhält die persönlichen Daten der vers. Person
  zurück.
  Die zurückgegebenen Werte (in der XCPD-Response) müssen mit den im E-Rezept des Versicherten hinterlegten Werten
  übereinstimmen (KVNR konkateniert mit Zugriffscode, Geburtsdatum, Vorname, Nachname).

  Hinweis: Für das EU-Land muss vorab konfigurativ im NCPeH und im VZD sichergestellt sein, dass beide Szenarien
  verfügbar sind

    Angenommen das EU-Land BELGIUM ist nur für das Szenario ePeD-A freigeschaltet und konfiguriert
    Und für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn die EU-Apothekerin für die versicherte Person die Patient Identification aufruft
    Dann erhält die EU-Apothekerin als Antwort die KVNR mit dem Zugriffscode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem E-Rezept der versicherten Person zurück

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_051
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_28067 @AFO-ID:A_27918 @AFO-ID:A_27921
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) für EU-Land mit vereinbarten Zugriff auf ePeD-A und PS-A
    Eine in Deutschland versicherte Person besucht einen EU-Mitgliedsstaat, für den mit Deutschland der Austausch von
    Gesundheitsdaten für die Szenarien Patient Summary und ePrescription/eDispensation vereinbart ist.
    Die vers. Person kann in ihrem FdV die Berechtigung zum Zugriff auf seine Rezepte für das EU-Land erteilen und
    übergibt die Daten (KVNR, Zugriffscode) an den EU-Apotheker.
    Der EU-Apotheker startet eine Patient Identification für ePeD und erhält die persönlichen Daten der vers. Person
    zurück.
    Die zurückgegebenen Werte (in der XCPD-Response) müssen mit den im E-Rezept des Versicherten hinterlegten Werten
    übereinstimmen (KVNR konkateniert mit Zugriffscode, Geburtsdatum, Vorname, Nachname).

    Hinweis: Für das EU-Land muss vorab konfigurativ im NCPeH und im VZD sichergestellt sein, dass beide Szenarien
      verfügbar sind

    Angenommen das EU-Land LATVIA ist für die Szenarien PS-A und ePeD-A freigeschaltet und konfiguriert
    Und für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land LATVIA in eine Apotheke zum Apotheker Jānis Liepiņš
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an die EU-Apothekerin übergeben
    Wenn die EU-Apothekerin für die versicherte Person die Patient Identification aufruft
    Dann erhält die EU-Apothekerin als Antwort die KVNR mit dem Zugriffscode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem E-Rezept der versicherten Person zurück

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_052
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_28067 @AFO-ID:A_27918 @AFO-ID:A_27921 @AFO-ID:A_27881
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) für EU-Land mit vereinbarten Zugriff nur auf PS-A
    Eine in Deutschland versicherte Person besucht einen EU-Mitgliedsstaat, für den mit Deutschland der Austausch von
    Gesundheitsdaten für die Szenarien Patient Summary vereinbart ist.
    Die vers. Person hat fälschlich in ihrem FdV die Berechtigung zum Zugriff auf Patient Summary für das EU-Land
    erteilt und übergibt die Daten (KVNR, Zugriffscode) an den EU-Apotheker.
    Der EU-Apotheker startet über den NCPeH die Patient Identification für ePeD-A zur versicherten Person.
    Die Ausführung der Patient Identification Operation wird wegen fehlender Freischaltung im NCPeH mit der
    Fehlermeldung für die Situation 'epedaIdentifyInvalidCountryScenario' vom NCPeH-FD abgelehnt.
    Referenzen: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27921 und
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27881
      TAB_NCPeH_XCPD_Prüfschritte_Fehlermeldungen_ePeD-A

    Hinweis: Für das EU-Land muss vorab konfigurativ im NCPeH und im VZD sichergestellt sein, dass nur das Szenario
      für PS-A verfügbar ist.

    Angenommen das EU-Land BULGARIA ist nur für das Szenario PS-A freigeschaltet und konfiguriert
    Und  für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BULGARIA in eine Apotheke zum Apotheker Asparuhk Michailow
    Und die versicherte Person hat irrtümlich den Zugriff dieses EU-Landes auf Patient Summary Daten berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn der EU-Apotheker für die versicherte Person die Patient Identification aufruft
    Dann erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaIdentifyInvalidCountryScenario

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_053
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_27921 @AFO-ID:A_27901
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Auflisten der Rezepte (UC10) für EU-Land mit vereinbarten Zugriff nur auf PS-A
    Von einem nicht für ePeD-A freigeschalteten Land-B wird missbräuchlich eine FindDocuments-Query für ePeD-A an den
    NCPeH-FD geschickt.
    Die Ausführung der FindDocuments-Query wird wegen fehlender Freischaltung im NCPeH mit der Fehlermeldung
    für die Situation 'epedaSearchInvalidCountryScenario' vom NCPeH-FD abgelehnt.
    Referenzen: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27921
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27901
      TAB_NCPeH_XCA_QUERY_ePeD-A_Fehlerbedingungen_Fehlercodes, Prüfung LOINC-Code in ALLOWEDLIST_NCPeH_COUNTRY-B

    Hinweise:
      1. Für das EU-Land muss vorab konfigurativ im NCPeH und im VZD sichergestellt sein, dass nur das Szenario
      für ePeD-A verfügbar ist.
      2. Die Teststeps sind hier aus Business-Sicht NICHT durchgehend, weil hier tatsächlich nur ein
      Missbrauch / fälschlicher Kontakt eines NCPeH Land-B vorliegen kann. Dieser Test wird nur durchgeführt,
      um die Sicherheit zu haben, dass in allen Situationen die EU-Country Prüfung wirksam ist und die vorgegebene
      Fehlermeldung als Antwort geliefert wird. Die einleitenden Teststeps dienen der Schaffung einer ausreichenden
      Ausgangssituation, um den Fehler provozieren zu können.

    Angenommen das EU-Land BULGARIA ist nur für das Szenario PS-A freigeschaltet und konfiguriert
    Und  für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BULGARIA in eine Apotheke zum Apotheker Asparuhk Michailow
    Und die versicherte Person hat irrtümlich den Zugriff dieses EU-Landes auf Patient Summary Daten berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    # keine Durchführung der Patient Identification, da diese bereits fehlschlagen müsste!
    Wenn der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und das Suchergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaSearchInvalidCountryScenario

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_054
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_28067 @AFO-ID:A_27921 @AFO-ID:A_27987
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Abruf eines Rezeptes (UC11) für EU-Land mit vereinbarten Zugriff nur auf PS-A
    Von einem nicht für ePeD-A freigeschalteten Land-B wird missbräuchlich ein RetrieveDocument-Request für ePeD-A an den
    NCPeH-FD geschickt.
    Die Ausführung der RetrieveDocument-Operation wird wegen fehlender Freischaltung im NCPeH mit der Fehlermeldung
    für die Situation 'epedaRetrieveInvalidCountryScenario' vom NCPeH-FD abgelehnt.
    Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28067
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27921
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27987
      TAB_NCPeH_Fehlerfälle_Verarbeitung_Cross_Gateway_Retrieve_Request_ePeD-A, Prüfung LOINC-Code in ALLOWEDLIST_NCPeH_COUNTRY-B

    Hinweise:
      1. Für das EU-Land muss vorab konfigurativ im NCPeH und im VZD sichergestellt sein, dass nur das Szenario
      für ePeD-A verfügbar ist.
      2. Die Teststeps sind hier aus Business-Sicht NICHT durchgehend, weil hier tatsächlich nur ein
      Missbrauch / fälschlicher Kontakt eines NCPeH Land-B vorliegen kann. Dieser Test wird nur durchgeführt,
      um die Sicherheit zu haben, dass in allen Situationen die EU-Country Prüfung wirksam ist und die vorgegebene
      Fehlermeldung als Antwort geliefert wird. Die einleitenden Teststeps dienen der Schaffung einer ausreichenden
      Ausgangssituation, um den Fehler provozieren zu können.

    Angenommen das EU-Land BULGARIA ist nur für das Szenario PS-A freigeschaltet und konfiguriert
    Und  für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BULGARIA in eine Apotheke zum Apotheker Asparuhk Michailow
    Und die versicherte Person hat irrtümlich den Zugriff dieses EU-Landes auf Patient Summary Daten berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    # zur Vereinfachung des Testablaufs keine Durchführung der Patient Identification und keine Durchführung
    # der Auflistung von Rezepten, um eine sonst nötige Umkonfiguration am NCPeH-FD zu vermeiden!
    Wenn der EU-Apotheker das E-Rezept der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker kein Rezept ausgegeben
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrieveInvalidCountryScenario
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_055
  @STATUS:Zurückgestellt @MODUS:Halbautomatisch @PRIO:3 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_28067 @AFO-ID:A_27921 @AFO-ID:A_27203-01
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Dispensierung eines Rezeptes (UC12) für EU-Land mit vereinbarten Zugriff nur auf PS-A
    Von einem nicht für ePeD-A freigeschalteten Land-B wird missbräuchlich ein ProvideAndRegisterDocumentSet-b-Request
    für ePeD-A an den NCPeH-FD geschickt.
    Die Ausführung der ProvideAndRegisterDocumentSet-b-Operation wird wegen fehlender Freischaltung im NCPeH mit der
    Fehlermeldung für die Situation 'epedaDispenseInvalidCountryScenario' vom NCPeH-FD abgelehnt.
    Referenzen: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28067
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27921
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27203-01

    # Zurückgestellt, weil aktuell nicht durchführbar wegen Limitation im Land-B Simulator (Simulator benötigt ein zuvor
    # abgerufenes Rezept zur Erstellung einer Dispensierung, was hier jedoch nicht gegeben ist.

    Angenommen das EU-Land BULGARIA ist nur für das Szenario PS-A freigeschaltet und konfiguriert
    Und für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BULGARIA in eine Apotheke zum Apotheker Asparuhk Michailow
    Und die versicherte Person hat irrtümlich den Zugriff dieses EU-Landes auf Patient Summary Daten berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    # keine Durchführung der Patient Identification, da diese bereits fehlschlagen müsste!
    # keine Durchführung der Auflistung von Rezepten, da diese bereits fehlschlagen müsste!
    # keine Durchführung des Abrufs von Rezepten, da diese bereits fehlschlagen müsste!
    Und der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet
    Dann enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für epedaDispenseInvalidCountryScenario
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress

#####################################################################################################################
####### Prüfung auf Fehlerbehandlung bei Mismatch Country-Code im TLS- und SAML-Signer-Zertifikat für ePeD-A ########
#####################################################################################################################

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_060
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_28457
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Patient Identification (UC9) mit Mismatch CountryCode zwischen TLS-Zert und IdA-Signer-Zert
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker startet die Patient Identification, aber die Identity Assertion ist mit einem Signer Zertifikat
    signiert, dessen Country-Code im Subject nicht mit dem Country-Code aus dem Subject des TLS-Zertifikates
    übereinstimmt.
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine SOAP-Fehlermeldung für die Situation
    'idaCountryCodeMismatch'
      Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28457

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn der EU-Apotheker die Patient Identification mit CountryCode für NETHERLANDS im Signerzertifikat der IdA aufruft
    Dann erhält der EU-Apotheker eine allgemeine Fehlermeldung für idaCountryCodeMismatch

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_NCP_061
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_28560
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A NCP-IOP Auflisten eines einlösbaren E-Rezeptes (UC10) mit Mismatch CountryCode zwischen TLS-Zert und TRC-Signer-Zert
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Der Apotheker startet die Patient Identification, aber die Identity Assertion ist mit einem Signer Zertifikat
    signiert, dessen Country-Code im Subject nicht mit dem Country-Code aus dem Subject des TLS-Zertifikates
    übereinstimmt.
    Als Antwort erhält der NCPeH-B / EU-Apotheker eine SOAP-Fehlermeldung für die Situation
    'trcCountryCodeMismatch'
      Referenz: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28560

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach den E-Rezepten mit CountryCode für NETHERLANDS im Signerzertifikat der TRC aufruft
    Dann erhält der EU-Apotheker eine allgemeine Fehlermeldung für trcCountryCodeMismatch
