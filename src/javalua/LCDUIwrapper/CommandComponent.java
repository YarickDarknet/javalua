package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class CommandComponent extends VarArgFunction{
    private Midlet app;
    
    public CommandComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String commandName = args.checkjstring(1);
        int commandType = args.checkint(2);
        int commandPriority = args.checkint(3);
        
        return create(new Command(commandName, commandType,commandPriority));
    }
    
    public LuaValue create(Command cmd){
        LuaValue command = new LuaTable();
        
        command.set("type", valueOf("Command"));
        command.set("element",LuaValue.userdataOf(cmd));
        
        command.set("getType", new OneArgFunction() {
            public LuaValue call(LuaValue command) {
                Command c1 = (Command) command.checktable().get("element").checkuserdata(Command.class);

                return LuaValue.valueOf(c1.getCommandType());
            }
        });
        
        command.set("getLabel", new OneArgFunction() {
            public LuaValue call(LuaValue command) {
                Command c1 = (Command) command.checktable().get("element").checkuserdata(Command.class);
                
                return LuaValue.valueOf(c1.getLabel());
            }
        });
        
        command.set("equals", new TwoArgFunction() {
            public LuaValue call(LuaValue first, LuaValue second) {
                Command c1 = (Command) first.checktable().get("element").checkuserdata(Command.class);
                Command c2 = (Command) second.checktable().get("element").checkuserdata(Command.class);

                return LuaValue.valueOf(c1.equals(c2));
            }
        });
        
        command.setmetatable(command);
        return command;
    }
}