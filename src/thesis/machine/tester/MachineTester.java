/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis.machine.tester;

import com.db4o.ObjectSet;
import thesis.core.Cobject;
import thesis.machine.ObjectIO;
import java.util.Date;
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
              "00000000-0000-0000-0000-000000000000",
              "11111111-1111-1111-1111-111111111111",
              new Date()
              );

          Cobject me2 = new Cobject();
          me2.setCreatedAt( new Date() );
          me2.setParent( me.getUUID() );
          me2.setUUID( UUID.randomUUID() );

          if (debug) System.out.println( "DEBUG:" + me.getUUID() );
          if (debug) System.out.println( "DEBUG:" + me2.getUUID() );

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
