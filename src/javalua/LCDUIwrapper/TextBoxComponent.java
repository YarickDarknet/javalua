package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class TextBoxComponent extends VarArgFunction{
    private Midlet app;
    
    public TextBoxComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String title = args.checkjstring(1);
        String text = args.optjstring(2, "");
        int maxSize = args.optint(3,1024);
        int constraints = args.optint(4, 0);
        
        return create(new TextBox(title, text, maxSize, constraints));
    }
    
    public LuaValue create(TextBox tb){
        LuaValue textBox = new LuaTable();
        textBox = new DisplayableComponent(app).initDisplayable(textBox);
        
        textBox.set("type", valueOf("TextBox"));
        textBox.set("element",LuaValue.userdataOf(tb));
        
        // Получение текста из TextBox
        textBox.set("getText", new OneArgFunction() {
            public LuaValue call(LuaValue a) {
                TextBox textBox = (TextBox) a.checktable().get("element").checkuserdata(TextBox.class);
                return LuaValue.valueOf(textBox.getString());
            }
        });

        // Установка текста в TextBox
        textBox.set("setText", new TwoArgFunction() {
            public LuaValue call(LuaValue a, LuaValue text) {
                TextBox textBox = (TextBox) a.checktable().get("element").checkuserdata(TextBox.class);
                textBox.setString(text.checkjstring());
                return NONE;
            }
        });
        
        textBox.setmetatable(textBox);
        return textBox;
    }
}