package com.trainticketbooking.app.Entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "provinces")
@Data
public class Province {

    @Id
    @Column(name = "province_id")
    private Integer provinceId;

    @Column(name = "name")
    private String name;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "full_name_en")
    private String fullNameEn;

    @Column(name = "code_name")
    private String codeName;

    @OneToMany(mappedBy = "province")
    private List<Station> stations;

}