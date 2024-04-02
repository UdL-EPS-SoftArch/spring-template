package cat.udl.eps.softarch.demo.utils;

import java.io.File;
import java.io.IOException;

public class ExternalCommandExecutor {
    public void executeYARRRMLParser(String inputFileName, String outputFileName) throws IOException, InterruptedException {
//        ProcessBuilder builder = new ProcessBuilder("docker", "build", "-t", "yarrrmlmapper",
//                "C:\\Users\\Zihan\\Desktop\\TFG\\tab2kgwiz-api\\src\\main\\java\\cat\\udl\\eps\\softarch\\demo\\utils\\docker", "&&"
//                ,"docker", "container", "run", "--rm", "--name", "yarrrmlmapper", "-v",
//                "\"C:\\Users\\Zihan\\Desktop\\TFG\\tab2kgwiz-api\\src\\main\\resources\":/mnt/data", "yarrrmlmapper"
//                , "mappings.yarrrml.yml");
//
//        // docker build -t yarrrmlmapper docker && docker container run --rm --name yarrrmlmapper -v "C:\Users\Zihan\Desktop\TFG\tab2kgwiz-api\src\main\resources":/mnt/data yarrrmlmapper mappings.yarrrml.yml --serialization turtle
//
//        Process process = builder.start();
//        int exitCode = process.waitFor();
//
//        if (exitCode != 0) {
//            throw new RuntimeException("YARRRML parser failed. Exit code: " + exitCode);
//        }

        ProcessBuilder builder1 = new ProcessBuilder("docker", "build", "-t", "yarrrmlmapper",
                "C:\\Users\\Zihan\\Desktop\\TFG\\tab2kgwiz-api\\src\\main\\java\\cat\\udl\\eps\\softarch\\demo\\utils\\docker");
        Process process1 = builder1.start();
        int exitCode1 = process1.waitFor();
        if (exitCode1 != 0) {
            throw new RuntimeException("Docker build failed. Exit code: " + exitCode1);
        }

        ProcessBuilder builder2 = new ProcessBuilder("docker", "container", "run", "--rm", "--name", "yarrrmlmapper", "-v",
                "C:\\Users\\Zihan\\Desktop\\TFG\\tab2kgwiz-api\\src\\main\\resources:/mnt/data", "yarrrmlmapper",
                "mappings.yarrrml.yml", "--serialization", "turtle");
        Process process2 = builder2.start();
        int exitCode2 = process2.waitFor();
        if (exitCode2 != 0) {
            throw new RuntimeException("YARRRML parser failed. Exit code: " + exitCode2);
        }
    }

    public void executeRMLMapper(String outputFileName) throws IOException, InterruptedException {
        //String inputPath = "/mnt/c/Users/Zihan/Desktop/TFG/tab2kgwiz-api/src/main/resources/" + outputFileName;
//        ProcessBuilder builder = new ProcessBuilder("wsl", "java", "-jar",
//                "/home/zihan/yarrrml/rmlmapper-6.5.1-r371-all.jar", "-m", inputPath, ">", "porkDefRmlOut.txt");

        ProcessBuilder builder = new ProcessBuilder("wsl", "java", "-jar",
                "rmlmapper-6.5.1-r371-all.jar", "-m", outputFileName, ">", "porkDefRmlOut.txt");

        builder.directory(new File("/home/zihan/yarrrml/"));
        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("RML mapper failed. Exit code: " + exitCode);
        }
    }
}
