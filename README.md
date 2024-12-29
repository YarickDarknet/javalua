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

### `util.u16`

#### `util.u16.makeString(array)`
  Creates a Unicode-16 string from an array of numbers.
  - `array` - A table containing numbers representing Unicode code points.

#### `util.u16.dumpString(string)`
  Returns an array of numbers representing the Unicode code points of a given string.
  - `string` - The input string.

### `json`

#### `json.encode(value)`
  Encodes a Lua value into a JSON string.
  - `value` - The Lua value to be encoded (can be table, string, number, boolean or nil).

#### `json.decode(string)`
  Decodes a JSON string into a Lua value.
  - `string` - The JSON string to be decoded.

#### `json.encodefile(filepath, value)`
  Encodes a Lua value into a JSON string and saves it to a file.
  - `filepath` - The path to the file where the JSON data will be saved.
  - `value` - The Lua value to be encoded.

#### `json.decodefile(filepath)`
  Decodes a JSON string from a file and returns it as a Lua value.
  - `filepath` - The path to the file containing the JSON data.

---

# LCDUI
All functions are within the `UI` namespace.

## Library Functions

- `enableTerminal()` - Returns the terminal object for interacting with the Lua interpreter.

---

- `display(Displayable, Alternative)` - Displays a `Displayable`.
  - `Displayable` - The element to be displayed.
  - `Alternative` - Optional element used for display after an Alert timer expires, only used if `Displayable` is an `Alert`.

## Alert, Inherits from Displayable

### Methods
- `new(title, atext, atype)` - Creates a new Alert element.
  - `title` - The title.
  - `atext` - The description.
  - `atype` - The alert type (`AlertType`).

### Constants

- `ALARM` - Alert type `AlertType.ALARM`.
- `CONFIRMATION` - Alert type `AlertType.CONFIRMATION`.
- `ERROR` - Alert type `AlertType.ERROR`.
- `INFO` - Alert type `AlertType.INFO`.
- `WARNING` - Alert type `AlertType.WARNING`.
- `FOREVER` - Special value for Alert timeout `Alert.FOREVER`.

### Element Methods
- `getTimeout()` - Returns the current timeout for the Alert.
- `setTimeout(time)` - Sets the timeout for the Alert.
  - `time` - Time in milliseconds.
- `setIndicator(indicator)` - Sets the indicator for the Alert.
  - `indicator` - A `Gauge` element that will be used as an indicator.
- `getIndicator()` - Returns the current indicator of the Alert.
- `setString(text)` - Sets the text for the Alert.
  - `text` - The string to be displayed in the Alert.
- `getString()` - Returns the current text of the Alert.

---

## FileChooser, Inherits from Displayable
### Methods

- `new(handler)` - Creates a new FileChooser element and registers a file selection handler.
  - `handler` - A function called when a file is selected. It accepts two arguments:
   - `filePath` - The path to the selected file.
   - `directoryPath` - The path to the directory containing the selected file.

---

## List, Inherits from Displayable

### Methods
- `new(title, listType)` - Creates a new List element.
  - `title` - The title of the list.
  - `listType` - The type of list (`List.EXCLUSIVE`, `List.IMPLICIT`, `List.MULTIPLE`, `List.POPUP`).

### Constants

- `IMPLICIT` - List type `List.IMPLICIT`.
- `EXCLUSIVE` - List type `List.EXCLUSIVE`.
- `MULTIPLE` - List type `List.MULTIPLE`.
- `POPUP` - List type `List.POPUP`.

### Element Methods
- `append(string)` - Adds an element to the list.
  - `string` - The text of the list item.
- `setSelectCommand(command)` - Sets the selection command for the list.
  - `command` - A `Command` element used as a selection command.
- `delete(index)` - Deletes a list element by its index.
  - `index` - The index of the element to be deleted.
- `getSelectedIndex()` - Returns the index of the selected element for list types `EXCLUSIVE` or `IMPLICIT`.
- `getSelectedFlags()` - Returns a table of boolean values indicating which elements are selected for a `MULTIPLE` type list.
- `getString(index)` - Returns the text of the list element by its index.
  - `index` - The index of the element.
- `clear()` - Removes all elements from the list.

---

## Form, Inherits from Displayable

### Methods
- `new(formname)` - Creates a new Form element.
  - `formname` - The name of the form.

### Element Methods
- `append(element)` - Adds an element to the form.
  - `element` - Can be a string or an `Item` type element.
