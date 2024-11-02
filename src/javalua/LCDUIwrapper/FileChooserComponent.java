package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import javalua.FileChooser;

class FileChooserComponent extends VarArgFunction{
    private Midlet app;
    private LuaValue handler;
    
    public FileChooserComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        handler = args.checkfunction(2);
        
        return create(new FileChooser(app.display,new FileChooser.FileSelectionHandler(){
            public void onFileSelected(String filePath, String directoryPath){
                try{
                    handler.call(valueOf(filePath.substring(7)),valueOf(directoryPath.substring(7)));
                }catch(LuaError e){
                    app.mainForm.append("> Error: "+e.getMessage().substring(app.scriptsize).trim() +"\n");
                    app.display.setCurrent(app.mainForm);
                }
            }
        }));
    }
    
    public LuaValue create(FileChooser fc){
        LuaValue fchooser = new LuaTable();
        fchooser = new ItemComponent(app).initItem(fchooser);
        
        fchooser.set("type", valueOf("FileChooser"));
        fchooser.set("element",LuaValue.userdataOf(fc));
        
        fchooser.setmetatable(fchooser);
        return fchooser;
    }
}