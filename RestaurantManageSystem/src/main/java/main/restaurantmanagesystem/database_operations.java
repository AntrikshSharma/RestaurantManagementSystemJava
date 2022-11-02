/*
Database Operations
=> newConnection(boolean with_dbname) - Returns connection to database. If with_dbname is true, returns connection to database.
=> databaseExists() - Checks if database exists. Returns true/false.
=> create_database(String name) - Creates database with name in argument.
=> create_tables() - Creates all the required tables.
=> delete_tables() - Deletes all the tables.
=> cluster_tables() - Clusters tables.

Insert Functions
=> ** All return 1 if value is inserted, 0 if value already present. **
=> insert_employee(int id, String name, String mobile, String email, String address, String date)
=> insert_waiter(int id, int salary, int emp_id)
=> insert_admin(int id, int emp_id)
=> insert_customer(int id, String name, String mobile, String email,String address, String joining_date)
=> insert_item(int id, String name, int price, String type)
=> insert_order(int id, int total, String status, int writer_id, int customer_id)
=> insert_orderitem(int order_id, int item_id, int quantity)

Remove Functions
=> ** All return 1 if value is deleted, 0 if value not present. **
=> remove_employee(int id) & remove_employee(String name)
=> remove_waiter(int id)
=> remove_admin(int id)
=> remove_customer(int id) & remove_customer(String name)
=> remove_item(int id) & remove_item(String name)
=> remove_order(int id)
=> remove_orderitem(int order_id)
=> remove_orderitem(int order_id, int item_id)

Select Functions
=>
=> public class employee { int id, String name, String mobile, String email, String address, String date }
=> employee[] getEmployee() = returns array of all employees of employee class object.
=> employee getEmployee(int id) = returns object of employee with 'id'.
=>
=> public class waiter { int id, int salary, int emp_id }
=> waiter[] getWaiter() = returns array of all waiters of waiter class object.
=> waiter getWaiter(int id) = returns object of waiter with 'id'.
=>
=> public class admin { int id, int emp_id }
=> admin[] getAdmin() = returns array of all admins of admin class object.
=> admin getAdmin(int id) = returns object of admin with 'id'.
=>
=> public class customer { int id, String name, String mobile, String email,String address, String joining_date }
=> customer[] getCustomer() = returns array of all customers of customer class object.
=> customer getCustomer(int id) = returns object of customer with 'id'.
=>
=> public class item { int id, String name, int price, String type }
=> item[] getItem() = returns array of all items of item class object.
=> item getItem(int id) = returns object of item with 'id'.
=>
=> public class order { int id, int total, String status, int writer_id, int customer_id }
=> order[] getOrder() = returns array of all orders of order class object.
=> order getOrder(int id) = returns object of order with 'id'.
=>
=> public class orderitem { int order_id, int item_id, int quantity }
=> orderitem[] getOrderItem() = returns array of all orderitems of orderitem class object.
=> orderitem[] getOrderItem(int order_id) = returns array of all orderitems of orderitem class object of 'order_id'.
=> orderitem getOrderItem(int order_id, item_id) = returns object of order with 'order_id' and 'item_id'.


 */


