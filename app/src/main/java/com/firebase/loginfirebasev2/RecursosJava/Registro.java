package com.firebase.loginfirebasev2.RecursosJava;

public class Registro {
    String id, nombre,apellidos, curp, nombredeusuario, telefono,
    correoelectronico,contraseña;

    public Registro(String Id, String nombre, String apellidos, String cp, String nombredeusuario, String telefono, String correoelectronico, String contraseña) {
        this.id = Id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.curp = cp;
        this.nombredeusuario = nombredeusuario;
        this.telefono = telefono;
        this.correoelectronico = correoelectronico;
        this.contraseña = contraseña;
    }

    public Registro(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombredeusuario() {
        return nombredeusuario;
    }

    public void setNombredeusuario(String nombredeusuario) {
        this.nombredeusuario = nombredeusuario;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoelectronico() {
        return correoelectronico;
    }

    public void setCorreoelectronico(String correoelectronico) {
        this.correoelectronico = correoelectronico;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "Id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", Curp='" + curp + '\'' +
                ", nombredeusuario='" + nombredeusuario + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correoelectronico='" + correoelectronico + '\'' +
                ", contraseña='" + contraseña + '\'' +
                '}';
    }
}
