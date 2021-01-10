package com.georgeisaev.multilingualstorage.dao.sameTable;

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
public class SameTableDao implements Dao {

	private static final String INSERT = "insert into \"01_same_table_account\" " +
			"(first_name_en, first_name_ru, first_name_ko) values (?,?,?);";
	private static final String SELECT = "select id, first_name_en, first_name_ru, first_name_ko " +
			"from \"01_same_table_account\"";
	private static final String UPDATE = "update \"01_same_table_account\" " +
			"set first_name_en = ?, first_name_ru = ?, first_name_ko = ? where id = ?;";
	private static final String DELETE = "delete from \"01_same_table_account\"; ALTER SEQUENCE \"01_same_table_account_id_seq\" RESTART;\n";
	private JdbcTemplate jdbc;

	@Autowired
	public SameTableDao(JdbcTemplate jdbc) {
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
				preparedStatement.setLong(4, accountsToUpdate.get(i).getId());
				preparedStatement.setString(1, accountsToUpdate.get(i).getFirstName().getString(Language.ENGLISH));
				preparedStatement.setString(2, accountsToUpdate.get(i).getFirstName().getString(Language.RUSSIAN));
				preparedStatement.setString(3, accountsToUpdate.get(i).getFirstName().getString(Language.KOREAN));
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
