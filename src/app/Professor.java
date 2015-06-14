package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.dropbox.core.DbxException;

public class Professor {
	private ArrayList<Comentarios> comentarios;
	private String nome;
	private File tempReader;
//	private final int votosMaximo=150;
//	private DbxClient ultimoVoto;
	
	public Professor(String nome){
		this.nome=nome;
		comentarios=new ArrayList<Comentarios>();
	}
//	public void removerComentario(int index, Rede rede){
//		DbxClient client= rede.getClient();
//		Comentarios comentario = comentarios.get(index);
//		if(comentario.getVotosNegativos() >= votosMaximo){
//			comentarios.remove(index);
//		}else if(ultimoVoto!=client){
//			comentario.setVotosNegativos();
//			ultimoVoto=client;
//			atualizarVotosNegativos(rede);
//		}
//	}	
	public File atualizarComentario(Rede rede){
        File input = new File("comentario.txt");
		FileWriter writer;
//		BufferedWriter writer;
		try {
			writer = new FileWriter(input);
			String dado= new String(getBackup(input,rede));
//			System.out.println(comentarios.size());
			int ultimo = comentarios.size();
			for(int i =ultimo-1;i<ultimo;i++) {
				dado+= comentarios.get(i).getComentario()+"\r\n\r\n";
			}
			writer.write(dado);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	
	private int strToInt(String numero){
		int d=1,r=0,k=1,tamanho=numero.length();
		for(int i = 0,p=d;i<tamanho; i++, p=p/10){
			if((numero.charAt(i)-'0')<10){//garantir que é numero
				k=numero.charAt(i)-'0';
			}
			k=k*p;
			r=r+k;		
		}
		return k;
	}
	
	public File atualizarVotos(String pastaProfessor,int escolha, Rede rede){
        File input = new File("remover.txt");
		FileWriter writer;
//		BufferedWriter writer;
		try {
			writer = new FileWriter(input);
			String dado= new String(getBackup(input,rede));
			String delim= "\r\n";
			String[] votos= dado.split(delim);
//			System.out.println(comentarios.size());
			int votosInt;
			votosInt = strToInt(votos[escolha]);
			votos[escolha]=(votosInt+1)+"";
			if((votos[escolha]+"").equalsIgnoreCase(new String(getMaxVotos(rede)))){

				String dadoComentarios= new String(getBackup(new File("comentario.txt"),rede));
				String[] comentarios= dadoComentarios.split(delim+delim);
				comentarios[escolha]="[removido]";

				dadoComentarios = Arrays.toString(comentarios).replace(", ", delim+delim).replaceAll("[\\[\\]]", "")+delim+delim;

				rede.uploadFile(pastaProfessor, atualizarDado(dadoComentarios, "comentario", rede));

				votosInt = strToInt(votos[escolha]);
				votos[escolha]=(votosInt-1)+"";
			}
			
			dado = Arrays.toString(votos).replace(", ", delim).replaceAll("[\\[\\]]", "")+delim;
			writer.write(dado);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	
	public File atualizarDado(String atualizado, String nome, Rede rede){
        File input = new File(nome+".txt");
		FileWriter writer;
//		BufferedWriter writer;
		try {
			writer = new FileWriter(input);
			writer.write(atualizado);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
	public void inserirComentario(String comentario){
		comentarios.add(new Comentarios(comentario));
	}
	public String getComentario(int index){
		return comentarios.get(index).getComentario();
	}
	
	protected char[] getBackup(File input, Rede rede){
		String endereco = Main.getEndereco()+input.getName();

		char[] backup = {};
		try {
			if(rede.getClient().getMetadata(endereco)!=null){
				tempReader = new File("tempReader.txt");
				FileOutputStream temp;
				temp = new FileOutputStream(tempReader);
				
				rede.getClient().getFile(endereco, null, temp);
				
				FileInputStream temp2 = new FileInputStream(tempReader);
				
				int tamanho=(int) tempReader.length();
				byte[] buffer = new byte[tamanho];
				temp2.read(buffer);
				backup = new char[tamanho];
				for(int i=0; i<backup.length;i++){
					backup[i]=(char)buffer[i];
				}
			}
//			else
//				System.out.println("arquivo nao existe"); #fix erro ler nao existente
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
		return backup;
		
	}
	
	protected char[] getMaxVotos(Rede rede){
		String endereco = "/3. MaxVotos";

		char[] backup = {};
		try {
			if(rede.getClient().getMetadata(endereco)!=null){
				tempReader = new File("tempReader.txt");
				FileOutputStream temp;
				temp = new FileOutputStream(tempReader);
				
				rede.getClient().getFile(endereco, null, temp);
				
				FileInputStream temp2 = new FileInputStream(tempReader);
				
				int tamanho=(int) tempReader.length();
				byte[] buffer = new byte[tamanho];
				temp2.read(buffer);
				backup = new char[tamanho];
				for(int i=0; i<backup.length;i++){
					backup[i]=(char)buffer[i];
				}
			}
//			else
//				System.out.println("arquivo nao existe"); #fix erro ler nao existente
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
		return backup;
		
	}
//	public void atualizarVotosNegativos(Rede rede){
//		File input = new File("negativos.txt");
//		FileWriter writer;
//		String endereco = "/"+nome+"/"+input.getName();
////		BufferedWriter writer;
//		try {
//			FileOutputStream temp = new FileOutputStream("tempRead.txt");
//			rede.getClient().getFile(endereco, null, temp);
//			FileInputStream temp2 = new FileInputStream("tempRead.txt");
//			int backup = (char)temp2.read()-'0';
//			if(backup>=votosMaximo){
//				backup=0;
//			}
//			System.out.println(backup);
//			writer = new FileWriter(input);
//			String dado="";
//			System.out.println(comentarios.size());
//			for(int i =0;i<comentarios.size();i++) {
//				dado= backup+comentarios.get(i).getVotosNegativos()+"\r\n";
//			}
//			writer.write(dado);
//			writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DbxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		rede.uploadFile(nome, input);
//	}
	
public String getNome() {
	return nome;
}
protected int getSize(Rede rede){
	//File input removed
	String endereco = Main.getEndereco()+"comentario.txt";
	tempReader = new File("tempReader.txt");
	FileOutputStream temp;
	int tamanho=0;
		try {
			temp = new FileOutputStream(tempReader);
		
		rede.getClient().getFile(endereco, null, temp);
		
		FileInputStream temp2 = new FileInputStream(tempReader);
		
		tamanho=(int) tempReader.length();
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
		return tamanho;
}
}
