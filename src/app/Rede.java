package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;

public class Rede {
	private DbxClient client;
	protected Rede(DbxClient client){
		this.client=client;		 
	}
	protected void uploadFile(String folder, File input){
            FileInputStream inputStream;
            String nome = input.getName(), path="/"+folder+"/", endereco= path+nome;
			try {
				if(client.getMetadata(endereco)==null){
//					System.out.println("precisei criar"); #fix erro de criar folder existente
					client.createFolder(endereco);
				}
//				client.delete(path+nome);
				inputStream = new FileInputStream(input);
//				System.out.println(inputFile.getName());
            DbxEntry.File uploadedFile = client.uploadFile(endereco,
                DbxWriteMode.force(), input.length(), inputStream);
//            System.out.println("Uploaded: " + uploadedFile.toString());
            
            inputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DbxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}   
    protected void downloadFile(String endereco){   
        try {
        	FileOutputStream outputStream = new FileOutputStream("download-teste.txt");
            DbxEntry.File downloadedFile = client.getFile(File.separator+endereco, null,
                outputStream);
            System.out.println("Metadata: " + downloadedFile.toString());
            
            outputStream.close();
        } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    }
    protected DbxClient getClient() {
		return client;
	}
}
