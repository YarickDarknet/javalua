package javalua;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import javalua.LCDUIwrapper.LCDUIWrapper;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jme.JmePlatform;

public class Midlet extends MIDlet implements CommandListener {

    public Display display;
    public Form mainForm;
    public int scriptsize = 0;
    private static String output;
    private TextBox tb;
    private boolean needinput = false;
    private String localpath;
    private FileChooser fileChooser;
    public boolean enableTerminal = true;
    Command terminalCommand;
    Command fileCommand;
    Command runCommand;
    Command back;
    Globals globals = JmePlatform.standardGlobals();
    
    LCDUIWrapper wrapper;
        public void startApp() {
            if(display == null){
                mainForm = new Form("Lua Terminal");
                tb = new TextBox("Код","",256,0);
                back = new Command("Назад",Command.BACK,0);
                runCommand = new Command("Выполнить",Command.OK,0);
                terminalCommand = new Command("Терминал",Command.OK,0);
                fileCommand = new Command("Файл",Command.OK,1);
                output = new String();
                
                mainForm.addCommand(terminalCommand);
                mainForm.addCommand(fileCommand);
                mainForm.setCommandListener(this);
                
                tb.addCommand(runCommand);
                tb.addCommand(back);
                tb.setCommandListener(this);
                
                display = Display.getDisplay(this);
                display.setCurrent(mainForm);
                
                globals.set("print", new Print());
                globals.set("clear", new clear());
                globals.set("sleep", new sleep());
                globals.get("io").set("dirs", new dirs());
                globals.set("require", new require());
                //globals.get("io").set("input", new input());
                globals.get("os").set("mkdir", new mkdir());
                globals.set("http", new LuaTable());
                globals.get("http").set("get", new get());
                globals.get("http").set("post", new post());
                globals.set("https", new LuaTable());
                globals.get("https").set("get", new sget());
                globals.get("https").set("post", new spost());
                wrapper = new LCDUIWrapper(globals, this);
                globals.set("UI", wrapper.getTable());
                globals.set("debug", new OneArgFunction() {
                    public LuaValue call(LuaValue arg) {
                        display.setCurrent(mainForm);
                        mainForm.append(">"+(arg.checkjstring())+"\n");
                        return NIL;
                    }
                });
            }
    }
        
    public void startTerminal(){
        enableTerminal = true;
        display.setCurrent(mainForm);
        tb.setString("");
    }
    
    public void pauseApp() {
    }
    
    public void quit(){
        destroyApp(true);
        notifyDestroyed();
    }
    
    public void destroyApp(boolean unconditional) {
    }
    
    public void show(){
        display.setCurrent(mainForm);
    }
    
    public void commandAction(Command c, Displayable d){
        if(d == tb && c == runCommand){
            String script = tb.getString();
            output= "";
            try{
                globals.load(script).call();
            }catch(LuaError e){
                output = e.getMessage();
                display.setCurrent(mainForm);
            }
            mainForm.append(">"+tb.getString()+"\n");
            mainForm.size();
            System.out.println(output);
            mainForm.append(output);
            if(needinput == false){
                if(enableTerminal){
                    display.setCurrent(mainForm);
                }
                tb.setString("");
            }
        }
        if(d == mainForm && c == terminalCommand){
            display.setCurrent(tb);
        }
        if(d == mainForm && c == fileCommand){
            FileChooser.FileSelectionHandler handler = new FileChooser.FileSelectionHandler() {
                public void onFileSelected(String filePath, String directoryPath) {
                    localpath = directoryPath;
                    String script = new String();
                    try{
                        FileConnection conn = (FileConnection) Connector.open( filePath, Connector.READ );
                        InputStream str = conn.openInputStream();
                        StringBuffer buf = new StringBuffer();
                        int val;
                        while((val = str.read()) != -1){
                            buf.append((char)val);
                        }
                        script = buf.toString();
                        conn.close();
                        str.close();
                    }catch(IOException e){
                        display.setCurrent(mainForm);
                        mainForm.append(">"+(e.getMessage())+"\n");
                    }catch(IllegalArgumentException e){
                        display.setCurrent(mainForm);
                        mainForm.append(">"+(e.getMessage())+"\n");
                    }
                    scriptsize = script.length();
                    try{
                        globals.load(script).call();
                    }catch(LuaError e){
                        mainForm.append("> Error: "+e.getMessage().substring(scriptsize).trim() +"\n");
                        display.setCurrent(mainForm);
                    }
                    if(enableTerminal){
                        display.setCurrent(mainForm);
                    }
                }
            };
            fileChooser = new FileChooser(display, handler);
            fileChooser.show();
        }
        if(c == back){
            tb.setString("");
            display.setCurrent(mainForm);
        }
    }
    
