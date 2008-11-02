/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis.machine.tester;

import com.db4o.ObjectSet;
import thesis.core.Cobject;
import thesis.machine.ObjectIO;
import java.util.UUID;
/**
 *
 * @author Daniel <dmilith> Dettlaff
 */
public class MachineTester extends Cobject {

    public static void main( String [] args ) {

        ObjectIO db = new ObjectIO();
        db.createConnectionToDb("my_test_db.db4o");

          Cobject me = new Cobject(
              "00000000-0000-0000-0000-000000000000", // UUID
              "11111111-1111-1111-1111-111111111111" // parent UUID
              );

          Cobject me2 = new Cobject( UUID.randomUUID(), me.getUUID() );
          Cobject me3 = new Cobject( UUID.randomUUID(), me2.getUUID() );

          if (debug) System.out.println( "DEBUG:" + me.getUUID() );
          if (debug) System.out.println( "DEBUG:" + me2.getUUID() );
          if (debug) System.out.println( "DEBUG:" + me3.getUUID() );

          ObjectSet sett;
          sett = db.load(new Cobject( me.getUUID() ));
          if (sett.hasNext()) {
              Cobject mee = (Cobject)sett.next();
              //store or update an object
              db.save( mee ); // query by example by default
          } else { //else save root object
              db.save( me );
          }
          db.save( me2 );
          db.save( me3 );

          if (debug) System.out.println( "\nDEBUG: Taking Cobject by UUID: ");
          sett = db.load( me2.getUUID() );
          System.out.println( sett.size() );
          while (sett.hasNext()) {
              Cobject nextOne = ((Cobject)sett.next());
              System.out.print( nextOne.getUUID() + " < ");
              System.out.print( nextOne.getParent() + " @ ");
              System.out.println( nextOne.getCreatedAt().toString() );
          }

          if (debug) System.out.println( "\nDEBUG: Attempt to take all Cobjects from base: ");
          sett = db.load( new Cobject( false ) );
          System.out.println( sett.size() );
          while (sett.hasNext()) {
              Cobject nextOne = ((Cobject)sett.next());
              System.out.print( nextOne.getUUID() + " < ");
              System.out.print( nextOne.getParent() + " @ ");
              System.out.println( nextOne.getCreatedAt().toString() );
          }
        db.closeConnectionToDb();

    }

}