- `deleteAll()` - Removes all elements from the form.
- `size()` - Returns the number of elements in the form.
- `insert(index, element)` - Inserts an element into the form at a specific position.
  - `index` - The index at which to insert the element.
  - `element` - An `Item` type element.

---

## TextBox, Inherits from Displayable

### Methods
- `new(title, text, maxSize, constraints)` - Creates a new TextBox element.
  - `title` - The title of the text box.
  - `text` - The default text (optional, defaults to an empty string).
  - `maxSize` - The maximum text size (optional, defaults to 1024).
  - `constraints` - The input constraints (optional, defaults to 0).

### Element Methods
- `getText()` - Returns the current text from the TextBox.
- `setText(text)` - Sets the text in the TextBox.
  - `text` - The new text to set.

---

## Command

### Methods
- `new(commandName, commandType, commandPriority)` - Creates a new Command element.
  - `commandName` - The name of the command.
  - `commandType` - The type of command (e.g., `Command.OK`, `Command.CANCEL`).
  - `commandPriority` - The priority of the command.

### Constants

- `BACK` - Command type `Command.BACK`.
- `CANCEL` - Command type `Command.CANCEL`.
- `EXIT` - Command type `Command.EXIT`.
- `HELP` - Command type `Command.HELP`.
- `ITEM` - Command type `Command.ITEM`.
- `OK` - Command type `Command.OK`.
- `SCREEN` - Command type `Command.SCREEN`.
- `STOP` - Command type `Command.STOP`.

### Element Methods
- `getType()` - Returns the type of the command.
- `getLabel()` - Returns the name of the command.
- `equals(command)` - Checks if two commands are equal.
  - `command` - The `Command` element to compare with. Returns `true` if commands are equal, otherwise `false`.

---

## Gauge Inherits from Item

### Methods

- `new(name, isInteractive, maxValue, defaultValue)`
   Creates a new `Gauge` element.
  - `name` - The name of the element.
  - `isInteractive` - A boolean value defining if element is interactive.
  - `maxValue` - The maximum value of the scale.
  - `defaultValue` - (optional) The initial value of the scale (defaults to: `0`).

### Element Methods

- `setValue(value)` - Sets the current value of the scale.
  - `value` - The new value of the scale.
- `getValue()` - Returns the current value of the scale.

---

## ChoiceGroup Inherits from Item

### Methods

- `new(label, choiceType)`
   Creates a new `ChoiceGroup` element.
   - `label` - The title of the group.
   - `choiceType` - The type of choice (e.g., `Choice.EXCLUSIVE`, `Choice.MULTIPLE`).

### Constants

- `IMPLICIT` - List type `ChoiceGroup.IMPLICIT`.
- `EXCLUSIVE` - List type `ChoiceGroup.EXCLUSIVE`.
- `MULTIPLE` - List type `ChoiceGroup.MULTIPLE`.
- `POPUP` - List type `ChoiceGroup.POPUP`.

### Element Methods

- `append(string)` - Adds a new element to the group.
  - `string` - The text of the element.

- `delete(index)` - Deletes an element by index.
  - `index` - The index of the element to delete.

- `insert(index, string)` - Inserts an element at the specified position.
  - `index` - The index of the position to insert at.
  - `string` - The text of the element.

- `set(index, string)` - Changes the text of an element at the specified index.
  - `index` - The index of the element to change.
  - `string` - The new text.

- `getSelectedIndex()` - Returns the index of the selected element for types `EXCLUSIVE` and `IMPLICIT`.

- `setSelectedIndex(index)` - Sets the selected element by index.
  - `index` - The index of the element to select.

- `getSelectedFlags()` - Returns an array of boolean values indicating which elements are selected (for type `MULTIPLE`).

- `setSelectedFlags(flags)` - Sets which elements are selected (for type `MULTIPLE`).
  - `flags` - Table of boolean values for the setup.

- `getString(index)` - Returns the text of an element at the specified index.
  - `index` - The index of the element.

- `size()` - Returns the number of elements in the group.

---

## TextField Inherits from Item

### Methods

- `new(name, text, maxSize, constraints)`
  Creates a new `TextField` element.
  - `name` - The name of the text field.
  - `text` - The text displayed by default.
  - `maxSize` - The maximum length of the text.
  - `constraints` - (optional) Input constraints (defaults to: `0`).

### Constants

