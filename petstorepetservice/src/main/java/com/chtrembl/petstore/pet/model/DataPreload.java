package com.chtrembl.petstore.pet.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DataPreload {

	private List<Pet> pets = new ArrayList<>();

}