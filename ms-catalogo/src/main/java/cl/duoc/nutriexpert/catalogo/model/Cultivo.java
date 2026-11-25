package cl.duoc.nutriexpert.catalogo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

// Cultivo con su rendimiento base (t/ha) y lo que extrae a ese rendimiento (kg/ha).
// VALORES DE DEMOSTRACION: no son recomendaciones agronomicas.
@Entity
@Table(name = "cultivos")
public class Cultivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "nombre es obligatorio")
    @Column(nullable = false, unique = true)
    private String nombre;

    @NotNull(message = "rendimientoBase es obligatorio")
    @Positive(message = "rendimientoBase debe ser mayor a 0")
    private Double rendimientoBase;

    @NotNull(message = "n es obligatorio")
    @PositiveOrZero(message = "n debe ser mayor o igual a 0")
    private Double n;

    @NotNull(message = "p2o5 es obligatorio")
    @PositiveOrZero(message = "p2o5 debe ser mayor o igual a 0")
    private Double p2o5;

    @NotNull(message = "k2o es obligatorio")
    @PositiveOrZero(message = "k2o debe ser mayor o igual a 0")
    private Double k2o;

    @NotNull(message = "cao es obligatorio")
    @PositiveOrZero(message = "cao debe ser mayor o igual a 0")
    private Double cao;

    @NotNull(message = "mgo es obligatorio")
    @PositiveOrZero(message = "mgo debe ser mayor o igual a 0")
    private Double mgo;

    public Cultivo() {
    }

    public Cultivo(Long id, String nombre, Double rendimientoBase,
                   Double n, Double p2o5, Double k2o, Double cao, Double mgo) {
        this.id = id;
        this.nombre = nombre;
        this.rendimientoBase = rendimientoBase;
        this.n = n;
        this.p2o5 = p2o5;
        this.k2o = k2o;
        this.cao = cao;
        this.mgo = mgo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getRendimientoBase() { return rendimientoBase; }
    public void setRendimientoBase(Double rendimientoBase) { this.rendimientoBase = rendimientoBase; }
    public Double getN() { return n; }
    public void setN(Double n) { this.n = n; }
    public Double getP2o5() { return p2o5; }
    public void setP2o5(Double p2o5) { this.p2o5 = p2o5; }
    public Double getK2o() { return k2o; }
    public void setK2o(Double k2o) { this.k2o = k2o; }
    public Double getCao() { return cao; }
    public void setCao(Double cao) { this.cao = cao; }
    public Double getMgo() { return mgo; }
    public void setMgo(Double mgo) { this.mgo = mgo; }
}
