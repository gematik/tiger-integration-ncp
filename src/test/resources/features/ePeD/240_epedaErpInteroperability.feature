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
@Ignore
Funktionalität: NCPeH ePeD Interoperabilität mit der Anbindung an die Anwendung E-Rezept
  Anwendungsfälle:
  AF_10379 - ePeD-A - Versicherten im Behandlungsland für ePeD-A identifizieren
  AF_10380 - Einlösbare E-Rezepte des Versicherten aus ePeD-A auflisten
  AF_10400 - Ausgewählte E-Rezepte abrufen
  AF_10401 - Abgabe von Arzneimitteln an Versicherte im Abgabeland
  Die benannten Anwendungsfälle basieren auf der Anbindung an den E-Rezept Fachdienst und der Nutzung
  der Anwendung E-Rezept. Betrachtet werden dabei folgende Themen zur Interoperabilität mit dem E-Rezept-Fachdienst
  betrachtet:
  1. Der Umgang mit der (fehlenden) Berechtigung des NCPeH für EU Mitgliedsstaaten im E-Rezept Fachdienst
  Entsprechende Interoperabilitätsszenarien sind zu testen.
  Nicht betrachtet wird die Transformation und Transkodierung der medizinischen Daten eines E-Rezeptes.

###############################################################
########            Berechtigung des NCPeH             ########
###############################################################

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_ERP_010
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_27933
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A eRP-IOP Löschen einer Berechtigung vor der Patient Identification
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert und an den
    EU-Apotheker die KVNR und den Zugriffscode übergeben.
    Im Verlaufe des Gesprächs mit dem Apotheker entscheidet sich der Versicherte, die Berechtigung
    zu widerrufen. Die Berechtigung des Landes und der an den Apotheker übergebene Zugriffscode werden
    dadurch ungültig.
    Der Apotheker startet die Patient Identification, aber der eRP-FD liefert ein http statuscode 403
    zurück, siehe
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27062
    Als Antwort erhält der EU-Apotheker eine Fehlermeldung für die Situation 'epedaIdentifyMissingErpAccessrights'.

    Hinweis: das Szenario hat den vergleichbaren Effekt wie
      das zeitliche Auslaufen einer erteilten Berechtigung vor Start des UseCase
      das Fehlen einer Berechtigung (wurde vom Versicherten nicht erteilt)
      die Berechtigung wurde überschrieben
      ein zu einer korrekten KVNR wurde ein falscher Zugriffscode eingegeben

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn die versicherte Person die Berechtigung des EU-Landes für den Zugriff auf E-Rezepte löscht
    Und der EU-Apotheker für die versicherte Person die Patient Identification aufruft
    Dann erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaIdentifyMissingErpAccessrights

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_ERP_011
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:3 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_27933
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @LongRunning @AccessPermissions
  Szenario: NCPeH - ePeD-A eRP-IOP Die Berechtigung läuft vor der Patient Identification ab
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Der Apotheker startet die Patient Identification >1h nach Erteilung der Berechtigung. Der Zugriff
    für das EU-Land wird dadurch ungültig.
    Der eRP-FD liefert deshalb ein http statuscode 403 zurück, siehe
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27062
    Als Antwort erhält der EU-Apotheker eine Fehlermeldung für die Situation 'epedaIdentifyMissingErpAccessrights'.

    Hinweise: Testfall Prio 3, da gleiche Reaktion wie im Testfall NCP2_IOP_ePeDA_ERP_010. Fokus ist hier der funktionierende
      Timeout der erteilten Berechtigung (Fokus eRP Fachdienst).
      Der zeitliche Ablauf wird hier etwas vereinfacht dargestellt im Vergleich zu realistischen Szenarien mit zeitlichen
      Verzögerungen.

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn der EU-Apotheker 61 Minuten nach der Berechtigungserteilung die Patient Identification für diese Person aufruft
    Dann erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaIdentifyMissingErpAccessrights


  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_ERP_020
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A eRP-IOP Erneuern einer Berechtigung vor Patient Identification
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert. Der Versicherte erneuert
    die erteilte, noch gültige Berechtigung vor Übergabe von KVNR und Accesscode.
    Der EU-Apotheker startet eine erfolgreiche Patient Identification.

    Hinweis: das Szenario hat den gleichen Effekt wie eine einfache Erteilung der Berechtigung. Es wird hier aber das
      erfolgreiche Überschreiben der Berechtigung im Positiv-Fall geprüft (Fokus eRP Fachdienst).

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihre E-Rezepte erneut berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Wenn der EU-Apotheker für die versicherte Person die Patient Identification aufruft
    Dann erhält der EU-Apotheker als Antwort die KVNR mit dem Zugriffscode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem E-Rezept der versicherten Person zurück

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_ERP_021
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10380
  @AFO-ID:A_27942
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_FindDocuments
  @IOP
  Szenario: NCPeH - ePeD-A eRP-IOP Erneuern einer Berechtigung vor Auflisten der Rezepte
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert. Die Identifikation
    des Patienten wurde vom LE-EU erfolgreich durchgeführt. Der NCPeH Land B hat intern eine
    Treatment Relationship Confirmation (TRC) erstellt.
    Die versicherte Person führt eine erneute Berechtigungserteilung für das EU-Land durch, so dass
    im E-Rezept FD ein neuer Zugriffscode hinterlegt wird. Der an den EU-Apotheker übergebene Zugriffscode
    wird dadurch ungültig.
    Der EU-Apotheker startet die Suche nach den verfügbaren E-Rezepten.
    Als Antwort erhält der EU-Apotheker eine Fehlermeldung für die Situation 'epedaSearchMissingErpAccessrights'.
    Referenzen:
    Der eRP-FD liefert ein http statuscode 403 zurück
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27062
    TUC_NCPeH_036: Liste der einlösbaren E-Rezepte des Versicherten aus dem E-Rezept-FD abrufen
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/gemSpec_NCPeH_FD_V2.0.1/#A_27942
      TAB_NCPeH_Allgemeine_Fehlerfälle_Auflistung_von_einlösbaren_E-Rezepten, HTTP Status Code 403

    Hinweis: das Szenario hat den vergleichbaren Effekt wie
      das zeitliche Auslaufen einer erteilten Berechtigung vor Start des UseCase
      das Fehlen einer Berechtigung (wurde vom Versicherten nicht erteilt)
      die Berechtigung wurde gelöscht

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Wenn die versicherte Person den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihre E-Rezepte erneut berechtigt
    Und der EU-Apotheker die Suche nach den E-Rezepten der versicherten Person aufruft
    Dann erhält der EU-Apotheker keine Rezepte gelistet
    Und das Suchergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaSearchMissingErpAccessrights

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_ERP_022
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:2 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10400
  @AFO-ID:A_28094 @AFO-ID:A_28009 @AFO-ID:A_28010 @AFO-ID:A_28011
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_RetrieveDocuments
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A eRP-IOP Erneuern einer Berechtigung vor Abruf eines Rezeptes
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Die Identifikation des Patienten und das Auflisten der Rezepte wurde vom LE-EU erfolgreich durchgeführt.
    Die versicherte Person führt eine erneute Berechtigungserteilung für das EU-Land durch, so dass
    im E-Rezept FD ein neuer Zugriffscode hinterlegt wird. Der an den EU-Apotheker übergebene Zugriffscode
    wird dadurch ungültig.
    Der EU-Apotheker startet den Abruf eines ausgewählten Rezeptes.
    Als Antwort erhält der EU-Apotheker eine Fehlermeldung für die Situation 'epedaRetrieveMissingErpAccessrights'.
    Referenzen:
    Der eRP-FD liefert ein http statuscode 403 zurück
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27062
    Es wird eine Statusinformation nach folgenden Anforderungen geliefert, siehe
    TUC_NCPeH_037: https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28094
    TUC_NCPeH_029:
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28009
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28010
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_28011
    und https://profiles.ihe.net/ITI/TF/Volume3/ch-4.2.html#4.2.4 -> Error

    Hinweis: das Szenario hat den vergleichbaren Effekt wie
      das zeitliche Auslaufen einer erteilten Berechtigung vor Start des UseCase
      das Fehlen einer Berechtigung (wurde vom Versicherten nicht erteilt)
      die Berechtigung wurde gelöscht

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Wenn die versicherte Person den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihre E-Rezepte erneut berechtigt
    Und der EU-Apotheker danach das E-Rezept der versicherten Person als CDA Level 3 abruft
    Dann erhält der EU-Apotheker kein Rezept ausgegeben
    Und das Ergebnis für den Abruf der ePrescriptions enthält die Fehlermeldung für epedaRetrieveMissingErpAccessrights
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status ready

  # PET/Polarion Tags
  @TCID:NCP2_IOP_ePeDA_ERP_023
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10401
  @AFO-ID:A_27268-01 @AFO-ID:A_27242 @AFO-ID:A_27243 @AFO-ID:A_27244
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_DispensePrescription
  @IOP @AccessPermissions
  Szenario: NCPeH - ePeD-A eRP-IOP Erneuern einer Berechtigung vor Dispensierung eines Rezeptes
    Für die versicherte Person ist ein in der EU einlösbares E-Rezept im E-Rezept FD eingestellt.
    Die vers. Person hat die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert.
    Die Identifikation des Patienten, das Auflisten der Rezepte und der Abruf eines Rezeptes wurde vom
    LE-EU erfolgreich durchgeführt.
    Die versicherte Person führt eine erneute Berechtigungserteilung für das EU-Land durch, so dass
    im E-Rezept FD ein neuer Zugriffscode hinterlegt wird. Der an den EU-Apotheker übergebene Zugriffscode
    wird dadurch ungültig.
    Der EU-Apotheker startet die Dispensierung eines ausgewählten Rezeptes.
    Nach TUC_NCPeH_038 liefert der NCPeH-FD die Fehlermeldung 'epedaDispenseMissingErpAccessrights'
    zurück. Das Rezept wird im E-Rezept-FD nicht dispensiert.
    Referenzen:
    Der eRP-FD liefert ein http statuscode 403 zurück
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27071
    Der Gesamtstatus der Operation-Response ergibt sich aus der Summe der Ergebnisse der anteiligen Dispensierungen.
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27268-01
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27242
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27243
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_NCPeH_FD/latest/#A_27244

    Hinweis: das Szenario hat den vergleichbaren Effekt wie
      das zeitliche Auslaufen einer erteilten Berechtigung vor Start des UseCase
      das Fehlen einer Berechtigung (wurde vom Versicherten nicht erteilt)
      die Berechtigung wurde gelöscht

    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt
    Und die versicherte Person hat ihre KVNR und den Zugriffscode an den EU-Apotheker übergeben
    Und der EU-Apotheker hat erfolgreich mit der versicherten Person die Patient Identification durchgeführt
    Und der EU-Apotheker hat erfolgreich die in der EU einlösbaren Rezepte der versicherten Person aufgelistet
    Und der EU-Apotheker hat erfolgreich den Abruf des Rezeptes als CDA Level 3 durchgeführt
    Wenn die versicherte Person den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihre E-Rezepte erneut berechtigt
    Und der EU-Apotheker das Medikament des abgerufenen Rezeptes an die versicherte Person abgibt und die zugehörige Dispensierinformation sendet
    Dann enthält das Ergebnis für die Dispensierung der ePrescriptions die Fehlermeldung für epedaDispenseMissingErpAccessrights
    Und die versicherte Person sieht im FdV das für die EU selektierte E-Rezept im Status in-progress
