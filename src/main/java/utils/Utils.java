package utils;

import java.io.File;
import java.util.List;

public class Utils {

    public static void searchDirRecursive(File currFile, String suffix, List<String> res) {
        if (currFile.isFile() && currFile.getPath().endsWith(suffix)) {
            res.add(currFile.getAbsolutePath());
        } else if (currFile.isDirectory()) {
            for (File file : currFile.listFiles()) {
                searchDirRecursive(file, suffix, res);
            }
        }
    }
}
