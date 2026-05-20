## InvenTrack POS

InvenTrack POS is an inventory management system with a point-of-sale (POS) workflow. It supports:

- User registration and login (Admin, Cashier, Stock Manager)
- Inventory management (products, categories, suppliers)
- POS terminal cart + checkout
- Order/transaction history

### Project Structure (High Level)

- `src/main/java/com/inventrack/config` : DB configuration
- `src/main/java/com/inventrack/dao` : JDBC DAOs (SQL access)
- `src/main/java/com/inventrack/service` : business logic layer
- `src/main/java/com/inventrack/controller` : servlets (routes/controllers)
- `src/main/webapp/*.jsp` : JSP views
- `src/main/resources/schema.sql` : database schema

### Database Setup

1. Start MySQL locally.
2. Run `src/main/resources/schema.sql` to create `inventrack_db` and tables.
3. Update credentials in `DBConnection.java` if your local DB user/password differ.

### Running

This is a Maven `war` project. Build with Maven:

- `./mvnw package`

Deploy the generated WAR from `target/` to a Jakarta Servlet container (Tomcat/Jetty) that supports Servlet 6.
