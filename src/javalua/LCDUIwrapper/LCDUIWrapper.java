package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import javalua.FileChooser;

public class LCDUIWrapper{
    
    private Midlet app;
    private LuaValue UI;
    
    public LCDUIWrapper(Globals globals, Midlet oapp) throws LuaError{
        app = oapp;
        UI = new LuaTable();
        UI.set("enableTerminal",new ZeroArgFunction(){
            public LuaValue call(){
                app.startTerminal();
                return NONE;
            }
        });
        UI.set("display",new TwoArgFunction(){
            public LuaValue call(LuaValue arg, LuaValue arg2){
                //app.display.setCurrent((Displayable) arg.checktable().get("element").checkuserdata(Displayable.class));
                if (!(arg.checktable().get("type").checkjstring().equals("Alert")) || arg2.isnil()) {
                    if(arg.checktable().get("type").checkjstring().equals("FileChooser")){
                        ((FileChooser)arg.checktable().get("element").checkuserdata(FileChooser.class)).show();
                        app.enableTerminal = false;
                        return NONE;
                    }
                    app.display.setCurrent((Displayable) arg.checktable().get("element").checkuserdata(Displayable.class));
                    app.enableTerminal = false;
                } else {
                    app.display.setCurrent((Alert) arg.checktable().get("element").checkuserdata(Alert.class), (Displayable) arg2.checktable().get("element").checkuserdata(Displayable.class));
                    app.enableTerminal = false;
                } 
                return NONE;
            }
        });
        UI.set("Form", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new FormComponent(app)}));
        UI.set("FileChooser", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new FileChooserComponent(app)}));
        UI.set("List", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new ListComponent(app),
            LuaValue.valueOf("IMPLICT"), LuaValue.valueOf(List.IMPLICIT),
            LuaValue.valueOf("EXCLUSIVE"), LuaValue.valueOf(List.EXCLUSIVE),
            LuaValue.valueOf("MULTIPLE"), LuaValue.valueOf(List.MULTIPLE),
            LuaValue.valueOf("POPUP"), LuaValue.valueOf(List.POPUP)
        }));
        UI.set("TextBox", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new TextBoxComponent(app)
        }));
        UI.set("Alert", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new AlertComponent(app),
            LuaValue.valueOf("ALARM"), LuaValue.userdataOf(AlertType.ALARM),
            LuaValue.valueOf("CONFIRMATION"), LuaValue.userdataOf(AlertType.CONFIRMATION),
            LuaValue.valueOf("ERROR"), LuaValue.userdataOf(AlertType.ERROR),
            LuaValue.valueOf("INFO"), LuaValue.userdataOf(AlertType.INFO),
            LuaValue.valueOf("WARNING"), LuaValue.userdataOf(AlertType.WARNING),
            LuaValue.valueOf("FOREVER"), LuaValue.valueOf(Alert.FOREVER)
        }));
        UI.set("Command", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new CommandComponent(app),
            LuaValue.valueOf("BACK"), LuaValue.valueOf(Command.BACK),
            LuaValue.valueOf("CANCEL"), LuaValue.valueOf(Command.CANCEL),
            LuaValue.valueOf("EXIT"), LuaValue.valueOf(Command.EXIT),
            LuaValue.valueOf("HELP"), LuaValue.valueOf(Command.HELP),
            LuaValue.valueOf("ITEM"), LuaValue.valueOf(Command.ITEM),
            LuaValue.valueOf("OK"), LuaValue.valueOf(Command.OK),
            LuaValue.valueOf("SCREEN"), LuaValue.valueOf(Command.SCREEN),
            LuaValue.valueOf("STOP"), LuaValue.valueOf(Command.STOP)
        }));
        UI.set("Gauge", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new GaugeComponent(app),
            LuaValue.valueOf("BUTTON"), LuaValue.valueOf(Item.BUTTON),
            LuaValue.valueOf("HYPERLINK"), LuaValue.valueOf(Item.HYPERLINK),
            LuaValue.valueOf("LAYOUT_2"), LuaValue.valueOf(Item.LAYOUT_2),
            LuaValue.valueOf("LAYOUT_BOTTOM"), LuaValue.valueOf(Item.LAYOUT_BOTTOM),
            LuaValue.valueOf("LAYOUT_CENTER"), LuaValue.valueOf(Item.LAYOUT_CENTER),
            LuaValue.valueOf("LAYOUT_DEFAULT"), LuaValue.valueOf(Item.LAYOUT_DEFAULT),
            LuaValue.valueOf("LAYOUT_EXPAND"), LuaValue.valueOf(Item.LAYOUT_EXPAND),
            LuaValue.valueOf("LAYOUT_LEFT"), LuaValue.valueOf(Item.LAYOUT_LEFT),
            LuaValue.valueOf("LAYOUT_NEWLINE_AFTER"), LuaValue.valueOf(Item.LAYOUT_NEWLINE_AFTER),
            LuaValue.valueOf("LAYOUT_NEWLINE_BEFORE"), LuaValue.valueOf(Item.LAYOUT_NEWLINE_BEFORE),
            LuaValue.valueOf("LAYOUT_RIGHT"), LuaValue.valueOf(Item.LAYOUT_RIGHT),
            LuaValue.valueOf("LAYOUT_SHRINK"), LuaValue.valueOf(Item.LAYOUT_SHRINK),
            LuaValue.valueOf("LAYOUT_TOP"), LuaValue.valueOf(Item.LAYOUT_TOP),
            LuaValue.valueOf("LAYOUT_VCENTER"), LuaValue.valueOf(Item.LAYOUT_VCENTER),
            LuaValue.valueOf("LAYOUT_VEXPAND"), LuaValue.valueOf(Item.LAYOUT_VEXPAND),
            LuaValue.valueOf("LAYOUT_VSHRINK"), LuaValue.valueOf(Item.LAYOUT_VSHRINK),
            LuaValue.valueOf("PLAIN"), LuaValue.valueOf(Item.PLAIN)
        }));
        UI.set("ChoiceGroup", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new ChoiceGroupComponent(app),
            LuaValue.valueOf("IMPLICIT"), LuaValue.valueOf(ChoiceGroup.IMPLICIT),
            LuaValue.valueOf("EXCLUSIVE"), LuaValue.valueOf(ChoiceGroup.EXCLUSIVE),
            LuaValue.valueOf("MULTIPLE"), LuaValue.valueOf(ChoiceGroup.MULTIPLE),
            LuaValue.valueOf("POPUP"), LuaValue.valueOf(ChoiceGroup.POPUP),
            LuaValue.valueOf("BUTTON"), LuaValue.valueOf(Item.BUTTON),
            LuaValue.valueOf("HYPERLINK"), LuaValue.valueOf(Item.HYPERLINK),
            LuaValue.valueOf("LAYOUT_2"), LuaValue.valueOf(Item.LAYOUT_2),
            LuaValue.valueOf("LAYOUT_BOTTOM"), LuaValue.valueOf(Item.LAYOUT_BOTTOM),
            LuaValue.valueOf("LAYOUT_CENTER"), LuaValue.valueOf(Item.LAYOUT_CENTER),
            LuaValue.valueOf("LAYOUT_DEFAULT"), LuaValue.valueOf(Item.LAYOUT_DEFAULT),
            LuaValue.valueOf("LAYOUT_EXPAND"), LuaValue.valueOf(Item.LAYOUT_EXPAND),
            LuaValue.valueOf("LAYOUT_LEFT"), LuaValue.valueOf(Item.LAYOUT_LEFT),
            LuaValue.valueOf("LAYOUT_NEWLINE_AFTER"), LuaValue.valueOf(Item.LAYOUT_NEWLINE_AFTER),
            LuaValue.valueOf("LAYOUT_NEWLINE_BEFORE"), LuaValue.valueOf(Item.LAYOUT_NEWLINE_BEFORE),
            LuaValue.valueOf("LAYOUT_RIGHT"), LuaValue.valueOf(Item.LAYOUT_RIGHT),
            LuaValue.valueOf("LAYOUT_SHRINK"), LuaValue.valueOf(Item.LAYOUT_SHRINK),
            LuaValue.valueOf("LAYOUT_TOP"), LuaValue.valueOf(Item.LAYOUT_TOP),
            LuaValue.valueOf("LAYOUT_VCENTER"), LuaValue.valueOf(Item.LAYOUT_VCENTER),
            LuaValue.valueOf("LAYOUT_VEXPAND"), LuaValue.valueOf(Item.LAYOUT_VEXPAND),
            LuaValue.valueOf("LAYOUT_VSHRINK"), LuaValue.valueOf(Item.LAYOUT_VSHRINK),
            LuaValue.valueOf("PLAIN"), LuaValue.valueOf(Item.PLAIN)
        }));
        UI.set("TextField", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new TextFieldComponent(app),
            LuaValue.valueOf("ANY"), LuaValue.valueOf(TextField.ANY),
            LuaValue.valueOf("CONSTRAINT_MASK"), LuaValue.valueOf(TextField.CONSTRAINT_MASK),
            LuaValue.valueOf("EMAILADDR"), LuaValue.valueOf(TextField.EMAILADDR),
            LuaValue.valueOf("NUMERIC"), LuaValue.valueOf(TextField.NUMERIC),
            LuaValue.valueOf("PASSWORD"), LuaValue.valueOf(TextField.PASSWORD),
            LuaValue.valueOf("PHONENUMBER"), LuaValue.valueOf(TextField.PHONENUMBER),
            LuaValue.valueOf("URL"), LuaValue.valueOf(TextField.URL),
            LuaValue.valueOf("BUTTON"), LuaValue.valueOf(Item.BUTTON),
            LuaValue.valueOf("HYPERLINK"), LuaValue.valueOf(Item.HYPERLINK),
            LuaValue.valueOf("LAYOUT_2"), LuaValue.valueOf(Item.LAYOUT_2),
            LuaValue.valueOf("LAYOUT_BOTTOM"), LuaValue.valueOf(Item.LAYOUT_BOTTOM),
            LuaValue.valueOf("LAYOUT_CENTER"), LuaValue.valueOf(Item.LAYOUT_CENTER),
            LuaValue.valueOf("LAYOUT_DEFAULT"), LuaValue.valueOf(Item.LAYOUT_DEFAULT),
            LuaValue.valueOf("LAYOUT_EXPAND"), LuaValue.valueOf(Item.LAYOUT_EXPAND),
            LuaValue.valueOf("LAYOUT_LEFT"), LuaValue.valueOf(Item.LAYOUT_LEFT),
            LuaValue.valueOf("LAYOUT_NEWLINE_AFTER"), LuaValue.valueOf(Item.LAYOUT_NEWLINE_AFTER),
            LuaValue.valueOf("LAYOUT_NEWLINE_BEFORE"), LuaValue.valueOf(Item.LAYOUT_NEWLINE_BEFORE),
            LuaValue.valueOf("LAYOUT_RIGHT"), LuaValue.valueOf(Item.LAYOUT_RIGHT),
            LuaValue.valueOf("LAYOUT_SHRINK"), LuaValue.valueOf(Item.LAYOUT_SHRINK),
            LuaValue.valueOf("LAYOUT_TOP"), LuaValue.valueOf(Item.LAYOUT_TOP),
            LuaValue.valueOf("LAYOUT_VCENTER"), LuaValue.valueOf(Item.LAYOUT_VCENTER),
            LuaValue.valueOf("LAYOUT_VEXPAND"), LuaValue.valueOf(Item.LAYOUT_VEXPAND),
            LuaValue.valueOf("LAYOUT_VSHRINK"), LuaValue.valueOf(Item.LAYOUT_VSHRINK),
            LuaValue.valueOf("PLAIN"), LuaValue.valueOf(Item.PLAIN)
        }));
        UI.set("StringItem", LuaValue.tableOf(new LuaValue[] {
            LuaValue.valueOf("new"),new StringItemComponent(app),
            LuaValue.valueOf("BUTTON"), LuaValue.valueOf(Item.BUTTON),
            LuaValue.valueOf("HYPERLINK"), LuaValue.valueOf(Item.HYPERLINK),
            LuaValue.valueOf("LAYOUT_2"), LuaValue.valueOf(Item.LAYOUT_2),
            LuaValue.valueOf("LAYOUT_BOTTOM"), LuaValue.valueOf(Item.LAYOUT_BOTTOM),
            LuaValue.valueOf("LAYOUT_CENTER"), LuaValue.valueOf(Item.LAYOUT_CENTER),
            LuaValue.valueOf("LAYOUT_DEFAULT"), LuaValue.valueOf(Item.LAYOUT_DEFAULT),
            LuaValue.valueOf("LAYOUT_EXPAND"), LuaValue.valueOf(Item.LAYOUT_EXPAND),
            LuaValue.valueOf("LAYOUT_LEFT"), LuaValue.valueOf(Item.LAYOUT_LEFT),
            LuaValue.valueOf("LAYOUT_NEWLINE_AFTER"), LuaValue.valueOf(Item.LAYOUT_NEWLINE_AFTER),
            LuaValue.valueOf("LAYOUT_NEWLINE_BEFORE"), LuaValue.valueOf(Item.LAYOUT_NEWLINE_BEFORE),
            LuaValue.valueOf("LAYOUT_RIGHT"), LuaValue.valueOf(Item.LAYOUT_RIGHT),
            LuaValue.valueOf("LAYOUT_SHRINK"), LuaValue.valueOf(Item.LAYOUT_SHRINK),
            LuaValue.valueOf("LAYOUT_TOP"), LuaValue.valueOf(Item.LAYOUT_TOP),
            LuaValue.valueOf("LAYOUT_VCENTER"), LuaValue.valueOf(Item.LAYOUT_VCENTER),
            LuaValue.valueOf("LAYOUT_VEXPAND"), LuaValue.valueOf(Item.LAYOUT_VEXPAND),
            LuaValue.valueOf("LAYOUT_VSHRINK"), LuaValue.valueOf(Item.LAYOUT_VSHRINK),
            LuaValue.valueOf("PLAIN"), LuaValue.valueOf(Item.PLAIN)
        }));
    }
    
    public LuaValue getTable(){
        return UI;
    }
    
}

    
