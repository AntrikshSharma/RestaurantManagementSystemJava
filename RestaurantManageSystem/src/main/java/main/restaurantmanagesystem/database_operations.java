package main.restaurantmanagesystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

public class database_operations {
    String dbname;
    database_operations()
    {
        dbname = "";
    }
    Connection newConnection(boolean with_dbname)
    {
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/";
            if(with_dbname)
                url += dbname;
            c = DriverManager
                    .getConnection(url, "postgres", "abc123");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return c;
    }
    boolean databaseExists()
    {
        boolean result = false;
        Connection c = newConnection(false);
        try {
            Statement s = c.createStatement();
            String query = "SELECT datname FROM pg_catalog.pg_database WHERE lower(datname) = lower('"+ dbname + "');";
            ResultSet r = s.executeQuery(query);
            if(r.next())
            {
                result = true;
            }
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return result;
    }
    void create_database(String name)
    {
        Connection c = newConnection(false);
        dbname = name;
        try {
            Statement s = c.createStatement();
            if(databaseExists())
            {
                System.out.println("Database already exists.");
            }
            else {
                s.executeUpdate("CREATE DATABASE " + dbname + ";");
                System.out.println("Database Created.");
            }
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    void delete_database()
    {
        Connection c = newConnection(false);
        try {
            Statement s = c.createStatement();
            if(databaseExists())
            {
                s.executeUpdate("DROP DATABASE " + dbname + ";");
                System.out.println("Database deleted.");
                dbname = "";
            }
            else {
                System.out.println("Database does not exist.");
            }
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    void create_tables()
    {
        Connection c = newConnection(true);
        try {
            Statement s = c.createStatement();
            String[] q = new String[12];
            if(!databaseExists())
            {
                System.out.println("Database does not exist.");
                return;
            }
            q[0] = "CREATE TABLE IF NOT EXISTS employee\n" +
                    "(\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    name VARCHAR NOT NULL,\n" +
                    "    mobile VARCHAR NOT NULL,\n" +
                    "    email VARCHAR NOT NULL,\n" +
                    "    address VARCHAR NOT NULL,\n" +
                    "    joining_date DATE DEFAULT CURRENT_DATE NOT NULL\n" +
                    ");";

            q[1] = "CREATE TABLE IF NOT EXISTS waiter\n" +
                    "(\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    salary INT NOT NULL,\n" +
                    "    employee_id INT NOT NULL REFERENCES employee(id) ON DELETE CASCADE\n" +
                    ");";

            q[2]  = "CREATE TABLE IF NOT EXISTS admin\n" +
                    "(\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    employee_id INT NOT NULL REFERENCES employee(id) ON DELETE CASCADE\n" +
                    ");";

            q[3]  = "CREATE TABLE IF NOT EXISTS customer\n" +
                    "(\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    name VARCHAR NOT NULL,\n" +
                    "    mobile VARCHAR NOT NULL,\n" +
                    "    email VARCHAR NOT NULL,\n" +
                    "    address VARCHAR NOT NULL,\n" +
                    "    joining_date DATE DEFAULT CURRENT_DATE NOT NULL\n" +
                    ");";

            q[4]  = "CREATE TYPE ITEM_TYPES AS ENUM ('beverage', 'food');";
            q[5] = "CREATE TABLE IF NOT EXISTS item\n" +
                    "(\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    name VARCHAR NOT NULL,\n" +
                    "    price INT NOT NULL,\n" +
                    "    type ITEM_TYPES NOT NULL\n" +
                    ");";

            q[6]  = "CREATE TYPE STATUS_TYPES AS ENUM ('received', 'ongoing', 'complete');";
            q[7]  = "CREATE TABLE IF NOT EXISTS \"order\"\n" +
                    "(\n" +
                    "    id SERIAL PRIMARY KEY,\n" +
                    "    total INT NOT NULL,\n" +
                    "    status STATUS_TYPES NOT NULL,\n" +
                    "    waiter_id INT NOT NULL REFERENCES waiter(id) ON DELETE SET NULL,\n" +
                    "    customer_id INT NOT NULL REFERENCES customer(id) ON DELETE SET NULL\n" +
                    ");";

            q[8]  = "CREATE TABLE IF NOT EXISTS order_item\n" +
                    "(\n" +
                    "    order_id INT NOT NULL REFERENCES \"order\"(id) ON DELETE SET NULL,\n" +
                    "    item_id INT NOT NULL REFERENCES item(id) ON DELETE SET NULL,\n" +
                    "    quantity INT NOT NULL,\n" +
                    "    PRIMARY KEY (order_id, item_id)\n" +
                    ");";

            q[9]  = "CREATE INDEX order_status_idx on \"order\"(status);";

            q[10]  = "CLUSTER \"order\" USING order_status_idx;";

            q[11]  = "CLUSTER order_item USING order_item_pkey;";
            for(int i= 0; i < q.length; i++) {
                s.addBatch(q[i]);
            }
            s.executeBatch();
            System.out.println("Tables Created.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }
    void delete_tables(){
        Connection c = newConnection(true);
        try {
            Statement s = c.createStatement();
            String[] q = new String[9];
            if(!databaseExists())
            {
                System.out.println("Database does not exist.");
                return;
            }
            q[0] = "DROP TABLE employee CASCADE;";

            q[1] = "DROP TABLE waiter CASCADE;";

            q[2]  = "DROP TABLE admin CASCADE;";

            q[3]  = "DROP TABLE customer CASCADE;";

            q[4]  = "DROP TYPE item_types CASCADE;";

            q[5] = "DROP TYPE status_types CASCADE;";

            q[6]  = "DROP TABLE item CASCADE;";

            q[7]  = "DROP TABLE \"order\" CASCADE;";

            q[8]  = "DROP TABLE order_item CASCADE;";

            for(int i= 0; i < q.length; i++) {
                s.addBatch(q[i]);
            }
            s.executeBatch();
            System.out.println("Tables Deleted.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void cluster(){
        Connection c = newConnection(true);
        try {
            Statement s = c.createStatement();
            String[] q = new String[2];
            if(!databaseExists())
            {
                System.out.println("Database does not exist.");
                return;
            }
            q[0] = "CLUSTER \"order\";";
            q[1] = "CLUSTER order_item;";
            for(int i= 0; i < q.length; i++) {
                s.addBatch(q[i]);
            }
            s.executeBatch();
            System.out.println("Clustering done.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        database_operations db = new database_operations();
        db.create_database("db");
        db.create_tables();
        db.cluster();
        db.delete_tables();
        db.delete_database();
    }
}