    class Print extends VarArgFunction {
	public Varargs invoke(Varargs args) {
            LuaValue tostring = globals.get("tostring");
            for ( int i=1, n=args.narg(); i<=n; i++ ) {
		if ( i>1 ) output+="    ";
		LuaString s = tostring.call( args.arg(i) ).strvalue();
		output+=(s.tojstring());
            }
            output+="\n";
            return NONE;
	}
    }
    
    class require extends OneArgFunction {
	public LuaValue call(LuaValue v) {
            String uri = v.checkjstring();
            String script = new String();
            try{
                FileConnection conn = (FileConnection) Connector.open("file:///" +  uri, Connector.READ );
                if(!conn.exists() && !localpath.equals("")){
                    conn = (FileConnection) Connector.open(localpath +  uri, Connector.READ );
                }
                InputStream str = conn.openInputStream();
                StringBuffer buf = new StringBuffer();
                int val;
                while((val = str.read()) != -1){
                    buf.append((char)val);
                }
                script = buf.toString();
                conn.close();
                str.close();
            }catch(IOException e){
                error(e.getMessage());
            }
            return globals.load(script).call();
	}
    }
    
    class dirs extends OneArgFunction {
	public LuaValue call(LuaValue v) {
            LuaTable directories = new LuaTable();
            String uri = "file:///" + v.strvalue();
            try{
                Enumeration e;
                FileConnection conn = (FileConnection) Connector.open( uri, Connector.READ );
                e = conn.list();
                while(e.hasMoreElements()){
                    directories.insert(directories.length()+1,LuaValue.valueOf((String) e.nextElement()));
                }
                conn.close();
            }catch(IOException e){
                error(e.getMessage());
            }
            return directories;
	}
    }
    
    class clear extends ZeroArgFunction {
	public LuaValue call() {
            mainForm.deleteAll();
            return NONE;
	}
    }
    
    class input extends OneArgFunction implements CommandListener {
        private String str = new String();
        private TextBox inp;
        private Command c;
        private LuaValue func;
	public LuaValue call(LuaValue v) {
            func = v.checkfunction();
            inp = new TextBox("Введите данные","",256,0);
            c = new Command("Готово",Command.OK,0);
            inp.addCommand(c);
            inp.setCommandListener(this);
            display.setCurrent(inp);
            needinput = true;
            
            return valueOf(str);
	}
        
        public void commandAction(Command c, Displayable d){
            if(c.getCommandType() == Command.OK){
                str = inp.getString();
                func.call(str);
                display.setCurrent(mainForm);
                needinput = false;
            }
        }
    }
    
    class get extends VarArgFunction{
	public Varargs invoke(Varargs args) {
            LuaValue uril =args.arg(1);
            LuaValue v = args.arg(2);
            LuaTable params = new LuaTable();
            if(v.istable()){
                params = v.checktable();
            }
            String uri = uril.checkjstring();
            LuaTable retvalue = new LuaTable();
            try{
                HttpConnection conn = (HttpConnection) Connector.open(uri);
                conn.setRequestMethod(HttpConnection.GET);
                if(!params.isnil()){
                    Varargs elem = LuaValue.NIL;
                    while((elem = params.next(elem.arg1())).arg1() != NIL){
                        conn.setRequestProperty(elem.arg1().checkjstring(), elem.arg(2).checkjstring());
                    }
                }
                
                retvalue.insert(retvalue.length()+1, valueOf(conn.getResponseCode()));
                InputStream str = conn.openInputStream();
                StringBuffer buf = new StringBuffer();
                int val;
                while((val = str.read()) != -1){
                    buf.append((char)val);
                }
                retvalue.insert(retvalue.length()+1, valueOf(buf.toString()));
                
                str.close();
                conn.close();
            }catch(Exception e){
                error(e.getMessage());
            }
            return retvalue.unpack();
	}
    }
    
