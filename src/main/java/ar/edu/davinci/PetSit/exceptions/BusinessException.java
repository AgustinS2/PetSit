package ar.edu.davinci.PetSit.exceptions;

public class BusinessException extends Exception {
	
	private static final long serialVersionUID = -9129839746807292097L;
	
	public BusinessException(String mensaje) {
		super(mensaje);
	}
}