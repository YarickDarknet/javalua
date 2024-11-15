# LCDUI  
All functions are located within the `UI` namespace.

## Library Functions  

- `enableTerminal()`  
  Returns a terminal for working with the Lua interpreter.

---

- `display(Displayable, Alternative)`  
  Displays a `Displayable`.  
  - `Displayable`: The element to display.  
  - `Alternative`: An optional element displayed after an `Alert` timer expires. Used only if `Displayable` is an `Alert`.

---

## Alert (inherits from Displayable)  

### Methods  

- `new(title, atext, atype)`  
  Creates a new `Alert` element.  
  - `title`: The title of the alert.  
  - `atext`: The alert description.  
  - `atype`: The alert type (`AlertType`).  

### Constants  

- `ALARM`: Alert type `AlertType.ALARM`.  
- `CONFIRMATION`: Alert type `AlertType.CONFIRMATION`.  
- `ERROR`: Alert type `AlertType.ERROR`.  
- `INFO`: Alert type `AlertType.INFO`.  
- `WARNING`: Alert type `AlertType.WARNING`.  
- `FOREVER`: Special value for `Alert.FOREVER` timeout.  

### Element Methods  

- `getTimeout()`: Returns the current timeout for the `Alert`.  
- `setTimeout(time)`: Sets the timeout for the `Alert`.  
  - `time`: Timeout in milliseconds.  
- `setIndicator(indicator)`: Sets an indicator for the `Alert`.  
  - `indicator`: A `Gauge` element used as the indicator.  
- `getIndicator()`: Returns the current indicator of the `Alert`.  
- `setString(text)`: Sets the text for the `Alert`.  
  - `text`: The text to display.  
- `getString()`: Returns the current text of the `Alert`.  

---

## FileChooser (inherits from Displayable)  

### Methods  

- `new(handler)`  
  Creates a new `FileChooser` element and registers a file selection handler.  
  - `handler`: A function called upon file selection. It receives two arguments:  
    - `filePath`: The selected file path.  
    - `directoryPath`: The directory containing the selected file.  

---

## List (inherits from Displayable)  

### Methods  

- `new(title, listType)`  
  Creates a new `List` element.  
  - `title`: The list title.  
  - `listType`: The list type (`List.EXCLUSIVE`, `List.IMPLICIT`, `List.MULTIPLE`, `List.POPUP`).  

### Constants  

- `IMPLICIT`: List type `List.IMPLICIT`.  
- `EXCLUSIVE`: List type `List.EXCLUSIVE`.  
- `MULTIPLE`: List type `List.MULTIPLE`.  
- `POPUP`: List type `List.POPUP`.  

### Element Methods  

- `append(string)`: Adds an item to the list.  
  - `string`: The text of the list item.  
- `setSelectCommand(command)`: Sets the selection command for the list.  
  - `command`: A `Command` element.  
- `delete(index)`: Deletes a list item by index.  
  - `index`: The index of the item to delete.  
- `getSelectedIndex()`: Returns the index of the selected item for `EXCLUSIVE` or `IMPLICIT` lists.  
- `getSelectedFlags()`: Returns a table of boolean values indicating selected items for `MULTIPLE` lists.  
- `getString(index)`: Returns the text of the list item at the specified index.  
  - `index`: The index of the item.  
- `clear()`: Removes all items from the list.  

---

## Form (inherits from Displayable)  

### Methods  

- `new(formname)`  
  Creates a new `Form` element.  
  - `formname`: The name of the form.  

### Element Methods  

- `append(element)`: Adds an element to the form.  
  - `element`: Can be a string or an `Item`.  
- `deleteAll()`: Removes all elements from the form.  
- `size()`: Returns the number of elements in the form.  
- `insert(index, element)`: Inserts an element into the form at the specified position.  
  - `index`: The position index.  
  - `element`: An `Item`.  

---

## TextBox (inherits from Displayable)  

### Methods  

- `new(title, text, maxSize, constraints)`  
  Creates a new `TextBox` element.  
  - `title`: The title of the text box.  
  - `text`: Default text (optional, default is an empty string).  
  - `maxSize`: Maximum text size (optional, default is 1024).  
  - `constraints`: Text input constraints (optional, default is 0).  

### Element Methods  

- `getText()`: Returns the current text from the `TextBox`.  
- `setText(text)`: Sets the text in the `TextBox`.  
  - `text`: The text to set.  

---

## Command  

### Methods  

- `new(commandName, commandType, commandPriority)`  
  Creates a new `Command` element.  
  - `commandName`: The name of the command.  
  - `commandType`: The type of command (e.g., `Command.OK`, `Command.CANCEL`).  
  - `commandPriority`: The priority of the command.  

### Constants  

- `BACK`, `CANCEL`, `EXIT`, `HELP`, `ITEM`, `OK`, `SCREEN`, `STOP`: Command types.  

### Element Methods  

- `getType()`: Returns the command type.  
- `getLabel()`: Returns the command name.  
- `equals(command)`: Checks equality of two `Command` elements.  
  - `command`: Another `Command` for comparison.  

---

## Gauge (inherits from Item)  

### Methods  

- `new(name, isInteractive, maxValue, defaultValue)`  
  Creates a new `Gauge` element.  
  - `name`: The element name.  
  - `isInteractive`: Boolean indicating if the element is interactive.  
  - `maxValue`: The maximum value of the gauge.  
  - `defaultValue`: (Optional) The initial value (default: `0`).  
