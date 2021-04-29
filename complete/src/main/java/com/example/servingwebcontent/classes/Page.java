package com.example.servingwebcontent.classes;

import com.example.servingwebcontent.classes.Article;
import com.example.servingwebcontent.classes.Maket;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "PAGE")
public class Page {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_magazine_id")
    private Magazine magazine;

    @ManyToOne
    @JoinColumn(name = "fk_article_id")
    private Article article;

    @ElementCollection
    @Column(name = "text")
    private List<String> text;

    @Column(name = "number")
    private int number;

    @ManyToOne
    @JoinColumn(name = "fk_maket")
    private Maket maket;
}
