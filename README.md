# javalua  
Lua terminal app for J2ME environment  

This app allows execution of Lua code on mobile devices supporting MIDLet.  

## Changes to Lua  

### `require`  
The `require()` function now requires the full path to the library or local path if opened via filebrowser

## Added Functions  

### HTTP and HTTPS Libraries  

Both libraries support `get` and `post` methods.  

#### Example  
```lua
uri = "http://example.com/"

code, answer = http.get(uri)
```

You can set additional headers by passing a table as the second argument:  
```lua
code, answer = http.get(uri, {["User-Agent"] = "Profile/MIDP-1.0 Configuration/CLDC-1.0"})
```

To use the `post` method, set the `Content-Type`:  
```lua
data = "SomeValue=value"
code, answer = http.post(uri, data, {["Content-Type"] = "application/x-www-form-urlencoded"})
```

### `io.dirs` and `io.mkdir`  

`io.dirs(path)` returns the list of files and folders at the specified absolute `path`.  

#### Example  
```lua
path = "somepath"
for k, v in pairs(io.dirs(path)) do
    print(v) -- Prints each file and folder in the path
end
```

`io.mkdir(path)` creates a new folder at the specified absolute `path`.  

### Global Functions: `sleep` and `clear`  

- `sleep(time)` pauses the program for the given duration in milliseconds.  
- `clear()` clears the screen.

---

# LCDUI  
Все функции находятся внутри пространства `UI`.

## Функции библиотеки

- `enableTerminal()` - Возвращает терминал для работы с интепретатором Lua

---

- `display(Displayable,Alternative)` - Отображает `Displayable`
 - `Displayable` - Элемент для отображения
 - `Alternative` - Опциональный элемент используемый для отображения после истечения таймера Alert, задается только если `Displayable` является `Alert` 

## Alert, Наследуется от Displayable  

### Методы  
- `new(title, atext, atype)` - Создает новый элемент Alert.  
  - `title` - Название.  
  - `atext` - Описание.  
  - `atype` - Тип оповещения (`AlertType`).  

### Константы  

- `ALARM` - Тип оповещения `AlertType.ALARM`.  
- `CONFIRMATION` - Тип оповещения `AlertType.CONFIRMATION`.  
- `ERROR` - Тип оповещения `AlertType.ERROR`.  
- `INFO` - Тип оповещения `AlertType.INFO`.  
- `WARNING` - Тип оповещения `AlertType.WARNING`.  
- `FOREVER` - Специальное значение для таймаута `Alert.FOREVER`.  

### Методы элемента  
- `getTimeout()` - Возвращает текущий таймаут для Alert.  
- `setTimeout(time)` - Устанавливает таймаут для Alert.  
  - `time` - Время в миллисекундах.  
- `setIndicator(indicator)` - Устанавливает индикатор для Alert.  
  - `indicator` - Элемент `Gauge`, который будет использоваться как индикатор.  
- `getIndicator()` - Возвращает текущий индикатор Alert.  
- `setString(text)` - Устанавливает текст для Alert.  
  - `text` - Строка, отображаемая в Alert.  
- `getString()` - Возвращает текущий текст Alert.

---

## FileChooser, Наследуется от Displayable
### Методы

- new(handler) - Создает новый элемент FileChooser и регистрирует обработчик выбора файла.
 - `handler` - Функция, вызываемая при выборе файла. Она принимает два аргумента:
 - `filePath` - Путь к выбранному файлу.
 - `directoryPath` - Путь к директории, содержащей выбранный файл.

---

## List, Наследуется от Displayable  

### Методы  
- `new(title, listType)` - Создает новый элемент List.  
  - `title` - Название списка.  
  - `listType` - Тип списка (`List.EXCLUSIVE`, `List.IMPLICIT`, `List.MULTIPLE`, `List.POPUP`).  

### Константы  

