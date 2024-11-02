package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class GaugeComponent extends VarArgFunction{
    private Midlet app;
    
    public GaugeComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String GaugeName = args.checkjstring(1);
        boolean IsInteractive = args.checkboolean(2);
        int MaxValue = args.checkint(3);
        int DefaultValue = args.optint(4,0);
        
        return create(new Gauge(GaugeName,IsInteractive,MaxValue,DefaultValue));
    }
    
    public LuaValue create(Gauge ge){
        LuaValue gauge = new LuaTable();
        gauge = new ItemComponent(app).initItem(gauge);
        
        gauge.set("type", valueOf("Gauge"));
        gauge.set("element",LuaValue.userdataOf(ge));
        
        gauge.set("setValue", new TwoArgFunction() {
            public LuaValue call(LuaValue gauge, LuaValue value) {
                Gauge ga = (Gauge) gauge.checktable().get("element").checkuserdata(Gauge.class);
                ga.setValue(value.checkint());

                return NONE;
            }
        });
        
        gauge.set("getValue", new TwoArgFunction() {
            public LuaValue call(LuaValue gauge, LuaValue value) {
                Gauge ga = (Gauge) gauge.checktable().get("element").checkuserdata(Gauge.class);

                return LuaValue.valueOf(ga.getValue());
            }
        });
        
        gauge.setmetatable(gauge);
        return gauge;
    }
}