# JHDB
Java JDBC Wrapper inspired by Laravel

Includes

- MySQL Connector 5.1.38
- SQLite 3.14.2.1

## Usage

### Single Connection

    DB.use(new MySQLAdapter("database", "username", "password"))

## Creating Tables

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
    
## Migrator

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

## Queries

### Direct SQL

    DB.select("...") // Returns ResultSet
    DB.insert("...") // Returns number of rows affected
    DB.update("...") // Returns number of rows affected
    DB.delete("...") // Returns number of rows affected
    
### Query Builder

    DB.table("users")
    
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

### Multiple Connections

    DB.register("mysql", new MySQLAdapter("database", "username", "password"))
    DB.register("sqlite", new SQLiteAdapter("database.db"))
    DB.register("sqlite.memory", new SQLiteMemoryAdapter())
    
    Schema.connection("sqlite")
    
    DB.connection("sqlite")
    DB.table("users", "sqlite")
