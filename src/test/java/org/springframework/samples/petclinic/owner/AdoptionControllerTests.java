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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for {@link AdoptionController}.
 *
 * @author Wick Dynex
 */
@WebMvcTest(AdoptionController.class)
@DisabledInNativeImage
@DisabledInAotMode
class AdoptionControllerTests {

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PetRepository pets;

	private Pet adoptablePet() {
		Pet pet = new Pet();
		pet.setId(TEST_PET_ID);
		pet.setName("Buddy");
		pet.setBirthDate(LocalDate.of(2021, 3, 15));
		PetType dog = new PetType();
		dog.setName("dog");
		pet.setType(dog);
		pet.setAdoptable(true);
		return pet;
	}

	@BeforeEach
	void setup() {
		given(this.pets.findByAdoptableTrueOrderByName()).willReturn(List.of(adoptablePet()));
		given(this.pets.findById(TEST_PET_ID)).willReturn(Optional.of(adoptablePet()));
	}

	@Test
	void testListAdoptablePets() throws Exception {
		mockMvc.perform(get("/adoptions"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("pets"))
			.andExpect(model().attribute("pets", hasSize(1)))
			.andExpect(model().attribute("pets", org.hamcrest.Matchers.hasItem(hasProperty("name", is("Buddy")))))
			.andExpect(view().name("adoptions/adoptionList"));
	}

	@Test
	void testListAdoptablePetsEmpty() throws Exception {
		given(this.pets.findByAdoptableTrueOrderByName()).willReturn(List.of());
		mockMvc.perform(get("/adoptions"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("pets", hasSize(0)))
			.andExpect(view().name("adoptions/adoptionList"));
	}

	@Test
	void testAdoptPetSuccess() throws Exception {
		mockMvc.perform(post("/adoptions/{petId}/adopt", TEST_PET_ID))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/adoptions"))
			.andExpect(flash().attribute("message", "Pet adopted successfully!"));
	}

	@Test
	void testAdoptPetNotAdoptable() throws Exception {
		Pet nonAdoptable = adoptablePet();
		nonAdoptable.setAdoptable(false);
		given(this.pets.findById(TEST_PET_ID)).willReturn(Optional.of(nonAdoptable));

		org.junit.jupiter.api.Assertions.assertThrows(Exception.class,
				() -> mockMvc.perform(post("/adoptions/{petId}/adopt", TEST_PET_ID)));
	}

}
