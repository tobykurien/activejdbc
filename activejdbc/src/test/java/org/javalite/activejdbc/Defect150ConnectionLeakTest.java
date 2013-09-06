package org.javalite.activejdbc;

import org.javalite.test.jspec.ExceptionExpectation;
import org.javalite.test.jspec.JSpecSupport;
import org.junit.*;

/**
 * @author Igor Polevoy
 */
@Ignore("MySql")
public class Defect150ConnectionLeakTest extends JSpecSupport {

    @After
    public void after(){
        DB.closeAllConnections();
    }


    @Test(expected = DBException.class)
    public void shouldThrowExceptionIfConnectionOpenedWithoutClosingPrevious() {

        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/activejdbc", "root", "p@ssw0rd");
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/activejdbc", "root", "p@ssw0rd");

    }
}
