package com.base.modelo.exemplo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Ayslan
 */
@Entity
public class PessoaExemplo implements Serializable {

    @Id
    @SequenceGenerator(name = "Person", allocationSize = 1, sequenceName = "person_id_seq")
    @GeneratedValue(generator = "Person")
    private Long id;
    
    @Size(max = 200)
    @NotBlank
    private String nome;
    
    @Size(max = 100)
    @Email
    private String email;
    
    private BigDecimal salary;
    
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
    @NotNull
    @Temporal(TemporalType.TIME)
    private Date horario;
    
    @NotBlank
    @Size(max=10)
    private String rg;
     
    private Boolean active;

    public PessoaExemplo() {
    }

    public Date getHorario() {
        return horario;
    }

    public void setHorario(Date horario) {
        this.horario = horario;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }


    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    

    @Override
    public String toString() {
        return nome+" - "+rg;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PessoaExemplo other = (PessoaExemplo) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    

}
