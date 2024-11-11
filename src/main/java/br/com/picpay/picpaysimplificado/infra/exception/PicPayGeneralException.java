package br.com.picpay.picpaysimplificado.infra.exception;

public class PicPayGeneralException extends RuntimeException {

    public PicPayGeneralException(String message) {
        super(message);
    }
}
