package br.com.fiap.market.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "TDS_TB_USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "USERNAME", unique = true, nullable = false)
    private String username;

    @Column(name = "SENHA", nullable = false)
    private String senha;

    @Column(name = "ROLE", nullable = false)
    private String role;
}
