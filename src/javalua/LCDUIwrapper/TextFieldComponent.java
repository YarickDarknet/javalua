package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class TextFieldComponent extends VarArgFunction{
    private Midlet app;
    
    public TextFieldComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String textFieldName = args.checkjstring(1);
        String textFieldText = args.checkjstring(2);
        int maxSize = args.checkint(3);
        int constraints = args.optint(4,0);
        return create(new TextField(textFieldName, textFieldText, maxSize, constraints));
    }
    
    public LuaValue create(TextField tf){
        LuaValue textfield = new LuaTable();
        textfield = new ItemComponent(app).initItem(textfield);
        
        textfield.set("type", valueOf("TextField"));
        textfield.set("element",LuaValue.userdataOf(tf));
        textfield.set("setText", new TwoArgFunction() {
            public LuaValue call(LuaValue field, LuaValue text) {
                TextField tf = (TextField) field.checktable().get("element").checkuserdata(TextField.class);
                tf.setString(text.checkjstring());
                return NONE;
            }
        });
        textfield.set("getText", new OneArgFunction() {
            public LuaValue call(LuaValue field){
                TextField tf = (TextField) field.checktable().get("element").checkuserdata(TextField.class);
                return LuaValue.valueOf(tf.getString());
            }
        });
        textfield.set("setMaxLength", new TwoArgFunction() {
            public LuaValue call(LuaValue field, LuaValue maxLength) {
                TextField tf = (TextField) field.checktable().get("element").checkuserdata(TextField.class);
                tf.setMaxSize(maxLength.checkint());
                return NONE;
            }
        });
        textfield.set("getMaxLength", new OneArgFunction() {
            public LuaValue call(LuaValue field) {
                TextField tf = (TextField) field.checktable().get("element").checkuserdata(TextField.class);
                return LuaValue.valueOf(tf.getMaxSize());
            }
        });
        textfield.set("getCaretPosition", new OneArgFunction() {
            public LuaValue call(LuaValue field) {
                TextField tf = (TextField) field.checktable().get("element").checkuserdata(TextField.class);
                return valueOf(tf.getCaretPosition());
            }
        });
        textfield.set("setInputConstraints", new TwoArgFunction() {
            public LuaValue call(LuaValue field, LuaValue constraints) {
                TextField tf = (TextField) field.checktable().get("element").checkuserdata(TextField.class);
                tf.setConstraints(constraints.checkint());
                return NONE;
            }
        });
        textfield.set("getInputConstraints", new OneArgFunction() {
            public LuaValue call(LuaValue field) {
                TextField tf = (TextField) field.checktable().get("element").checkuserdata(TextField.class);
                return LuaValue.valueOf(tf.getConstraints());
            }
        });
        
        textfield.setmetatable(textfield);
        return textfield;
    }
}