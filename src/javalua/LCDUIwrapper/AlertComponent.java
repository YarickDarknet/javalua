package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class AlertComponent extends VarArgFunction{
    private Midlet app;
    
    public AlertComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String title = args.checkjstring(1);
        String atext = args.checkjstring(2);
        AlertType atype = (AlertType)args.checkuserdata(3,AlertType.class);
        return create(new Alert(title, atext, null, atype));
    }
    
    public LuaValue create(Alert al){
        LuaValue alert = new LuaTable();
        alert = new DisplayableComponent(app).initDisplayable(alert);
        
        alert.set("type", valueOf("Alert"));
        alert.set("element",LuaValue.userdataOf(al));
        
        alert.set("getTimeout", new OneArgFunction() {
            public LuaValue call(LuaValue a) {
                Alert al = (Alert) a.checktable().get("element").checkuserdata(Alert.class);
                return valueOf(al.getTimeout());
            }
        });
        alert.set("setTimeout", new TwoArgFunction() {
            public LuaValue call(LuaValue a, LuaValue timeout) {
                Alert al = (Alert) a.checktable().get("element").checkuserdata(Alert.class);
                al.setTimeout(timeout.checkint());
                return NONE;
            }
        });
        alert.set("setIndicator", new TwoArgFunction() {
            public LuaValue call(LuaValue a, LuaValue indicator) {
                Alert al = (Alert) a.checktable().get("element").checkuserdata(Alert.class);
                al.setIndicator((Gauge)indicator.checkuserdata(Gauge.class));
                return NONE;
            }
        });
        alert.set("getIndicator", new OneArgFunction() {
            public LuaValue call(LuaValue a) {
                Alert al = (Alert) a.checktable().get("element").checkuserdata(Alert.class);
                return userdataOf(al.getIndicator());
            }
        });
        
        alert.set("setString", new TwoArgFunction() {
            public LuaValue call(LuaValue a, LuaValue string) {
                Alert al = (Alert) a.checktable().get("element").checkuserdata(Alert.class);
                al.setString(string.checkjstring());
                return NONE;
            }
        });
        alert.set("getString", new OneArgFunction() {
            public LuaValue call(LuaValue a) {
                Alert al = (Alert) a.checktable().get("element").checkuserdata(Alert.class);
                return valueOf(al.getString());
            }
        });
        
        alert.setmetatable(alert);
        return alert;
    }
}