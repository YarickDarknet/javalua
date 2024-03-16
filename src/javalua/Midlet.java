/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javalua;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.microedition.lcdui.*;  

import java.lang.Thread;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jme.JmePlatform;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import java.util.Enumeration;

/**
 * @author User
 */
public class Midlet extends MIDlet implements CommandListener {

    protected Display display;
    private Form mainForm;
    private TextField commandTextBox;
    private static String output;
    private TextBox tb;
    private boolean needinput = false;
    Command submitCommand;
    Globals globals = JmePlatform.standardGlobals();
        public void startApp() {
            if(display == null){
                mainForm = new Form("Lua Terminal");
                commandTextBox = new TextField("Команда","",100,0);
                tb = new TextBox("Код","",256,0);
                submitCommand = new Command("Выполнить",Command.OK,0);
                output = new String();
                
                mainForm.addCommand(submitCommand);
                //mainForm.append(commandTextBox);
                mainForm.setCommandListener(this);
                
                tb.addCommand(submitCommand);
                tb.setCommandListener(this);
                
                display = Display.getDisplay(this);
                display.setCurrent(mainForm);
                
                globals.set("print", new Print());
                globals.set("clear", new clear());
                globals.set("sleep", new sleep());
                globals.set("require", new require());
                globals.get("io").set("dirs", new dirs());
                globals.get("io").set("input", new input());
                globals.get("os").set("mkdir", new mkdir());
                globals.set("http", new LuaTable());
                globals.get("http").set("get", new get());
                globals.get("http").set("post", new post());
                globals.set("https", new LuaTable());
                globals.get("https").set("get", new sget());
                globals.get("https").set("post", new spost());
            }
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
        if(c.getCommandType() == Command.OK && d == tb){
            Thread thr = new runner();
            thr.run();
            if(needinput == false){
                display.setCurrent(mainForm);
                tb.setString("");
            }
        }
        if(d == mainForm){
            display.setCurrent(tb);
        }
    }
    
    class runner extends Thread{
        public void run(){
            String script = tb.getString();
            output= "";
            try{
                globals.load(script).call();
            }catch(LuaError e){
                output = e.getMessage();
            }
            mainForm.append(new StringItem("",">"+tb.getString()+"\n"));
            mainForm.size();
            System.out.println(output);
            mainForm.append(new StringItem("",output));
            
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
    
    class require extends OneArgFunction {
	public LuaValue call(LuaValue v) {
            String uri = "file:///" + v.checkstring();
            String script = new String();
            try{
                FileConnection conn = (FileConnection) Connector.open( uri, Connector.READ );
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