package main.restaurantmanagesystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

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

    int insert_employee(int id, String name, String mobile, String email, String address, String date)
    {
        Date d = Date.valueOf(date);
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from employee where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next()){
                c.close();
                return 0;
            }
                
            String query = "INSERT INTO EMPLOYEE VALUES (?, ?, ?, ?, ?, ?);";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setString(2, name);
            prep.setString(3, mobile);
            prep.setString(4, email);
            prep.setString(5, address);
            prep.setDate(6, d);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int insert_waiter(int id, int salary, int emp_id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from waiter where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next()){
                c.close();
                return 0;
            }
            String query = "INSERT INTO WAITER VALUES (?, ?, ?);";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setInt(2, salary);
            prep.setInt(3, emp_id);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int insert_admin(int id, int emp_id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from admin where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next()){
                c.close();
                return 0;
            }
            String query = "INSERT INTO ADMIN VALUES (?, ?);";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setInt(2, emp_id);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int insert_customer(int id, String name, String mobile, String email,String address, String joining_date)
    {
        Date d = Date.valueOf(joining_date);
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from customer where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next()){
                c.close();
                return 0;
            }
            String query = "INSERT INTO CUSTOMER VALUES (?, ?, ?, ?, ?, ?);";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setString(2, name);
            prep.setString(3, mobile);
            prep.setString(4, email);
            prep.setString(5, address);
            prep.setDate(6, d);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int insert_item(int id, String name, int price, String type)
    {
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from item where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next()){
                c.close();
                return 0;
            }
            String query = "INSERT INTO ITEM VALUES (?, ?, ?, ?);";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setString(2, name);
            prep.setInt(3, price);
            prep.setString(3, type);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int insert_order(int id, int total, String status, int writer_id, int customer_id)
    {
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next()){
                c.close();
                return 0;
            }
            String query = "INSERT INTO ORDER VALUES (?, ?, ?, ?, ?);";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.setInt(2, total);
            prep.setString(3, status);
            prep.setInt(4, writer_id);
            prep.setInt(5, customer_id);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int insert_orderitem(int order_id, int item_id, int quantity)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order where order_id=?;");
            prep.setInt(1, order_id);
            ResultSet rs= prep.executeQuery();
            if(rs.next()){
                c.close();
                return 0;
            }
            String query = "INSERT INTO ORDER_ITEM VALUES (?, ?, ?);";
            prep = c.prepareStatement(query);
            prep.setInt(1, order_id);
            prep.setInt(2, item_id);
            prep.setInt(3, quantity);
            prep.executeUpdate();
            System.out.println("Value Inserted.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_employee(int id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from employee where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM EMPLOYEE WHERE ID = ?;";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_employee(String name)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from employee where name=?;");
            prep.setString(1, name);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM EMPLOYEE WHERE NAME = ?;";
            prep = c.prepareStatement(query);
            prep.setString(1, name);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_waiter(int id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from waiter where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM WAITER WHERE ID = ?;";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_admin(int id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from admin where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM ADMIN WHERE ID = ?;";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_customer(int id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from customer where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM CUSTOMER WHERE ID = ?;";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_customer(String name)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from customer where name=?;");
            prep.setString(1, name);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM CUSTOMER WHERE NAME = ?;";
            prep = c.prepareStatement(query);
            prep.setString(1, name);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_item(int id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from item where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM ITEM WHERE ID = ?;";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_item(String name)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from item where name=?;");
            prep.setString(1, name);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM ITEM WHERE NAME = ?;";
            prep = c.prepareStatement(query);
            prep.setString(1, name);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_order(int id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order where id=?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM ORDER WHERE ID = ?;";
            prep = c.prepareStatement(query);
            prep.setInt(1, id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }
    int remove_orderitem(int order_id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order_item where order_id=?;");
            prep.setInt(1, order_id);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM ORDER_ITEM WHERE order_id = ? and item_id = ?;";
            prep = c.prepareStatement(query);
            prep.setInt(1, order_id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }

    int remove_orderitem(int order_id, int item_id)
    {

        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order_item where order_id=? and item_id = ?");
            prep.setInt(1, order_id);
            prep.setInt(2, item_id);
            ResultSet rs= prep.executeQuery();
            if(!rs.next()){
                c.close();
                return 0;
            }
            String query = "DELETE FROM ORDER_ITEM WHERE order_id = ? and item_id = ?;";
            prep = c.prepareStatement(query);
            prep.setInt(1, order_id);
            prep.setInt(2, item_id);
            prep.executeUpdate();
            System.out.println("Value REMOVED.");
            c.close();
            return 1;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return 0;
    }
    public class employee
    {
        int id;
        String name, mobile, email, address, date;
        employee()
        {
            id = -1;
            name = "";
            mobile = "";
            email = "";
            address = "";
            date = "";
        }
    }
    employee[] getEmployee()
    {
        employee[] arr = new employee[]{new employee()};
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from employee;");
            ResultSet rs= prep.executeQuery();
            ResultSet temp = rs;
            int l = 0, i = 0;
            while(temp.next())
                l++;
            arr = new employee[l];
            while(rs.next())
            {
                employee a = new employee();
                a.id = rs.getInt(1);
                a.name = rs.getString(2);
                a.mobile = rs.getString(3);
                a.email = rs.getString(4);
                a.address = rs.getString(5);
                a.date = rs.getString(6);
                arr[i++] = a;
            }
            c.close();
            return arr;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return arr;
    }
    employee getEmployee(int id)
    {
        employee a = new employee();
        int i = 0;
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from employee where id = ?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next())
            {
                a.id = rs.getInt(1);
                a.name = rs.getString(2);
                a.mobile = rs.getString(3);
                a.email = rs.getString(4);
                a.address = rs.getString(5);
                a.date = rs.getString(6);
            }
            c.close();
            return a;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return a;
    }

    public class waiter
    {
        int id, salary, emp_id;
        waiter()
        {
            id = -1;
            salary = -1;
            emp_id = -1;
        }
    }
    waiter[] getWaiter()
    {
        waiter[] arr = new waiter[]{new waiter()};
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from waiter;");
            ResultSet rs= prep.executeQuery();
            ResultSet temp = rs;
            int l = 0, i = 0;
            while(temp.next())
                l++;
            arr = new waiter[l];
            while(rs.next())
            {
                waiter a = new waiter();
                a.id = rs.getInt(1);
                a.salary = rs.getInt(2);
                a.emp_id = rs.getInt(3);
                arr[i++] = a;
            }
            c.close();
            return arr;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return arr;
    }
    waiter getWaiter(int id)
    {
        waiter a = new waiter();
        int i = 0;
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from waiter where id = ?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next())
            {
                a.id = rs.getInt(1);
                a.salary = rs.getInt(2);
                a.emp_id = rs.getInt(3);
            }
            c.close();
            return a;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return a;
    }


    public class admin
    {
        int id, emp_id;
        admin()
        {
            id = -1;
            emp_id = -1;
        }
    }
    admin[] getAdmin()
    {
        admin[] arr = new admin[]{new admin()};
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from admin;");
            ResultSet rs= prep.executeQuery();
            ResultSet temp = rs;
            int l = 0, i = 0;
            while(temp.next())
                l++;
            arr = new admin[l];
            while(rs.next())
            {
                admin a = new admin();
                a.id = rs.getInt(1);
                a.emp_id = rs.getInt(2);
                arr[i++] = a;
            }
            c.close();
            return arr;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return arr;
    }
    admin getAdmin(int id)
    {
        admin a = new admin();
        int i = 0;
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from admin where id = ?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next())
            {

                a.id = rs.getInt(1);
                a.emp_id = rs.getInt(2);
            }
            c.close();
            return a;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return a;
    }

    public class customer
    {
        int id;
        String name, mobile, email, address, joining_date;
        customer()
        {
            id = -1;
            name = "";
            mobile = "";
            email = "";
            address = "";
            joining_date = "";
        }
    }
    customer[] getCustomer()
    {
        customer[] arr = new customer[]{new customer()};
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from customer;");
            ResultSet rs= prep.executeQuery();
            ResultSet temp = rs;
            int l = 0, i = 0;
            while(temp.next())
                l++;
            arr = new customer[l];
            while(rs.next())
            {
                customer a = new customer();
                a.id = rs.getInt(1);
                a.name = rs.getString(2);
                a.mobile = rs.getString(3);
                a.email = rs.getString(4);
                a.address = rs.getString(5);
                a.joining_date = rs.getString(6);
                arr[i++] = a;
            }
            c.close();
            return arr;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return arr;
    }
    customer getCustomer(int id)
    {
        customer a = new customer();
        int i = 0;
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from customer where id = ?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next())
            {
                a.id = rs.getInt(1);
                a.name = rs.getString(2);
                a.mobile = rs.getString(3);
                a.email = rs.getString(4);
                a.address = rs.getString(5);
                a.joining_date = rs.getString(6);
            }
            c.close();
            return a;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return a;
    }

    public class item
    {
        int id, price;
        String name, type;
        item()
        {
            id = -1;
            name = "";
            price = -1;
            type = "";
        }
    }
    item[] getItem()
    {
        item[] arr = new item[]{new item()};
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from item;");
            ResultSet rs= prep.executeQuery();
            ResultSet temp = rs;
            int l = 0, i = 0;
            while(temp.next())
                l++;
            arr = new item[l];
            while(rs.next())
            {
                item a = new item();
                a.id = rs.getInt(1);
                a.name = rs.getString(2);
                a.price = rs.getInt(3);
                a.type = rs.getString(4);
                arr[i++] = a;
            }
            c.close();
            return arr;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return arr;
    }
    item getItem(int id)
    {
        item a = new item();
        int i = 0;
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from item where id = ?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next())
            {
                a.id = rs.getInt(1);
                a.name = rs.getString(2);
                a.price = rs.getInt(3);
                a.type = rs.getString(4);
            }
            c.close();
            return a;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return a;
    }


    public class order
    {
        int id, total, writer_id, customer_id;
        String status;
        order()
        {
            id = -1;
            total = -1;
            writer_id = -1;
            customer_id = -1;
            status = "";
        }
    }
    order[] getOrder()
    {
        order[] arr = new order[]{new order()};
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order;");
            ResultSet rs= prep.executeQuery();
            ResultSet temp = rs;
            int l = 0, i = 0;
            while(temp.next())
                l++;
            arr = new order[l];
            while(rs.next())
            {
                order a = new order();
                a.id = rs.getInt(1);
                a.total = rs.getInt(2);
                a.status = rs.getString(3);
                a.writer_id = rs.getInt(4);
                a.customer_id = rs.getInt(5);
                arr[i++] = a;
            }
            c.close();
            return arr;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return arr;
    }
    order getOrder(int id)
    {
        order a = new order();
        int i = 0;
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order where id = ?;");
            prep.setInt(1, id);
            ResultSet rs= prep.executeQuery();
            if(rs.next())
            {
                a.id = rs.getInt(1);
                a.total = rs.getInt(2);
                a.status = rs.getString(3);
                a.writer_id = rs.getInt(4);
                a.customer_id = rs.getInt(5);
            }
            c.close();
            return a;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return a;
    }

    public class orderitem
    {
        int order_id, item_id, quantity;
        orderitem()
        {
            order_id = -1;
            item_id = -1;
            quantity = -1;
        }
    }
    orderitem[] getOrderItem()
    {
        orderitem[] arr = new orderitem[]{new orderitem()};
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order_item;");
            ResultSet rs= prep.executeQuery();
            ResultSet temp = rs;
            int l = 0, i = 0;
            while(temp.next())
                l++;
            arr = new orderitem[l];
            while(rs.next())
            {
                orderitem a = new orderitem();
                a.order_id = rs.getInt(1);
                a.item_id = rs.getInt(2);
                a.quantity = rs.getInt(3);
                arr[i++] = a;
            }
            c.close();
            return arr;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return arr;
    }
    orderitem[] getOrderItem(int order_id)
    {
        orderitem[] arr = new orderitem[]{new orderitem()};
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order_item where order_id = ?;");
            prep.setInt(1, order_id);
            ResultSet rs= prep.executeQuery();
            ResultSet temp = rs;
            int l = 0, i = 0;
            while(temp.next())
                l++;
            arr = new orderitem[l];
            while(rs.next())
            {
                orderitem a = new orderitem();
                a.order_id = rs.getInt(1);
                a.item_id = rs.getInt(2);
                a.quantity = rs.getInt(3);
                arr[i++] = a;
            }
            c.close();
            return arr;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return arr;
    }
    orderitem getOrderItem(int order_id, int item_id)
    {
        orderitem a = new orderitem();
        int i = 0;
        Connection c = newConnection(true);
        try {
            PreparedStatement prep = c.prepareStatement("select * from order_item where order_id = ? and item_id = ?;");
            prep.setInt(1, order_id);
            prep.setInt(1, item_id);
            ResultSet rs= prep.executeQuery();
            if(rs.next())
            {
                a.order_id = rs.getInt(1);
                a.item_id = rs.getInt(2);
                a.quantity = rs.getInt(3);
            }
            c.close();
            return a;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return a;
    }





    /*public static void main(String[] args) {
        int x;
        database_operations db = new database_operations();
        db.create_database("db");
        db.create_tables();
        db.cluster();
        x = db.insert_employee(1, "Anupam", "9842568104", "anupam@gmail.com",
                "Example Address, Test Road, Test City - 411038", "2022-09-16");
        System.out.println(x);
        x = db.insert_employee(1, "Anupam", "9842568104", "anupam@gmail.com",
                "Example Address, Test Road, Test City - 411038", "2022-09-16");
        System.out.println(x);
        employee a[] = db.getEmployee();
        employee b = db.getEmployee(1);
        System.out.println(a);
        System.out.println(b.id);
        System.out.println(b.name);
        db.remove_employee(1);
        db.delete_tables();
        db.delete_database();
    }*/
}
