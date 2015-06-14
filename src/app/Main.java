package app;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;

public class Main {
	 private static DbxClient client;
	 private final static String accessToken = /*key*/;
	 private final static String APP_KEY = /*key*/, APP_SECRET = /*key*/;
	 
	 private static String endereco;
	 private static final String quebraTxt="--------------------------------------------------------";
	 private static final String cabecalho="      |---------------------------------------|";
	 private static final String versao=" v1.3";
	 
	 private static Professor professor;
	 private static String disciplina;
	 private static String pastaProfessor;
	 
//	 private static int maxVotes=150;
//	 private static ArrayList<Professor> professores;
	 
	 public static void main(String[] args) throws IOException, DbxException {
        // Get your app key and secret from the Dropbox developers website.
//        professores=new ArrayList<Professor>();
		 System.out.println("Carregando...");
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

        DbxRequestConfig config = new DbxRequestConfig(
        		"anonymous", Locale.getDefault().toString());
//        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        
//        String authorizeUrl = webAuth.start();
//        System.out.println("1. Go to: " + authorizeUrl);
//        System.out.println("2. Click \"Allow\" (you might have to log in first)");
//        System.out.println("3. Copy the authorization code.");
//        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
        
//        DbxAuthFinish authFinish = webAuth.finish(code);
//        String accessToken = authFinish.accessToken;
        
        client = new DbxClient(config, accessToken);
        System.out.println("App: " + client.getAccountInfo().displayName+versao);
        URL urlUpdate = new URL("http://j.mp/updateRF");
        System.out.println("Update: "+urlUpdate.getAuthority()+urlUpdate.getPath()+'\n');
        Rede rede = new Rede(client);
//        String path = "/folder/";
        System.out.println("[Cadastro atual]:");
        System.out.println(listarPastas()+"**[add agr comentario seu, nos que existem ou novos]**\n");
        System.out.println(cabecalho);
        System.out.print("      | Voce pode visualizar ou inserir       |      \n      | comentario sobre aula dos professores |      \n");
		System.out.println(cabecalho+'\n');
        setProfessor();
     
//        int contador=2;
//        do{
        boolean continuar=true;
        boolean bloqueado=false;
        
        
        do{
        	System.out.print(quebraTxt);
            System.out.println("\n[MENU] professor: "+professor.getNome()+getMenu());
            System.out.print("opcao: ");
            int op=new BufferedReader(new InputStreamReader(System.in)).read()-'0';
            System.out.print(quebraTxt);
	        System.out.println();
	        	        
	        switch(op){
	        	case 0:
	        		System.out.println("[Comentarios sobre aula] de professor ["+professor.getNome()+" em "+disciplina+"]");
	        		System.out.print(visualizarComentario(professor,rede));
	        		break;
	        	case 1:
	        		inserirComentario(professor, rede);
	        		rede.uploadFile(pastaProfessor, professor.atualizarComentario(rede));
	        		bloqueado=false;
	        		break;
	        	case 2:
	        		if(bloqueado){
	        			System.out.println("voto bloqueado,\nescolha qualquer professor e insira um novo comentario,\npara liberar o seu voto");
	        			break;
	        		}else{
		        		int escolha= removerComentario(rede);
						rede.uploadFile(pastaProfessor, professor.atualizarVotos(pastaProfessor,escolha,rede));
		    			
						bloqueado=true;
						break;
	        		}
	        	case 3:
	        		setProfessor();
	        		break;
	        	case 4:
	        		continuar=false;
	        		break;
	        }
        }while(continuar);
//	    inserirComentario(professor);
//	        contador--;
//        }while(contador!=0);
//        professor.removerComentario(1, rede);
        
//        professores.add(professor);
//        System.out.println(professores.get(0).getComentario(0));
    }	
	 
	 private static int removerComentario(Rede rede){
 		String comentarios = visualizarComentario(professor,rede);
 		String votos = visualizarVotos(professor, rede);
 		String[] listaComentarios = comentarios.split("\r\n\r\n"), listaVotos= votos.split("\r\n");
 		
 		//fix possivel erro de listaRemover desatualizado
 		int qteComentarios=listaComentarios.length, qteVotos=listaVotos.length;
 		if(qteVotos<qteComentarios){
 			int diferenca = qteComentarios-qteVotos;
 			String[] temp = new String[qteVotos+diferenca];
            System.arraycopy(listaVotos,0,temp,0,qteVotos);
            for(int i=qteVotos; i<temp.length;i++){
            	temp[i]="0";
            }
            listaVotos=temp;

			votos = Arrays.toString(listaVotos).replace(", ", "\r\n").replaceAll("[\\[\\]]", "")+"\r\n";

    		rede.uploadFile(pastaProfessor, professor.atualizarDado(votos,"remover",rede));
            
 		}
 		
 		System.out.println("qual comentario deseja remover?");
 		for(int i=0; i<qteComentarios;i++){
 			System.out.println(i+"-"+listaComentarios[i]);
 		}

        int dado=-1;
        Scanner numScan= new Scanner(System.in);
        boolean errar=false;
        do{
        	try{
        		dado= numScan.nextInt();
        		errar=false;
        	}catch(InputMismatchException e){
        		errar=true;
        	}
    		if(dado>qteComentarios || dado<0 || errar==true){
    			System.out.println("valor invalido");
    		}
        }while(errar || dado>qteComentarios || dado<0);
        
        return dado;
        
	 }
	 private static String getMenu(){
		 String retorno="";
		 String[]menu={
				 "visualizar comentarios",
				 "inserir comentarios",
				 "remover comentario",
				 "mudar professor e disciplina",
				 "sair"				 
		 };
		 for(int i =0; i<menu.length;i++){
			 retorno+="\n"+i+"-"+menu[i];
		 }		 
		 return retorno;
	 }
	 