- `IMPLICIT` - Тип списка `List.IMPLICIT`.  
- `EXCLUSIVE` - Тип списка `List.EXCLUSIVE`.  
- `MULTIPLE` - Тип списка `List.MULTIPLE`.  
- `POPUP` - Тип списка `List.POPUP`.  

### Методы элемента  
- `append(string)` - Добавляет элемент в список.  
  - `string` - Текст элемента списка.  
- `setSelectCommand(command)` - Устанавливает команду выбора для списка.  
  - `command` - Элемент `Command`, используемый как команда выбора.  
- `delete(index)` - Удаляет элемент списка по индексу.  
  - `index` - Индекс удаляемого элемента.  
- `getSelectedIndex()` - Возвращает индекс выбранного элемента для списка типов `EXCLUSIVE` или `IMPLICIT`.  
- `getSelectedFlags()` - Возвращает таблицу булевых значений, указывающих, какие элементы выбраны, для списка типа `MULTIPLE`.  
- `getString(index)` - Возвращает текст элемента списка по индексу.  
  - `index` - Индекс элемента.  
- `clear()` - Удаляет все элементы из списка.

---

## Form, Наследуется от Displayable  

### Методы  
- `new(formname)` - Создает новый элемент Form.  
  - `formname` - Название формы.  

### Методы элемента  
- `append(element)` - Добавляет элемент в форму.  
  - `element` - Может быть строкой или элементом типа `Item`.  
- `deleteAll()` - Удаляет все элементы из формы.  
- `size()` - Возвращает количество элементов в форме.  
- `insert(index, element)` - Вставляет элемент в форму на определенную позицию.  
  - `index` - Индекс, на который нужно вставить элемент.  
  - `element` - Элемент типа `Item`. 

---

## TextBox, Наследуется от Displayable  

### Методы  
- `new(title, text, maxSize, constraints)` - Создает новый элемент TextBox.  
  - `title` - Название текстового поля.  
  - `text` - Текст по умолчанию (необязательный, по умолчанию пустая строка).  
  - `maxSize` - Максимальный размер текста (необязательный, по умолчанию 1024).  
  - `constraints` - Ограничения для ввода текста (необязательный, по умолчанию 0).  

### Методы элемента  
- `getText()` - Возвращает текущий текст из TextBox.  
- `setText(text)` - Устанавливает текст в TextBox.  
  - `text` - Новый текст для установки.

---

## Command  

### Методы  
- `new(commandName, commandType, commandPriority)` - Создает новый элемент Command.  
  - `commandName` - Название команды.  
  - `commandType` - Тип команды (например, `Command.OK`, `Command.CANCEL`).  
  - `commandPriority` - Приоритет команды.

### Константы  

- `BACK` - Тип команды `Command.BACK`.  
- `CANCEL` - Тип команды `Command.CANCEL`.  
- `EXIT` - Тип команды `Command.EXIT`.  
- `HELP` - Тип команды `Command.HELP`.  
- `ITEM` - Тип команды `Command.ITEM`.  
- `OK` - Тип команды `Command.OK`.  
- `SCREEN` - Тип команды `Command.SCREEN`.  
- `STOP` - Тип команды `Command.STOP`.  

### Методы элемента  
- `getType()` - Возвращает тип команды.  
- `getLabel()` - Возвращает название команды.  
- `equals(command)` - Проверяет равенство двух команд.  
  - `command` - Элемент `Command` для сравнения. Возвращает `true`, если команды равны, иначе `false`.     

---

## Gauge Наследуется от Item  

### Методы  

- `new(name, isInteractive, maxValue, defaultValue)`  
  Создает новый элемент `Gauge`.  
  - `name` - Название элемента.  
  - `isInteractive` - Булево значение, определяющее, интерактивен ли элемент.  
  - `maxValue` - Максимальное значение шкалы.  
  - `defaultValue` - (опционально) Начальное значение шкалы (по умолчанию: `0`).  

### Методы элемента  

