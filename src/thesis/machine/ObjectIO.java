/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thesis.machine;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import thesis.core.Cobject;

/**
 *
 * @author Daniel <dmilith> Dettlaff
 */
public class ObjectIO extends Cobject {

    // main objects
    private ObjectContainer db;
    private boolean connected = false;


    public void createConnectionToDb( String filename ) {
        db = Db4o.openFile( filename );
        connected = true;
    }

    public void closeConnectionToDb() {
        db.close();
        connected = false;
    }

    public void save( Cobject myobj ) {
        if (!connected) return;
            db.set( myobj );
            if (debug) System.out.println( "DEBUG: Stored " + myobj );
    }

    /*
     * load() will ask db4o for objects with specified type
     */
    public ObjectSet load( Cobject match ) {
        if (!connected) return null;
            ObjectSet results = db.queryByExample( match );
            if (debug) System.out.println( "DEBUG: Loaded Object" );
            return results;
    }

    public ObjectSet load( Cobject match, int type ) {
        if (!connected) return null;
            ObjectSet results;
            switch (type) {
                case 0: {
                    // Query By Example
                    results = db.queryByExample( match );
                    break;
                }
                case 1: {
                    // not implemented yet
                }
                default:
                    results = null;
            }
            if (debug) System.out.println( "DEBUG: Loaded Object" );
            return results;
    }

}
