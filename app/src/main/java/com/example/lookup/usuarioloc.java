package com.example.lookup;

public class usuarioloc {

    private String usuarioloc;
    private String contraseñaloc;
    private String nombreloc;
    private String telefonoloc;
    private String foto1;

    public usuarioloc() {
    }

    public usuarioloc(String usuarioloc, String contraseñaloc, String nombreloc, String telefonoloc, String foto1) {
        this.usuarioloc = usuarioloc;
        this.contraseñaloc = contraseñaloc;
        this.nombreloc = nombreloc;
        this.telefonoloc = telefonoloc;
        this.foto1 = foto1;
    }

    public String getUsuarioloc() {
        return usuarioloc;
    }

    public void setUsuarioloc(String usuarioloc) {
        this.usuarioloc = usuarioloc;
    }

    public String getContraseñaloc() {
        return contraseñaloc;
    }

    public void setContraseñaloc(String contraseñaloc) {
        this.contraseñaloc = contraseñaloc;
    }

    public String getNombreloc() {
        return nombreloc;
    }

    public void setNombreloc(String nombreloc) {
        this.nombreloc = nombreloc;
    }

    public String getTelefonoloc() {
        return telefonoloc;
    }

    public void setTelefonoloc(String telefonoloc) {
        this.telefonoloc = telefonoloc;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }
}
