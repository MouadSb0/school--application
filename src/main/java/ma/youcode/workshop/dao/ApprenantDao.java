package ma.youcode.workshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import ma.youcode.workshop.models.Apprenant;
import ma.youcode.workshop.util.ConnectionFactory;

public class ApprenantDao {

    private DataSource dataSource;

    public List<Apprenant> findAll() {
    List<Apprenant> list = new ArrayList<>();
    System.out.println(">>> [DAO] findAll() démarré");
    try (Connection cn = ConnectionFactory.getConnection();
         Statement stmt = cn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM etudiant")) {

        System.out.println(">>> [DAO] Connexion OK, URL = " + cn.getMetaData().getURL());
        System.out.println(">>> [DAO] Base = " + cn.getCatalog());

        while (rs.next()) {
            System.out.println(">>> [DAO] Ligne id=" + rs.getInt("id"));
            list.add(new Apprenant(
                rs.getInt("id"), rs.getString("nom"),
                rs.getString("prenom"), rs.getString("email"),
                rs.getString("filiere")));
        }
    } catch (Exception e) {
        System.out.println(">>> [DAO] ERREUR : " + e.getClass().getName() + " - " + e.getMessage());
        e.printStackTrace();
    }
    System.out.println(">>> [DAO] findAll() terminé : " + list.size() + " lignes");
    return list;
}

    public void save(Apprenant apprenant){
        String sql="insert into etudiant (nom, prenom, email, filiere) values (?, ?, ?, ?)";
        try (Connection cn=ConnectionFactory.getConnection();
        PreparedStatement ps=cn.prepareStatement(sql)) {
            ps.setString(1, apprenant.getNom());
            ps.setString(2, apprenant.getPrenom());
            ps.setString(3, apprenant.getEmail());
            ps.setString(4, apprenant.getFiliere());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de l'apprenant", e);
        }
    }

    public Apprenant findById(int id) {
    String sql = "SELECT * FROM etudiant WHERE id = ?";
    try (Connection cn = dataSource.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql)) {

        ps.setInt(1, id);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Apprenant a = new Apprenant();
                a.setId(rs.getInt("id"));
                a.setNom(rs.getString("nom"));
                a.setPrenom(rs.getString("prenom"));
                a.setEmail(rs.getString("email"));
                a.setFiliere(rs.getString("filiere"));
                return a;
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return null;
    }
    
    public Apprenant create(Apprenant a) {
    String sql = "INSERT INTO etudiant (nom, prenom, email, filiere) VALUES (?, ?, ?, ?)";
    try (Connection cn = dataSource.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, a.getNom());
        ps.setString(2, a.getPrenom());
        ps.setString(3, a.getEmail());
        ps.setString(4, a.getFiliere());

        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                a.setId(rs.getInt(1));
            }
        }
        return a;
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

public void update(Apprenant a) {
    String sql = "UPDATE etudiant SET nom=?, prenom=?, email=?, filiere=? WHERE id=?";
    try (Connection cn = dataSource.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setString(1, a.getNom());
        ps.setString(2, a.getPrenom());
        ps.setString(3, a.getEmail());
        ps.setString(4, a.getFiliere());
        ps.setInt(5, a.getId());
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

public void delete(int id) {
    String sql = "DELETE FROM etudiant WHERE id=?";
    try (Connection cn = dataSource.getConnection();
         PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
}

}
