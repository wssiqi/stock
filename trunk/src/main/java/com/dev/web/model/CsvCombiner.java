package com.dev.web.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CsvCombiner {

    private static final int BUFFER_64K = 64 * 1024;
    private static final Logger LOGGER = Logger.getLogger(CsvCombiner.class);
    private static final char EXTENSION_SEPERATOR = '.';
    private Charset charset;

    public static void main(String[] args) {
        new CsvCombiner().setCharset("gbk1");
    }

    public void setCharset(String charsetName) {
        try {
            charset = Charset.forName(charsetName);
        } catch (Exception e) {
            charset = Charset.defaultCharset();
            LOGGER.error(String.format("Illegal charset name '%s'.", charsetName), e);
        }
        LOGGER.info(String.format("Use charset '%s'.", charset.name()));
    }

    public void combine(List<File> csvFiles, File saveToFile) {
        checkParameters(csvFiles);
        checkParameters(saveToFile);

        BufferedReader bufferReader = null;
        BufferedWriter bufferWriter = null;
        try {
            bufferWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveToFile), charset),
                    BUFFER_64K);
            if (!csvFiles.isEmpty()) {
                Iterator<File> iterator = csvFiles.iterator();
                File firstFile = iterator.next();
                bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(firstFile), charset),
                        BUFFER_64K);
                copyLineFromReaderToWriter(bufferReader, bufferWriter);
                IOUtils.closeQuietly(bufferReader);
                while (iterator.hasNext()) {
                    bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(firstFile), charset),
                            BUFFER_64K);
                    bufferReader.readLine();// skip title line
                    copyLineFromReaderToWriter(bufferReader, bufferWriter);
                    IOUtils.closeQuietly(bufferReader);
                }

            }

        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(bufferReader);
            IOUtils.closeQuietly(bufferWriter);
        }

    }

    private void copyLineFromReaderToWriter(BufferedReader bufferReader, BufferedWriter bufferWriter)
            throws IOException {
        String line = null;
        while ((line = bufferReader.readLine()) != null) {
            if (StringUtils.trim(line).isEmpty()) {
                continue;
            }
            bufferWriter.write(line);
            bufferWriter.newLine();
        }
    }

    private void checkParameters(List<File> csvFiles) {
        if (csvFiles == null) {
            throw new RuntimeException("csvFiles is null");
        }
        for (File csvFile : csvFiles) {
            checkParameters(csvFile);
        }
    }

    private void checkParameters(File csvFile) {
        String fileExtension = getFileExtension(csvFile);
        if (!fileExtension.equalsIgnoreCase(".csv")) {
            throw new RuntimeException(String.format("'%s' is not a csv file, wrong file extension.",
                    getAbsolutePath(csvFile)));
        }
    }

    private String getAbsolutePath(File file) {
        if (file == null) {
            return null;
        }
        try {
            return file.getAbsolutePath();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return file.getPath();
        }
    }

    private String getFileExtension(File file) {
        if (file == null) {
            return "";
        }
        String fileName = file.getName();
        int extensionSeperatorIndex = fileName.lastIndexOf(EXTENSION_SEPERATOR);
        if (extensionSeperatorIndex == -1) {
            return "";
        }
        return fileName.substring(extensionSeperatorIndex + 1);
    }

}
