/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto1.serviciobuses.backend.dao;
import com.proyecto1.serviciobuses.backend.Conexion.ConexionDB;
import com.proyecto1.serviciobuses.backend.Model.Chofer;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
/**
 *
 * @author wilian
 */
public class ChoferDAO {
    
    private ConexionDB conexiondb;
    
    private static final String CREAR_TABLA = """
          CREATE TABLE IF NOT EXISTS chofer (
                   usuario_id INT,
                   foto VARCHAR(255) NOT NULL,
                    numero_licencia VARCHAR(30) NOT NULL,
                     tipo_licencia CHAR(1) NOT NULL,
                     fecha_vencimiento DATE NOT NULL,
                     salario_base_por_viaje DECIMAL(10,2) NOT NULL,
                     estado BOOLEAN NOT NULL,
                                              
                     CONSTRAINT pk_chofer PRIMARY KEY (usuario_id),
                                              
                    CONSTRAINT fk_chofer_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id),
                                              
                    CONSTRAINT ak_chofer_licencia UNIQUE (numero_licencia),
                                              
                    CONSTRAINT chk_chofer_salario CHECK (salario_base_por_viaje >= 0)
                                                      )                                     
                                              """;
    private static final String INSERTAR_USUARIO = """
                                                   INSERTAR INTO usuario (nombre,nit,dpi,telefono,direccion,username,password,estado,rol,sucursal_id)
                                                   VALUES (?,?,?,?,?,?,?,?, 'CHOFER', ?) 
                                                   """;
    private static final String INSERTAR_CHOFER = """
                                                  INSERT INTO chofer  (usuario_id, foto, numero_licencia, tipo_licencia, fecha_vencimiento, salario_base_por_viaje, estado)
                                                          VALUES (?, ?, ?, ?, ?, ?, ?)
                                                  """ ;   
    private static final String ACTUALIZAR_USUARIO = """
                                                     UPDATE usuario SET nombre = ?, nit = ? , dpi = ?, telefono = ?, direccion = ?, username = ?, password = ?, estado = ?, sucursal_id = ?
                                                             WHERE usuario_id = ?
                                                     """;
    private static final String ACTUALIZAR_CHOFER = """
                                                    UPDATE chofer SET foto = ?, numero_licencia = ?, tipo_licencia = ?, fecha_vencimiento = ?,  salario_base_por_viaje = ?, estado = ?
                                                            WHERE usuario_id = ?
                                                    """;
    private static final String BUSCAR_POR_ID = """
                                                 SELECT u.usuario_id, u.nombre, u.nit, u.dpi, u.telefono, u.direccion, u.username, u.password, u.estado AS usuario_estado, u.sucursal_id,
                                                        c.foto, c.numero_licencia, c.tipo_licencia, c.fecha_vencimiento, c.salario_base_por_viaje, c.estado AS chofer_estado
                                                        FROM usuario u
                                                        INNER JOIN chofer c  ON u.usuario_id = c.usuario_id
                                                        WHERE u.usuario_id = ?
                                                """;
    private static final String LISTAR = """
                                          SELECT u.usuario_id, u.nombre, u.nit,  u.dpi, u.telefono, u.direccion,  u.username, u.password, u.estado AS usuario_estado, u.sucursal_id,
                                                     c.foto, c.numero_licencia,  c.tipo_licencia, c.fecha_vencimiento,  c.salario_base_por_viaje,  c.estado AS chofer_estado
                                                 FROM usuario u
                                                 INNER JOIN chofer c ON u.usuario_id = c.usuario_id
                                                 ORDER BY u.nombr
                                         """;
    private static final String CAMBIAR_ESTADO = "UPDATE chofer SET estado = ? WHERE usuario_id = ? ";
    
    public ChoferDAO (ConexionDB conexiondb){
        this.conexiondb = conexiondb;
    }
    
    public void crearTabla(){
        Connection con = conexiondb.obtenerConeccion();
        Statement statement = null;
        
        try {
            statement = con.createStatement();
            statement.execute(CREAR_TABLA);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(statement);
        }
    }
    
    public boolean insertar(Chofer chofer){
        if (chofer == null ) {
            return false;
        }
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement usuarioPs = null;
        PreparedStatement choferPs = null;
        ResultSet claves = null;
        
         boolean autoCommitAnterior = true;
         
         try {
            autoCommitAnterior = con.getAutoCommit();
            con.setAutoCommit(false);
            
            usuarioPs = con.prepareStatement(INSERTAR_USUARIO, Statement.RETURN_GENERATED_KEYS);
             usuarioPs.setString(1, chofer.getNombre());
            usuarioPs.setString(2, chofer.getNit());
            usuarioPs.setString(3,chofer.getDpi());
            usuarioPs.setString(4,chofer.getTelefono() );
            usuarioPs.setString( 5, chofer.getDireccion());
            usuarioPs.setString( 6,chofer.getUserName());
            usuarioPs.setString(7, chofer.getPassword());
            usuarioPs.setBoolean(8, chofer.isEstado() );
            usuarioPs.setInt(9, chofer.getSucursalId());
            
            if (usuarioPs.executeUpdate() == 0) {
                con.rollback();
                return false;
            }

            claves = usuarioPs.getGeneratedKeys();

            if (!claves.next()) {
                con.rollback();
                return false;
            }

            int usuarioId = claves.getInt(1);

            choferPs = con.prepareStatement( INSERTAR_CHOFER );
            choferPs.setInt(1, usuarioId );
            choferPs.setString(2, chofer.getFoto() );
            choferPs.setString( 3,chofer.getNumeroLicencia());
            choferPs.setString(4,String.valueOf( chofer.getTipoLicencia()));
            choferPs.setDate(5, Date.valueOf(chofer.getFechaVencimiento() ));
            choferPs.setDouble(6, chofer.getSalarioBasePorViaje() );
            choferPs.setBoolean( 7, chofer.isEstado() );

            if (choferPs.executeUpdate() == 0) {
                con.rollback();
                return false;
            }

            con.commit();

            chofer.setId(usuarioId);

            return true;
        } catch (SQLException e) {
            rollback(con);
            e.printStackTrace();
            return false;
        } finally{
              cerrar(claves);
            cerrar(choferPs);
            cerrar(usuarioPs);

            restauraAutoCommit(con,autoCommitAnterior);
         }
    }
    
