package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class DisplayableComponent{
    protected Midlet app;
    
    public DisplayableComponent(Midlet app){
        this.app = app;
    }
    
    public LuaValue initDisplayable(LuaValue disp){
        disp.set("addCommand",new TwoArgFunction(){
            public LuaValue call(LuaValue a, LuaValue elem){
                Displayable dp = (Displayable) a.checktable().get("element").checkuserdata(Displayable.class);
                dp.addCommand((Command) elem.checktable().get("element").checkuserdata(Command.class));
                return NONE;
            }
        });
        disp.set("setCommandHandler",new TwoArgFunction(){
            public LuaValue call(LuaValue a, LuaValue elem){
                Displayable dp = (Displayable) a.checktable().get("element").checkuserdata(Displayable.class);
                final LuaValue eventHandler = elem.checkfunction();
                dp.setCommandListener(new CommandListener() {
                    public void commandAction(Command c, Displayable d) {
                        try{
                            eventHandler.call(new CommandComponent(app).create(c),new DisplayableComponent(app).create(d));
                        }catch(LuaError e){
                            app.mainForm.append("> Error: "+e.getMessage().substring(app.scriptsize).trim() +"\n");
                            app.display.setCurrent(app.mainForm);
                        }
                    }
                });
                return NONE;
            }
        });
        disp.set("removeCommand", new TwoArgFunction() {
            public LuaValue call(LuaValue a, LuaValue cmdValue) {
                Displayable dp = (Displayable) a.checktable().get("element").checkuserdata(Displayable.class);
                Command cmd = (Command) cmdValue.checktable().get("element").checkuserdata(Command.class);
                dp.removeCommand(cmd);
                return NONE;
            }
        });
        disp.set("getTitle", new TwoArgFunction() {
            public LuaValue call(LuaValue a, LuaValue cmdValue) {
                Displayable dp = (Displayable) a.checktable().get("element").checkuserdata(Displayable.class);
                return valueOf(dp.getTitle());
            }
        });
        disp.set("equals", new TwoArgFunction() {
            public LuaValue call(LuaValue first, LuaValue second) {
                Displayable d1 = (Displayable) first.checktable().get("element").checkuserdata(Displayable.class);
                Displayable d2 = (Displayable) second.checktable().get("element").checkuserdata(Displayable.class);

                return LuaValue.valueOf(d1.equals(d2));
            }
        });
        
        return disp;
    }
    
    public LuaValue create(Displayable dp){
        LuaValue disp = new LuaTable();
        disp.set("element", LuaValue.userdataOf(dp));
        disp.set("type",LuaValue.valueOf("Displayable"));
        disp = new DisplayableComponent(app).initDisplayable(disp);
        
        return disp;
    }
}