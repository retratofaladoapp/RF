package app;


public class Comentarios {
	private String comentario;
	private int votosNegativos;
	
	public Comentarios(String comentario){
		this.comentario=comentario;
		votosNegativos=0;
	}
	public String getComentario() {
		return comentario;
	}
	public void setComentario(String comentario){
		this.comentario=comentario;
	}
	public int getVotosNegativos() {
		return votosNegativos;
	}
	public void setVotosNegativos(){
		votosNegativos++;
	}

}
