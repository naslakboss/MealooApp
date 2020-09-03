package codebuddies.MealooApp.dataProviders;

public class ImageDTO {


    String fileUrl;

    public ImageDTO() {
    }

    public ImageDTO( String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
