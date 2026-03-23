/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for the pet adoption workflow.
 *
 * <p>
 * Note: {@link PetRepository#save(Object)} is used directly rather than going through the
 * {@link Owner} aggregate because only the scalar {@code adoptable} flag is mutated — no
 * collection invariants are at risk.
 * </p>
 *
 * @author Wick Dynex
 */
@Controller
class AdoptionController {

	private final PetRepository pets;

	AdoptionController(PetRepository pets) {
		this.pets = pets;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("pet")
	public Pet findPet(@PathVariable(name = "petId", required = false) Integer petId) {
		if (petId == null) {
			return null;
		}
		return this.pets.findById(petId)
			.orElseThrow(() -> new IllegalArgumentException("Pet not found with id: " + petId));
	}

	@GetMapping("/adoptions")
	public String listAdoptablePets(Model model) {
		model.addAttribute("pets", this.pets.findByAdoptableTrueOrderByName());
		return "adoptions/adoptionList";
	}

	@PostMapping("/adoptions/{petId}/adopt")
	public String adoptPet(@ModelAttribute("pet") Pet pet, RedirectAttributes redirectAttributes) {
		if (!pet.isAdoptable()) {
			throw new IllegalArgumentException("Pet with id " + pet.getId() + " is not available for adoption.");
		}
		pet.setAdoptable(false);
		this.pets.save(pet);
		redirectAttributes.addFlashAttribute("message", "Pet adopted successfully!");
		return "redirect:/adoptions";
	}

}
