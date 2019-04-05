package com.example.lookup;

public class localizados {

    private int idloc;
    private String usuarioloc1;
    private String contraseñaloc1;
    private String nombreloc1;
    private String telefonoloc1;
    private Double longitud;
    private Double latitud;
    private String direccionloc;
    private String foto1;

    public localizados() {
    }


    public localizados(int idloc, String usuarioloc1, String contraseñaloc1, String nombreloc1, String telefonoloc1, Double longitud, Double latitud, String direccionloc, String foto1) {
        this.idloc = idloc;
        this.usuarioloc1 = usuarioloc1;
        this.contraseñaloc1 = contraseñaloc1;
        this.nombreloc1 = nombreloc1;
        this.telefonoloc1 = telefonoloc1;
        this.longitud = longitud;
        this.latitud = latitud;
        this.direccionloc = direccionloc;
        this.foto1 = foto1;
    }

    public int getIdloc() {
        return idloc;
    }

    public void setIdloc(int idloc) {
        this.idloc = idloc;
    }

    public String getUsuarioloc1() {
        return usuarioloc1;
    }

    public void setUsuarioloc1(String usuarioloc1) {
        this.usuarioloc1 = usuarioloc1;
    }

    public String getContraseñaloc1() {
        return contraseñaloc1;
    }

    public void setContraseñaloc1(String contraseñaloc1) {
        this.contraseñaloc1 = contraseñaloc1;
    }

    public String getNombreloc1() {
        return nombreloc1;
    }

    public void setNombreloc1(String nombreloc1) {
        this.nombreloc1 = nombreloc1;
    }

    public String getTelefonoloc1() {
        return telefonoloc1;
    }

    public void setTelefonoloc1(String telefonoloc1) {
        this.telefonoloc1 = telefonoloc1;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public String getDireccionloc() {
        return direccionloc;
    }

    public void setDireccionloc(String direccionloc) {
        this.direccionloc = direccionloc;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    @Override
    public String toString() {
        return nombreloc1 +"\n"+ telefonoloc1 +"\n"+direccionloc;
    }
}
