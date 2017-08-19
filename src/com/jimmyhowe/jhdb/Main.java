package com.jimmyhowe.jhdb;

import com.jimmyhowe.consolecolors.Console;
import com.jimmyhowe.jhdb.core.DB;
import com.jimmyhowe.jhdb.mysql.MySQLPlugin;

public class Main
{
    public static void main(String[] args)
    {
        DB.onCantConnect(() -> {
            Console.red("Cant Connect to Database");
            System.exit(0);
        });

        MySQLPlugin mySQLPlugin = new MySQLPlugin();

        DB.use(mySQLPlugin);

//        Schema.create("users", new Blueprint()
//        {
//            @Override
//            public void build(Table table)
//            {
//                table.increments("id");
//                table.string("name");
//            }
//        });

        DB.execute("DROP TABLE users");

        DB.execute(
                "CREATE TABLE users (id INT UNSIGNED NOT NULL AUTO_INCREMENT, name VARCHAR(50) NOT NULL, PRIMARY KEY (id))");

        DB.select("SELECT * FROM users");

        int result = DB.table("users").insertInto("name").values("JimmyHowe");

        System.out.println(result);
    }
}