    class post extends VarArgFunction {
	public Varargs invoke(Varargs args) {
            LuaValue uril =args.arg(1);
            LuaValue data = args.arg(2);
            LuaValue v = args.arg(3);
            LuaTable params = new LuaTable();
            LuaTable retvalue = new LuaTable();
            if(v.istable()){
                params = v.checktable();
            }
            String uri = uril.checkjstring();
            try{
                HttpConnection conn = (HttpConnection) Connector.open(uri);
                conn.setRequestMethod(HttpConnection.POST);
                if(!params.isnil()){
                    Varargs elem = LuaValue.NIL;
                    while((elem = params.next(elem.arg1())).arg1() != NIL){
                        conn.setRequestProperty(elem.arg1().checkjstring(), elem.arg(2).checkjstring());
                    }
                }
                
                OutputStream in = conn.openOutputStream();
                in.write(data.checkjstring().getBytes());
                
                retvalue.insert(retvalue.length()+1, valueOf(conn.getResponseCode()));
                InputStream str = conn.openInputStream();
                StringBuffer buf = new StringBuffer();
                int val;
                while((val = str.read()) != -1){
                    buf.append((char)val);
                }
                retvalue.insert(retvalue.length()+1, valueOf(buf.toString()));
                
                str.close();
                conn.close();
            }catch(Exception e){
                error(e.getMessage());
            }
            return retvalue.unpack();
	}
    }
    
    class sget extends VarArgFunction {
	public Varargs invoke(Varargs args) {
            LuaValue uril =args.arg(1);
            LuaValue v = args.arg(2);
            LuaTable params = new LuaTable();
            LuaTable retvalue = new LuaTable();
            if(v.istable()){
                params = v.checktable();
            }
            String uri = uril.checkjstring();
            try{
                HttpsConnection conn = (HttpsConnection) Connector.open(uri);
                conn.setRequestMethod(HttpsConnection.GET);
                if(!params.isnil()){
                    Varargs elem = LuaValue.NIL;
                    while((elem = params.next(elem.arg1())).arg1() != NIL){
                        conn.setRequestProperty(elem.arg1().checkjstring(), elem.arg(2).checkjstring());
                    }
                }
                
                retvalue.insert(retvalue.length()+1, valueOf(conn.getResponseCode()));
                InputStream str = conn.openInputStream();
                StringBuffer buf = new StringBuffer();
                int val;
                while((val = str.read()) != -1){
                    buf.append((char)val);
                }
                retvalue.insert(retvalue.length()+1, valueOf(buf.toString()));
                
                str.close();
                conn.close();
            }catch(Exception e){
                error(e.getMessage());
            }
            return retvalue.unpack();
	}
    }
    
    class spost extends VarArgFunction {
	public Varargs invoke(Varargs args) {
            LuaValue uril =args.arg(1);
            LuaValue data = args.arg(2);
            LuaValue v = args.arg(3);
            LuaTable params = new LuaTable();
            LuaTable retvalue = new LuaTable();
            if(v.istable()){
                params = v.checktable();
            }
            String uri = uril.checkjstring();
            try{
                HttpsConnection conn = (HttpsConnection) Connector.open(uri);
                conn.setRequestMethod(HttpsConnection.POST);
                if(!params.isnil()){
                    Varargs elem = LuaValue.NIL;
                    while((elem = params.next(elem.arg1())).arg1() != NIL){
                        conn.setRequestProperty(elem.arg1().checkjstring(), elem.arg(2).checkjstring());
                    }
                }
                
                OutputStream in = conn.openOutputStream();
                in.write(data.checkjstring().getBytes());
                
                retvalue.insert(retvalue.length()+1, valueOf(conn.getResponseCode()));
                InputStream str = conn.openInputStream();
                StringBuffer buf = new StringBuffer();
                int val;
                while((val = str.read()) != -1){
                    buf.append((char)val);
                }
                retvalue.insert(retvalue.length()+1, valueOf(buf.toString()));
                
                str.close();
                conn.close();
            }catch(Exception e){
                error(e.getMessage());
            }
            return retvalue.unpack();
	}
    }
    
    class sleep extends OneArgFunction {
        public LuaValue call(LuaValue time){
            try{
                Thread.sleep(time.checklong());
            }catch(Exception e){
                error(e.getMessage());
            }
            
            return NONE;
        }
    }
    
    class mkdir extends OneArgFunction {
        public LuaValue call(LuaValue path){
            try{
		FileConnection conn = (FileConnection) Connector.open( "file:///" + path.checkjstring(), Connector.READ_WRITE );
                conn.mkdir();
                conn.close();
            }catch(Exception e){
                error(e.getMessage());
            }
            
            return NONE;
        }
    }
    
}


