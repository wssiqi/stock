package com.stock.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.dev.web.CommUtils;

public class CsvCombiner {

	private static final int BUFFER_64K = 64 * 1024;
	private static final Logger LOGGER = Logger.getLogger(CsvCombiner.class);
	private Charset charsetForRead = Charset.defaultCharset();
	private Charset charsetForWrite = Charset.defaultCharset();

	public void setCharsetForRead(Charset charsetForRead) {
		this.charsetForRead = charsetForRead;
	}

	public void setCharsetForWrite(Charset charsetForWrite) {
		this.charsetForWrite = charsetForWrite;
	}

	public void combine(List<File> csvFiles, File saveToFile) {
		checkParameters(csvFiles);
		checkParameters(saveToFile);

		BufferedReader bufferReader = null;
		BufferedWriter bufferWriter = null;
		try {
			bufferWriter = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(saveToFile), charsetForWrite),
					BUFFER_64K);
			if (!csvFiles.isEmpty()) {
				Iterator<File> iterator = csvFiles.iterator();
				File firstFile = iterator.next();
				LOGGER.info(firstFile.getName());
				bufferReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(firstFile), charsetForRead),
						BUFFER_64K);
				copyLineFromReaderToWriter(bufferReader, bufferWriter);
				IOUtils.closeQuietly(bufferReader);
				while (iterator.hasNext()) {
					File nextCsvFile = iterator.next();
					LOGGER.info(nextCsvFile.getName());
					bufferReader = new BufferedReader(new InputStreamReader(
							new FileInputStream(nextCsvFile), charsetForRead),
							BUFFER_64K);
					bufferReader.readLine();// skip title line
					copyLineFromReaderToWriter(bufferReader, bufferWriter);
					IOUtils.closeQuietly(bufferReader);
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(bufferReader);
			IOUtils.closeQuietly(bufferWriter);
		}

	}

	private void copyLineFromReaderToWriter(BufferedReader bufferReader,
			BufferedWriter bufferWriter) throws IOException {
		String line = null;
		while ((line = bufferReader.readLine()) != null) {
			if (StringUtils.trim(line).isEmpty() || line.contains("没有相关")) {
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
		String fileExtension = CommUtils.getFileExtension(csvFile);
		if (!fileExtension.equalsIgnoreCase("csv")) {
			throw new RuntimeException(String.format(
					"'%s' is not a csv file, wrong file extension.",
					CommUtils.getAbsolutePath(csvFile)));
		}
	}

}
