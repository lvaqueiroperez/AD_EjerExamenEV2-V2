package pvehiculos2;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Clientes2 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    String dni;
    String nomec;
    int ncompras;

    public Clientes2() {
    }

    public Clientes2(String dni, String nomec, int ncompras) {
        this.dni = dni;
        this.nomec = nomec;
        this.ncompras = ncompras;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNomec() {
        return nomec;
    }

    public void setNomec(String nomec) {
        this.nomec = nomec;
    }

    public int getNcompras() {
        return ncompras;
    }

    public void setNcompras(int ncompras) {
        this.ncompras = ncompras;
    }

    @Override
    public String toString() {
        return "Clientes{" + "dni=" + dni + ", nomec=" + nomec + ", ncompras=" + ncompras + '}';
    }

}
