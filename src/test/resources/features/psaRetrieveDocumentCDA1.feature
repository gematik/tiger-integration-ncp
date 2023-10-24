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
Funktionalität: NCPeH PSA Use Case 4 - Versichertendatensatz als PDF abrufen
  AF_10123 - Versichertendatensatz als PDF abrufen
  In diesem Anwendungsfall befindet sich eine in Deutschland versicherte Person bei einem europäischen
  Leistungserbringer ("LE-EU"), um sich von ihm behandeln zu lassen (Behandlungskontext).
  Der Versicherte möchte im Rahmen der Behandlung dem LE-EU seine elektronische Patientenkurzakte in
  Form einer Patient Summary zur Verfügung stellen.
  (eHDSI Anwendungsszenario "Patient Summary Country A")
  Im Geradeaus-Fall wurde der Versicherte vom LE-EU bereits erfolgreich identifiziert (Use Case 1 -
  siehe patientIdentification.feature) und die Liste der verfügbaren Dokumente wurde bereits abgerufen
  (Use Case 2 - siehe psaFindDocuments.feature).
  In folgenden Use Case #3 soll der LE-EU das ePKA-Dokument im Aktenkonto der versicherten Person als
  Patient Summary in Form einer PDF-Datei (CDA Level 1) abrufen. Die PDF-Datei enthält keine Transkodierung
  von eventuell im ePKA Dokument enthaltenen Codes. Es enthält nur die ins PDF transformierte
  NFD Composition des ePKA Dokumentes.

  Grundlage:
    Angenommen die Versicherte Prof. Dr. Felicitas Anna Julia ÐoriaTEST-ONLY hat ein aktives ePA Konto
    Und in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist
    Und die versicherte Person begibt sich in dem EU-Land NETHERLANDS bei LE-EU van der Meer in Behandlung
    Und die versicherte Person hat den NCPeH Fachdienst für den Zugriff dieses EU-Landes auf ihr ePA Konto berechtigt
    Und die versicherte Person hat ihre KVNR und den AccessCode an den LE-EU übergeben
    Und der LE-EU hat erfolgreich die Patient Identification der versicherten Person durchgeführt
    Und der LE-EU hat erfolgreich Find Documents zum Patient Summary der versicherten Person durchgeführt

  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_UC4_001
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10123
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA1
  @E2E
  Szenario: NCPeH - PSA UC4 Patient Summary als PDF-Dokument (CDA1) abrufen
    Im Aktenkonto des Versicherten ist ein ePKA Dokument (als Datenbasis für das Patient Summary) vorhanden.
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren Dokumente
    ermittelt.
    Der LE-EU ruft das verfügbare Patient Summary als PDF Dokument ab. Als Antwort erhält der LE-EU
    ein Patient Summary Dokument im PDF-Format (CDA Level 1).

    Hinweis: es erfolgt kein ausreichender Test der PDF-Transformation.
      Es wird nur visuell die Lesbarkeit und das Vorhandensein der persönlichen Daten des/der Versicherten im
      Ergebnisdokument geprüft.
      # Falls weitergehende Prüfungen der Transformation erfolgen sollten, so sind
      # diese in einem separaten feature-File abzuhandeln

    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 1 abruft
    Dann erhält der LE-EU ein Dokument der versicherten Person zurück
    Und das Dokument ist im Format CDA Level 1
    # Die inhaltliche Prüfung des PDF-Dokumentes ist ein manueller Testschritt
    Und das PDF Dokument enthält die persönlichen Daten der versicherten Person

  # PET/Polarion Tags
  @TCID:NCP1_E2E_PSA_UC4_002
  @STATUS:Spezifiziert @MODUS:Halbautomatisch @PRIO:1 @TESTFALL:Positiv
  @AF-ID:AF_10122 @AF-ID:AF_10123
  @DESCRIPTION
  # Weitere Tags, team-intern
  @eHDSI-Szenario:PSA_RetrieveDocumentCDA3 @eHDSI-Szenario:PSA_RetrieveDocumentCDA1
  @E2E
  Szenario: NCPeH - PSA UC3+UC4 Patient Summary gleichzeitig als CDA3 und CDA1 abrufen
    Im Aktenkonto des Versicherten ist ein ePKA Dokument (als Datenbasis für das Patient Summary) vorhanden.
    Die Identifikation des Patienten wurde vom LE-EU erfolgreich durchgeführt, der NCPeH Land B hat intern
    eine Treatment Relationship Confirmation (TRC) erstellt und der LE-EU hat die Liste der verfügbaren
    Dokumente ermittelt.
    Der LE-EU ruft in einem Schritt die Patient Summary in beiden CDA-Formen (strukturiert und als PDF) ab.
    Als Antwort erhält der LE-EU ein Patient Summary Dokument in beiden CDA-Varianten (Level 3 und Level 1).

    Wenn der LE-EU das Patient Summary Dokument der versicherten Person als CDA Level 3 und Level 1 abruft
    Dann erhält der LE-EU beide Patient Summary Dokumente der versicherten Person zurück
    Und ein Dokument ist im Format CDA Level 3
    Und das strukturierte Dokument enthält die persönlichen Daten der versicherten Person
    Und ein Dokument ist im Format CDA Level 1
    # Die inhaltliche Prüfung des PDF-Dokumentes ist ein manueller Testschritt
    Und das PDF Dokument enthält die persönlichen Daten der versicherten Person


  # Hinweis: ein PartialSuccess bei gleichzeitigem Abruf beider Dokumentvarianten könnte möglich sein
  # wenn z.B. die Transkodierung nach CDA3 nicht erfolgreich ist, aber die PDF-Generierung schon.
  # Das könnte mit einem bestimmten ePKA Dokument provoziert werden (z. B. eine Basic Section fehlt),
  # es sei denn, in der Transformation nach PDF sind Regeln vorgeschaltet, die auch eine PDF-
  # Generierung verhindern (-> BfArM).
  # Kann nach aktuellen Vereinbarungen nur durch BfArM und/oder DVKA getestet werden, da sie die
  # Transformations- und Transkodierungsregeln ausarbeiten, implementieren bzw. integrieren und testen.
