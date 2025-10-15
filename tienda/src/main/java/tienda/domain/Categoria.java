package tienda.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name="categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_categoria")
    private Integer idCategoria;
    
    @Column(unique=true, nullable=false, length=50)
    @NotNull
    @Size(max=50)
    private String descripcion;
    
    @Column(length=1024)
    @Size(max=1024)
    private String rutaImagen;
    
    private boolean activo;
    
        // Relación de uno a muchos con la clase Producto
    // Sin "cascade" ni "orphanRemoval" para evitar la propagación de operaciones.
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos;

    
}
