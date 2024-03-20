package cat.udl.eps.softarch.demo.utils;

import java.io.File;
import java.io.IOException;

public class ExternalCommandExecutor {
    public void executeYARRRMLParser(String inputFileName, String outputFileName) throws IOException, InterruptedException {
        String inputPath = "/mnt/c/Users/Zihan/Desktop/TFG/tab2kgwiz-api/src/main/resources/" + inputFileName;
        String outputPath = "/mnt/c/Users/Zihan/Desktop/TFG/tab2kgwiz-api/src/main/resources/" + outputFileName;

        ProcessBuilder builder = new ProcessBuilder("wsl",
                "/home/zihan/.nvm/versions/node/v21.5.0/bin/yarrrml-parser", "-i", inputPath, "-o", outputPath);

        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("YARRRML parser failed. Exit code: " + exitCode);
        }
    }

    public void executeRMLMapper(String outputFileName) throws IOException, InterruptedException {
        String outputPath = "/mnt/c/Users/Zihan/Desktop/TFG/tab2kgwiz-api/src/main/resources/" + outputFileName;
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", "rmlmapper-6.5.1-r371-all.jar", "-m", outputPath);

        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("RML mapper failed. Exit code: " + exitCode);
        }
    }
}
