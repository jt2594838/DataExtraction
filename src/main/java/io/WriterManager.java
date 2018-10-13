package io;

import org.dom4j.io.OutputFormat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WriterManager {

    private Map<String, MyXMLWriter> writerMap = new ConcurrentHashMap<>();

    private static WriterManager INSTANCE = new WriterManager();

    public static WriterManager getInstance() {
        return INSTANCE;
    }

    public MyXMLWriter getWriter(String path) throws IOException {
        MyXMLWriter writer = writerMap.get(path);
        if (writer == null) {
            synchronized (path.intern()){
                writer = writerMap.get(path);
                if (writer == null) {
                    new File(path).delete();
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(path, true));
                    writer = new MyXMLWriter(outputStream, OutputFormat.createPrettyPrint());
                    writerMap.putIfAbsent(path, writer);
                }
            }
        }
        return writerMap.get(path);
    }

    public void closeAll() throws IOException {
        for(MyXMLWriter writer : writerMap.values())
            writer.close();
    }
}
