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
Funktionalität: NCPeH ePeD Land A: NCPeH.UC_10 - Einlösbare E-Rezepte des Versicherten aus ePeD-A auflisten
  AF_10380 - "Einlösbare E-Rezepte des Versicherten aus ePeD-A auflisten"
  In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
  Apotheker ("EU-Apotheker"), um bei ihm ein E-Rezept einzulösen (Behandlungskontext).
  Der Versicherte möchte im Rahmen der Beratung in der Apotheke dem EU-Apotheker seine einlösbaren E-Rezepte
  zu Verfügung stellen. (eHDSI Anwendungsszenario "ePrescription")
  Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und das EU-Land berechtigt.
  Die Identifikation des Patienten wurde vom EU-Apotheker erfolgreich durchgeführt. Der NCPeH Land B hat intern eine
  Treatment Relationship Confirmation (TRC) erstellt (UC_9 - siehe 200_epedaPatientIdentification.feature).
  In diesem nachfolgenden Use Case soll der EU-Apotheker die im E-Rezept Fachdienst einlösbaren Rezepte
  der versicherten Person auflisten.

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC10_001
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10380
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC10 Auflisten eines einlösbaren E-Rezeptes eines Versicherten durch einen EU-Apotheker
    Der EU-Apotheker startet die Suche nach den verfügbaren E-Rezepten. Als Antwort erhält der EU-Apotheker
    eine Metadaten-Liste zu zwei abrufbaren Dokumenten (für ein E-Rezept) mit
      uniqueId pro E-Rezept in Form eines strukturierten Dokumentes (CDA Level 3) und
      uniqueId pro E-Rezept in Form eines PDF-Dokumentes (CDA Level 1).

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker eine Liste mit 2 E-Rezept Referenzen zurück
    Und für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level 1 gelistet
    Und für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level 3 gelistet

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC10_010
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10380
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC10 Auflisten von 2 einlösbaren E-Rezepten eines Versicherten durch einen EU-Apotheker
    Für die versicherte Person sind zwei in der EU einlösbare E-Rezepte im E-Rezept FD eingestellt.
    Der EU-Apotheker startet die Suche nach den verfügbaren E-Rezepten. Als Antwort erhält er eine Metadaten-Liste
    zu zwei*zwei abrufbaren Dokumenten (zwei CDA-Varianten für zwei E-Rezepte) mit
      uniqueId pro E-Rezept in Form eines strukturierten Dokumentes (CDA Level 3) und
      uniqueId pro E-Rezept in Form eines PDF-Dokumentes (CDA Level 1).

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
      | Ibuflam 600mg                                  | 06313390 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker eine Liste mit 4 E-Rezept Referenzen zurück
    Und für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level 1 gelistet
    Und für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level 3 gelistet

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC10_011
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10380
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @E2E
  @CI
  Szenario: NCPeH - ePeD-A UC10 Auflisten von 10 einlösbaren E-Rezepten eines Versicherten durch einen EU-Apotheker
    Für die versicherte Person sind 10 in der EU einlösbare E-Rezepte im E-Rezept FD eingestellt.
    Der EU-Apotheker startet die Suche nach den verfügbaren E-Rezepten. Als Antwort erhält er eine Metadaten-Liste
    zu zwei*zehn abrufbaren Dokumenten (zwei CDA-Varianten für zehn E-Rezepte) mit
      uniqueId pro E-Rezept in Form eines strukturierten Dokumentes (CDA Level 3) und
      uniqueId pro E-Rezept in Form eines PDF-Dokumentes (CDA Level 1).

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
      | Ibuflam 600mg                                  | 06313390 | nein                |
      | Etoricoxib beta 90mg Filmtabletten             | 12444251 | nein                |
      | Paracetamol 500 mg IPA Tabletten               | 12747135 | nein                |
      | Aspirin N 100mg                                | 05387239 | nein                |
      | Aciclovir HEUMANN Creme                        | 06977954 | nein                |
      | selen-Loges 200\uc2b5g                         | 17150382 | nein                |
      | Laxans AL                                      | 10916154 | nein                |
      | Grippostad C Hartkapseln                       | 00571748 | nein                |
      | Wasserstoffperoxid L\uc3b6sung 3%              | 07284644 | nein                |
    # Special Characters in UTF-8: µ (µg) und ö (Lösung)
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker eine Liste mit 20 E-Rezept Referenzen zurück
    Und für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level 1 gelistet
    Und für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level 3 gelistet

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC10_012
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_27942
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP
  Szenario: NCPeH - ePeD-A UC10 Auflisten eines einlösbaren E-Rezeptes - kein Rezept verfügbar
    In dem Zeitraum zwischen der Patient Identification und dem Start des Auflistens der Rezepte durch den
    EU-Apotheker wird das Rezept wird von der deutschen Apotheke akzeptiert oder vom ausstellenden
    Arzt gelöscht.
    Als Antwort erhält der EU-Apotheker eine Fehlermeldung für die Situation 'epedaSearchNoPrescriptions'.
    Referenzen:
    Der eRP-FD liefert den http statuscode 404 zurück, siehe
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27065
    TUC_NCPeH_036: Liste der einlösbaren E-Rezepte des Versicherten aus dem E-Rezept-FD abrufen
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#A_27942
      TAB_NCPeH_Allgemeine_Fehlerfälle_Auflistung_von_einlösbaren_E-Rezepten, HTTP Status Code 404

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der ausstellende deutsche Arzt alle offenen E-Rezepte der versicherten Person löscht
    Und der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und das Suchergebnis für den EU-Apotheker enthält die Warnung für epedaSearchNoPrescriptions

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC10_030
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10380
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP
  Szenario: NCPeH - ePeD-A UC10 Auflisten eines MVO - erster Rezeptanteil ist abrufbar
    Für die versicherte Person ist vom Arzt ein E-Rezept vom Typ Mehrfachverordnung (MVO) im E-Rezept
    FD eingestellt.
    Der EU-Apotheker startet die Suche nach den verfügbaren E-Rezepten. Als Antwort erhält der EU-Apotheker
    ein Rezept aus der MVO mit einer Metadaten-Liste zu zwei abrufbaren Dokumenten (für ein E-Rezept) mit
      uniqueId pro E-Rezept in Form eines strukturierten Dokumentes (CDA Level 3) und
      uniqueId pro E-Rezept in Form eines PDF-Dokumentes (CDA Level 1).

    # Hinweis: es wird hier voraussichtlich keine Variante des Verarbeitungsmechanismus im NCPeH-FD
    # geprüft. Es wird vor allem der Mechanismus der Auslieferung von E-Rezepten durch den
    # E-Rezept-Fachdienst auf der EU-Schnittstelle geprüft.
    # Als besondere Variante eines E-Rezeptes wird der Rezepttyp MVO einmal im NCPeH betrachtet.
    # Der Testfall auf Prio 2 herabgestuft.

    Angenommen für die versicherte Person Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgende Mehrfachverordnung abrufbar:
      | Name                                                | PZN      | Rezeptanzahl | Aktivindex |
      | BD Micro-Fine\ue284a2+ U 40 Insulinspritzen 12,7 mm | 04400127 | 4            | 1          |
    # Special Characters in UTF-8: ™
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker eine Liste mit 2 E-Rezept Referenzen zurück
    Und für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level 1 gelistet
    Und für jedes in der EU einlösbare E-Rezept der versicherten Person wird ein abrufbares Dokument in der Dokumentform CDA Level 3 gelistet

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC10_050
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_27911 @AFO-ID:A_27912 @AFO-ID:A_27914
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @BfArM
  @CI
  Szenario: NCPeH - ePeD-A UC10 Auflisten eines einlösbaren E-Rezeptes eines Versicherten - Verpflichtende Metadaten liefern
    Der EU-Apotheker startet die Suche nach den verfügbaren E-Rezepten. Als Antwort erhält der EU-Apotheker
    eine Metadaten-Liste zu zwei abrufbaren Dokumenten (für ein E-Rezept) mit
      uniqueId pro E-Rezept in Form eines strukturierten Dokumentes (CDA Level 3) und
      uniqueId pro E-Rezept in Form eines PDF-Dokumentes (CDA Level 1).
    Zu beiden Dokumentvarianten werden die verpflichtenden Attribute zu einer ePrescription
    ausgegeben. (siehe eHDSI_NCPeH_Components_Specification#3.1.4.2.3 und
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27914)

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker eine Liste mit 2 E-Rezept Referenzen zurück
    Und mit jeder gelisteten Dokumentreferenz werden die geforderten Metadaten geliefert

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC10_051
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10380
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP @BfArM
  @CI
  Szenario: NCPeH - ePeD-A UC10 Auflisten eines einlösbaren E-Rezeptes eines Versicherten - Metadaten mit aktivem Substitutionsverbot liefern
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt, in dem
    die Substitution verboten ist (aut-idem Information = true).
    Der EU-Apotheker startet die Suche nach den verfügbaren E-Rezepten. Als Antwort erhält der EU-Apotheker
    eine Metadaten-Liste zu zwei abrufbaren Dokumenten (für ein E-Rezept).
    Mit beiden Dokumentvarianten werden die verpflichtenden Attribute
      KVNR,
      PrescriptionId,
      Beschreibung des Medikamentes
      Ausstellungsdatum des E-Rezeptes
      ATC Code (entweder als eigenes Element oder zu Beginn der Beschreibung des Medikamentes )
    ausgegeben.
    Zusätzlich ist das optionale Element 'Substitution' vorhanden und hat den Wert 'N'
    (siehe https://wiki.hl7.de/index.php?title=2.16.840.1.113883.5.1070)

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY sind folgende in der EU einlösbare E-Rezepte im Fachdienst abrufbar:
      | Name                       | PZN      | Substitutionsverbot |
      | Imodium\uc2ae akut lingual | 01689854 | ja                  |
    # Special Characters in UTF-8: ®
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker eine Liste mit 2 E-Rezept Referenzen zurück
    Und mit jeder gelisteten Dokumentreferenz werden die geforderten Metadaten geliefert
