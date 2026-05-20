<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Entry point: redirect root requests to the login page. --%>
<%
    response.sendRedirect(request.getContextPath() + "/login");
%>
