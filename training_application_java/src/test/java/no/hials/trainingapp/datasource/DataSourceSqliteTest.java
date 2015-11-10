/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.hials.trainingapp.datasource;

import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pitmairen
 */
public class DataSourceSqliteTest {

    private DataSourceSqlite ds;

    public DataSourceSqliteTest() {
    }

    @BeforeClass
    public static void setUpClass() throws ClassNotFoundException {
        DataSourceSqlite.initPool("jdbc:sqlite:/tmp/trainingdbjava.db");
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        ds = new DataSourceSqlite();
    }

    /**
     * Test of getCustomerByUsername method, of class DataSourceSqlite.
     */
    @Test
    public void testGetCustomerByUsername() throws Exception {

        DataItem r = ds.getCustomerByUsername("test");
        assertEquals("test", r.get("customer_email"));

    }

    /**
     * Test of getCustomerById method, of class DataSourceSqlite.
     */
    @Test
    public void testGetCustomerById() throws Exception {
        DataItem r = ds.getCustomerById(1);
        assertEquals("test", r.get("customer_email"));
    }

    /**
     * Test of getAllCustomers method, of class DataSourceSqlite.
     */
    @Test
    public void testGetAllCustomers() throws Exception {

        List<DataItem> r = ds.getAllCustomers(5);
        assertEquals(5, r.size());

    }

    /**
     * Test of getNextWorkoutsForCustomer method, of class DataSourceSqlite.
     */
    @Test
    public void testGetNextWorkoutsForCustomer() throws Exception {

        List<DataItem> result = ds.getNextWorkoutsForCustomer(1, 10);

    }

    /**
     * Test of getWorkoutLogForCustomer method, of class DataSourceSqlite.
     */
    @Test
    public void testGetWorkoutLogForCustomer_int_int() throws Exception {

        List<DataItem> result = ds.getWorkoutLogForCustomer(1, 10);

    }

    /**
     * Test of getWorkoutLogForCustomer method, of class DataSourceSqlite.
     */
    @Test
    public void testGetWorkoutLogForCustomer_int_Pagination() throws Exception {

        Pagination pag = new Pagination(10, 1);
        List<DataItem> result = ds.getWorkoutLogForCustomer(1, pag);

    }

    /**
     * Test of getWorkout method, of class DataSourceSqlite.
     */
    @Test
    public void testGetWorkout() throws Exception {

        DataItem r = ds.getWorkout(1);
        assertEquals(1, r.get("workout_id"));

    }

    /**
     * Test of getExerciseById method, of class DataSourceSqlite.
     */
    @Test
    public void testGetExerciseById() throws Exception {

        DataItem r = ds.getExerciseById(1);
        assertEquals(1, r.get("exercise_id"));

    }

    /**
     * Test of getProgressForExercise method, of class DataSourceSqlite.
     */
    @Test
    public void testGetProgressForExercise() throws Exception {

        List<DataItem> result = ds.getProgressForExercise(1, 1);

    }

    /**
     * Test of getAllExercises method, of class DataSourceSqlite.
     */
    @Test
    public void testGetAllExercises() throws Exception {

        List<DataItem> r = ds.getAllExercises();
        assertTrue(r.size() > 0);

    }

    /**
     * Test of getProgramById method, of class DataSourceSqlite.
     */
    @Test
    public void testGetProgramById() throws Exception {

        DataItem r = ds.getProgramById(1);
        assertEquals(1, r.get("program_id"));

    }

    /**
     * Test of getSets method, of class DataSourceSqlite.
     */
    @Test
    public void testGetSets() throws Exception {

        List<DataItem> result = ds.getSets(1);

    }

}
