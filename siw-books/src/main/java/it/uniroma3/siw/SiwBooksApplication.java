package it.uniroma3.siw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiwBooksApplication {

	public static void main(String[] args) {
		System.setProperty("org.apache.tomcat.util.http.fileupload.FileUploadBase.fileCountMax", "10000");
		System.setProperty("org.apache.tomcat.util.http.fileupload.FileUploadBase.fileSizeMax", "10485760");
		SpringApplication.run(SiwBooksApplication.class, args);
	}

}