- `setValue(value)` - Устанавливает текущее значение шкалы.  
  - `value` - Новое значение шкалы.  
- `getValue()` - Возвращает текущее значение шкалы.  

---

## ChoiceGroup Наследуется от Item  

### Методы  

- `new(label, choiceType)`  
  Создает новый элемент `ChoiceGroup`.  
  - `label` - Заголовок группы.  
  - `choiceType` - Тип выбора (например, `Choice.EXCLUSIVE`, `Choice.MULTIPLE`).  

### Константы  

- `IMPLICIT` - Тип списка `ChoiceGroup.IMPLICIT`.  
- `EXCLUSIVE` - Тип списка `ChoiceGroup.EXCLUSIVE`.  
- `MULTIPLE` - Тип списка `ChoiceGroup.MULTIPLE`.  
- `POPUP` - Тип списка `ChoiceGroup.POPUP`.  

### Методы элемента  

- `append(string)` - Добавляет новый элемент в группу.  
  - `string` - Текст элемента.  

- `delete(index)` - Удаляет элемент по индексу.  
  - `index` - Индекс удаляемого элемента.  

- `insert(index, string)` - Вставляет элемент на заданную позицию.  
  - `index` - Индекс позиции для вставки.  
  - `string` - Текст элемента.  

- `set(index, string)` - Изменяет текст элемента по индексу.  
  - `index` - Индекс изменяемого элемента.  
  - `string` - Новый текст.  

- `getSelectedIndex()` - Возвращает индекс выбранного элемента для типов `EXCLUSIVE` и `IMPLICIT`.  

- `setSelectedIndex(index)` - Устанавливает выбранный элемент по индексу.  
  - `index` - Индекс элемента для выбора.  

- `getSelectedFlags()` - Возвращает массив булевых значений, определяющих выбранность элементов (для типа `MULTIPLE`).  

- `setSelectedFlags(flags)` - Устанавливает выбранность элементов (для типа `MULTIPLE`).  
  - `flags` - Таблица булевых значений для установки.  

- `getString(index)` - Возвращает текст элемента по индексу.  
  - `index` - Индекс элемента.  

- `size()` - Возвращает количество элементов в группе. 

---

## TextField Наследуется от Item

### Методы  

- `new(name, text, maxSize, constraints)`  
  Создает новый элемент `TextField`.  
  - `name` - Название текстового поля.  
  - `text` - Текст, отображаемый по умолчанию.  
  - `maxSize` - Максимальная длина текста.  
  - `constraints` - (опционально) Ограничения ввода (по умолчанию: `0`).

### Константы  

- `ANY` - Ограничение `TextField.ANY`.  
- `CONSTRAINT_MASK` - Ограничение `TextField.CONSTRAINT_MASK`.  
- `EMAILADDR` - Ограничение `TextField.EMAILADDR`.  
- `NUMERIC` - Ограничение `TextField.NUMERIC`.  
- `PASSWORD` - Ограничение `TextField.PASSWORD`.  
- `PHONENUMBER` - Ограничение `TextField.PHONENUMBER`.  
- `URL` - Ограничение `TextField.URL`.  

### Методы элемента  

- `setText(text)` - Устанавливает текст в поле.  
  - `text` - Новый текст.  

- `getText()` - Возвращает текущий текст из поля.  

- `setMaxLength(length)` - Устанавливает максимальную длину текста.  
  - `length` - Максимальная длина.  

- `getMaxLength()` - Возвращает текущую максимальную длину текста.  

- `getCaretPosition()` - Возвращает текущую позицию каретки (курсор).  

- `setInputConstraints(constraints)` - Устанавливает ограничения ввода.  
  - `constraints` - Новые ограничения.  

- `getInputConstraints()` - Возвращает текущие ограничения ввода.  

---

## StringItem Наследуется от Item

### Методы  

- `new(label, text, constraints)`  
  Создает новый элемент `StringItem`.  
  - `label` - Метка для текста.  
  - `text` - Отображаемый текст.  
  - `constraints` - (опционально) Режим отображения (по умолчанию: `0`).  

