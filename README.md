# javalua
Lua terminal app for J2ME enviroment

This app allows to execute Lua code on the mobile devices with MIDLet's support

## Changes to lua

### `require`
`require()` need full path to library

### `io.input`
`io.input()` requires callback function
#### Example
```
function printvalue(str)
    print("You Writed: "..str)
end

io.input(printvalue)
```

## Added functions

### `http` and `https` library
Both have `get` and `post` methods

#### Example
```
uri = "http://example.com/"

code, answer = http.get(uri)
```
You can set parameters by passing table as second argument
```
code, answer = http.get(uri, {["User-Agent"] = "Profile/MIDP-1.0 Configuration/CLDC-1.0"})
 
```
You must set Content-Type to use post methods

```
data = "SomeValue=value"
code, answer = http.get(uris, data, {["Content-Type"] = "application/x-www-form-urlencoded"})
```

### `dirs` and `mkdir` in `io`
`io.dirs(path)` returns files and folders in `path`, where `path` is absolute path

#### Example
```
path = "somepath"
for k,v in pairs(io.dirs(path)) do
    print(v) --prints every file and folder in path
end
```
`io.mkdir(path)` creates new folder in `path`, where `path` is absolute path

### `sleep` and `clear`
Global functions

`sleep(time)` causes the program to go to sleep in the amount of time specified in ms

`clear()` cleans the screen