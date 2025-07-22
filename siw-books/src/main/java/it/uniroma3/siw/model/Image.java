package it.uniroma3.siw.model;

import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;


@Entity
public class Image {
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;
	@Lob
	private byte[] imageData;
	private String fileName;
    private String contentType;
    private long size;
	public Long getId() {
		return id;
	}
	public Image() {}
	public Image(byte[]imageData, String fileName, String contentType, long size) {
		this.imageData=imageData;
		this.fileName=fileName;
		this.contentType=contentType;
		this.size=size;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
	public byte[] getImageData() {
		return imageData;
	}
	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(imageData);
		result = prime * result + Objects.hash(contentType, fileName, size);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		return Objects.equals(contentType, other.contentType) && Objects.equals(fileName, other.fileName)
				&& Arrays.equals(imageData, other.imageData) && size == other.size;
	}
    
    public String getBase64Data() {
        if (this.imageData == null || this.imageData.length == 0) {
            return null; // Or return a default empty image Base64 string if preferred
        }
        return Base64.getEncoder().encodeToString(this.imageData);
    }

    


	

}
