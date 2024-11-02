package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class StringItemComponent extends VarArgFunction{
    private Midlet app;
    
    public StringItemComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String Label = args.checkjstring(1);
        String Text = args.checkjstring(2);
        int constraints = args.optint(3,0);
        
        return create(new StringItem(Label,Text,constraints));
    }
    
    public LuaValue create(StringItem si){
        LuaValue stringitem = new LuaTable();
        stringitem = new ItemComponent(app).initItem(stringitem);
        
        stringitem.set("type", valueOf("StringItem"));
        stringitem.set("element",LuaValue.userdataOf(si));
        
        stringitem.set("getAppearanceMode", new OneArgFunction(){
            public LuaValue call(LuaValue a){
                StringItem str = (StringItem) a.checktable().get("element").checkuserdata(StringItem.class);
                return valueOf(str.getAppearanceMode());
            }
        });
        
        stringitem.set("setText", new TwoArgFunction(){
            public LuaValue call(LuaValue a, LuaValue b){
                StringItem str = (StringItem) a.checktable().get("element").checkuserdata(StringItem.class);
                str.setText(b.checkjstring());
                return NONE;
            }
        });
        
        stringitem.set("getText", new OneArgFunction(){
            public LuaValue call(LuaValue a){
                StringItem str = (StringItem) a.checktable().get("element").checkuserdata(StringItem.class);
                return valueOf(str.getText());
            }
        });
        
        stringitem.setmetatable(stringitem);
        return stringitem;
    }
}