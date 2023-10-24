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
Funktionalität: NCPeH PSA Use Case 2 - Verfügbare Versichertendatensätze des ePKA MIO auflisten
  AF_10121 - Verfügbare Versichertendatensätze des ePKA MIO auflisten
  In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
  Leistungserbringer ("LE-EU"), um sich von ihm behandeln zu lassen (Behandlungskontext).
  Der Versicherte möchte im Rahmen der Behandlung dem LE-EU seine elektronische Patientenkurzakte in
  zu Form eines Patient Summaries zu Verfügung stellen.
  (eHDSI Anwendungsszenario "Patient Summary Country A")
  Der Versicherte wurde vom LE-EU bereits erfolgreich identifiziert (Use Case 1 - siehe patientIdentification.feature).
  In diesem nachfolgenden Use Case soll der LE-EU die im Aktenkonto des/der Versicherten als
  Patient Summary abrufbaren Dokumente ermitteln.

  Grundlage:
    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt

  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_UC2_001
  @STATUS:Spezifiziert @MODUS:Automatisch @PRIO:1 @TESTFALL:Positiv @TESTSTUFE:3
  @AF-ID:AF_10121
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_FindDocuments
  @E2E
  @CI
  Szenario: NCPeH - PSA UC2 Suche nach den verfügbaren Patient Summary Dokumenten eines Versicherten durch einen LE-EU
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt und die Durchführung
    einer Behandlung akzeptiert und der NCPeH Land B hat intern eine Treatment Relationship Confirmation (TRC) erstellt.
    Im Aktenkonto des Versicherten ist ein ePKA Dokument (als Datenbasis für das Patient Summary) vorhanden.
    Der LE-EU startet die Suche nach den verfügbaren Patient Summary Dokumenten. Als Antwort erhält der LE-EU
    eine Metadaten-Liste zu zwei abrufbaren Dokumenten mit
      uniqueId für ein Patient Summary als strukturiertes Dokument (CDA Level 3) und
      uniqueId für ein Patient Summary als PDF-Dokument (CDA Level 1)

    Wenn der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft
    Dann erhält der LE-EU eine Liste mit zwei Dokumentreferenzen zurück
    Und ein Dokument in der Liste referenziert das Patient Summary der versicherten Person mit CDA Level 1
    Und ein Dokument in der Liste referenziert das Patient Summary der versicherten Person mit CDA Level 3


  # PET/Polarion Tags
  @TCID:NCP1_IOP_PSA_UC2_010
  @STATUS:InBearbeitung @MODUS:Automatisch @PRIO:1 @TESTFALL:Negativ @TESTSTUFE:3
  @AF-ID:AF_10121
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_FindDocuments
  @IOP
  Szenario: NCPeH - PSA UC2 Suche nach den verfügbaren Patient Summary Dokumenten - es wird kein ePKA Dokument gefunden
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt und die Durchführung
    einer Behandlung akzeptiert und der NCPeH Land B hat intern eine Treatment Relationship Confirmation (TRC) erstellt.
    Im Aktenkonto des Versicherten wurde im Zeitraum zwischen der Identifikation des Patienten und der Suche
    das ePKA Dokument gelöscht.
    Der NCPeH-FD erhält vom ePA Aktensystem einen ResponseStatus Success mit einer leeren Dokumentenliste zurück
    Situationen mit vergleichbarer Reaktion des Aktensystems:
      Dokument fehlt
      Berechtigung ist abgelaufen (siehe gemSpec_Dokumentenverwaltung#A_13585)
      AccessCode ist falsch (bzw. mit der Berechtigung abgelaufen) (siehe gemSpec_Dokumentenverwaltung#A_13585)
    Der NCPeH gibt eine Antwort gemäß TUC_NCPeH_013 für NCPeH UseCase 2 mit Fehlercode ERROR_GENERIC_DOCUMENT_MISSING
    zurück.
    In diesem Szenario wird mit geprüft, dass durch Update / Neuerteilung der Berechtigung des EU-Landes vom
    FdV eine neuer AccessCode berechnet und im ePA Aktensystem in die Policy geschrieben wird.

    Wenn die versicherte Person die Berechtigung des EU-Landes erneuert
    Und der LE-EU die Suche nach den Patient Summary Dokumenten der versicherten Person aufruft
    Dann erhält der LE-EU keine Liste von Dokumentreferenzen
    Und der LE-EU erhält einen FindDocuments RegistryError mit errorCode ERROR_GENERIC_DOCUMENT_MISSING

