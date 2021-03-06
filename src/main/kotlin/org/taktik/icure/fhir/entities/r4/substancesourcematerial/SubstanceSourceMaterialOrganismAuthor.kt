//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.substancesourcematerial

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import kotlin.String
import kotlin.collections.List
import org.taktik.icure.fhir.entities.r4.backboneelement.BackboneElement
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.extension.Extension

/**
 * 4.9.13.6.1 Author type (Conditional)
 *
 * 4.9.13.6.1 Author type (Conditional).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
class SubstanceSourceMaterialOrganismAuthor(
  /**
   * The author of an organism species shall be specified. The author year of an organism shall also
   * be specified when applicable; refers to the year in which the first author(s) published the
   * infraspecific plant/animal name (of any rank)
   */
  var authorDescription: String? = null,
  /**
   * The type of author of an organism species shall be specified. The parenthetical author of an
   * organism species refers to the first author who published the plant/animal name (of any rank). The
   * primary author of an organism species refers to the first author(s), who validly published the
   * plant/animal name
   */
  var authorType: CodeableConcept? = null,
  override var extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override var id: String? = null,
  override var modifierExtension: List<Extension> = listOf()
) : BackboneElement
