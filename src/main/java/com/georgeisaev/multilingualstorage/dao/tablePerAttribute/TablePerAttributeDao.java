package com.georgeisaev.multilingualstorage.dao.tablePerAttribute;

import com.georgeisaev.multilingualstorage.dao.Dao;
import com.georgeisaev.multilingualstorage.domain.Account;
import com.georgeisaev.multilingualstorage.domain.Language;
import com.georgeisaev.multilingualstorage.domain.MultilingualString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class TablePerAttributeDao implements Dao {

	private static final String INSERT = "with ids as (insert into \"03_table_per_attribute_account\" default values returning id),\n" +
			"en as (insert into \"03_table_per_attribute_account_first_name_translations\" (id, language, translation) select id, 'en', ? from ids),\n" +
			"ru as (insert into \"03_table_per_attribute_account_first_name_translations\" (id, language, translation) select id, 'ru', ? from ids),\n" +
			"ko as (insert into \"03_table_per_attribute_account_first_name_translations\" (id, language, translation) select id, 'ko', ? from ids)\n" +
			"select id from ids;";
	private static final String SELECT = "select account.id,\n" +
			"translations_en.translation as first_name_en,\n" +
			"translations_ru.translation as first_name_ru,\n" +
			"translations_ko.translation as first_name_ko\n" +
			"from \"03_table_per_attribute_account\" AS account\n" +
			"  left join \"03_table_per_attribute_account_first_name_translations\" AS \"translations_en\"\n" +
			"     on account.id = \"translations_en\".id and translations_en.language = 'en'\n" +
			"  left join \"03_table_per_attribute_account_first_name_translations\" AS \"translations_ru\"\n" +
			"     on account.id = \"translations_ru\".id and translations_ru.language = 'ru'\n" +
			"  left join \"03_table_per_attribute_account_first_name_translations\" AS \"translations_ko\"\n" +
			"     on account.id = \"translations_ko\".id and translations_ko.language = 'ko'";
	private static final String UPDATE = "with ids as (select id from \"03_table_per_attribute_account\" where id = ?),\n" +
			"     en as (update \"03_table_per_attribute_account_first_name_translations\" as en set translation = ? from (select id from ids) AS ids where en.id = ids.id and language = 'en'),\n" +
			"     ru as (update \"03_table_per_attribute_account_first_name_translations\" as ru set translation = ? from (select id from ids) AS ids where ru.id = ids.id and language = 'ru'),\n" +
			"     ko as (update \"03_table_per_attribute_account_first_name_translations\" as ko set translation = ? from (select id from ids) AS ids where ko.id = ids.id and language = 'ko')\n" +
			"select id from ids;";
	private static final String DELETE = "delete from \"03_table_per_attribute_account_first_name_translations\";\n" +
			"delete from \"03_table_per_attribute_account\"\n;" +
			"ALTER SEQUENCE \"03_table_per_attribute_account_id_seq\" RESTART;\n";
	private JdbcTemplate jdbc;

	@Autowired
	public TablePerAttributeDao(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public void save(List<Account> accounts) {
		List<Account> accountsToInsert = accounts.stream()
				.filter(account -> account.getId() == 0)
				.collect(Collectors.toList());
		List<Account> accountsToUpdate = accounts.stream()
				.filter(account -> account.getId() != 0)
				.collect(Collectors.toList());
		insert(accountsToInsert);
		update(accountsToUpdate);
	}


	private void insert(List<Account> accountsToInsert) {
		jdbc.batchUpdate(INSERT, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
				preparedStatement.setString(1, accountsToInsert.get(i).getFirstName().getString(Language.ENGLISH));
				preparedStatement.setString(2, accountsToInsert.get(i).getFirstName().getString(Language.RUSSIAN));
				preparedStatement.setString(3, accountsToInsert.get(i).getFirstName().getString(Language.KOREAN));
			}

			@Override
			public int getBatchSize() {
				return accountsToInsert.size();
			}
		});
	}

	private void update(List<Account> accountsToUpdate) {
		jdbc.batchUpdate(UPDATE, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
				preparedStatement.setLong(1, accountsToUpdate.get(i).getId());
				preparedStatement.setString(2, accountsToUpdate.get(i).getFirstName().getString(Language.ENGLISH));
				preparedStatement.setString(3, accountsToUpdate.get(i).getFirstName().getString(Language.RUSSIAN));
				preparedStatement.setString(4, accountsToUpdate.get(i).getFirstName().getString(Language.KOREAN));
			}

			@Override
			public int getBatchSize() {
				return accountsToUpdate.size();
			}
		});
	}

	@Override
	public List<Account> findAll() {
		return jdbc.query(SELECT,
				(resultSet, i) -> new Account(
						resultSet.getLong("id"),
						MultilingualString.of(0,
								resultSet.getString("first_name_en"),
								resultSet.getString("first_name_ru"),
								resultSet.getString("first_name_ko"))
				));
	}

	@Override
	public void deleteAll() {
		jdbc.execute(DELETE);
	}


}
