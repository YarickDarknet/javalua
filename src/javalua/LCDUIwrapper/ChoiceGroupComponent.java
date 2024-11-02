package javalua.LCDUIwrapper;

import javalua.Midlet;
import javax.microedition.lcdui.*;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

class ChoiceGroupComponent extends VarArgFunction{
    private Midlet app;
    
    public ChoiceGroupComponent(Midlet app){
        this.app = app;
    }
    
    public Varargs invoke(Varargs args){
        String label = args.checkjstring(1);
        int choiceType = args.checkint(2);
        return create(new ChoiceGroup(label, choiceType));
    }
    
    public LuaValue create(ChoiceGroup cg){
        LuaValue choiceGroup = new LuaTable();
        choiceGroup = new ItemComponent(app).initItem(choiceGroup);
        
        choiceGroup.set("type", valueOf("ChoiceGroup"));
        choiceGroup.set("element",LuaValue.userdataOf(cg));
        // Добавление нового элемента в ChoiceGroup (без изображения)
        choiceGroup.set("append", new TwoArgFunction() {
            public LuaValue call(LuaValue cgValue, LuaValue stringPart) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                cg.append(stringPart.checkjstring(), null);  // Изображение задаётся как null
                return NONE;
            }
        });

        // Удаление элемента по индексу
        choiceGroup.set("delete", new TwoArgFunction() {
            public LuaValue call(LuaValue cgValue, LuaValue index) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                cg.delete(index.checkint());
                return NONE;
            }
        });

        // Вставка элемента в заданную позицию (без изображения)
        choiceGroup.set("insert", new ThreeArgFunction() {
            public LuaValue call(LuaValue cgValue, LuaValue index, LuaValue stringPart) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                cg.insert(index.checkint(), stringPart.checkjstring(), null);  // Изображение задаётся как null
                return NONE;
            }
        });

        // Установка значения для существующего элемента (без изображения)
        choiceGroup.set("set", new ThreeArgFunction() {
            public LuaValue call(LuaValue cgValue, LuaValue index, LuaValue stringPart) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                cg.set(index.checkint(), stringPart.checkjstring(), null);  // Изображение задаётся как null
                return NONE;
            }
        });

        // Получение выбранного индекса (для EXCLUSIVE или IMPLICIT ChoiceGroup)
        choiceGroup.set("getSelectedIndex", new OneArgFunction() {
            public LuaValue call(LuaValue cgValue) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                return LuaValue.valueOf(cg.getSelectedIndex());
            }
        });

        // Установка выбранного элемента по индексу
        choiceGroup.set("setSelectedIndex", new TwoArgFunction() {
            public LuaValue call(LuaValue cgValue, LuaValue index) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                cg.setSelectedIndex(index.checkint(), true);  // Устанавливаем элемент выбранным
                return NONE;
            }
        });

        // Получение массива флагов выбранных элементов (для MULTIPLE ChoiceGroup)
        choiceGroup.set("getSelectedFlags", new OneArgFunction() {
            public LuaValue call(LuaValue cgValue) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                boolean[] selectedFlags = new boolean[cg.size()];
                cg.getSelectedFlags(selectedFlags);
                LuaValue flagTable = new LuaTable();
                for (int i = 0; i < selectedFlags.length; i++) {
                    flagTable.set(i + 1, LuaValue.valueOf(selectedFlags[i]));
                }
                return flagTable;
            }
        });

        // Установка флагов выбранности для MULTIPLE ChoiceGroup
        choiceGroup.set("setSelectedFlags", new TwoArgFunction() {
            public LuaValue call(LuaValue cgValue, LuaValue flagTable) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                boolean[] selectedFlags = new boolean[cg.size()];
                for (int i = 0; i < selectedFlags.length; i++) {
                    selectedFlags[i] = flagTable.get(i + 1).toboolean();
                }
                cg.setSelectedFlags(selectedFlags);
                return NONE;
            }
        });

        // Получение текста элемента по индексу
        choiceGroup.set("getString", new TwoArgFunction() {
            public LuaValue call(LuaValue cgValue, LuaValue index) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                return LuaValue.valueOf(cg.getString(index.checkint()));
            }
        });

        // Получение количества элементов в ChoiceGroup
        choiceGroup.set("size", new OneArgFunction() {
            public LuaValue call(LuaValue cgValue) {
                ChoiceGroup cg = (ChoiceGroup) cgValue.checktable().get("element").checkuserdata(ChoiceGroup.class);
                return LuaValue.valueOf(cg.size());
            }
        });
        
        choiceGroup.setmetatable(choiceGroup);
        return choiceGroup;
    }
}