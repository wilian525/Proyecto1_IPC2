<%-- 
    Document   : login
    Created on : 13 sep 2026, 10:03:21 p.m.
    Author     : wilian
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <jsp:include page="/includes/resources.jsp"/>
    </head>
    <body>
                <main>
                    <div class="container min-vh-100 d-flex justify-content-center align-items-center">
                        <div class="card shadow"> 
                            <div class="card-hader text-center"> 
                                <i class="bi bi-bus-front-fill fs-1"></i>
                               <h3 class="mt-2">Gestion de Flota</h3>   
                             </div>           
                            
                            <div class="card-body">  
                                <div class="text-center mb-4"> 
                                    <i class="bi bi-person-circle display-4"></i>
                                    <h4 class="mt-2"
                                            Iniciar Sesion
                                    </h4>
                                    <p class="text-muted"> 
                                        Ingrese sus datos para continuar
                                    </p>    
                                </div>
                                
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger" role="alert"> 
                                        <i class="bi bi-exclamation-triangle-fill"></i>
                                        ${error}
                                    </div>
                                </c:if>
                                
                                <c:if test="${not empty mensaje}">
                                     <div class="alert alert-success text-center" role="alert">
                                             ${mensaje}
                                       </div>
                                </c:if>
                                
                                <form action="${pageContext.servletContext.contextPath}/AutenticacionServlet"
                                      method="POST"> 
                                    <div class="mb-3"> 
                                        <label for="username" class="form-label">
                                               Usuario
                                    </label>
                                    
                                    <div class="input-group"> 
                                        <span class="input-group-text">
                                             <i class="bi bi-person"></i>
                                        </span>

                                       <input type="text"
                                              class="form-control"
                                               id="username"
                                              name="username"
                                               placeholder="Ingrese su usuario"
                                               required>
                                    </div>
                                 </div>

                <div class="mb-3">
                    <label for="password" class="form-label">
                        Contraseña
                    </label>

                    <div class="input-group">
                        <span class="input-group-text">
                            <i class="bi bi-lock"></i>
                        </span>

                        <input type="password"
                               class="form-control"
                               id="password"
                               name="password"
                               placeholder="Ingrese su contraseña"
                               required>
                    </div>
                </div>

                <div class="d-grid mt-4">
                    <button type="submit" class="btn btn-primary">
                        <i class="bi bi-box-arrow-in-right"></i>
                        Iniciar Sesión
                    </button>
                </div>
                                    
                  <div class="text-center mt-3">

    <p class="text-muted mb-2">
        ¿No tienes una cuenta?
    </p>

    <a href="${pageContext.servletContext.contextPath}/UsuarioServlet?accion=registro"
       class="btn btn-outline-secondary">

        <i class="bi bi-person-plus"></i>
        Crear cuenta

    </a>

</div>                 

            </form>

        </div>

        <div class="card-footer text-center">
            <small class="text-muted">
                Sistema de Gestión de Flota de Buses
            </small>
        </div>

    </div>
                    </div>
                  </main>
    </body>
</html>
