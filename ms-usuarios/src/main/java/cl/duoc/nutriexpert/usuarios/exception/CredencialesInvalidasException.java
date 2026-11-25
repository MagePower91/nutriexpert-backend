package cl.duoc.nutriexpert.usuarios.exception;

// Siempre el mismo mensaje: no revela si fallo el correo o la clave.
public class CredencialesInvalidasException extends RuntimeException {
    public CredencialesInvalidasException() {
        super("Correo o clave incorrectos");
    }
}
