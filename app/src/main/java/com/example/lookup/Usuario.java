package com.example.lookup;

public class Usuario {

    private int id;
    private String usuario;
    private String contraseña;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String estado;
    private String correo;
    private String telefono;
    private String foto;

    public Usuario() {
    }

    public Usuario(int id, String usuario, String contraseña, String nombre, String direccion, String ciudad, String estado, String correo, String telefono, String foto) {
        this.id = id;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.estado = estado;
        this.correo = correo;
        this.telefono = telefono;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
