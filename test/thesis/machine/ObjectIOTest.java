/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis.machine;

import thesis.core.*;
import com.db4o.ObjectSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import thesis.core.Cobject;
import thesis.core.Csoul;
import java.util.Vector;
import java.util.Date;

/**
 *
 * @author Daniel <dmilith> Dettlaff
 */
public class ObjectIOTest implements ServerConfiguration {

    private ObjectIO myobj;
    private Vector<Ccoordinates> coord = new Vector<Ccoordinates>(90);
    private Ccore core;
    private Cobject root, root2;
    private Csoul player;
    private Cbody body, body2;

    public ObjectIOTest() {
        myobj = new ObjectIO();
        myobj.createConnectionToRemoteDb( HOST, PORT );
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

        root = new Cobject(
            "00000000-0000-0000-0000-000000000000", // UUID
            "11111111-1111-1111-1111-111111111111" // parent UUID
            );

        core = new Ccore(); // core object will contain current world settings
        core.setParent( root.getUUID() );

        for (int i = 0; i < 90; i++) {
            coord.add( i, new Ccoordinates() );
            myobj.save( coord.elementAt(i) );
        }

        player = new Csoul();
        player.setMana( 10 );
        player.setParent( root.getUUID() );

        body = new Cbody();
        body.setSoul( player ); 

        body2 = new Cbody(); // we want two bodies! ;>
        body2.setSoul( player );
        body2.setParent( player.getUUID() );

//        System.out.println("saving root");
        myobj.save( root );

        root2 = new Cobject();
        root2.setUUID ( root.getUUID() );

//        System.out.println("saving root2");
        myobj.save( root2 );

      //  System.out.println("saving core");
        myobj.save( core );
    //    System.out.println("saving player");
        myobj.save( player );
  //      System.out.println("saving body2");
        myobj.save( body );
//        System.out.println("saving body2");
        myobj.save( body2 );

       // System.out.println( "saving newest Cobject");
        Cobject z = new Cobject();
        z.setParent( root.getUUID() );
        z.setUUID( "550e8400-e29b-41d4-a716-446655440000" );
        myobj.save( z );
        // FIXME TODO OPTIMIZE Make sure that this commit below is correct.
        myobj.getDb().commit();
    }

    @After
    public void tearDown() {
        //myobj.closeConnectionToDb();
    }

    /**
     * Test of loadByUUID method, of class ObjectIO.
     */
    @Test
    public void testAllUsingSODA() {
       ObjectSet sett;
       Date time_now = new Date();
       sett = myobj.loadAllByDateUsingSODA( new Cobject( false ) );
       Date time_after = new Date();
       /* while (sett.hasNext()) {
           Cobject mee = (Cobject)sett.next();
              System.out.println( "DEBUG:GOT-> RC" + mee.getClass() + ", TC" + mee.getObjectType() + ", #" +
                   mee.getUUID() + ", %"  + mee.getParent() + ", @" + mee.getCreatedAt() );
              assertNotNull( mee.getCreatedAt() );
              assertNotNull( mee.getObjectType() );
              assertNotNull( mee.getUUID() );
        }*/
      System.out.println( "Przed: " + time_now.getTime() );
      System.out.println( "Po: " + time_after.getTime() );
      System.out.println( "CZAS w ms: " + ( time_after.getTime() - time_now.getTime() ) );
      System.out.println( "\n\n\nObjects in database: " + myobj.objectCount() );
    }


    /**
     * Test of save method, of class ObjectIO.
     */
    @Test
    public void testSave() {
        assertEquals(player.getParent(), root.getUUID());
        assertEquals(body.getParent(), player.getUUID());
        assertEquals(body2.getParent(), player.getUUID());
        assertEquals(body.getSoul(), player);
        assertTrue( body.getParent().equals( player.getUUID() ) );
    }

    /**
     * Test of loadByUUID method, of class ObjectIO.
     */
    @Test
    public void testLoadByUUID() {
        System.out.println("load_by_uuid");
        ObjectSet sett;
        sett = myobj.loadByUUID( player.getUUID() );
        while (sett.hasNext()) {
           Cobject mee = (Cobject)sett.next();
              System.out.println( "DEBUG:GOT-> RC" + mee.getClass() + ", TC" + mee.getObjectType() + ", #" +
                   mee.getUUID() + ", %"  + mee.getParent() + ", @" + mee.getCreatedAt() );
              assertEquals( mee.getClass(), mee.getObjectType() );
              assertNotNull( mee.getCreatedAt() );
              assertNotNull( mee.getObjectType() );
              assertNotNull( mee.getUUID() );
        }
        assertTrue(sett.size() >= 1);
    }

