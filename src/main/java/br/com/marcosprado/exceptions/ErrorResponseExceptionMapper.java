//package br.com.marcosprado.exceptions;
//
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.ext.ExceptionMapper;
//import jakarta.ws.rs.ext.Provider;
//
//@Provider
//public class ErrorResponseExceptionMapper implements ExceptionMapper<Exception> {
//    @Override
//    public Response toResponse(Exception e) {
//        if (e instanceof ClientNotFoundException) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//        if (e instanceof InsufficientLimitException || e instanceof InvalidArgumentException) {
//            return Response.status(422).build();
//        }
//        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//    }
//}
