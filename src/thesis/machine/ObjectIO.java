package thesis.machine;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import java.util.UUID;

import thesis.core.*;

/**
 *
 * @author Daniel <dmilith> Dettlaff
 * @version 0.0.3
 */
public class ObjectIO extends Cobject implements ServerConfiguration {

    // directly access to ObjectContainer class from ObjectIO
    private ObjectContainer db;
    // is ObjectIO connected to database?
    private boolean connected = false;

   /**
     * returns reference to ObjectContainer which we operate on
     */
    public ObjectContainer getDb() {
        return db;
    }

   /**
     * creates connection to db4o
     */
    public void createConnectionToDb( String filename ) {
        try {
            db = Db4o.openFile( filename );
            connected = true;
        } catch (Exception e ) {
            System.out.println( e.getStackTrace() );
            connected = false;
        }
    }

   /**
     * creates connection to db4o
     */
    public void createConnectionToRemoteDb( String location, int port ) {
        try {
            // connect to the server
            db = Db4o.openClient( HOST, PORT, USER, PASS );
            connected = true;
        } catch (Exception e) {
            // e.printStackTrace();
             System.out.println( "\n* connection refused or database server is down.\n* will use local storage." );
             createConnectionToDb( LOCAL_DBFILE );
             connected = true;
        }
    }

   /**
     * closing connection to db4o
     */
    public void closeConnectionToDb() {
        if ( connected ) {
            connected = false;
            db.close();
        }
    }

   /**
     * save an Cobject or his children
     */
    public void save( Cobject myobj ) {
        if (!connected) return;
          db.store( myobj );
          if (debug) System.out.println( "DEBUG: Stored " + myobj );
    }

   /**
     * native load Cobject by uuid
     */
    public ObjectSet loadByUUID( final UUID uuid ) {
        if (!connected) return null;
          ObjectSet objects = db.query( new Predicate<Cobject>() {
            public boolean match(Cobject object) {
                return object.getUUID().equals( uuid );
            }
          });
          if (debug) System.out.println( objects );
          return objects;
    }

   /**
     * native load Cobject (or list of Cobjects) by uuid of parent
     */
    public ObjectSet loadByParent( final UUID parent ) {
        if (!connected) return null;
          ObjectSet objects = db.query( new Predicate<Cobject>() {
            public boolean match(Cobject object) {
                return object.getParent() == ( parent );
            }
          });
          return objects;
    }

    public Cobject loadOneByTypeWithNewestVersion( final Cobject parent ) {
        if (!connected) return null;
          ObjectSet objects = db.query( new Predicate<Cobject>() {
            public boolean match(Cobject object) {
                return object.getClass().equals( parent.getClass() );
            }
          });
          long value = 0;
          Cobject finalObject = null;
          while (objects.hasNext()) {
            Cobject newer = (Cobject)objects.next();
            if ( newer.getCreatedAt().getTime() > value ) {
                finalObject = newer;
                value = newer.getCreatedAt().getTime();
            }
          }
          return finalObject;
    }

    public Cobject loadOneByUUIDWithNewestVersion( final Cobject parent ) {
        if (!connected) return null;
          ObjectSet objects = db.query( new Predicate<Cobject>() {
            public boolean match(Cobject object) {
                return object.getUUID().equals( parent.getUUID() );
            }
          });
          long value = 0;
          Cobject finalObject = null;
          while (objects.hasNext()) {
            Cobject newer = (Cobject)objects.next();
            if ( newer.getCreatedAt().getTime() > value ) {
                finalObject = newer;
                value = newer.getCreatedAt().getTime();
            }
          }
          return finalObject;
    }

   /**
     * native load Cobject (or list of Cobjects) by uuid of parent
     */
    public ObjectSet loadByType( final Cobject parent ) {
        if (!connected) return null;
          ObjectSet objects = db.query( new Predicate<Cobject>() {
            public boolean match(Cobject object) {
                return object.getClass().equals( parent.getClass() );
            }
          });
          return objects;
    }

   /**
     * objectCount() will return amount of objects in database
     */
    public int objectCount() {
        if (!connected) return -1;
          ObjectSet results = this.loadByType( new Cobject( false ) ); //queryByExample( new Cobject( false ) );
          return results.size();
    }

}
