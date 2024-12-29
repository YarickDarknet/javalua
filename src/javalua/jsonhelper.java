package javalua;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class jsonhelper {

    private Midlet app;
    private LuaValue jh;
    
    public jsonhelper(final Midlet app){
        this.app = app;
        jh = app.globals.get("json");
        jh.set("decodefile",new OneArgFunction(){
            public LuaValue call(LuaValue path){
                String text = new String();
                String uri = path.checkjstring();
                try{
                    FileConnection conn = (FileConnection) Connector.open("file:///" +  uri, Connector.READ );
                    if(!conn.exists() && !app.localpath.equals("")){
                        conn = (FileConnection) Connector.open(app.localpath +  uri, Connector.READ );
                    }
                    InputStream str = conn.openInputStream();
                    StringBuffer buf = new StringBuffer();
                    int val;
                    while((val = str.read()) != -1){
                        buf.append((char)val);
                    }
                    text = buf.toString();
                    conn.close();
                    str.close();
                }catch(IOException e){
                    error(e.getMessage());
                }
                return jh.get("decode").call(text);
            }
        });
        
        jh.set("encodefile",new TwoArgFunction(){
            public LuaValue call(LuaValue path, LuaValue json){
                String text = jh.get("encode").call(json).checkjstring();
                String uri = path.checkjstring();
                try {
                    FileConnection conn = (FileConnection) Connector.open("file:///" + uri, Connector.READ_WRITE);
                    if (!conn.exists()) {
                        // Optionally, you can handle the case when the file doesn't exist, for example by creating it
                        conn.create(); // Create the file if it doesn't exist
                    }
                    // Open OutputStream to write the content into the file
                    OutputStream str = conn.openOutputStream();
                    // Write the text into the file
                    byte[] data = text.getBytes();
                    str.write(data);
                    str.close();
                    conn.close();
                } catch (IOException e) {
                    error(e.getMessage());
                }
                return NONE;
            }
        });
    }
    
}
