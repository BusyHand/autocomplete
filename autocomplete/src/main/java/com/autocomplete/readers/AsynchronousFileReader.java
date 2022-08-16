package com.autocomplete.readers;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;

/*
 * Class provides opportunity to use
 * The most faster file reader
 */
public class AsynchronousFileReader {

	/*
	 * High file reading speed is achieved through the use of asynchronous read
	 */
	public void readAndDo(ResourcesTemplate<AsynchronousFileChannel> openChannel) {
		Path path = Path.of("src", "main", "resources", "airports.csv");
		
		try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
			openChannel.excecute(fileChannel);
		} catch (ExecutionException | InterruptedException | IOException e) {
			e.printStackTrace();// =)
		}
	}
}
