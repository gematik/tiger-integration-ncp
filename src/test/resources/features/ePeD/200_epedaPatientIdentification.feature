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
@PRODUKT:NCPeH_FD,NCPeH_ePeDA
@PRODUKT:eRp_FD @PRODUKT:IDP-D @PRODUKT:eRp_FdV
@PRODUKT:TSP\sX.509\snonQES\s-\sSMC-B
Funktionalität: NCPeH ePeD Land A: NCPeH.UC_9 - Versicherten im Behandlungsland für ePeD-A identifizieren
  AF_10379 - "ePeD-A - Versicherten im Behandlungsland für ePeD-A identifizieren"
  In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
  Apotheker ("EU-Apotheker"), um bei ihm ein E-Rezept einzulösen (Behandlungskontext).
  Der Versicherte möchte im Rahmen der Beratung in der Apotheke dem EU-Apotheker seine einlösbaren
  E-Rezepte zu Verfügung stellen. (eHDSI Anwendungsszenario "ePrescription Country A").
  Die vers. Person hat mit seinem FdV die Verarbeitung von E-Rezepten in dem EU-Land akzeptiert, ein einzulösendes
  E-Rezept zur Einlösung in der EU selektiert und das EU-Land berechtigt.
  Bei der Berechtigungserteilung durch den Versicherten mit seinem FdV wird ein Zugriffscode erzeugt.
  Im ersten Schritt identifiziert der EU-Apotheker den Patienten mit Hilfe seiner Versichertennummer (KVNR) und dem
  Zugriffscode. Für diesen Vorgang übergibt die vers. Person diese beiden Daten dem EU-Apotheker.
  Als Antwort soll der EU-Apotheker weitere persönliche Daten des Patienten erhalten, die er mit Hilfe der EHIC
  (Rückseite der eGK) oder des Ausweises des Patienten überprüfen kann (vollständiger Name, Geburtsdatum,
  KVNR konkateniert mit Zugriffscode).

  Grundlage:
    Angenommen für die Versicherte Gräfin Maude Adelheid Lilo Johanna GõdofskýTEST-ONLY ist folgendes in der EU einlösbare E-Rezept im Fachdienst abrufbar:
      | Name                                           | PZN      | Substitutionsverbot |
      | Metoprolol-Succinat AL 47,5 mg Retardtabletten | 07097020 | nein                |
    Und die versicherte Person begibt sich in dem EU-Land BELGIUM in eine Apotheke zur Apothekerin Dr. Elisabeth Pauwels
    Und die versicherte Person hat den Zugriff dieses EU-Landes auf alle ihre in der EU einlösbaren E-Rezepte berechtigt

  # PET/Polarion Tags
  @TCID:NCP2_E2E_ePeDA_UC9_001
  @STATUS:Implementiert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10379
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @CI
  @E2E
  Szenario: NCPeH - ePeD-A UC9 Identifikation einer Versicherten bei einem EU-Apotheker mittels KVNR und Accesscode
    Der EU-Apotheker ruft über den NCPeH die persönlichen Informationen der versicherten Person ab, wobei
    die der Suchparameter KVNR und als Sicherheitsparameter der Zugriffscode übergeben werden.
    Der Zugriffscode wird bei Erteilung der Berechtigung generiert, im E-Rezept Fachdienst hinterlegt und muss vom Versicherten
    dem EU-Apotheker übergeben werden.
    Die zurückgegebenen Werte (in der XCPD-Response) müssen mit den im E-Rezept des Versicherten hinterlegten Werten übereinstimmen
    (KVNR konkateniert mit Zugriffscode, Geburtsdatum, Vorname, Nachname).

    Wenn die versicherte Person ihre KVNR und den Zugriffscode an den EU-Apotheker übergibt
    Und der EU-Apotheker mit KVNR und Zugriffscode der versicherten Person die Patient Identifikation aufruft
    Dann erhält der EU-Apotheker als Antwort die KVNR mit dem Zugriffscode kombiniert, das Geburtsdatum und den vollständig strukturierten Namen aus dem E-Rezept der versicherten Person zurück


  @TCID:NCP2_E2E_ePeDA_UC9_010
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10379
  @AFO-ID:A_27933
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:ePeDA_PatIdentification
  @E2E
  Szenario: NCPeH - ePeD-A UC9 Identifikation eines Versicherten - Suche findet kein E-Rezept
    Für eine versicherte Person existiert nur genau ein E-Rezept im eRP-FD, dass in der EU einlösbar ist.
    Die versicherte Person selektiert im eRP-FdV dieses Rezept und erteilt eine Zugriffsberechtigung für
    das EU-Land. Die vers. Person erhält einen Zugriffscode und gibt alle nötigen Infos an den EU-Apotheker
    weiter.
    In der Zwischenzeit wird das Rezept entweder von einer deutschen Apotheke reserviert oder vom ausstellenden
    deutschen LE wieder gelöscht, so dass das Rezept im eRP-FD für den NCPeH-FD nicht mehr zugreifbar ist.
    Der EU-Apotheker startet die Patient Identification mit gültiger KVNR und erteilter Berechtigung.
    Die Suche nach dem Dokument schlägt jedoch fehl.
    Der eRP-FD liefert deshalb ein http statuscode 404 zurück, siehe
      https://gemspec.gematik.de/docs/gemSpec/gemSpec_FD_eRp/latest/#A_27065
    Als Antwort erhält der EU-Apotheker eine Fehlermeldung für die Situation 'epedaIdentifyPatientNotFound'.

    Angenommen der ausstellende deutsche Arzt löscht alle offenen E-Rezepte der versicherten Person
    Wenn die versicherte Person ihre KVNR und den Zugriffscode an den EU-Apotheker übergibt
    Und der EU-Apotheker mit KVNR und Zugriffscode der versicherten Person die Patient Identifikation aufruft
    Dann erhält der EU-Apotheker ein Identifikationsergebnis ohne Versichertendaten zurück
    Und das Identifikationsergebnis für den EU-Apotheker enthält die Fehlermeldung für epedaIdentifyPatientNotFound


  # Hinweis: weitere Negativszenarien drehen sich primär um fehlende Berechtigung und sind deshalb in features/ePeD/epedErpInteroperability.feature zu finden