	 private static void inserirComentario(Professor professor, Rede rede){
//				fw = new FileWriter(file.getAbsoluteFile());
			//			BufferedWriter bw = new BufferedWriter(fw);
						System.out.println("insira o comentario");
				        String dado;
				        for(boolean invalido=true; invalido;){
				        	dado= readKeyboard();
					        if(analisarTexto(dado, rede)){
					        	professor.inserirComentario(dado);
					        	invalido=false;
					        }
					        else{
					        	System.out.println("protecao ativada\ncomentario nao inserido");
					        	break;
					        	}
				        }
			//			bw.write(dado);
			//			bw.close();
	 }
	 
	 private static String listarPastas(){
		 String dado="";
		 DbxEntry.WithChildren listing,listing2;
		try {
			listing = client.getMetadataWithChildren("/");
		 for (DbxEntry disciplinaFolder : listing.children) {
			    dado+=(disciplinaFolder.name+'\n');
			 if(disciplinaFolder.isFolder()){
				 listing2= client.getMetadataWithChildren("/"+disciplinaFolder.name);
				 for (DbxEntry professoresFolder : listing2.children) {
						 if (professoresFolder == null) {
						     System.out.println("No file or folder at that path.");
						 } else {
							 dado+=("	"  + professoresFolder.name+'\n');
						 }
				 }
			 }
		 }
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return dado;
	 }
	 private static void setEndereco(String endereco) {
		Main.endereco = "/"+endereco+"/";
	}
	 public static String getEndereco() {
		return endereco;
	}
	 private static String visualizarComentario(Professor professor, Rede rede){
		 return new String(professor.getBackup(new File("comentario.txt"), rede));
	 }
	 private static String visualizarVotos(Professor professor, Rede rede){
		 return new String(professor.getBackup(new File("remover.txt"), rede));
	 }
	 
	 private static void setProfessor(){
		System.out.println("*nao use acento*");
		System.out.print("Informe o nome do professor: ");
		pastaProfessor = readKeyboard();
		
		professor = new Professor(pastaProfessor);
		 
		System.out.print("Informe a disciplina do professor "+pastaProfessor+": ");
		disciplina=readKeyboard();
		pastaProfessor=disciplina+"/"+pastaProfessor;
		
		setEndereco(pastaProfessor);		 
	 }
	 public static boolean analisarTexto(String dado, Rede rede){
		 boolean resultado=false;
		 if(dado.length()<=150 && limitarQuebraLinha(dado) && tamMaxFile(rede)){
			 resultado=true;
		 }
		 return resultado;
	 }
	 private static boolean limitarQuebraLinha(String dado){
		 int contador=0;
		 for(char c:dado.toCharArray()){
			 if(c=='\n'||c=='\r'){
				 contador++;
			 }
		 }
		 if(contador<8){
			 return true;
		 }else{
			 return false;
		 }
	 }
	 private static boolean tamMaxFile(Rede rede){
		 //20mb de comentarios no max
//		 System.out.println(professor.getSize(rede));
		if(professor.getSize(rede)<20971510){
			return true;
		}else{
			return false;
		}
	 }
	 private static String readKeyboard(){
			//qlqr termo que nao esteja entre a e z
			Pattern p = Pattern.compile("[^a-z ,.]");
			String dado="";
			boolean invalido;
			 do{
				 try {
					dado=new BufferedReader(new InputStreamReader(System.in)).readLine().trim().toLowerCase();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 invalido=p.matcher(dado).find();
				 if(invalido==true){
					 System.out.println("invalido, insira novamente");
				 }
			 }while(invalido);
		 return dado;
	 }

	 private static String removerCharEspecial(String dado){
//		 dado=dado.replaceAll("é","eh");
		 dado=Normalizer.normalize(dado, Normalizer.Form.NFKD);
		 dado=dado.replaceAll("[^a-zA-Z,.? ]+","");
		 return dado;
	 }
}