package com.pluralsight.io;

import java.nio.file.Path;

/**
 * Interface for objects that can save themselves to a file.
 */

public interface ISaveable {

    /**
     * Save this object to a file under the provided directory.
     * @param directory folder path
     * @return Path to saved file
     * @throws Exception on IO error
     */

    Path saveToFile(String directory) throws Exception;
}
