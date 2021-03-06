package it.nextre.academy.springdemo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="portata")
public class Pasto extends BaseEntity {

    /*
    //Rimosso perchè ereditato da baseEntity
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    */

    @NotEmpty(message = "Campo obbligatorio!")
    @NotNull(message = "Campo obbligatorio!!!")
    String titolo;

    @Column(columnDefinition = "TEXT")
    String descrizione;

    @JsonIgnore
    @Size(min=5,max = 255, message = "immagine non valida")
    String image;



    @JsonProperty("immagine")
    public String getImageURL(){
        if(this.image!=null)
        return this.image.replace("\\","/");
        return "images/noimage.jpg";
    }


    public void setTitolo(String titolo) {
        this.titolo = titolo.trim();
    }
}//end class