    public boolean actualizar(Chofer chofer){
        if (chofer  == null ) {
            return false;
        }
        
        Connection conexion = conexiondb.obtenerConeccion();
        PreparedStatement usuarioPs = null;
        PreparedStatement choferPs = null;
        
        boolean autoCommitAnterior = true;
        
        try {
            autoCommitAnterior = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            
            usuarioPs = conexion.prepareStatement(ACTUALIZAR_USUARIO);
            usuarioPs.setString(1, chofer.getNombre());
            usuarioPs.setString( 2, chofer.getNit() );
            usuarioPs.setString(3, chofer.getDpi() );
            usuarioPs.setString(4,chofer.getTelefono());
            usuarioPs.setString(5, chofer.getDireccion() );
            usuarioPs.setString(6, chofer.getUserName() );
            usuarioPs.setString(7, chofer.getPassword() );
            usuarioPs.setBoolean(8,chofer.isEstado());
            usuarioPs.setInt(9,chofer.getSucursalId());
            usuarioPs.setInt(10, chofer.getId());
            
            if (usuarioPs.executeUpdate() == 0) {
                conexion.rollback();
                return false;
            }
            choferPs = conexion.prepareStatement(ACTUALIZAR_CHOFER);
            choferPs.setString(1,chofer.getFoto());
            choferPs.setString(2,chofer.getNumeroLicencia() );
            choferPs.setString(3,String.valueOf(chofer.getTipoLicencia()));
            choferPs.setDate( 4,Date.valueOf( chofer.getFechaVencimiento()));
            choferPs.setDouble(5,chofer.getSalarioBasePorViaje());
            choferPs.setBoolean(6,chofer.isEstado() );
            choferPs.setInt(7, chofer.getId() );
            
            if (choferPs.executeUpdate() == 0) {
                conexion.rollback();
                return false;
            }
            conexion.commit();
            return true;

        } catch (SQLException e) {
            rollback(conexion);
            e.printStackTrace();
            return false;
        } finally {
            cerrar(choferPs);
            cerrar(usuarioPs);
            restauraAutoCommit(conexion, autoCommitAnterior);
        }
        }
    
    public Optional<Chofer> buscarPorId(int id){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        Chofer chofer = null;
        
        try {
            ps = con.prepareStatement(BUSCAR_POR_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                chofer = construirChofer(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return Optional.ofNullable(chofer);
    }
    
    public Collection<Chofer> listar(){
        Collection<Chofer> choferes = new ArrayList<>();
        
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        ResultSet rs = null; 
        
        try {
            ps = con.prepareStatement(LISTAR);
            rs = ps.executeQuery();
            
            while(rs.next()){
                choferes.add(construirChofer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cerrar(rs);
            cerrar(ps);
        }
        return choferes;
    }
    
    public boolean cambiarEstado(int id,boolean estado){
        Connection con = conexiondb.obtenerConeccion();
        PreparedStatement ps = null;
        
        try {
            ps = con.prepareStatement(CAMBIAR_ESTADO);
            ps.setBoolean(1, estado);
            ps.setInt(2, id);
         
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            cerrar(ps);
        }
    }
    
    private Chofer construirChofer(ResultSet rs) throws SQLException {
        Chofer chofer = new Chofer();
        chofer.setId(rs.getInt("usuario_id"));
        chofer.setNombre(rs.getString("nombre"));
        chofer.setNit(rs.getString("nit") );
        chofer.setDpi( rs.getString("dpi"));
        chofer.setTelefono( rs.getString("telefono"));
        chofer.setDireccion( rs.getString("direccion") );
        chofer.setUserName(rs.getString("username"));
        chofer.setPassword( rs.getString("password"));
        chofer.setEstado(rs.getBoolean("usuario_estado"));
        chofer.setSucursalId( rs.getInt("sucursal_id"));
        chofer.setFoto(rs.getString("foto"));
        chofer.setNumeroLicencia( rs.getString("numero_licencia"));
        String licencia = rs.getString("tipo_licencia");
        
        if (licencia != null && !licencia.isEmpty()) {
             chofer.setTipoLicencia(licencia.charAt(0));
        }
        chofer.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
        chofer.setSalarioBasePorViaje(rs.getDouble("salario_base_por_viaje"));
        chofer.setEstado(rs.getBoolean("chofer_estado"));
        
        return chofer;
    }
    
    private void rollback(Connection conexion){
        if (conexion != null )  {
            try {
                conexion.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void restauraAutoCommit(Connection conexion,boolean autoCommit){
        if (conexion != null)  {
            try {
                conexion.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                e.printStackTrace();       
            }
        }
    }
    
    private void cerrar(Statement statement) {

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void cerrar(ResultSet resultSet) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
