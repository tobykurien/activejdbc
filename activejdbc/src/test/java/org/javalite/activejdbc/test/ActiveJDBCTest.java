/*
Copyright 2009-2010 Igor Polevoy 

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. 
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 
*/


package org.javalite.activejdbc.test;

import static org.javalite.activejdbc.test.JdbcProperties.*;
import static org.javalite.common.Collections.list;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.javalite.activejdbc.*;
import org.javalite.activejdbc.cache.QueryCache;
import org.javalite.activejdbc.test_models.*;
import org.javalite.activejdbc.test_models.Reader;
import org.javalite.test.jspec.JSpecSupport;
import org.junit.*;


public abstract class ActiveJDBCTest extends JSpecSupport {
  protected ModelContext<User> User= Model.with(User.class);
  protected ModelContext<Person> Person= Model.with(Person.class);
  protected ModelContext<Apple> Apple= Model.with(Apple.class);
  protected ModelContext<Vehicle> Vehicle= Model.with(Vehicle.class);
  protected ModelContext<Classification> Classification= Model.with(Classification.class);
  protected ModelContext<Mammal> Mammal= Model.with(Mammal.class);
  protected ModelContext<Address> Address= Model.with(Address.class);
  protected ModelContext<Room> Room= Model.with(Room.class);
  protected ModelContext<Doctor> Doctor= Model.with(Doctor.class);
  protected ModelContext<Patient> Patient= Model.with(Patient.class);
  protected ModelContext<Prescription> Prescription= Model.with(Prescription.class);
  protected ModelContext<DoctorsPatients> DoctorsPatients= Model.with(DoctorsPatients.class);
  protected ModelContext<Comment> Comment= Model.with(Comment.class);
  protected ModelContext<Article> Article= Model.with(Article.class);
  protected ModelContext<SubClassification> SubClassification= Model.with(SubClassification.class);
  protected ModelContext<Student> Student= Model.with(Student.class);
  protected ModelContext<Plant> Plant= Model.with(Plant.class);
  protected ModelContext<Programmer> Programmer= Model.with(Programmer.class);
  protected ModelContext<Project> Project= Model.with(Project.class);
  protected ModelContext<Assignment> Assignment= Model.with(Assignment.class);
  protected ModelContext<Watermelon> Watermelon= Model.with(Watermelon.class);
  protected ModelContext<Member> Member= Model.with(Member.class);
  protected ModelContext<Item> Item= Model.with(Item.class);
  protected ModelContext<Image> Image= Model.with(Image.class);
  protected ModelContext<Sword> Sword= Model.with(Sword.class);
  protected ModelContext<Cheese> Cheese= Model.with(Cheese.class);
  protected ModelContext<Cake> Cake= Model.with(Cake.class);
  protected ModelContext<Library> Library= Model.with(Library.class);
  protected ModelContext<NoArg> NoArg= Model.with(NoArg.class);
  protected ModelContext<Page> Page= Model.with(Page.class);
  protected ModelContext<Computer> Computer= Model.with(Computer.class);
  protected ModelContext<Motherboard> Motherboard= Model.with(Motherboard.class);
  protected ModelContext<Book> Book= Model.with(Book.class);
  protected ModelContext<Reader> Reader= Model.with(Reader.class);
  protected ModelContext<Ingredient> Ingredient= Model.with(Ingredient.class);
  protected ModelContext<Node> Node= Model.with(Node.class);
  protected ModelContext<ContentGroup> ContentGroup= Model.with(ContentGroup.class);
  protected ModelContext<Keyboard> Keyboard= Model.with(Keyboard.class);
  protected ModelContext<Recipe> Recipe= Model.with(Recipe.class);
  protected ModelContext<Post> Post= Model.with(Post.class);
  protected ModelContext<Course> Course= Model.with(Course.class);
  protected ModelContext<University> University= Model.with(University.class);
  protected ModelContext<Animal> Animal= Model.with(Animal.class);
  protected ModelContext<Account> Account= Model.with(Account.class);
  
    static boolean schemaGenerated = false;

