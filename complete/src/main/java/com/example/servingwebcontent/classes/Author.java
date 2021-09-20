package com.example.servingwebcontent.classes;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String fullName;

}
