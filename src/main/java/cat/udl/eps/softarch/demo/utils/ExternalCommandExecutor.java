package cat.udl.eps.softarch.demo.utils;

import java.io.File;
import java.io.IOException;

public class ExternalCommandExecutor {
    public void executeYARRRMLParser() {

        ProcessBuilder builder1 = new ProcessBuilder("docker", "build", "-t", "yarrrmlmapper",
                "C:\\\\Users\\\\Zihan\\\\Desktop\\\\TFG\\\\tab2kgwiz-api\\\\src\\\\main\\\\java\\\\cat\\\\udl\\\\eps\\\\softarch\\\\demo\\\\utils\\\\docker"); // Assuming Dockerfile is the name

        try {
            Process process = builder1.start();
            process.waitFor(); // Wait for the build process to finish
            int exitCode = process.exitValue();

            if (exitCode == 0) {
                System.out.println("Docker image built successfully!");
            } else {
                System.err.println("Error building Docker image. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        ProcessBuilder builder2 = new ProcessBuilder("docker", "container", "run", "--rm", "--name", "yarrrmlmapper",
                "-v", "C:\\Users\\Zihan\\Desktop\\TFG\\tab2kgwiz-api\\src\\main\\resources:/mnt/data",
                "yarrrmlmapper", "mappings.yarrrml.yml");

        // Redirect standard output (STDOUT) to a file
        builder2.redirectOutput(new File(
                "C:\\Users\\Zihan\\Desktop\\TFG\\tab2kgwiz-api\\src\\main\\resources\\output.txt"));

        try {
            Process process = builder2.start();
            process.waitFor(); // Wait for the container to finish
            int exitCode = process.exitValue();

            if (exitCode == 0) {
                System.out.println("Docker container run successfully!");
            } else {
                System.err.println("Error running Docker container. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
