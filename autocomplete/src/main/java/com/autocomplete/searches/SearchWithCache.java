package com.autocomplete.searches;

import static com.autocomplete.cache.Cache.addFromCache;
import static com.autocomplete.cache.Cache.cache;
import static com.autocomplete.props.Indicators.DELIMITR;
import static com.autocomplete.props.Indicators.NEXT_LINE;
import static com.autocomplete.props.SearchParameters.column;
import static com.autocomplete.props.SearchParameters.input;
import static com.autocomplete.props.SearchParameters.isNumbers;
import static com.autocomplete.searches.SearchResults.addResult;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Future;

import com.autocomplete.props.BenchmarkProp;
import com.autocomplete.readers.AsynchronousFileReader;

/*
 * One of the main classes
 * Which implement the autocomplete algorithm by 
 * Use cached and parse bytes of file
 * 
 */
public class SearchWithCache {

	AsynchronousFileReader reader = new AsynchronousFileReader();

	/*
	 * Search algorithm: 1. Check firsts lines in cached and skip if they exist 2.
	 * Find needed lines by parsing 3. Check another's line which go after needed
	 * lines and skip if they exist 4. -> 2. Find needed lines by parsing 5. -> 3.
	 * Check another's line which go after needed lines and skip if they exist ...
	 * until all bytes of the file have been read
	 */
	public void search() throws URISyntaxException {
		reader.readAndDo(fileChannel -> {
			int bufferSize = 1024 * 400;
			ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

			// position in file
			int position = 0;

			int currentLine = 1;
			BenchmarkProp.start = System.nanoTime();

			// Search start
			while (true) {
				// read file
				Future<Integer> operation = fileChannel.read(buffer, position);

				// data get?
				if (operation.get() == -1) {
					break;
				}

				buffer.flip();
				byte[] dataFile = new byte[buffer.limit()];
				buffer.get(dataFile);

				// End file?
				if (dataFile.length == 1) {
					break;
				}

				// Start algorithm
				// Set position and limit of current data
				int limitData = 0;
				for (int i = dataFile.length - 1; i > 0; i--) {
					if (dataFile[i] == 10) {
						position += i + 1;
						limitData = i;
						break;
					}
				}

				// main index
				int index = 0;

				// Check firsts lines in cache
				// If they exist, they will be skip
				// Until line that not exist
				int startLine = 0;
				while (cache.containsKey(currentLine) && index < limitData) {
					List<byte[]> cachedData = cache.get(currentLine);
					byte[] cachedLine = cachedData.get(0);
					boolean isValid = false;
					if (cachedLine.length >= input.length) {
						isValid = true;
						if (!isNumbers) {
							for (int i = 0; i < input.length && i < cachedLine.length; i++) {
								if (cachedLine[i] == input[i]) {
									continue;
								} else if (cachedLine[i] <= 90 && cachedLine[i] >= 65
										&& cachedLine[i] + 32 == input[i]) {
									continue;
								} else {
									isValid = false;
									break;
								}
							}
						} else {
							for (int i = 0; i < input.length && i < cachedLine.length; i++) {
								if (cachedLine[i] != input[i]) {
									isValid = false;
									break;
								}
							}
						}
					}
					if (isValid) {
						addFromCache(currentLine, cachedData.get(1));
					}
					currentLine++;
					index += cachedData.get(1).length + 1;
					startLine = index;
				}

				// Search what we need going here

				// Count of delimiters
				byte countColumn = 1;
				for (; index < dataFile.length && index < limitData; index++) {

					// If line have less column that we need
					// It will be skip to next line and check in cache
					if (dataFile[index] == NEXT_LINE.index) {
						currentLine++;
						index++;
						while (cache.containsKey(currentLine)) {
							List<byte[]> cachedData = cache.get(currentLine);
							if (index + cachedData.get(1).length + 1 < limitData) {
								byte[] cachedLine = cachedData.get(0);
								boolean isValid = false;
								if (cachedLine.length >= input.length) {
									isValid = true;
									if (!isNumbers) {
										for (int i = 0; i < input.length && i < cachedLine.length; i++) {
											if (cachedLine[i] == input[i]) {
												continue;
											} else if (cachedLine[i] <= 90 && cachedLine[i] >= 65
													&& cachedLine[i] + 32 == input[i]) {
												continue;
											} else {
												isValid = false;
												break;
											}
										}
									} else {
										for (int i = 0; i < input.length && i < cachedLine.length; i++) {
											if (cachedLine[i] != input[i]) {
												isValid = false;
												break;
											}
										}
									}
								}
								if (isValid) {
									addFromCache(currentLine, cachedData.get(1));
								}
								currentLine++;
								index += cachedData.get(1).length + 1;
							} else {
								break;
							}

						}
						countColumn = 1;
						startLine = index + 1;
					}

					// Column counter
					if (dataFile[index] == DELIMITR.index) {
						++countColumn;
					}

					// Save block
					if (countColumn == column) {
						int innerIndex, searchIndex;
						boolean isValid = true;
						if (!isNumbers) {
							// For symbols
							for (innerIndex = index + 2, searchIndex = 0; searchIndex < input.length
									&& innerIndex <= limitData; innerIndex++, searchIndex++) {

								if (dataFile[innerIndex] == input[searchIndex]) {
									continue;
								} else if (dataFile[innerIndex] <= 90 && dataFile[innerIndex] >= 65
										&& dataFile[innerIndex] + 32 == input[searchIndex]) {
									continue;
								} else {
									isValid = false;
									break;
								}
							}

						} else {
							// For numbers
							innerIndex = column == 1 ? index : index + 1;
							for (searchIndex = 0; searchIndex < input.length
									&& innerIndex <= limitData; innerIndex++, searchIndex++) {
								if (dataFile[innerIndex] == input[searchIndex]) {
									continue;
								} else {
									isValid = false;
									break;
								}
							}
						}

						// Skip what already read
						index = innerIndex;

						// Skip to the next line character
						while (index < limitData && dataFile[index] != NEXT_LINE.index) {
							index++;
						}

						// Save if valid
						if (isValid) {
							addResult(dataFile, startLine, index, currentLine);
						}

						// Next Line
						currentLine++;

						// New line will be check in cache
						while (cache.containsKey(currentLine) && index < limitData) {
							List<byte[]> cachedData = cache.get(currentLine);
							byte[] cachedLine = cachedData.get(0);
							isValid = false;
							if (cachedLine.length >= input.length) {
								isValid = true;
								if (!isNumbers) {
									for (int i = 0; i < input.length; i++) {
										if (cachedLine[i] == input[i]) {
											continue;
										} else if (cachedLine[i] <= 90 && cachedLine[i] >= 65
												&& cachedLine[i] + 32 == input[i]) {
											continue;
										} else {
											isValid = false;
											break;
										}
									}
								} else {
									for (int i = 0; i < input.length && i < cachedLine.length; i++) {
										if (cachedLine[i] != input[i]) {
											isValid = false;
											break;
										}
									}
								}
							}
							if (isValid) {
								addFromCache(currentLine, cachedData.get(1));
							}
							currentLine++;
							index += cachedData.get(1).length + 1;
						}
						countColumn = 1;
						startLine = index + 1;
					}
				}
				buffer.clear();
			}
			BenchmarkProp.finish = System.nanoTime();
		});
	}

}
