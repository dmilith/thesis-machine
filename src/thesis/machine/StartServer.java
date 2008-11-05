package thesis.machine;

import com.db4o.*;
import com.db4o.messaging.*;

/**
 *
 * @author Daniel <dmilith> Dettlaff
 */

public class StartServer implements ServerConfiguration, MessageRecipient {

    /**
      * setting the value to true denotes that the server should be closed
      */
    private boolean stop = false;

    /**
      * messaging callback
      * @see com.db4o.messaging.MessageRecipient#processMessage(ObjectContainer,Object)
      */
    public void processMessage(MessageContext con, Object message) {
      if( message instanceof StopServer ) {
        close();
      }
    }

    /**
      * starts a db4o server using the configuration from
      * {@link ServerConfiguration}.
      */
    public static void main(String[] arguments) {
        System.out.println( "\n* thesis.machine.StartServer: " + Db4o.version() + "\n* started on port: " + PORT +
                            "\n* listening at: " + HOST + "\n* attached to file: " + DBFILE );
        new StartServer().runServer();
    }

  /**
    * opens the ObjectServer, and waits forever until close() is called
    * or a StopServer message is being received.
    */
  public void runServer(){
    synchronized(this){
      ObjectServer db4oServer = Db4o.openServer(DBFILE, PORT);
      db4oServer.grantAccess( USER, PASS );
      // Using the messaging functionality to redirect all
      // messages to this.processMessage
      db4oServer.ext().configure().clientServer().setMessageRecipient(this);
      // to identify the thread in a debugger
      Thread.currentThread().setName(this.getClass().getName());
      // We only need low priority since the db4o server has
      // it's own thread.
      Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        try {
              if(! stop){
                  // wait forever for notify() from close()
                  this.wait(Long.MAX_VALUE);
              }
        } catch (Exception e) {
              e.printStackTrace();
        }
      db4oServer.close();
      }
  }

  /**
  * closes this server.
  */
  public void close(){
      synchronized(this){
          stop = true;
          this.notify();
      }
  }

}
