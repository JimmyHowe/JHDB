# Test Case SQL

## Schema

    CREATE TABLE table_name (column_1 datatype, column_2 datatype, column_3 datatype);

    ALTER TABLE table_name ADD column datatype;

## Queries

### Selects

    SELECT * FROM table_name;
    
    SELECT column_name(s) FROM table_name;
    
    SELECT DISTINCT column_name FROM table_name;
    
    SELECT column_name AS 'Alias' FROM table_name;
    
    SELECT column_name(s) FROM table_name LIMIT number;

#### Wheres
    
    SELECT column_name(s) FROM table_name WHERE column_name operator value;
    
    SELECT column_name(s) FROM table_name WHERE column_name NOT OPERATOR value
    
    SELECT column_name(s) FROM table_name WHERE column_name BETWEEN value_1 AND value_2;
    
    SELECT column_name(s) FROM table_name WHERE column_1 = value_1 AND column_2 = value_2;
    
    SELECT column_name FROM table_name WHERE column_name = value_1 OR column_name = value_2;
    
    SELECT column_name(s) FROM table_name WHERE column_name LIKE pattern;

    SELECT column_name(s) FROM table_1 JOIN table_2 ON table_1.column_name = table_2.column_name;
    
    SELECT column_name(s) FROM table_1 LEFT JOIN table_2 ON table_1.column_name = table_2.column_name;
    
    SELECT column_name FROM table_name ORDER BY column_name ASC|DESC;

### Inserts

    INSERT INTO table_name (column_1, column_2, column_3) VALUES (value_1, 'value_2', value_3);

### Update
    
    UPDATE table_name SET some_column = some_value WHERE some_column = some_value;
    
### Deletes
    
    DELETE FROM table_name WHERE some_column = some_value;

### Misc

    SELECT COUNT(column_name) FROM table_name;
    
    SELECT SUM(column_name) FROM table_name;
    
    SELECT AVG(column_name) FROM table_name;
    
    SELECT COUNT(*) FROM table_name GROUP BY column_name;