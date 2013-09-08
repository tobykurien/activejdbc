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

package org.javalite.activejdbc;

import org.javalite.activejdbc.cache.QueryCache;
import org.javalite.activejdbc.test.ActiveJDBCTest;
import org.javalite.activejdbc.test_models.Person;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Igor Polevoy
 */
//@Ignore("Eclipse: when running all test the cache is colliding. e.g this is colliding with Defect122Test and with some others"
//		+ "Soirce of problem is that Cahce is classloader global. It should be loval for db conncetion ???")
public class Defect123Test extends ActiveJDBCTest {

    @Test(expected = StaleModelException.class)
    public void test() {

        deleteAndPopulateTable("people");
        Person p = Person.findById(1);
        Person.delete("id = 1");
        p.refresh();
    }
    
    @After
    public void teardown(){
    	QueryCache.instance().purgeTableCache("people");
    }
}
