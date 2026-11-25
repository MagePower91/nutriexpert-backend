package cl.duoc.nutriexpert.usuarios.exception;

public class CorreoExistenteException extends RuntimeException {
    public CorreoExistenteException() {
        super("El correo ya esta registrado");
    }
}
