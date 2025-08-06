package es.cic.curso25.proy011.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @OneToMany(mappedBy = "mesa", orphanRemoval = false, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Silla> sillas = new ArrayList<>();

    // Método para generar la relación bidireccionalmente al añadir un objeto silla
    @Transactional
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

    public Mesa() {
    }

    public Mesa(String color, String forma, @Max(20) int numPatas, String material) {
        this.color = color;
        this.forma = forma;
        this.numPatas = numPatas;
        this.material = material;
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

    public void setMaterial(String material) {
        this.material = material;
    }

    public List<Silla> getSillas() {
        return sillas;
    }

    public void setSillas(List<Silla> nuevasSillas) {
        // Eliminar las sillas que ya no están en la nueva lista
        List<Silla> sillasParaEliminar = new ArrayList<>();
        for (Silla sillaExistente : this.sillas) {
            if (!nuevasSillas.contains(sillaExistente)) {
                this.deleteSilla(sillaExistente);
            }
        }
        this.sillas.removeAll(sillasParaEliminar);

        // Agregar o actualizar las nuevas sillas
        for (Silla nuevaSilla : nuevasSillas) {
            if (!this.sillas.contains(nuevaSilla)) {
                addSilla(nuevaSilla); // Asocia mesa <-> silla
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
