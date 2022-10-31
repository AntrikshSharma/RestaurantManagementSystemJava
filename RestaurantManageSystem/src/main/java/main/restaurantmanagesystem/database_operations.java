package main.restaurantmanagesystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

/*
Database Operations
=> newConnection(boolean with_dbname) - Returns connection to database. If with_dbname is true, returns connection to database.
=> databaseExists() - Checks if database exists. Returns true/false.
=> create_database(String name) - Creates database with name in argument.
=> create_tables() - Creates all the required tables.
=> delete_tables() - Deletes all the tables.
=> cluster_tables() - Clusters tables.

Insert Functions
=> insert_employee(int id, String name, String mobile, String email, String address, String date)
=> insert_waiter(int id, int salary, int emp_id)
=> insert_admin(int id, int emp_id)
=> insert_customer(int id, String name, String mobile, String email,String address, String date)
=> insert_item(int id, String name, int price, String type)
=> insert_order(int id, int total, String status, int writer_id, int customer_id)

Remove Functions
=> remove_employee(int id) & remove_employee(String name)
=> remove_waiter(int id)
=> remove_admin(int id)
=> remove_customer(int id) & remove_customer(String name)
=> remove_item(int id) & remove_item(String name)
=> remove_order(int id)
=> remove_orderitem(int order_id)

 */

public class database_operations {
    String dbname;
    String[] table_names;
    database_operations()
    {
        dbname = "";
        table_names = new String[]{"employee", "waiter", "admin", "customer", "\"order\"", "order_item"};
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

    void insert_employee(int id, String name, String mobile, String email, String address, String date)
    {

        Connection c = newConnection(true);
        try {
            String query = "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setString(2, name);
            prep.setString(3, mobile);
            prep.setString(4, email);
            prep.setString(5, address);
            prep.setString(6, date);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void insert_waiter(int id, int salary, int emp_id)
    {

        Connection c = newConnection(true);
        try {
            String query = "INSERT INTO WAITER VALUES (?, ?, ?);";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setInt(2, salary);
            prep.setInt(3, emp_id);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void insert_admin(int id, int emp_id)
    {

        Connection c = newConnection(true);
        try {
            String query = "INSERT INTO ADMIN VALUES (?, ?);";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setInt(2, emp_id);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void insert_customer(int id, String name, String mobile, String email,String address, String date)
    {

        Connection c = newConnection(true);
        try {
            String query = "INSERT INTO CUSTOMER VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setString(2, name);
            prep.setString(3, mobile);
            prep.setString(4, email);
            prep.setString(5, address);
            prep.setString(6, date);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void insert_item(int id, String name, int price, String type)
    {
        Connection c = newConnection(true);
        try {
            String query = "INSERT INTO ITEM VALUES (?, ?, ?, ?);";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setString(2, name);
            prep.setInt(3, price);
            prep.setString(3, type);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void insert_order(int id, int total, String status, int writer_id, int customer_id)
    {
        Connection c = newConnection(true);
        try {
            String query = "INSERT INTO ORDER VALUES (?, ?, ?, ?, ?);";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setInt(2, total);
            prep.setString(3, status);
            prep.setInt(4, writer_id);
            prep.setInt(5, customer_id);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void insert_orderitem(int order_id, int item_id, int quantity)
    {

        Connection c = newConnection(true);
        try {
            String query = "INSERT INTO ORDER_ITEM VALUES (?, ?, ?);";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, order_id);
            prep.setInt(2, item_id);
            prep.setInt(3, quantity);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_employee(int id)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM EMPLOYEE WHERE ID = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_employee(String name)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM EMPLOYEE WHERE NAME = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setString(1, name);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_waiter(int id)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM WAITER WHERE ID = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_admin(int id)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM ADMIN WHERE ID = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_customer(int id)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM CUSTOMER WHERE ID = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_customer(String name)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM CUSTOMER WHERE NAME = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setString(1, name);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_item(int id)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM ITEM WHERE ID = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_item(String name)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM ITEM WHERE NAME = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setString(1, name);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_order(int id)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM ORDER WHERE ID = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    void remove_orderitem(int order_id)
    {

        Connection c = newConnection(true);
        try {
            String query = "DELETE FROM ORDER_ITEM WHERE ID = ?;";
            PreparedStatement prep = c.prepareStatement(query);
            prep.setInt(1, order_id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
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
        db.insert_employee(1, "Anupam", "9842568104", "anupam@gmail.com",
                "Example Address, Test Road, Test City - 411038", "2022-09-16");
        db.delete_tables();
        db.delete_database();
    }
}
