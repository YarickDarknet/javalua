package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class ListComponent extends VarArgFunction{
    private Midlet app;
    
    public ListComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String title = args.arg(1).checkjstring();
        int listType = args.arg(2).checkint();
        
        return create(new List(title, listType));
    }
    
    public LuaValue create(List ls){
        LuaValue list = new LuaTable();
        list = new DisplayableComponent(app).initDisplayable(list);
        
        list.set("element", LuaValue.userdataOf(ls));
        list.set("type", valueOf("List"));
        // Добавление элемента в список (с опциональной картинкой)
        list.set("append", new TwoArgFunction() {
            public LuaValue call(LuaValue listValue, LuaValue stringElem) {
                List list = (List) listValue.checktable().get("element").checkuserdata(List.class);
                String element = stringElem.checkjstring();
                list.append(element, null);
                return NONE;
            }
        });
        list.set("setSelectCommand", new TwoArgFunction() {
            public LuaValue call(LuaValue listValue, LuaValue comelem) {
                List list = (List) listValue.checktable().get("element").checkuserdata(List.class);
                Command element = (Command)comelem.checktable().get("element").checkuserdata(Command.class);
                list.setSelectCommand(element);
                return NONE;
            }
        });
        // Удаление элемента из списка по индексу
        list.set("delete", new TwoArgFunction() {
            public LuaValue call(LuaValue listValue, LuaValue index) {
                List list = (List) listValue.checktable().get("element").checkuserdata(List.class);
                int idx = index.checkint();
                list.delete(idx);
                return NONE;
            }
        });

        // Получение выбранного элемента для списков типа EXCLUSIVE или IMPLICIT
        list.set("getSelectedIndex", new OneArgFunction() {
            public LuaValue call(LuaValue listValue) {
                List list = (List) listValue.checktable().get("element").checkuserdata(List.class);
                return LuaValue.valueOf(list.getSelectedIndex());
            }
        });

        // Получение массива выбранных элементов для списка типа MULTIPLE
        list.set("getSelectedFlags", new OneArgFunction() {
            public LuaValue call(LuaValue listValue) {
                List list = (List) listValue.checktable().get("element").checkuserdata(List.class);
                boolean[] selectedFlags = new boolean[list.size()];
                list.getSelectedFlags(selectedFlags);

                // Конвертация boolean[] в таблицу Lua
                LuaTable luaSelectedFlags = new LuaTable();
                for (int i = 0; i < selectedFlags.length; i++) {
                    luaSelectedFlags.set(i + 1, LuaValue.valueOf(selectedFlags[i]));
                }
                return luaSelectedFlags;
            }
        });

        // Получение текста элемента по индексу
        list.set("getString", new TwoArgFunction() {
            public LuaValue call(LuaValue listValue, LuaValue index) {
                List list = (List) listValue.checktable().get("element").checkuserdata(List.class);
                int idx = index.checkint();
                return LuaValue.valueOf(list.getString(idx));
            }
        });

        // Очистка списка
        list.set("clear", new OneArgFunction() {
            public LuaValue call(LuaValue listValue) {
                List list = (List) listValue.checktable().get("element").checkuserdata(List.class);
                list.deleteAll();
                return NONE;
            }
        });
        
        list.setmetatable(list);
        return list;
    }
}