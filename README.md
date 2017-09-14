# JHDB
Java JDBC Wrapper inspired by Laravel

Includes

- MySQL Connector 5.1.38
- SQLite 3.14.2.1

## Usage

# Registering Plugins

SO far there are only two plugins MySQL and SQLite. To register the plugins simple use the `DB.use(Plugin)` method.

    DB.use(new MySQLPlugin())
    DB.use(new SQLitePlugin())
    DB.use(new SQLitePlugin().inMemory())
    
This sets the default plugin to be used when creating connections to the databases.

To register multiple plugins use the `DB.register(String, Plugin)` method.

    DB.register("mysql", new MySQLPlugin())
    DB.register("sqlite", new SQLitePlugin())
    DB.register("sqlite.memory", new SQLitePlugin().inMemory()) 

These can then be accesses via the `DB.connection("first")` method.
Most of the methods can be used via the `DB` or `connection` methods.
For example:

    DB.select("SELECT * FROM users")
    DB.connection("first").select("SELECT * FROM users")
    
With the first method using the default connection.

## Creating Tables

The `Schema` object is used to manipulate and create tables.

    Schema.create("users", new Blueprint()
    {
        @Override
        public void build(Table table)
        {
            table.increments("id");
            table.string("name");
            table.string("email", 255);
            table.bool("activated").nullable();
            table.softDeletes();
            table.timestamps();
        }
    });
    
With Java 8 this can be shortened to

    Schema.create("users", table -> {
            table.increments("id");
            table.string("name");
            table.string("email", 255);
            table.bool("activated").nullable();
            table.softDeletes();
            table.timestamps();
        });
    
The schema builder can be used on specific connections like so

    Schema.connection("sqlite").create(...)

### Direct SQL

To run direct SQL queries on a connection simple use the helper methods.

Selects will return ResultSets

    DB.select("...")
    DB.connection("sqlite").select("...")
    
Inserts will return boolean values to let you know if it worked
    
    DB.insert("...")
    DB.connection("sqlite").insert("...")

Update and delete methods return the number of rows affected.
    
    DB.update("...")
    DB.connection("sqlite").update("...")
    DB.delete("...")
    DB.connection("sqlite").delete("...")

### Query Builder

The query builder has many methods to simplify sql queries. and can be accessed on specific connections. 

    DB.table("users")
    DB.table("users", "sqlite")
    DB.connection("sqlite").table("users")
    
#### Selects

    DB.table("users").get()
    DB.table("users").select("id", "name").get()
    
#### Inserts

    DB.table("users").insertInto("name", "age").values("Jimmy", 33)
    
    DB.table("users").insert("name", "Jimmy", "age", 33);
    
#### Updates

    DB.table("users").where("id", 1).set("name", "Brian").set("age", 32).update();

    DB.table("users").where("id", 1).update("name", "Brian");
    
    DB.table("users").where("id", 1).update("name", "Brian", "age", 32);
    
#### Deletes

    DB.table("users").where("id", 1).delete();

#### Wheres

    DB.table("users").where("id", 1).get()
    DB.table("users").where("id", ">", 1).get()
    DB.table("users").where("id", 1).where("name", "Jimmy").get()

#### Order By
    
    DB.table("users").orderBy("name").get()
    DB.table("users").orderByDesc("name").get()

## Migrator

The Migrator can be used for migrating tables up and down. This feature is pretty simple at the moment.

    Migrator.add(new Migration()
    {
        @Override
        public void up()
        {
            Schema.create("users", new Blueprint()
            {
                @Override
                public void build(Table table)
                {
                    table.increments("id");
                    table.string("name");
                    table.integer("age").unsigned();
                }
            });
        }

        @Override
        public void down()
        {
            Schema.drop("users");
        }
    });
    
    