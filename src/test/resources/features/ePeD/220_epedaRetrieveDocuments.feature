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
Funktionalität: NCPeH ePeD Land A: NCPeH.UC_11 - Ausgewählte E-Rezepte aus ePeD-A abrufen
  AF_10400 - "Ausgewählte E-Rezepte abrufen"
  In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
  Apotheker ("EU-Apotheker"), um bei ihm ein E-Rezept einzulösen (Behandlungskontext).
  Der EU-Apotheker möchte nach seiner Beratung mit der versicherten Person die ausgewählten, einlösbaren E-Rezepte
  abrufen. (eHDSI Anwendungsszenario "ePrescription", Land A)
  Die vers. Person hat mit seinem FdV die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert, ein oder mehrere
  E-Rezepte zum Abruf in der EU ausgewählt und das EU-Land berechtigt.
  Die Identifikation des Patienten (UC_9) und das Auflisten der einlösbaren Rezepte wurden vom EU-Apotheker
  erfolgreich durchgeführt. Der NCPeH Land B hat intern eine Treatment Relationship Confirmation (TRC) erstellt.
  In diesem nachfolgenden Use Case soll der EU-Apotheker die im E-Rezept Fachdienst einlösbaren Rezepte
  der versicherten Person abrufen (vollständige Bereitstellung der Rezept-Daten für einzulösende Rezepte).


  @TCID:NCP2_E2E_ePeDA_UC11_001
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10400
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC11 Ausgewähltes E-Rezept einer versicherten Person als CDA3 abrufen
    Der EU-Apotheker hat sich mit der versicherten Person auf die Abgabe eines Rezeptes geeinigt und
    startet den Abruf dieses E-Rezeptes als CDA Level 3. Als Antwort erhält der EU-Apotheker das E-Rezept
    als strukturiertes Dokument (CDA Level 3) ausgegeben.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker das E-Rezept der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker 1 Rezept-Dokument zurück
    Und 1 Rezept-Dokument ist im Format CDA Level 3
    Und das Rezept-Dokument als CDA 3 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress

  @TCID:NCP2_E2E_ePeDA_UC11_002
  @STATUS:Implementiert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10400
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  Szenario: NCPeH - ePeD-A UC11 Ausgewähltes E-Rezept einer versicherten Person als CDA1 abrufen
    Der EU-Apotheker hat sich mit der versicherten Person auf die Abgabe eines Rezeptes geeinigt und
    startet den Abruf dieses E-Rezeptes als CDA Level 1. Als Antwort erhält der EU-Apotheker das E-Rezept
    als PDF-Dokument (CDA Level 1) ausgegeben.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker das E-Rezept der versicherten Person als CDA Level 1 abruft
    Dann erhält der EU-Apotheker 1 Rezept-Dokument zurück
    Und 1 Rezept-Dokument ist im Format CDA Level 1
    Und das Rezept-Dokument als CDA 1 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress

  @TCID:NCP2_E2E_ePeDA_UC11_003
  @STATUS:Implementiert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10400
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  Szenario: NCPeH - ePeD-A UC11 Ausgewähltes E-Rezept einer versicherten Person gleichzeitig als CDA3 und CDA1 abrufen
    Der EU-Apotheker hat sich mit der versicherten Person auf die Abgabe eines Rezeptes geeinigt und
    startet den Abruf dieses E-Rezeptes sowohl als CDA Level 1 und Level 3. Als Antwort erhält der EU-Apotheker das E-Rezept
    als PDF-Dokument (CDA Level 1) und als XML-Dokument (CDA Level 3) ausgegeben.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker das E-Rezept der versicherten Person als CDA Level 1 und 3 abruft
    Dann erhält der EU-Apotheker 2 Rezept-Dokumente zurück
    Und 1 Rezept-Dokument ist im Format CDA Level 1
    Und das Rezept-Dokument als CDA 1 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und 1 Rezept-Dokument ist im Format CDA Level 3
    Und das Rezept-Dokument als CDA 3 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress

  @TCID:NCP2_E2E_ePeDA_UC11_004
  @STATUS:Implementiert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10400
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  Szenario: NCPeH - ePeD-A UC11 Ausgewähltes E-Rezept einer versicherten Person nacheinander als CDA1 und CDA3 abrufen
    Der EU-Apotheker hat sich mit der versicherten Person auf die Abgabe eines Rezeptes geeinigt und
    startet den Abruf dieses E-Rezeptes sowohl als CDA Level 1 und Level 3. Als Antwort erhält der EU-Apotheker das E-Rezept
    als PDF-Dokument (CDA Level 1) und als XML-Dokument (CDA Level 3) ausgegeben.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker das E-Rezept der versicherten Person als CDA Level 1 abruft
    Dann erhält der EU-Apotheker 1 Rezept-Dokumente zurück
    Und 1 Rezept-Dokument ist im Format CDA Level 1
    Und das Rezept-Dokument als CDA 1 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress
    Wenn der EU-Apotheker danach das E-Rezept der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker 1 Rezept-Dokument zurück
    Und 1 Rezept-Dokument ist im Format CDA Level 3
    Und das Rezept-Dokument als CDA 3 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC11_010
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10400
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC11 Zwei ausgewählte E-Rezepte einer versicherten Person als CDA3 abrufen
    Für die versicherte Person sind zwei in der EU einlösbare E-Rezepte im E-Rezept FD eingestellt.
    Der EU-Apotheker hat sich mit der versicherten Person auf die Abgabe beider Rezepte geeinigt und
    startet den Abruf dieser E-Rezepte als CDA Level 3. Als Antwort erhält der EU-Apotheker die beiden
    E-Rezepte als strukturierte Dokumente (CDA Level 3) ausgegeben.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | ja                  |
      | Ibuflam 600mg                                  | 06313390 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker die E-Rezepte der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker 2 Rezept-Dokumente zurück
    Und 2 Rezept-Dokumente sind im Format CDA Level 3
    Und jedes Rezept-Dokument als CDA 3 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung
    Und der Verarbeitungsstatus der erfolgreich abgerufenen Rezepte ist gesetzt

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC11_011
  @STATUS:Implementiert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10400
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  Szenario: NCPeH - ePeD-A UC11 Zwei ausgewählte E-Rezepte einer versicherten Person als CDA1 abrufen
    Für die versicherte Person sind zwei in der EU einlösbare E-Rezepte im E-Rezept FD eingestellt.
    Der EU-Apotheker hat sich mit der versicherten Person auf die Abgabe beider Rezepte geeinigt und
    startet den Abruf dieser E-Rezepte als CDA Level 1. Als Antwort erhält der EU-Apotheker die beiden
    E-Rezepte als PDF-Dokumente (CDA Level 1) ausgegeben.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | ja                  |
      | Ibuflam 600mg                                  | 06313390 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker die E-Rezepte der versicherten Person als CDA Level 1 abruft
    Dann erhält der EU-Apotheker 2 Rezept-Dokumente zurück
    Und 2 Rezept-Dokumente sind im Format CDA Level 1
    Und jedes Rezept-Dokument als CDA 1 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung
    Und der Verarbeitungsstatus der erfolgreich abgerufenen Rezepte ist gesetzt

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC11_012
  @STATUS:Implementiert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10400
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  Szenario: NCPeH - ePeD-A UC11 Zwei ausgewählte E-Rezepte einer versicherten Person als CDA1 und CDA3 abrufen
    Für die versicherte Person sind zwei in der EU einlösbare E-Rezepte im E-Rezept FD eingestellt.
    Der EU-Apotheker hat sich mit der versicherten Person auf die Abgabe beider Rezepte geeinigt und
    startet den Abruf dieser E-Rezepte als CDA Level 1 und 3. Als Antwort erhält der EU-Apotheker die beiden
    E-Rezepte als PDF- und XML-Dokumente ausgegeben (2*2 = 4 Dokumente insgesamt).

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
      | Ibuflam 600mg                                  | 06313390 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker die E-Rezepte der versicherten Person als CDA Level 1 und 3 abruft
    Dann erhält der EU-Apotheker 4 Rezept-Dokumente zurück
    Und 2 Rezept-Dokumente sind im Format CDA Level 1
    Und jedes Rezept-Dokument als CDA 1 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und 2 Rezept-Dokumente sind im Format CDA Level 3
    Und jedes Rezept-Dokument als CDA 3 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung
    Und der Verarbeitungsstatus der erfolgreich abgerufenen Rezepte ist gesetzt

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC11_013
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10400
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC11 Fünf von 10 einlösbaren E-Rezepten eines Versicherten als CDA 3 abrufen
    Für die versicherte Person sind 10 in der EU einlösbare E-Rezepte im E-Rezept FD eingestellt und
    wurden vom Versicherten in seiner App zum Einlösen in der EU markiert.
    Der EU-Apotheker listet die 10 einlösbaren E-Rezepte auf. Der EU-Apotheker hat sich mit der
    versicherten Person auf die Abgabe von 5 Rezepten geeinigt. Der EU-Apotheker startet den Abruf
    dieser E-Rezepte als CDA Level 3. Als Antwort erhält der EU-Apotheker die 5 Rezepte als
    XML-Dokumente ausgegeben.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Grippostad C Hartkapseln                       | 00571748 | nein                |
      | Ibuflam 600mg                                  | 06313390 | nein                |
      | Etoricoxib beta 90mg Filmtabletten             | 12444251 | nein                |
      | Aspirin N 100mg                                | 05387239 | nein                |
      | Aciclovir HEUMANN Creme                        | 06977954 | nein                |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | ja                  |
      | Paracetamol 500 mg IPA Tabletten               | 12747135 | ja                  |
      | selen-Loges 200\uc2b5g                         | 17150382 | nein                |
      | Laxans AL                                      | 10916154 | nein                |
      | Wasserstoffperoxid L\uc3b6sung 3%              | 07284644 | nein                |
    # Hinweis: davon ausgehend, dass die letzten Rezepte die jüngsten im Fachdienst sind, wird
    # erwartet, dass dies die abzurufenden Rezepte sein werden.
    # Special Characters in UTF-8: µ (µg) und ö (Lösung)
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der EU-Apotheker 5 der einlösbaren Rezepte der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker 5 Rezept-Dokumente zurück
    Und 5 Rezept-Dokumente sind im Format CDA Level 3
    Und jedes Rezept-Dokument als CDA 3 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und das Ergebnis für den Abruf der ePrescriptions enthält keine Fehlermeldung
    Und der Verarbeitungsstatus der erfolgreich abgerufenen Rezepte ist gesetzt

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC11_020
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_28092 @AFO-ID:A_28009 @AFO-ID:A_28010 @AFO-ID:A_28011
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  Szenario: NCPeH - ePeD-A UC11 Rezepte abrufen - ein von zwei Rezepten wurde gelöscht
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat
    intern eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der
    verfügbaren Dokumente ermittelt.
    Auf dem Weg der Bereitstellung der Liste der verfügbaren Dokumente bis zum Abruf der Rezepte
    durch den EU-Apotheker trat eine Änderung am E-Rezept auf (z. B. Löschung oder Reservierung
    durch eine deutsche Apotheke).
    Der E-Rezept-FD meldet beim Retrieve-Versuch nur das eine einlösbare Rezept zurück (siehe
    https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27067).
    Für das nicht ausgegebene Rezept wird zusätzlich eine Statusinformation mitgeliefert, siehe
    TUC_NCPeH_037: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28092
    TUC_NCPeH_029:
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28009
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28010
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28011
    und https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4 -> partialSuccess

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | ja                  |
      | Ibuflam 600mg                                  | 06313390 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der ausstellende deutsche Arzt ein E-Rezept löscht
    Und der EU-Apotheker danach die aufgelisteten E-Rezepte der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker 1 Rezept-Dokument zurück
    Und 1 Rezept-Dokument ist im Format CDA Level 3
    Und das Rezept-Dokument als CDA 3 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrievePrescriptionsPartialNotFound

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC11_021
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_28092 @AFO-ID:A_28009 @AFO-ID:A_28010 @AFO-ID:A_28011
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  Szenario: NCPeH - ePeD-A UC11 Rezepte abrufen - ein von zwei Rezepten ist nicht mehr für den EU-Zugriff markiert
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat
    intern eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der
    verfügbaren Dokumente ermittelt.
    Auf dem Weg der Bereitstellung der Liste der verfügbaren Dokumente bis zum Abruf der Rezepte
    durch den EU-Apotheker wurde bei einem Rezept die Markierung für den EU-Zugriff entfernt.
    Der E-Rezept-FD meldet beim Retrieve-Versuch nur das eine einlösbare Rezept zurück (siehe
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27067 zusammen mit
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27063).
    Für das nicht ausgegebene Rezept wird zusätzlich eine Statusinformation mitgeliefert, siehe
    TUC_NCPeH_037: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28092
    TUC_NCPeH_029:
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28009
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28010
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28011
    und https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4 -> partialSuccess

    Hinweis: dieses Szenario ist von der erwarteten Außenwirkung her gleich zu NCP2_E2E_ePeDA_UC11_020,
      allerdings ist die Bedingung dafür im eRP-FD insofern anders, als dass das Rezept prinzipiell
      noch vorhanden und im Status offen ist. Deshalb Testfallpriorität 2.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | ja                  |
      | Ibuflam 600mg                                  | 06313390 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn die versicherte Person in ihrer E-Rezept-App ein E-Rezept zur Einlösung in der EU deselektiert
    Und der EU-Apotheker danach die aufgelisteten E-Rezepte der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker 1 Rezept-Dokument zurück
    Und 1 Rezept-Dokument ist im Format CDA Level 3
    Und das Rezept-Dokument als CDA 3 enthält wichtige Rezeptdaten des jeweils abgerufenen E-Rezeptes
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrievePrescriptionsPartialNotFound
    Und die versicherte Person sieht im FdV das NICHT für die EU selektierte E-Rezept im Status ready

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC11_022
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_28094 @AFO-ID:A_28009 @AFO-ID:A_28010 @AFO-ID:A_28011
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @E2E
  Szenario: NCPeH - ePeD-A UC11 Rezepte abrufen - kein Rezept zum Abruf mehr verfügbar
  Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat
  intern eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der
  verfügbaren Dokumente ermittelt.
  Auf dem Weg der Bereitstellung der Liste der verfügbaren Dokumente bis zum Abruf der Rezepte
  durch den EU-Apotheker trat eine Änderung am E-Rezept auf (z. B. Löschung oder Reservierung
  durch eine deutsche Apotheke).
  Der E-Rezept-FD meldet beim Retrieve-Versuch, dass kein Rezept zur Einlösung in der EU verfügbar ist (siehe
  https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27067, http status 404).
  Es wird eine Statusinformation nach folgenden Anforderungen geliefert, siehe
  TUC_NCPeH_037: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28094
  TUC_NCPeH_029:
    https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28009
    https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28010
    https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28011
  und https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4 -> Error

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn der ausstellende deutsche Arzt alle offenen E-Rezepte der versicherten Person löscht
    Und der EU-Apotheker danach die aufgelisteten E-Rezepte der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker kein Rezept ausgegeben
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrieveNoPrescriptions
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready
