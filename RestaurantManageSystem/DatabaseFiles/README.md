## **EXPLANATION OF FILES IN THIS DIRECTORY**

1) schema.sql - This file has to be run only once on creation, after the cleanup of the entire database to minimize errors.
2) reset.sql - This file simply cleans the database, **run before `schema.sql` if any changes are made to the schema.**.
3) cluster.sql - Reclusters the clustered tables, namely `order` and `order_item`.
4) seed.sql - Seeds the data in tables, run after **`schema.sql`**.

Other important details such as driver, username, etc are shared over private channels and should be implemented in env files if possible.