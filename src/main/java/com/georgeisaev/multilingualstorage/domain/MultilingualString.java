package com.georgeisaev.multilingualstorage.domain;

import java.util.EnumMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MultilingualString {

	private final long id;
	private final Map<Language, String> strings;

	public MultilingualString(long id) {
		this.id = id;
		this.strings = new EnumMap<>(Language.class);
	}

	public long getId() {
		return id;
	}

	public Map<Language, String> getStrings() {
		return strings;
	}

	public void setString(Language language, String string) {
		this.strings.put(requireNonNull(language), requireNonNull(string));
	}

	public String getString(Language language) {
		return this.strings.getOrDefault(requireNonNull(language), "");
	}

	public static MultilingualString of(long id, String en, String ru, String ko) {
		var multilingualString = new MultilingualString(id);
		multilingualString.setString(Language.ENGLISH, en);
		multilingualString.setString(Language.RUSSIAN, ru);
		multilingualString.setString(Language.KOREAN, ko);
		return multilingualString;
	}

	public static MultilingualString of(String en, String ru, String ko) {
		return of(0, en, ru, ko);
	}

}
