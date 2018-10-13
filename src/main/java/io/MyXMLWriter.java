package io;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.OutputStream;

import static conf.Configuration.TAG_END_PAGES;
import static conf.Configuration.TAG_PAGES;

public class MyXMLWriter extends XMLWriter {

    public MyXMLWriter(OutputStream out, OutputFormat format) throws IOException {
        super(out, format);
        start();
    }

    private void start() throws IOException {
        this.writer.write(TAG_PAGES);
        this.writePrintln();
        this.flush();
    }

    private void end() throws IOException {
        this.writePrintln();
        this.writer.write(TAG_END_PAGES);
        this.flush();
    }

    @Override
    synchronized public void write(Element element) throws IOException {
        super.write(element);
    }

    @Override
    public void close() throws IOException {
        end();
        super.close();
    }
}