    @Before
    public void before() throws Exception {
        Base.open(driver(), url(), user(), password());

        if(!schemaGenerated){
            generateSchema();
            schemaGenerated = true;
            System.out.println("DB: " + db() + ", Driver:" + driver());
        }

        Base.connection().setAutoCommit(false);

    }

    @After
    public void after() {

        try {
            Base.connection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Base.close();
    }

    protected void generateSchema() throws SQLException, ClassNotFoundException {
        if (db().equals("mysql")) {
            DefaultDBReset.resetSchema(getStatements(";", "mysql_schema.sql"));
        }else if (db().equals("postgresql")) {
            DefaultDBReset.resetSchema(getStatements(";", "postgres_schema.sql"));
        } else if (db().equals("h2")) {
            DefaultDBReset.resetSchema(getStatements(";", "h2_schema.sql"));
        } else if (db().equals("oracle")) {
            OracleDBReset.resetOracle(getStatements("-- BREAK", "oracle_schema.sql"));
        } else if (db().equals("mssql")) {
        	DefaultDBReset.resetSchema(getStatements("; ", "mssql_schema.sql"));
        }
    }

    /**
     * Returns array of strings where text was separated by semi-colons.
     *
     * @return array of strings where text was separated by semi-colons.
     */
    public String[] getStatements(String delimiter, String file) {
        try {

            System.out.println("Getting statements from file: " + file);
            InputStreamReader isr = new InputStreamReader(ActiveJDBCTest.class.getClassLoader().getResourceAsStream(file));
            BufferedReader reader = new BufferedReader(isr);
            StringBuffer text = new StringBuffer();
            String t;
            while ((t = reader.readLine()) != null) {
                text.append(t + '\n');
            }
            return text.toString().split(delimiter);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convenience method for testing.
     *
     * @param year
     * @param month - 1 through 12
     * @param day   - day of month
     * @return Timestamp instance
     */
    public Timestamp getTimestamp(int year, int month, int day) {
        return new Timestamp(getTime(year, month, day));
    }


    public java.sql.Date getDate(int year, int month, int day) {
        return new java.sql.Date(getTime(year, month, day));
    }

    /**
     * there is nothing more annoying than Java date/time APIs!
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public long getTime(int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DATE, day);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime().getTime();
    }


    protected void deleteAndPopulateTables(String... tables)  {
        for (String table : tables)
            deleteAndPopulateTable(table);
    }

    protected void deleteAndPopulateTable(String table) {
        deleteFromTable(table);
        populateTable(table);
    	QueryCache.instance().purgeTableCache(table);

    }

    
    protected void deleteFromTable(String table){
        executeStatements(list(getStatementProvider().getDeleteStatement(table)));
    }

    protected void populateTable(String table) {        
        executeStatements(getStatementProvider().getPopulateStatements(table));
    }

    private StatementProvider getStatementProvider(){
        StatementProvider statementProvider = null;
        if (db().equals("mysql")) {
            statementProvider = new MySQLStatementProvider();
        } else if (db().equals("oracle")) {
            statementProvider = new OracleStatementProvider();
        } else if (db().equals("postgresql")) {
            statementProvider = new PostgreSQLStatementProvider();
        } else if (db().equals("h2")) {
            statementProvider = new H2StatementProvider();
        } else if (db().equals("mssql")) {
            statementProvider = new MSSQLStatementProvider();
        } else {
        	throw new RuntimeException("Unknown db:" + db());
        }
        return statementProvider;
    }



    private void executeStatements(List<String> statements) {
    	String curStmt = "";
        try {
            for (String statement : statements) {
            	curStmt = statement;
                Statement st;
                st = Base.connection().createStatement();
                try{
                    st.executeUpdate(statement);
                }
                catch(Exception e){
                    throw e;
                }

                st.close();
            }            
        }
        catch (Exception e) {
            throw new RuntimeException(curStmt, e);
        }
    }


    PrintStream errOrig;
    PrintStream err;
    ByteArrayOutputStream bout;

    protected void replaceSystemError() {
        errOrig = System.err;
        bout = new ByteArrayOutputStream();
        err = new PrintStream(bout);
        System.setErr(err);
    }

    protected String getSystemError() throws IOException {
        err.flush();
        bout.flush();
        return bout.toString();
    }
}
