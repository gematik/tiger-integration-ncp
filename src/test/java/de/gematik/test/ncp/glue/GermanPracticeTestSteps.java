/*
 * Copyright 2023 gematik GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.gematik.test.ncp.glue;

import static de.gematik.test.ncp.screenplay.GermanPracticeActions.*;
import static de.gematik.test.ncp.screenplay.PatientActions.*;
import static de.gematik.test.ncp.screenplay.Practice.*;

import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import io.cucumber.junit.CucumberOptions;

@CucumberOptions(monochrome = true)
public class GermanPracticeTestSteps {
  @Angenommen("^(?:der|die) Versicherte (.+) hat ein aktives ePA Konto$")
  public void getPatientConfiguration(String patientName) {
    // take patient configuration from file fitting to patient name
    // must contain Name, KVNR and birthdate
    // GermanPracticeActions.user().wasAbleTo(GermanPracticeActions.findAktenkontoForPatient());
    germanPractice()
        .callWaitingPatientWithName(patientName)
        .verifyThat(leistungserbringerIsAuthorizedForAktenkonto(germanPractice().practitioner()))
        .has(activeAktenkonto());
  }

  @Angenommen("^(?:der|die) Versicherte (.+) hat ein ePA Aktenkonto im Status Dismissed$")
  public void getPatientConfigurationWithAktenkontoDismissed(String patientName) {
    // take patient configuration from file fitting to patient name
    // must contain Name, KVNR and birthdate
    // GermanPracticeActions.user().wasAbleTo(GermanPracticeActions.findAktenkontoForPatient());
    // germanPractice().callWaitingPatientWithName(patientName).has(dismissedAktenkonto());
  }

  @Angenommen(
      "^eine versicherte Person hat ein aktives ePA Konto im Aktensystem des Betreibers (IBM|Rise|xtension)$")
  public void getPatientConfigurationForAktensystemInstance(String aktensystem) {
    // get a patient name from config, that is assigned to the given Aktensystem instance
    // retrieved patientName must contain Name, KVNR and birthdate
    germanPractice().callWaitingPatientWithKontoInAktensystem(aktensystem);
  }

  @Und(
      "^in dem Konto befindet sich ein ECC signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist$")
  public void prepareAndPutEpkaDocument() {
    // take data from patient being on stage
    // fill a default ePKA document template with KVNR, name and birthdate of the patient
    // sign the constructed ePKA document with an LE-DE identity
    // store constructed and signed ePKA document in the ePA account of the patient
    germanLei()
        .verifyThat(leistungserbringerIsAuthorizedForAktenkonto())
        .verifyThat(noEpkaIsPresentInAktenkonto())
        .attemptsTo(createEpka().then(signEpka()).then(storeEpkaInAktenkonto()))
        .acknowledgeThat(epkaCanBeFoundInAktenkonto());
  }

  @Und(
      "^in dem Konto befindet sich ein vom Konnektor (Koco|Rise|Secunet) mit (ECC|RSA) signiertes ePKA-Dokument, in dem Name und Geburtsdatum der versicherten Person enthalten ist$")
  public void prepareAndPutEpkaDocumentWithKonnektorAndSignatureAlgorithm(
      String konnektorType, String signatureAlgorithm) {
    // take data from patient being on stage
    // fill a default ePKA document template with KVNR, name and birthdate of the patient
    // sign the constructed ePKA document with an LE-DE identity
    //    with given konnektorType and
    //    with given signatureAlgorithm
    // store constructed and signed ePKA document in the ePA account of the patient
    //    via given konnektorType
    germanLei()
        .attemptsTo(
            selectKonnektor(konnektorType)
                .then(createEpka())
                .then(signEpkaUsingConfiguration(true, signatureAlgorithm))
                .then(storeEpkaInAktenkonto()))
        .acknowledgeThat(epkaCanBeFoundInAktenkonto());
  }

  @Wenn("ein LE-DE das ePKA Dokument der versicherten Person überschreibt")
  public void searchAndOverwriteEpkaDocument() {
    // ensure access rights for the LE-DE
    // search for the document
    // write a new ePKA document (can even be exactly the same - just ensure a new DocumentUniqueId
    // being is generated)
  }

  @Angenommen("das ePKA Dokument der versicherten Person wurde gelöscht")
  @Wenn("^das ePKA Dokument der versicherten Person gelöscht wurde$")
  public void searchAndDeleteEpkaDocument() {
    germanLei()
        .treat(germanPractice().currentPatient())
        .verifyThat(leistungserbringerIsAuthorizedForAktenkonto())
        .verifyThat(noEpkaIsPresentInAktenkonto());
  }

  @Und("^das ePKA Dokument der versicherten Person ist nicht konform zum FHIR Schema des ePKA MIO$")
  public void prepareSchemaDefectiveEpkaDocumentAndPutDocument() {
    // take data from patient being on stage
    // fill a schema-defect ePKA document template (missing mandatory element gender) with KVNR,
    // name and birthdate of the patient
    // sign the constructed ePKA document with an LE-DE identity
    // store constructed and signed ePKA document in the ePA account of the patient
    /*
    germanLei()
        .usingAbilityTo(LeiActor.TREAT_PATIENTS)
        .onPatient(germanPractice().currentPatient())
        .verifyThat(leistungserbringerIsAuthorizedForAktenkonto())
        // TODO: .ensuresThat(noEpkaIsPresentInAktenkonto()) implementieren
        //.ensuresThat(noEpkaIsPresentInAktenkonto())
        // TODO: createSchemaDefectiveEpka implementieren
        //.attemptsTo(createSchemaDefectiveEpka().then(signEpka()).then(storeEpkaInAktenkonto()))
        .acknowledgeThat(epkaCanBeFoundInAktenkonto());

     */
  }

  @Und("^die Signatur des ePKA Dokumentes der versicherten Person ist mathematisch nicht gültig$")
  public void prepareSignatureDefectiveEpkaDocumentAndPutDocument() {
    // take data from patient being on stage
    // fill a default ePKA document template with KVNR, name and birthdate of the patient
    // sign the constructed ePKA document with an LE-DE identity
    // manipulate the signature of the ePKA document
    // store constructed and signed ePKA document in the ePA account of the patient
    /*
    germanLei()
        .usingAbilityTo(LeiActor.TREAT_PATIENTS)
        .onPatient(germanPractice().currentPatient())
        .verifyThat(leistungserbringerIsAuthorizedForAktenkonto())
        // TODO: .ensuresThat(noEpkaIsPresentInAktenkonto()) implementieren
        //.ensuresThat(noEpkaIsPresentInAktenkonto())
        // TODO: signEpkaDefective implementieren
        //.attemptsTo(createEpka().then(signEpkaDefective()).then(storeEpkaInAktenkonto()))
        .acknowledgeThat(epkaCanBeFoundInAktenkonto());

     */
  }

  @Und("^die Signatur des ePKA Dokumentes der versicherten Person fehlt$")
  public void prepareSignatureMissingEpkaDocumentAndPutDocument() {
    // take data from patient being on stage
    // fill a default ePKA document template with KVNR, name and birthdate of the patient
    // store constructed and NOT SIGNED signed ePKA document in the ePA account of the patient
    /*
    germanLei()
        .usingAbilityTo(LeiActor.TREAT_PATIENTS)
        .onPatient(germanPractice().currentPatient())
        .verifyThat(leistungserbringerIsAuthorizedForAktenkonto())
        // TODO: .ensuresThat(noEpkaIsPresentInAktenkonto()) implementieren
        //.ensuresThat(noEpkaIsPresentInAktenkonto())
        .attemptsTo(createEpka().then(storeEpkaInAktenkonto()))
        .acknowledgeThat(epkaCanBeFoundInAktenkonto());

     */
  }

  @Und(
      "^in dem Konto befindet sich ein valides ePKA-Dokument, in dem nur eine DPE Composition enthalten ist$")
  public void prepareAndPutEpkaDocumentWithoutNfdComposition() {
    // take data from patient being on stage
    // fill a ePKA document template
    // - with KVNR, name and birthdate of the patient in the DPE composition
    // - without NFD composition
    // !!! sign the constructed ePKA document with an LE-DE identity
    // store constructed and signed ePKA document in the ePA account of the patient
    /*
    germanLei()
        .usingAbilityTo(LeiActor.TREAT_PATIENTS)
        .onPatient(germanPractice().currentPatient())
        .verifyThat(leistungserbringerIsAuthorizedForAktenkonto())
        // TODO: .ensuresThat(noEpkaIsPresentInAktenkonto()) implementieren
        // .ensuresThat(noEpkaIsPresentInAktenkonto())
        // TODO: createEpkaWithDpeOnly implementieren
        //.attemptsTo(createEpkaWithDpeOnly().then(signEpka()).then(storeEpkaInAktenkonto()))
        .acknowledgeThat(epkaCanBeFoundInAktenkonto());

     */
  }

  @Und("^das ePKA-Dokument wurde mit einem nonQES-Zertifikat des LE-DE signiert$")
  public void prepareAndPutEpkaDocumentWithNonQesSignature() {
    // take data from patient being on stage
    // fill a default ePKA document template with KVNR, name and birthdate of the patient
    // sign the constructed ePKA document with an LE-DE nonQES identity
    // store constructed and signed ePKA document in the ePA account of the patient
    germanLei()
        .verifyThat(leistungserbringerIsAuthorizedForAktenkonto())
        .verifyThat(noEpkaIsPresentInAktenkonto())
        .attemptsTo(
            createEpka()
                .then(signEpkaUsingConfiguration(false, "ECC"))
                .then(storeEpkaInAktenkonto()))
        .acknowledgeThat(epkaCanBeFoundInAktenkonto());
  }

  @Wenn("^ein LE-DE das ePKA Dokument der versicherten Person löscht$")
  public void deleteEpkaDocumentOfPatient() {
    // delete the document as LE-DE who did put the document before
  }
}
