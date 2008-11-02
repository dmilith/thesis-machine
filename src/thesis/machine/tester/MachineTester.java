/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis.machine.tester;

import com.db4o.ObjectSet;
import thesis.core.*;
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

          Cobject root = new Cobject(
              "00000000-0000-0000-0000-000000000000", // UUID
              "11111111-1111-1111-1111-111111111111" // parent UUID
              );

          Cobject me2 = new Cobject( UUID.randomUUID(), root.getUUID() );
          Cobject me3 = new Cobject( UUID.randomUUID(), me2.getUUID() );

          Csoul player = new Csoul();
          player.setMana( 10 );
          player.setParent("00f00000-0000-0000-0000-000000000000");

          if (debug) System.out.println( "DEBUG:" + root.getUUID() );
          if (debug) System.out.println( "DEBUG:" + me2.getUUID() );
          if (debug) System.out.println( "DEBUG:" + me3.getUUID() );

          ObjectSet sett;
          sett = db.load_by_uuid( root.getUUID() );
          if (sett.hasNext()) {
              Cobject mee = (Cobject)sett.next();
              //store or update an object
              db.save( mee ); // query by example by default
          } else { //else save root object
              db.save( root );
          }
          db.save( me2 );
          if (debug) System.out.println( "\nDEBUG: OBJECT TYPE => " + me3.getObjectType() );
          db.save( me3 );
          player.setObjectType( Csoul.class );
          if (debug) System.out.println( "\nDEBUG: OBJECT TYPE => " + player.getObjectType() );
          db.save( player );

          if (debug) System.out.println( "\nDEBUG: Taking Cobject by UUID: ");

          //sett = db.load_by_parent( root.getUUID() );
          sett = db.load_by_parent( UUID.fromString("00f00000-0000-0000-0000-000000000000") );

          System.out.println( sett.size() );
          while (sett.hasNext()) {
              Cobject nextOne = ((Cobject)sett.next());
              System.out.print( nextOne.getUUID() + " < ");
              System.out.print( nextOne.getParent() + " @ ");
              System.out.println( nextOne.getCreatedAt().toString() );
              if ( nextOne.getObjectType() == Csoul.class ) {
                    if (debug) System.out.println( "DEBUG: SOUL! Yeah! => " + nextOne.getUUID() );
              }
          }

          if (debug) System.out.println( "\nDEBUG: Attempt to take all Cobjects from base: ");
          sett = db.load( new Cobject( false ) ); // get ALL Cobjects
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
