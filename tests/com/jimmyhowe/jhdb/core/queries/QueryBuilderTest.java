package com.jimmyhowe.jhdb.core.queries;

import com.jimmyhowe.jhdb.core.Plugin;
import com.jimmyhowe.jhdb.core.processors.TableProcessor;
import com.jimmyhowe.jhdb.core.queries.components.AndWhere;
import com.jimmyhowe.jhdb.core.queries.components.OrWhere;
import com.jimmyhowe.jhdb.core.queries.components.Where;
import com.jimmyhowe.jhdb.sqlite.SQLiteMemoryPlugin;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryBuilderTest
{
    @NotNull
    private Plugin plugin = new SQLiteMemoryPlugin();

    private QueryBuilder queryBuilder;

    @BeforeEach
    public void setUp() throws Exception
    {
        queryBuilder = getQueryBuilder();
    }

    @AfterEach
    public void tearDown() throws Exception
    {

    }

    private QueryBuilder getQueryBuilder()
    {
        return new QueryBuilder(
                plugin.getConnection(),
                plugin.getQueryGrammar(),
                new TableProcessor()
        ).from("test");
    }

    @Test
    public void it_can_return_table_name() throws Exception
    {
        assertEquals("test", this.queryBuilder.getTableName());
    }

    @Test
    public void it_can_generate_a_simple_select_all() throws Exception
    {
        assertEquals("SELECT * FROM \"test\"", this.queryBuilder.toSql());
    }

    @Test
    public void it_can_generate_a_simple_select_with_columns() throws Exception
    {
        assertEquals("SELECT \"id\", \"name\", \"email\" FROM \"test\"",
                     this.queryBuilder.select("id", "name", "email").toSql());
    }

    @Test
    public void it_can_generate_a_simple_select_with_columns_and_alias() throws Exception
    {
        String expected = "SELECT \"id\", \"name\" AS \"username\", \"email\" FROM \"test\"";

        assertEquals(expected, this.queryBuilder.select("id", "name as username", "email").toSql());
    }

    @Test
    public void it_can_generate_a_select_all_with_a_where_statement() throws Exception
    {
        assertEquals("SELECT * FROM \"test\" WHERE name = 'Jimmy'",
                     this.queryBuilder.where("name", "Jimmy").toSql());
    }

    @Test
    public void it_can_generate_a_select_all_with_a_where_not_statement() throws Exception
    {
        assertEquals("SELECT * FROM \"test\" WHERE name != 'Jimmy'",
                     this.queryBuilder.whereNot("name", "Jimmy").toSql());
    }

    @Test
    public void it_can_generate_a_select_all_with_a_where_statement_with_operator() throws Exception
    {
        assertEquals("SELECT * FROM \"test\" WHERE name != 'Jimmy'",
                     this.queryBuilder.where("name", "!=", "Jimmy").toSql());
    }

    @Test
    public void it_can_generate_a_select_all_with_an_order_statement() throws Exception
    {
        assertEquals("SELECT * FROM \"test\" ORDER BY name ASC", this.queryBuilder.orderBy("name").toSql());
    }

    @Test
    public void it_can_generate_a_select_all_with_an_order_desc_statement() throws Exception
    {
        assertEquals("SELECT * FROM \"test\" ORDER BY name DESC", this.queryBuilder.orderByDesc("name").toSql());
    }

    @Test
    public void it_can_generate_a_select_all_with_a_specified_order_statement() throws Exception
    {
        assertEquals("SELECT * FROM \"test\" ORDER BY name DESC", this.queryBuilder.orderBy("name", "DESC").toSql());
    }

    @Test
    public void it_can_generate_a_select_all_with_an_order_by_and_a_where_statement() throws Exception
    {
        String expected = "SELECT * FROM \"test\" WHERE name = 'Jimmy' ORDER BY name DESC";

        Object actual = this.queryBuilder.where("name", "Jimmy").orderBy("name", "DESC").toSql();

//        assertEquals(expected, actual);
    }

    @Test
    public void it_can_generate_a_select_first_with_an_order_by_and_a_where_statement() throws Exception
    {
        String expected = "SELECT * FROM \"test\" WHERE name = 'Jimmy' ORDER BY name DESC LIMIT 1";

        Object actual = this.queryBuilder.where("name", "Jimmy").orderBy("name", "DESC").limit(1).toSql();

        assertEquals(expected, actual);
    }

    @Test
    public void it_can_generate_a_select_distinct() throws Exception
    {
        String expected = "SELECT DISTINCT \"email\" FROM \"test\"";

        Object actual = this.queryBuilder.distinct().select("email").toSql();

        assertEquals(expected, actual);
    }

    @Test
    public void it_can_generate_select_with_and_where() throws Exception
    {
        String expected = "SELECT * FROM \"test\" WHERE id = '1' AND id = '2'";

        Object actual = this.queryBuilder.where("id", 1).andWhere("id", 2).toSql();

        assertEquals(expected, actual);
    }

    @Test
    public void it_can_generate_select_with_or_where() throws Exception
    {
        String expected = "SELECT * FROM \"test\" WHERE id = '1' OR id = '2'";

        Object actual = this.queryBuilder.where("id", 1).orWhere("id", 2).toSql();

        assertEquals(expected, actual);
    }

    @Test
    public void it_can_generate_select_with_multiple_wheres() throws Exception
    {
        String expected = "SELECT * FROM \"test\" WHERE ( id = '1' AND name = 'Jimmy' )";

        Object actual = this.queryBuilder.where(
                new Where("id", 1),
                new AndWhere("name", "Jimmy")
        ).toSql();

        assertEquals(expected, actual);
    }

    @Test
    public void it_can_generate_select_with_multiple_wheres_and_or_where() throws Exception
    {
        String expected = "SELECT * FROM \"test\" WHERE ( id = '1' AND name = 'Jimmy' ) OR id = '2'";

        Object actual = this.queryBuilder.where(
                new Where("id", 1),
                new AndWhere("name", "Jimmy")
        ).orWhere("id", 2).toSql();

        assertEquals(expected, actual);
    }

    @Test
    public void it_can_generate_select_with_multiple_and_or_where() throws Exception
    {
        String expected = "SELECT * FROM \"test\" WHERE ( id = '1' OR name = 'Jimmy' ) AND ( id = '2' OR name = 'Jurij' )";

        Object actual = this.queryBuilder
                .where(new Where("id", 1), new OrWhere("name", "Jimmy"))
                .andWhere(new Where("id", 2), new OrWhere("name", "Jurij"))
                .toSql();

        assertEquals(expected, actual);
    }

    @Test
    public void it_can_generate_an_update_statement() throws Exception
    {
//        String expected = "UPDATE \"test\" SET one = 'hello', two = 'world'";
//
//        Object actual = queryBuilder.set("one", "hello").set("two", "world").getGrammar().compileUpdate(queryBuilder);
//
//        assertEquals(expected, actual);
    }

//    @Test
//    public void it_can_generate_an_update_statement_with_where_clause() throws Exception
//    {
//        String expected = "UPDATE \"test\" SET name = 'Jimmy' WHERE id = '1'";
//
//        QueryBuilder queryBuilder = this.queryBuilder.set("name", "Jimmy").where("id", 1);
//
//        assertEquals(expected, queryBuilder.getGrammar());
//    }
//
//    @Test
//    public void it_can_generate_a_delete_statement() throws Exception
//    {
//        String expected = "DELETE FROM \"test\" WHERE id = '1'";
//
//        Object actual = this.queryBuilder.where("id", 1).delete();
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    public void it_can_generate_a_delete_statement_with_multiple_wheres() throws Exception
//    {
//        String expected = "DELETE FROM \"test\" WHERE id = '1' AND name = 'Jimmy'";
//
//        Object actual = this.queryBuilder.where("id", 1).andWhere("name", "Jimmy").delete();
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void it_can_generate_a_delete_statement_with_limit() throws Exception
//    {
//        String expected = "DELETE FROM \"test\" WHERE age < '21' LIMIT 1";
//
//        Object actual = this.queryBuilder.where("age", "<", 21).limit(1).delete();
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    public void it_can_use_raw_select() throws Exception
//    {
//        String expected = "SELECT count(*) as user_count FROM \"test\"";
//
//        Object actual = this.queryBuilder.select(DB.raw("count(*) as user_count")).toSql();
//
//        assertEquals(expected, actual);
//    }

//    @Test
//    public void it_can_use_raw_select_and_add_selects() throws Exception
//    {
//        String expected = "SELECT *, count(*) as user_count FROM \"test\"";
//
//        Object actual = this.queryBuilder
//                .select()
//                .addSelect(DB.raw("count(*) as user_count"))
//                .toSql();
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void it_can_generate_an_insert_statement() throws Exception
//    {
//        String expected = "INSERT INTO \"test\" (name) VALUES ('Jimmy')";
//        Object actual = this.queryBuilder.insertInto("name").values("Jimmy");
//
//        assertEquals(expected, actual);
//    }

}