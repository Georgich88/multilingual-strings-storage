# Multilingual strings storage

This project demonstrates different ways to store multilingual strings for dynamic data

## Problem

Efficient storage of language sensitive data, including:
* Insertion
* Selection
* Updating
* Deletion
* Add new language
* Add new language sensitive data

*For instance, user profile details with language sensitive data such as first, second and middle name. Name can be spelled differently
in different languages.  
Name **Alexander** spelling varies:*

| Language | Spelling  |
|----------|-----------|
| en       | Alexander |
| be       | Аляксандр |
| ru       | Александр |
| ko       | 알렉산드로스|


*Another example is a localized product name for international manufacturers.  
Name **Mitsubishi Pajero** spelling varies:*

| Language | Spelling  |
|----------|-----------|
| en       | Mitsubishi Pajero |
| es       | Mitsubishi Montero|
| ja       | 三菱・パジェロ |

## Main approaches

There are two wide known approaches:

1. Store language specific data in the same table
2. Store language specific in the auxiliary table
   - one extension table
   - extension table for each multilingual attribute
   - shared extension table for all multilingual data (with surrogate key)

## Store language specific data in the same table

**Table `account`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - primary key  |
| **first_name_en**              | nvarchar           |
| **first_name_ru**              | nvarchar           |
| **second_name_en**             | nvarchar           |
| **second_name_ru**             | nvarchar           |
| other language-specific fields |                    |
| other language-neutral fields  |                    |

## Store language specific in the auxiliary table
### Extension table for parent table
**Table `account`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - primary key  |
| other language-neutral fields  |                    |

**Table `account_translations`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - foreign key to `account` table |
| **language**                   | varchar  (e.g. "en", "ru")           |
| **first_name**                 | nvarchar           |
| **second_name**                | nvarchar           |
| other language-specific fields |                    |

### Extension table for parent table for each attribute
**Table `account`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - primary key  |
| other language-neutral fields  |                    |

**Table `account_first_name_translations`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - foreign key to `account` table |
| **language**                   | varchar  (e.g. "en", "ru")           |
| **translation**                | nvarchar           |

**Table `account_second_name_translations`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - foreign key to `account` table |
| **language**                   | varchar  (e.g. "en", "ru")           |
| **translation**                | nvarchar           |

### A shared table for all translatable fields of all tables
**Table `account`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - primary key  |
| **first_name**                 | int - foreign key to `localized_text` table |
| **second_name**                | int - foreign key to `localized_text` table |
| other language-neutral fields  |                    |

**Table `localized_text`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - primary key  |

**Table `translations`**

| Field                          | Type               |
|--------------------------------|--------------------|
| id                             | int - foreign key to `localized_text` table |
| **language**                   | varchar  (e.g. "en", "ru")           |
| **translation**                | nvarchar           |