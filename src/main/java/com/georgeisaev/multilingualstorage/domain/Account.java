package com.georgeisaev.multilingualstorage.domain;

import lombok.Data;

@Data
public class Account {

	private final long id;
	private final MultilingualString firstName;

}
