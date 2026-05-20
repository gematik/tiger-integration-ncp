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
Funktionalität: NCPeH ePeD Land A: NCPeH.UC_12 - Abgabe von Arzneimitteln an Versicherte im Abgabeland
  AF_10401 - "Abgabe von Arzneimitteln an Versicherte im Abgabeland"
  In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem
  europäischen Apotheker ("EU-Apotheker"), um bei ihm ein E-Rezept einzulösen (Behandlungskontext).
  Der EU-Apotheker möchte nach seiner Beratung mit der versicherten Person die abgerufenen,
  einlösbaren E-Rezepte an die in Deutschland versicherte Person abgeben. (eHDSI Anwendungsszenario
  "eDispensation", Land A)
  Die vers. Person hat mit seinem FdV die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert,
  ein oder mehrere E-Rezepte zum Abruf in der EU ausgewählt und das EU-Land berechtigt.
  Die der Dispensierung vorangehenden Anwendungsfälle Identifikation des Patienten (UC_9), Auflisten
  der im EU-Ausland einlösbaren Rezepte (UC_10) und der Abruf ausgewählter Rezepte (UC_11) wurden
  vom EU-Apotheker erfolgreich durchgeführt. Der NCPeH Land B hat im Zuge dessen eine Treatment
  Relationship Confirmation (TRC) erstellt.
  In diesem nachfolgenden Use Case soll der EU-Apotheker die abgerufenen Rezepte an die versicherte
  Person abgeben und eine Dispensierinformation an das Land A übermitteln. Durch Übermittlung der
  Dispensierinformation der abgegebenen Rezepte wird der Status des jeweiligen E-Rezeptes im
  deutschen E-Rezept-Fachdienst auf "completed" gesetzt.
  # eHDSI_XDR_Profile
  # eHDSI_Components_Specification#6.4 eHealth DSI Error Codes, Table 1
  # https://profiles.ihe.net/ITI/TF/Volume1/ch-15.html
  # https://profiles.ihe.net/ITI/TF/Volume2/ITI-41.html
  # https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4.2

  @TCID:NCP2_E2E_ePeDA_UC12_001
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10401
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC12 Ein als CDA3 abgerufenes Rezept an eine versicherte Person abgeben
    Nach Abruf des verfügbaren E-Rezeptes als CDA Level 3 hat sich der EU-Apotheker mit der
    versicherten Person auf die Abgabe des Rezeptes geeinigt, gibt das Medikament an die versicherte
    Person ab und sendet eine entsprechende Dispensierinformation an das Land A.
    Als Antwort erhält der EU-Apotheker eine Erfolgsinformation zur erfolgreichen Durchführung der
    Dispensierung im Land A. Das E-Rezept ist im deutschen Fachdienst geschlossen und hat eine
    abrufbare Dispensierinformation.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 3 durchgeführt
    Wenn der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet
    Dann erhält der EU-Apotheker eine Information zur erfolgreichen Verarbeitung der Dispensierinformation zurück
    Und die versicherte Person kann im FdV die Dispensierinformationen über das letzte dispensierte E-Rezept abrufen

  @TCID:NCP2_E2E_ePeDA_UC12_002
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10401
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @E2E
  Szenario: NCPeH - ePeD-A UC12 Ein als CDA1 abgerufenes Rezept an eine versicherte Person abgeben
    Nach Abruf des verfügbaren E-Rezeptes als CDA Level 1 hat sich der EU-Apotheker mit der
    versicherten Person auf die Abgabe des Rezeptes geeinigt, gibt das Medikament an die versicherte
    Person ab und sendet eine entsprechende Dispensierinformation an das Land A.
    Als Antwort erhält der EU-Apotheker eine Erfolgsinformation zur erfolgreichen Durchführung der
    Dispensierung im Land A. Das E-Rezept ist im deutschen Fachdienst geschlossen und hat eine
    abrufbare Dispensierinformation.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 1 durchgeführt
    Wenn der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet
    Dann erhält der EU-Apotheker eine Information zur erfolgreichen Verarbeitung der Dispensierinformation zurück
    Und die versicherte Person kann im FdV die Dispensierinformationen über das letzte dispensierte E-Rezept abrufen

  @TCID:NCP2_E2E_ePeDA_UC12_010
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10401
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC12 Zwei ausgewählte E-Rezepte an eine versicherte Person abgeben
    Nach Abruf des verfügbaren E-Rezeptes als CDA Level 3 hat sich der EU-Apotheker mit der
    versicherten Person auf die Abgabe der Rezepte geeinigt. Er gibt die Medikamente an die
    versicherte Person ab und sendet eine entsprechende Dispensierinformation an das Land A.
    Als Antwort erhält der EU-Apotheker eine Erfolgsinformation zur erfolgreichen Durchführung der
    Dispensierungen im Land A. Beide E-Rezepte sind im deutschen Fachdienst geschlossen und haben
    eine abrufbare Dispensierinformation.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | ja                  |
      | Ibuflam 600mg                                  | 06313390 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf der Rezepte als CDA Level 3 durchgeführt
    Wenn der EU-Apotheker die Medikamente aller abgerufenen Rezepte an die versicherte Person abgibt und die zugehörigen Dispensierinformationen sendet
    Dann erhält der EU-Apotheker eine Information zur erfolgreichen Verarbeitung der Dispensierinformationen zurück
    Und die versicherte Person kann im FdV die Dispensierinformationen über die letzten dispensierten E-Rezepte abrufen

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC12_011
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10401
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC12 Fünf von 10 einlösbaren E-Rezepten an eine versicherte Person abgeben
    Für die versicherte Person sind 10 in der EU einlösbare E-Rezepte im E-Rezept FD eingestellt und
    wurden vom Versicherten in seiner App zum Einlösen in der EU markiert.
    Der EU-Apotheker listet die 10 einlösbaren E-Rezepte auf. Der EU-Apotheker hat sich mit der
    versicherten Person auf die Abgabe von 5 Rezepten geeinigt. Der EU-Apotheker hat diese fünf
    E-Rezepte als CDA Level 3 erfolgreich abgerufen.
    Der EU-Apotheker gibt die Medikamente an die versicherte Person ab und sendet eine entsprechende
    Dispensierinformation an das Land A. Als Antwort erhält der EU-Apotheker eine Erfolgsinformation
    zur erfolgreichen Durchführung der Dispensierungen im Land A. Alle fünf E-Rezepte sind im
    deutschen Fachdienst geschlossen und haben eine abrufbare Dispensierinformation.

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
    Und der EU-Apotheker hat erfolgreich 5 der einlösbaren Rezepte der versicherten Person als CDA Level 3 abgerufen
    Wenn der EU-Apotheker die Medikamente aller abgerufenen Rezepte an die versicherte Person abgibt und die zugehörigen Dispensierinformationen sendet
    Dann erhält der EU-Apotheker eine Information zur erfolgreichen Verarbeitung der Dispensierinformationen zurück
    Und die versicherte Person kann im FdV die Dispensierinformationen über die letzten dispensierten E-Rezepte abrufen

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_UC12_020
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10401
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP
  Szenario: NCPeH - ePeD-A UC12 Rezepte abgeben - ein von zwei Rezepten ist nicht mehr für den EU-Zugriff markiert
    Eine versicherte Person kann mit seinem FdV jederzeit die Markierung von Rezepten zur Einlösung
    in der EU wieder entfernen. Allerdings erfolgt die Verarbeitung einer Dispensierinformation vom
    NCPeH-FD auch, wenn diese Markierung von der versicherten Person entfernt wurde.
    Der Test soll zeigen, dass eine fehlende EU-Markierung im Rezept die Dispensierung durch den NCPeH nicht
    verhindert.

    Hinweis: es wird hier keine Variante des Verarbeitungsmechanismus im NCPeH-FD geprüft. Es wird vor allem der
      Mechanismus der Dispensierung von E-Rezepten durch den E-Rezept-Fachdienst über die EU-Schnittstelle geprüft.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | ja                  |
      | Ibuflam 600mg                                  | 06313390 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf der Rezepte als CDA Level 3 durchgeführt
    Wenn die versicherte Person in ihrer E-Rezept-App ein E-Rezept zur Einlösung in der EU deselektiert
    Und der EU-Apotheker die Medikamente aller abgerufenen Rezepte an die versicherte Person abgibt und die zugehörigen Dispensierinformationen sendet
    Dann erhält der EU-Apotheker eine Information zur erfolgreichen Verarbeitung der Dispensierinformationen zurück
    Und die versicherte Person kann im FdV die Dispensierinformationen über die letzten dispensierten E-Rezepte abrufen

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_UC12_021
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_27241 @AFO-ID:A_27242 @AFO-ID:A_27243 @AFO-ID:A_27244
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP
  Szenario: NCPeH - ePeD-A UC12 Rezepte abgeben - Versuch einer doppelten Dispensierung eines Rezeptes mit zwei Operationen
    Ein EU-Apotheker ruft ein Rezept als CDA Level 3 ab und sendet dann die Dispensier-
    Information zum abgegebenen Rezept. Die Dispensierung wird vom NCPeH-FD und dem E-Rezept-FD
    erfolgreich verarbeitet. Aber beim Empfang des Ergebnisses der Dispensieroperation
    tritt im Land B ein Problem auf und der Apotheker führt die Dispensieroperation erneut aus.
    Mit der zweiten Operation liefert der E-Rezept-FD einen http 404 Fehler zurück.
    Nach TUC_NCPeH_038 liefert der NCPeH-FD für das Rezept die Fehlermeldung epedaDispensePrescriptionNotFound zurück.
    Der Gesamtstatus der Operation-Response ergibt sich aus der Summe der Ergebnisse der anteiligen Dispensierungen.
    Referenzen:
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27241
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27242
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27243
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27244

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 1 und 3 durchgeführt
    Wenn der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet
    Dann verarbeitet der NCPeH-FD die Dispensierinformation erfolgreich und sendet eine Information zur erfolgreichen Verarbeitung an das Land B zurück
    Und die versicherte Person kann im FdV die Dispensierinformationen über das letzte dispensierte E-Rezept abrufen
    Wenn im Land B ein Problem mit dem Empfang des Dispensierergebnisses entsteht und der Apotheker keine information erhält
    # dieser Wenn-Schritt ist leer, da wir im Testaufbau inhaltlich/technisch kein Problem
    # produzieren müssen. Stattdessen senden wir im nächsten Step einfach die Dispensierung erneut
    Und der Apotheker sendet die Dispensierinformation erneut an das Land A
    Dann enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für epedaDispensePrescriptionNotFound

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_UC12_022
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_27241 @AFO-ID:A_27242 @AFO-ID:A_27243 @AFO-ID:A_27244
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP
  Szenario: NCPeH - ePeD-A UC12 Rezepte abgeben - Versuch einer doppelten Dispensierung eines Rezeptes innerhalb einer Operation
    Ein EU-Apotheker ruft ein Rezept als CDA Level 1 und 3 ab und sendet dann die Dispensier-
    Information zum abgegebenen Rezept sowohl für CDA Level 1 als auch 3. Der NCPeH Land-B
    erstellt daraus einen Request zur Dispensierung mit zwei Dispense-Dokumenten für das gleiche
    Rezept. Der NCPeH-FD muss dementsprechend zwei Dispense-Operationen für das gleiche Rezept
    ausführen. Mit der zweiten Operation liefert der E-Rezept-FD einen http 404 Fehler zurück.
    Nach TUC_NCPeH_038 liefert der NCPeH-FD für das zweite Rezept die Fehlermeldung
    epedaDispensePrescriptionsPartialNotFound zurück.
    Das erste Rezept aus der Liste der Dispense-Dokumente wird jedoch erfolgreich dispensiert.
    Der Gesamtstatus der Operation-Response ergibt sich aus der Summe der Ergebnisse der anteiligen Dispensierungen.
    Referenzen:
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27241
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27242
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27243
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27244

    Hinweis: das Szenario erscheint etwas theoretisch. Da wir aber nicht wissen können, ob eine
      Fehlbedienung oder Fehlimplementierung zu so einer Situation führen können, wird das Szenario
      zu Prüfung der Robustheit bzgl. IOP geprüft. Die Prio des Testfalls ist dementsprechend nur
      auf 2 gesetzt.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 1 und 3 durchgeführt
    Wenn der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und für beide CDA-Level eine Dispensierinformation sendet
    Dann enthält das Ergebnis der Dispensieroperation eine Information zur erfolgreichen Verarbeitung der ersten Dispensierinformation zurück
    Und das Ergebnis für die Dispensierung der ePrescriptions enthält die Fehlermeldung für epedaDispensePrescriptionsPartialNotFound
    Und die versicherte Person kann im FdV die Dispensierinformationen über das letzte dispensierte E-Rezept abrufen

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_UC12_023
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_27268-01 @AFO-ID:A_27242 @AFO-ID:A_27243 @AFO-ID:A_27244
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP
  Szenario: NCPeH - ePeD-A UC12 Rezepte abgeben - Versuch einer Dispensierung ohne vorigen Abruf eines Rezeptes
    Ein EU-Apotheker listet ein Rezept als CDA Level 3 auf und sendet danach sofort eine Dispensier-
    Information zum abgegebenen Rezept (ein Abruf erfolgt nicht)). Der NCPeH Land-B
    erstellt daraus einen Request zur Dispensierung mit einer gültigen PrescriptionId, jedoch nicht
    in einem E-Rezept-Status der eine Dispensierung zulässt.
    Der E-Rezept-FD liefert einen http 403 Fehler zurück.
    Nach TUC_NCPeH_038 liefert der NCPeH-FD daraufhin die Fehlermeldung epedaDispenseMissingErpAccessrights zurück.
    Der Gesamtstatus der Operation-Response ergibt sich aus der Summe der Ergebnisse der anteiligen Dispensierungen.
    Referenzen:
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27268-01
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27242
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27243
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27244

    Hinweis: das Szenario erscheint etwas theoretisch. Da wir aber nicht wissen können, ob eine
    Fehlbedienung oder Fehlimplementierung zu so einer Situation führen kann, wird das Szenario
    zu Prüfung der Robustheit bzgl. IOP geprüft. Die Prio des Testfalls ist dementsprechend nur
    auf 2 gesetzt.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat keinen Abruf des Rezeptes durchgeführt
    Wenn der EU-Apotheker das Medikament des Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet
    Dann enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für epedaDispenseMissingErpAccessrights
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready
