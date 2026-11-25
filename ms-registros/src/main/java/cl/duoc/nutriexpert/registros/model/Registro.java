package cl.duoc.nutriexpert.registros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

// Registro de terreno enviado por la app. Mismos campos que RegistroTerreno en Room, menos la foto.
// usuarioId + idLocal es unico: si la app reintenta la sincronizacion, no se duplica.
@Entity
@Table(name = "registros",
        uniqueConstraints = @UniqueConstraint(name = "uk_usuario_idlocal", columnNames = {"usuario_id", "id_local"}))
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "idLocal es obligatorio")
    @Column(name = "id_local", nullable = false)
    private Long idLocal;

    @NotNull(message = "usuarioId es obligatorio")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @NotBlank(message = "cultivo es obligatorio")
    @Column(nullable = false)
    private String cultivo;

    @NotBlank(message = "lote es obligatorio")
    @Column(nullable = false)
    private String lote;

    @NotNull(message = "metaRendimiento es obligatorio")
    @Positive(message = "metaRendimiento debe ser mayor a 0")
    private Double metaRendimiento;

    @NotNull(message = "eficienciaRiego es obligatorio")
    @DecimalMin(value = "1", message = "eficienciaRiego debe estar entre 1 y 100")
    @DecimalMax(value = "100", message = "eficienciaRiego debe estar entre 1 y 100")
    private Double eficienciaRiego;

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

    @NotNull(message = "creadoEn es obligatorio")
    private Long creadoEn;

    public Registro() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdLocal() { return idLocal; }
    public void setIdLocal(Long idLocal) { this.idLocal = idLocal; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getCultivo() { return cultivo; }
    public void setCultivo(String cultivo) { this.cultivo = cultivo; }
    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }
    public Double getMetaRendimiento() { return metaRendimiento; }
    public void setMetaRendimiento(Double metaRendimiento) { this.metaRendimiento = metaRendimiento; }
    public Double getEficienciaRiego() { return eficienciaRiego; }
    public void setEficienciaRiego(Double eficienciaRiego) { this.eficienciaRiego = eficienciaRiego; }
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
    public Long getCreadoEn() { return creadoEn; }
    public void setCreadoEn(Long creadoEn) { this.creadoEn = creadoEn; }
}
