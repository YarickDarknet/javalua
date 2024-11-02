package javalua;

import javax.microedition.lcdui.*;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.util.Enumeration;
import javax.microedition.io.file.FileSystemRegistry;

public class FileChooser {
    private Display display;
    private List fileMenu;
    private Displayable defaultForm;
    private Command select;
    private Command exit;
    private String currentDirectory;  // Начальная директория
    private FileSelectionHandler selectionHandler; // Обработчик выбора файла
    
    // Интерфейс для обработчика выбора файла
    public interface FileSelectionHandler {
        void onFileSelected(String filePath, String directoryPath);
    }

    // Конструктор
    public FileChooser(Display display, FileSelectionHandler handler) {
        this.display = display;
        this.selectionHandler = handler;
        fileMenu = new List("Выберите файл", List.IMPLICIT);
        select = new  Command("Выбрать",Command.OK,0);
        exit = new  Command("Выйти",Command.BACK,0);
        
        //fileMenu.addCommand(select);
        fileMenu.addCommand(exit);
        fileMenu.setSelectCommand(select);
        fileMenu.setCommandListener(new CommandListener(){
            public void commandAction(Command c, Displayable d){
                if(c == select && d == fileMenu){
                    handleSelection(fileMenu.getSelectedIndex());
                }
                if(c == exit && d == fileMenu){
                    hide();
                }
            }
        });
        
        currentDirectory = "file:///";
        listFiles(currentDirectory);
    }

    // Отображение меню
    public void show() {
        defaultForm = display.getCurrent();
        display.setCurrent(fileMenu);
    }
    
    private void hide() {
        display.setCurrent(defaultForm);
    }

    // Метод для отображения файлов и директорий из текущей папки
    private void listFiles(String directory) {
        fileMenu.deleteAll(); // Очищаем меню перед загрузкой списка файлов
        try {
            FileConnection fc;
            Enumeration files;

            // Добавляем пункт для возврата в родительскую директорию
            if (!directory.equals("file:///")) {
                fileMenu.append("..", null);
                fc = (FileConnection) Connector.open(directory);
                files = fc.list();
                fc.close();
            }else{
                files = FileSystemRegistry.listRoots();
            }

            // Перебираем файлы и папки
            while (files.hasMoreElements()) {
                String fileName = (String) files.nextElement();
                fileMenu.append(fileName, null);
            }

            // Закрываем соединение
        } catch (IOException e) {
            Alert alert = new Alert("Ошибка", "Не удалось загрузить файлы: " + e.getMessage(), null, AlertType.ERROR);
            alert.setTimeout(5000);
            display.setCurrent(alert);
        }
    }

    // Обработка выбора элемента меню
    public void handleSelection(int selectedIndex) {
        String selectedFile = fileMenu.getString(selectedIndex);

        if (selectedFile.equals("..")) {
            // Переход в родительскую директорию
            int lastSlash = currentDirectory.lastIndexOf('/', currentDirectory.length() - 2);
            currentDirectory = currentDirectory.substring(0, lastSlash + 1);
            listFiles(currentDirectory);
        } else {
            // Переход в выбранную папку или выбор файла
            String newPath = currentDirectory + selectedFile;
            try {
                FileConnection fc = (FileConnection) Connector.open(newPath);
                if (fc.isDirectory()) {
                    // Если это папка, то открываем её
                    currentDirectory = newPath;
                    listFiles(currentDirectory);
                } else {
                    // Если это файл, то вызываем обработчик выбора файла
                    if (selectionHandler != null) {
                        selectionHandler.onFileSelected(newPath, currentDirectory);
                    }
                }
                fc.close();
            } catch (IOException e) {
                Alert alert = new Alert("Ошибка", "Не удалось открыть файл/папку: " + e.getMessage(), null, AlertType.ERROR);
                alert.setTimeout(5000);
                display.setCurrent(alert);
            }
        }
    }
}
