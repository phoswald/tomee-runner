package phoswald.tomee.runner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.Container;

import phoswald.daemon.utils.Daemon;

public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length != 1) {
            System.out.println("Usage: java -jar tomee-runner.jar <webapp.war>");
            return;
        }
        Path webappFile = Paths.get(args[0]);
        int port = Integer.parseInt(System.getProperty("runner.port", "8080"));
        String context = System.getProperty("runner.context", "");
        Path tomcatDir = Files.createTempDirectory("runner-tomee");
        Path webappDir = Files.createTempDirectory("runner-webapp");
        Path webappCopy = webappDir.resolve(webappFile.getFileName());
        System.out.println("Using Webapp:          " + webappFile);
        System.out.println("Using Tomcat temp dir: " + tomcatDir);
        System.out.println("Using Webapp temp dir: " + webappDir);
        System.out.println("Using Webapp copy:     " + webappCopy);
        System.out.println("Serving:               http://localhost:" + port + "/" + context);

        Files.copy(webappFile, webappCopy);
        //tomcatDir.toFile().deleteOnExit();
        //webappDir.toFile().deleteOnExit();

        //System.out.println("*** Starting...");
        Configuration configuration = new Configuration();
        configuration.setDir(tomcatDir.toString());
        configuration.setHttpPort(port);

        Container container = new Container();
        container.setup(configuration);
        container.start();
        container.deploy(context, webappCopy.toFile(), true);
        //System.out.println("*** Started.");

        // Daemon.main(() -> run(container), () -> stop(container), () -> done());
        
		System.out.println("[Press Ctrl+C or send SIGTERM to stop]");
    	Daemon.main(container::await, container::stop, () -> System.out.println("[Stopped]"));
    }
    	
//	private static void run(Container container) throws Exception {
//		System.out.println("*** [Press Ctrl+C to stop]");
//		container.await();
//    }
//
//    private static void stop(Container container) throws Exception {
//        System.out.println("*** Stopping...");
//        container.stop();
//		Thread.sleep(1000);
//    }
//    
//    private static void done() {
//        System.out.println("*** Stopped.");
//    }
}