### Методы элемента  

- `getAppearanceMode()` - Возвращает текущий режим отображения элемента.  

- `setText(text)` - Устанавливает текст элемента.  
  - `text` - Новый текст.  

- `getText()` - Возвращает текущий текст элемента.  

---

## Displayable  

### Добавляемые методы  

- `addCommand(command)`  
  Добавляет команду к элементу `Displayable`.  
  - `command` - Объект `Command`.  

- `setCommandHandler(handler)`  
  Устанавливает обработчик команд для элемента `Displayable`.  
  - `handler` - Lua-функция для обработки событий команд.

	```
    	--Пример функции для обработки событий
		function a(Command, Displayable)

		end

	```


- `removeCommand(command)`  
  Удаляет указанную команду из элемента `Displayable`.  
  - `command` - Объект `Command`.  

- `getTitle()`  
  Возвращает заголовок элемента `Displayable`.  

- `equals(otherDisplayable)`  
  Проверяет, равны ли два элемента `Displayable`.  
  - `otherDisplayable` - Второй элемент для сравнения.  

---

## Item  

### Добавляемые методы  

- `setDefaultCommand(command)`  
  Устанавливает команду по умолчанию для элемента `Item`.  
  - `command` - Объект `Command`.  

- `setCommandListener(listener)`  
  Устанавливает обработчик команд для элемента `Item`.  
  - `listener` - Lua-функция для обработки команд.  

	```
    	--Пример функции для обработки событий
		function a(Command, Item)

		end

	```

- `getClassName()`  
  Возвращает имя класса элемента `Item`.  

- `getLayout()`  
  Возвращает текущую разметку элемента `Item`.  

- `setLayout(layout)`  
  Устанавливает разметку для элемента `Item`.  
  - `layout` - Целое число, определяющее разметку.  

- `getLabel()`  
  Возвращает текущую метку элемента `Item`.  

- `setLabel(label)`  
  Устанавливает метку для элемента `Item`.  
  - `label` - Новая метка.  

- `equals(otherItem)`  
  Проверяет, равны ли два элемента `Item`.  
  - `otherItem` - Второй элемент для сравнения. 

### Константы
- `BUTTON` - Тип элемента `Item.BUTTON`.  
- `HYPERLINK` - Тип элемента `Item.HYPERLINK`.  
- `LAYOUT_2` - Разметка `Item.LAYOUT_2`.  
- `LAYOUT_BOTTOM` - Разметка `Item.LAYOUT_BOTTOM`.  
- `LAYOUT_CENTER` - Разметка `Item.LAYOUT_CENTER`.  
- `LAYOUT_DEFAULT` - Разметка `Item.LAYOUT_DEFAULT`.  
- `LAYOUT_EXPAND` - Разметка `Item.LAYOUT_EXPAND`.  
- `LAYOUT_LEFT` - Разметка `Item.LAYOUT_LEFT`.  
- `LAYOUT_NEWLINE_AFTER` - Разметка `Item.LAYOUT_NEWLINE_AFTER`.  
- `LAYOUT_NEWLINE_BEFORE` - Разметка `Item.LAYOUT_NEWLINE_BEFORE`.  
- `LAYOUT_RIGHT` - Разметка `Item.LAYOUT_RIGHT`.  
- `LAYOUT_SHRINK` - Разметка `Item.LAYOUT_SHRINK`.  
- `LAYOUT_TOP` - Разметка `Item.LAYOUT_TOP`.  
- `LAYOUT_VCENTER` - Разметка `Item.LAYOUT_VCENTER`.  
- `LAYOUT_VEXPAND` - Разметка `Item.LAYOUT_VEXPAND`.  
- `LAYOUT_VSHRINK` - Разметка `Item.LAYOUT_VSHRINK`.  
- `PLAIN` - Тип элемента `Item.PLAIN`.  
