package es.cic.curso25.proy011.model;

import java.util.Iterator;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;

@Entity
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 18)
    private String color;

    private String forma;

    @Max(20)
    private int numPatas;

    @Column(length = 20)
    private String material;

    @OneToMany(mappedBy = "mesa", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Silla> sillas;

    // Método para generar la relación bidireccionalmente al añadir un objeto silla
    public void addSilla(Silla silla) {
        silla.setMesa(this);
        sillas.add(silla);
    }

    // Método para eliminar la relación bidireccionalmente al eliminar un objeto
    // silla
    public void deleteSilla(Silla silla) {
        silla.setMesa(null);
        sillas.remove(silla);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public int getNumPatas() {
        return numPatas;
    }

    public void setNumPatas(int numPatas) {
        this.numPatas = numPatas;
    }

    
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String meterial) {
        this.material = meterial;
    }

    public List<Silla> getSillas() {
        return sillas;
    }

    public void setSillas(List<Silla> sillas) {
        //Quitamos las referencias actuales
        Iterator<Silla> iterator = this.sillas.iterator();
        while(iterator.hasNext()){
            Silla silla = iterator.next();
            silla.setMesa(null);//Quitamos la referencia al padre del hijo
            iterator.remove(); //Y la referencia al hijo del padre
        }

        //Agregamos las nuevas relaciones bidireccionalmente, utilizando el método add
        if(sillas != null){
            for(Silla silla : sillas){
                addSilla(silla);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Mesa other = (Mesa) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Mesa [id=" + id + ", color=" + color + ", forma=" + forma + ", numPatas=" + numPatas + ", sillas="
                + sillas + "]";
    }

}
