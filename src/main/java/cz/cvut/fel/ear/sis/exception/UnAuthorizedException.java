package cz.cvut.fel.ear.sis.exception;

public class UnAuthorizedException extends BaseException{
    public UnAuthorizedException(Throwable cause){super(cause);}
    public UnAuthorizedException(String cause){super(cause);}
}
