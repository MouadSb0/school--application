<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des apprenants</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Liste des apprenants</h1>
        <p class="subtitle">Gestion de l'école – API REST & JSP</p>

        <div class="table-wrapper">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Email</th>
                        <th>Filière</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="a" items="${apprenants}">
                        <tr>
                            <td>${a.id}</td>
                            <td>${a.nom}</td>
                            <td>${a.prenom}</td>
                            <td>${a.email}</td>
                            <td>${a.filiere}</td>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty apprenants}">
                        <tr class="empty-row">
                            <td colspan="5">Aucun apprenant trouvé.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>