    /**
     * Test of loadByParent method, of class ObjectIO.
     */
    @Test
    public void testLoadByParent() {
        System.out.println("load_by_parent");
        ObjectSet sett;
        sett = myobj.loadByParent( player.getUUID() );
        while (sett.hasNext()) {
           Cobject mee = (Cobject)sett.next();
              System.out.println( "DEBUG:GOT-> RC" + mee.getClass() + ", TC" + mee.getObjectType() + ", #" +
                   mee.getUUID() + ", %"  + mee.getParent() + ", @" + mee.getCreatedAt() );
              assertEquals( mee.getClass(), mee.getObjectType() );
              assertNotNull( mee.getCreatedAt() );
              assertNotNull( mee.getObjectType() );
              assertNotNull( mee.getUUID() );
              assertNotNull( mee.getParent() );
        }
    }

    /**
     * Test of loadAll method, of class ObjectIO.
     */
    @Test
    public void testLoadByType() {
        System.out.println("load_by_type");
        ObjectSet sett = myobj.loadByType( root );
        assertTrue( sett.size() > 0 );
        while (sett.hasNext()) {
           Cobject mee = (Cobject)sett.next();
              System.out.println( "DEBUG:GOT-> RC" + mee.getClass() + ", TC" + mee.getObjectType() + ", #" +
                   mee.getUUID() + ", %"  + mee.getParent() + ", @" + mee.getCreatedAt() );
              assertEquals( mee.getClass(), mee.getObjectType() );
              assertNotNull( mee.getCreatedAt() );
              assertNotNull( mee.getObjectType() );
              assertNotNull( mee.getUUID() );
        }
    }

    /**
     * Test of load method, of class ObjectIO.
     */
    @Test
    public void testLoadAllRoots() {
        System.out.println("load");
          System.out.println( "\nDEBUG: Getting all objects:" );
            ObjectSet sett;
            sett = myobj.loadByType( root );
            while (sett.hasNext()) {
              Cobject mee = (Cobject)sett.next();
              assertNotNull( mee.getClass() );
              System.out.println( "DEBUG:GOT-> RC-" + mee.getClass() + ",TC-" + mee.getObjectType() + ", #-" +
                    mee.getUUID() + ", %-"  + mee.getParent() + ", @-" + mee.getCreatedAt() );
              assertNotNull( mee.getCreatedAt() );
              assertNotNull( mee.getObjectType() );
              assertNotNull( mee.getUUID() );
            }
    }

    /**
     * Test of load method, of class ObjectIO.
     */
    @Test
    public void testLoadAllPlayers() {
            System.out.println( "Load all players" );
            ObjectSet sett2;
            sett2 = myobj.loadByType( player );
            while (sett2.hasNext()) {
              Cobject mee = (Cobject)sett2.next();
              System.out.println( "DEBUG:GOT-> RC-" + mee.getClass() + ",TC-" + mee.getObjectType() + ", #-" +
                    mee.getUUID() + ", %-"  + mee.getParent() + ", @-" + mee.getCreatedAt() );
              assertNotNull( mee.getCreatedAt() );
              assertNotNull( mee.getObjectType() );
              assertNotNull( mee.getUUID() );
            }
    }

    /**
     * Test of objectCount method, of class ObjectIO.
     */
    @Test
    public void testFindNewestVersionByType() {
        System.out.println( "\n\n\n\n**************************************************\n" +
                             myobj.loadOneByTypeWithNewestVersion( new Cobject( false )).getCreatedAt() );
      //  assertEquals( UUID.fromString( "550e8400-e29b-41d4-a716-446655440000" ),
        assertNotNull( myobj.loadOneByTypeWithNewestVersion( new Cobject( false )).getUUID() );
    }

    /**
     * Test of objectCount method, of class ObjectIO.
     */
    @Test
    public void testFindNewestVersionByUUID() {
        System.out.println( "\n\n\n\n**************************************************\n" +
                             myobj.loadOneByUUIDWithNewestVersion( root ).getCreatedAt() );
        ObjectSet z = myobj.loadByUUID( root.getUUID() );
        assertTrue( z.size() > 1 );
        while ( z.hasNext() ) {
            Cobject zz = ((Cobject)z.next());
            System.out.println( zz.getCreatedAt() );
            assertNotNull( zz );
        }
    }

    /**
     * Test of objectCount method, of class ObjectIO.
     */
    @Test
    public void testObjectCount() {
        assertTrue( myobj.objectCount() > 3 );
        System.out.println( "\n\n\nObjects in database: " + myobj.objectCount() );
    }

}