package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class FormComponent extends VarArgFunction{
    private Midlet app;
    
    public FormComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String formname = args.checkjstring(1);
        System.out.println(formname);
        
        return create(new Form(formname));
    }
    
    public LuaValue create(Form fr){
        LuaValue form = new LuaTable();
        form = new DisplayableComponent(app).initDisplayable(form);
        
        form.set("type", valueOf("Form"));
        form.set("element",LuaValue.userdataOf(fr));
        
        form.set("append",new TwoArgFunction(){
            public LuaValue call(LuaValue a, LuaValue elem){
                Form form = (Form) a.checktable().get("element").checkuserdata(Form.class);
                if (elem.isstring()) {
                    form.append(elem.checkjstring());
                    return NONE;
                }
                if (elem.istable()) {
                    form.append((Item) elem.checktable().get("element").checkuserdata(Item.class));
                    return NONE;
                }
                error("Unsupported element type for form append!");
                return NONE;
            }
        });
        form.set("deleteAll",new OneArgFunction(){
            public LuaValue call(LuaValue a){
                Form form = (Form) a.checktable().get("element").checkuserdata(Form.class);
                form.deleteAll();
                return NONE;
            }
        });
        form.set("size",new OneArgFunction(){
            public LuaValue call(LuaValue a){
                Form form = (Form) a.checktable().get("element").checkuserdata(Form.class);
                return valueOf(form.size());
            }
        });
        form.set("insert",new ThreeArgFunction(){
            public LuaValue call(LuaValue a, LuaValue pos, LuaValue elem){
                Form form = (Form) a.checktable().get("element").checkuserdata(Form.class);
                if (elem.istable()) {
                    form.insert(pos.checkint(),(Item) elem.checktable().get("element").checkuserdata(Item.class));
                    return NONE;
                }
                error("Unsupported element type for form append!");
                return NONE;
            }
        });        
        form.setmetatable(form);
        return form;
    }
}