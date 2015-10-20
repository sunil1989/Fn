package etc;

/**
 * Created by Sunil on 10/15/2015.
 */
public class AttachData {

    int id;
    String filepath;
    String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public AttachData(){}

    public AttachData(String curFileName,String dd) {
        this.filepath=curFileName;
        this.file=dd;
    }

    public String getFiledata() {
        return filedata;
    }

    public void setFiledata(String filedata) {
        this.filedata = filedata;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    String filedata;
}