- `ANY` - Constraint `TextField.ANY`.
- `CONSTRAINT_MASK` - Constraint `TextField.CONSTRAINT_MASK`.
- `EMAILADDR` - Constraint `TextField.EMAILADDR`.
- `NUMERIC` - Constraint `TextField.NUMERIC`.
- `PASSWORD` - Constraint `TextField.PASSWORD`.
- `PHONENUMBER` - Constraint `TextField.PHONENUMBER`.
- `URL` - Constraint `TextField.URL`.

### Element Methods

- `setText(text)` - Sets the text in the field.
  - `text` - The new text.

- `getText()` - Returns the current text in the field.

- `setMaxLength(length)` - Sets the maximum text length.
  - `length` - The maximum length.

- `getMaxLength()` - Returns the current maximum text length.

- `getCaretPosition()` - Returns the current position of the caret (cursor).

- `setInputConstraints(constraints)` - Sets the input constraints.
  - `constraints` - The new constraints.

- `getInputConstraints()` - Returns the current input constraints.

---

## StringItem Inherits from Item

### Methods

- `new(label, text, constraints)`
  Creates a new `StringItem` element.
  - `label` - The label for the text.
  - `text` - The text to display.
  - `constraints` - (optional) Display mode (defaults to: `0`).

### Element Methods

- `getAppearanceMode()` - Returns the current display mode of the element.

- `setText(text)` - Sets the text of the element.
  - `text` - The new text.

- `getText()` - Returns the current text of the element.

---

## Displayable

### Added Methods

- `addCommand(command)`
  Adds a command to the `Displayable` element.
  - `command` - A `Command` object.

- `setCommandHandler(handler)`
  Sets the command handler for the `Displayable` element.
  - `handler` - A Lua function for handling command events.

    ```lua
    	--Example function for handling events
		function a(Command, Displayable)

		end

	```

- `removeCommand(command)`
  Removes the specified command from the `Displayable` element.
  - `command` - A `Command` object.

- `getTitle()`
  Returns the title of the `Displayable` element.

- `equals(otherDisplayable)`
  Checks if two `Displayable` elements are equal.
  - `otherDisplayable` - The second element for comparison.

---

## Item

### Added Methods

- `setDefaultCommand(command)`
  Sets the default command for the `Item` element.
  - `command` - A `Command` object.

- `setCommandListener(listener)`
  Sets the command handler for the `Item` element.
  - `listener` - A Lua function for handling command events.

	```lua
    	--Example function for handling events
		function a(Command, Item)

		end

	```

- `getClassName()`
  Returns the class name of the `Item` element.

- `getLayout()`
  Returns the current layout of the `Item` element.

- `setLayout(layout)`
  Sets the layout for the `Item` element.
  - `layout` - An integer defining the layout.

- `getLabel()`
  Returns the current label of the `Item` element.

- `setLabel(label)`
  Sets the label for the `Item` element.
  - `label` - The new label.

- `equals(otherItem)`
  Checks if two `Item` elements are equal.
  - `otherItem` - The second element for comparison.

### Constants
- `BUTTON` - Element type `Item.BUTTON`.
- `HYPERLINK` - Element type `Item.HYPERLINK`.
- `LAYOUT_2` - Layout `Item.LAYOUT_2`.
- `LAYOUT_BOTTOM` - Layout `Item.LAYOUT_BOTTOM`.
- `LAYOUT_CENTER` - Layout `Item.LAYOUT_CENTER`.
- `LAYOUT_DEFAULT` - Layout `Item.LAYOUT_DEFAULT`.
- `LAYOUT_EXPAND` - Layout `Item.LAYOUT_EXPAND`.
- `LAYOUT_LEFT` - Layout `Item.LAYOUT_LEFT`.
- `LAYOUT_NEWLINE_AFTER` - Layout `Item.LAYOUT_NEWLINE_AFTER`.
- `LAYOUT_NEWLINE_BEFORE` - Layout `Item.LAYOUT_NEWLINE_BEFORE`.
- `LAYOUT_RIGHT` - Layout `Item.LAYOUT_RIGHT`.
- `LAYOUT_SHRINK` - Layout `Item.LAYOUT_SHRINK`.
- `LAYOUT_TOP` - Layout `Item.LAYOUT_TOP`.
- `LAYOUT_VCENTER` - Layout `Item.LAYOUT_VCENTER`.
- `LAYOUT_VEXPAND` - Layout `Item.LAYOUT_VEXPAND`.
- `LAYOUT_VSHRINK` - Layout `Item.LAYOUT_VSHRINK`.
- `PLAIN` - Element type `Item.PLAIN`.
