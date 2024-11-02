package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class ItemComponent{
    protected Midlet app;
    
    public ItemComponent(Midlet app){
        this.app = app;
    }
    
    public LuaValue initItem(LuaValue ifunctions){
        ifunctions.set("setDefaultCommand", new TwoArgFunction() {
            public LuaValue call(LuaValue item, LuaValue command) {
                Item c1 = (Item) item.checktable().get("element").checkuserdata(Item.class);
                c1.setDefaultCommand((Command) command.checktable().get("element").checkuserdata(Command.class));

                return NONE;
            }
        });
        
        ifunctions.set("setCommandListener", new TwoArgFunction() {
            public LuaValue call(LuaValue item, LuaValue Listener) {
                Item it = (Item)item.checktable().get("element").checkuserdata(Item.class);
                final LuaValue eventHandler = Listener.checkfunction();
                it.setItemCommandListener(new ItemCommandListener() {
                    public void commandAction(Command c, Item i) {
                        try{
                            eventHandler.call(new CommandComponent(app).create(c),new ItemComponent(app).create(i));
                        }catch(LuaError e){
                            app.mainForm.append("> Error: "+e.getMessage().substring(app.scriptsize).trim() +"\n");
                            app.display.setCurrent(app.mainForm);
                        }
                    }
                });
                return NONE;
            }
        });
        
        ifunctions.set("getClassName", new OneArgFunction() {
            public LuaValue call(LuaValue item) {
                Item it = (Item)item.checktable().get("element").checkuserdata(Item.class);
                
                return LuaValue.valueOf(it.getClass().getName().substring(26));
            }
        });
        
        ifunctions.set("getLayout", new OneArgFunction() {
            public LuaValue call(LuaValue item) {
                Item it = (Item)item.checktable().get("element").checkuserdata(Item.class);
                
                return LuaValue.valueOf(it.getLayout());
            }
        });
        
        ifunctions.set("setLayout", new TwoArgFunction() {
            public LuaValue call(LuaValue item, LuaValue lay) {
                Item it = (Item)item.checktable().get("element").checkuserdata(Item.class);
                it.setLayout(lay.checkint());
                return NONE;
            }
        });
        
        ifunctions.set("getLabel", new OneArgFunction() {
            public LuaValue call(LuaValue item) {
                Item it = (Item)item.checktable().get("element").checkuserdata(Item.class);
                return LuaValue.valueOf(it.getLabel());
            }
        });
        
        ifunctions.set("setLabel", new TwoArgFunction() {
            public LuaValue call(LuaValue item, LuaValue lab) {
                Item it = (Item)item.checktable().get("element").checkuserdata(Item.class);
                it.setLabel(lab.checkjstring());
                return NONE;
            }
        });
        ifunctions.set("equals", new TwoArgFunction() {
            public LuaValue call(LuaValue first, LuaValue second) {
                Item i1 = (Item) first.checktable().get("element").checkuserdata(Item.class);
                Item i2 = (Item) second.checktable().get("element").checkuserdata(Item.class);

                return LuaValue.valueOf(i1.equals(i2));
            }
        });

        return ifunctions;
    }
    
    public LuaValue create(Item it){
        LuaValue item = new LuaTable();
        item = new ItemComponent(app).initItem(item);
        
        item.set("type", LuaValue.valueOf("Item"));
        item.set("element",LuaValue.userdataOf(it));
        
        return item;
    }
}